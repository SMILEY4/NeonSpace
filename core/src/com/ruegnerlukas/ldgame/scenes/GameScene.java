package com.ruegnerlukas.ldgame.scenes;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.utils.Align;
import com.ruegnerlukas.input.InputManager;
import com.ruegnerlukas.ldgame.GDXInputReciever;
import com.ruegnerlukas.ldgame.game.Cell;
import com.ruegnerlukas.ldgame.game.WaveGenerator;
import com.ruegnerlukas.ldgame.game.World;
import com.ruegnerlukas.ldgame.game.entities.Entity;
import com.ruegnerlukas.ldgame.game.entities.Player;
import com.ruegnerlukas.ldgame.game.entities.enemies.BulletEnemy;
import com.ruegnerlukas.ldgame.game.entities.enemies.CircleEnemy;
import com.ruegnerlukas.ldgame.game.entities.enemies.Enemy;
import com.ruegnerlukas.ldgame.game.entities.enemies.LaserEnemy;
import com.ruegnerlukas.ldgame.game.entities.weapons.Bomb;
import com.ruegnerlukas.ldgame.game.entities.weapons.Bullet;
import com.ruegnerlukas.ldgame.game.entities.weapons.Laser;
import com.ruegnerlukas.ldgame.game.particles.ParticleManager;
import com.ruegnerlukas.scenes.Scene;
import com.ruegnerlukas.scenes.SceneManager;
import com.ruegnerlukas.scenes.SceneManager.TransitionState;
import com.ruegnerlukas.scenes.transition.ColorFadeTransition;
import com.ruegnerlukas.simplemath.interpolation.InterpolationSineIn;
import com.ruegnerlukas.simplemath.interpolation.InterpolationSineOut;
import com.ruegnerlukas.simplemath.vectors.vec3.Vector3f;
import com.ruegnerlukas.simplemath.vectors.vec4.Vector4f;

public class GameScene extends Scene {

	private ShapeRenderer shapeRenderer;
	private SpriteBatch batch;
	private BitmapFont font;
	
	private ShaderProgram defaultShader, lightenShader;
	
	private Texture texGlow, textureObjects, textureItems;
	private TextureRegion texPlayer, texEnemyCircle, texEnemyLaser, texEnemyBullet, texBullet, texLaser, texBomb;
	
	private final int N_STARS = 800;
	private final float STARS_SPEED_MIN = 0.2f;
	private final float STARS_SPEED_MAX = 8f;
	private Vector3f[] stars;
	
	private InputManager input;

	private WaveGenerator waveGenerator;
	private int waveCooldown = 0;
	
	private final int cellSize = 100;
	private final int cols = 12;
	private final int rows = 6;
	private World world;
	
	private Player player;
	
	private int turnType = 0; // 0 = player, 1 = neutral, 2 = enemies
	
	private final int turnPauseDur = 200;
	private int turnPause = turnPauseDur;
	
	private long tick = 0;
	private long turn = 0;
	
	public static ParticleManager particleMng;
	
	public static int shakeFrames = 0;
	public static int shakeSize = 0;

	
	
	
	
	@Override
	public void load() {
		
		tick = 0;
		turn = 0;
		turnType = 0;
		turnPause = turnPauseDur;
		waveCooldown = 0;
		
		particleMng = new ParticleManager();
		
		shapeRenderer = new ShapeRenderer();
		
		batch = new SpriteBatch();
		
		String vertexShader = "attribute vec4 " + ShaderProgram.POSITION_ATTRIBUTE + ";\n" //
				+ "attribute vec4 " + ShaderProgram.COLOR_ATTRIBUTE + ";\n" //
				+ "attribute vec2 " + ShaderProgram.TEXCOORD_ATTRIBUTE + "0;\n" //
				+ "uniform mat4 u_projTrans;\n" //
				+ "varying vec4 v_color;\n" //
				+ "varying vec2 v_texCoords;\n" //
				+ "\n" //
				+ "void main()\n" //
				+ "{\n" //
				+ "   v_color = " + ShaderProgram.COLOR_ATTRIBUTE + ";\n" //
				+ "   v_color.a = v_color.a * (255.0/254.0);\n" //
				+ "   v_texCoords = " + ShaderProgram.TEXCOORD_ATTRIBUTE + "0;\n" //
				+ "   gl_Position =  u_projTrans * " + ShaderProgram.POSITION_ATTRIBUTE + ";\n" //
				+ "}\n";
			String fragmentShader = "#ifdef GL_ES\n" //
				+ "#define LOWP lowp\n" //
				+ "precision mediump float;\n" //
				+ "#else\n" //
				+ "#define LOWP \n" //
				+ "#endif\n" //
				+ "varying LOWP vec4 v_color;\n" //
				+ "varying vec2 v_texCoords;\n" //
				+ "uniform sampler2D u_texture;\n" //
				+ "void main()\n"//
				+ "{\n" //
				+ "  gl_FragColor = vec4(max(v_color.rgb, texture2D(u_texture, v_texCoords).rgb), texture2D(u_texture, v_texCoords).a*v_color.a );\n" //
				+ "}";
		
		lightenShader = new ShaderProgram(vertexShader, fragmentShader);
		defaultShader = SpriteBatch.createDefaultShader();
		
		font = new BitmapFont(Gdx.files.internal("fonts/cornerstone36mod.fnt"));
		font.setUseIntegerPositions(true);
		font.setOwnsTexture(true);
		
		texGlow = new Texture(Gdx.files.internal("glow.png"));

		textureItems = new Texture(Gdx.files.internal("items.png"));
		textureObjects = new Texture(Gdx.files.internal("objects/objects.png"));
		texPlayer = new TextureRegion(textureObjects, 0, 0, 100, 100);
		texEnemyCircle = new TextureRegion(textureObjects, 100, 0, 100, 100);
		texEnemyLaser = new TextureRegion(textureObjects, 200, 0, 100, 100);
		texEnemyBullet = new TextureRegion(textureObjects, 300, 0, 100, 100);
		texBullet = new TextureRegion(textureObjects, 400, 0, 100, 100);
		texLaser = new TextureRegion(textureObjects, 500, 0, 100, 100);
		texBomb = new TextureRegion(textureObjects, 600, 0, 100, 100);

		input = new InputManager();
		input.setReciever(new GDXInputReciever());

		waveGenerator = new WaveGenerator();
		
		world = new World(cols, rows, cellSize);
		
		Random random = new Random();
		
		stars = new Vector3f[N_STARS];
		for(int i=0; i<N_STARS; i++) {
			stars[i] = new Vector3f(
					random.nextInt(cols*cellSize),
					random.nextInt(rows*cellSize),
					random.nextFloat()*(STARS_SPEED_MAX-STARS_SPEED_MIN)+STARS_SPEED_MIN );
		}
		
		player = new Player();
		world.getCell(1, 3).add(player);
		
		player.power=100;
	}

	
	
	
	@Override
	public void update(int deltaMS) {
		
//		if(Gdx.input.justTouched()) {
//			int mx = Gdx.input.getX();
//			int my = Gdx.graphics.getHeight()-Gdx.input.getY();
//			int cx = mx / 100;
//			int cy = my / 100;
//			Cell cell = world.getCell(cx, cy);
//			if(cell != null) {
//				System.out.println("CELL (" + cx + "," + cy +"):");
//				for(Entity e : cell.getEntities()) {
//					System.out.println("  " + e);
//				}
//			}
//		}
		
		// game over
		if(player.health <= 0 && SceneManager.get().getTransitionState() == TransitionState.NO_TRANSITION) {
			SceneManager.get().changeScene("gameover_scene",
					new ColorFadeTransition(4000, new Vector4f(1,0,1,0f), new Vector4f(0,0,0,1f), new InterpolationSineOut()),
					new ColorFadeTransition( 600, new Vector4f(0,0,0,1f), new Vector4f(0,0,0,0), new InterpolationSineIn())
					);
			
		// still playing
		} else {
			
			int nEnemies = 0;
			for(int x=0; x<cols; x++) {
				for(int y=0; y<rows; y++) {
					Cell cell = world.getCell(x, y);
					for(int i=0; i<cell.getEntities().size(); i++) {
						Entity e = cell.getEntities().get(i);
						if(e instanceof Enemy) {
							nEnemies++;
						}
					}
				}
			}
			if(nEnemies == 0) {
				if(waveCooldown <= 0) {
					for(Enemy e : waveGenerator.getNext()) {
						world.getCell(e.x, e.y).add(e);
					}
					waveCooldown = 1000;
				} else {
					waveCooldown-=deltaMS;
				}
			}
			
			
			tick++;
			
			// UPDATE
			turnPause -= deltaMS;
			if(turnPause <= 0) {
				
				boolean nextTurn = false;
				
				// player
				if(turnType == 0) {
					if(player.lastUpdate != tick) {
						nextTurn = player.update(turn, turnType, world);
						player.lastUpdate = tick;
					}
					
				// other
				} else {
					for(int x=0; x<cols; x++) {
						for(int y=0; y<rows; y++) {
							Cell cell = world.getCell(x, y);
							
							for(int i=0; i<cell.getEntities().size(); i++) {
								Entity e = cell.getEntities().get(i);
								if(e.lastUpdate != tick) {
									e.update(turn, turnType, world);
									e.lastUpdate = tick;
								}
							}
							
						}
					}
					nextTurn = true;
				}
				
				
				for(int x=0; x<cols; x++) {
					for(int y=0; y<rows; y++) {
						Cell cell = world.getCell(x, y);
						cell.removeAllDead();
					}
				}
				
				if(nextTurn) {
					turnType++;
					if(turnType >= 3) {
						turnType = 0;
						Player.score += 5;
					}
					turnPause = turnPauseDur;
					if(turnType == 0) {
						turn++;
					}
				}
				
			}
			
			
		}
		
		
		// screenshape laser
		for(int x=0; x<cols; x++) {
			for(int y=0; y<rows; y++) {
				Cell cell = world.getCell(x, y);
				for(int i=0; i<cell.getEntities().size(); i++) {
					Entity e = cell.getEntities().get(i);
					if(e instanceof Laser) {
						Laser laser = (Laser)e;
						if(laser.getState() == 0) {
							GameScene.shakeFrames = Math.max(GameScene.shakeFrames, 2);
							GameScene.shakeSize = Math.max(GameScene.shakeSize, 1);
						}
						if(laser.getState() == 1) {
							GameScene.shakeFrames = Math.max(GameScene.shakeFrames, 2);
							GameScene.shakeSize = Math.max(GameScene.shakeSize, 5);
						}
						if(laser.getState() == 2) {
							GameScene.shakeFrames = Math.max(GameScene.shakeFrames, 2);
							GameScene.shakeSize = Math.max(GameScene.shakeSize, 2);
						}
					}
				}
				
			}
		}
		
		
		// RENDER
		
		// clear background
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(0.0f, 0.0f, 0.0f, 1f);
		shapeRenderer.rect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		shapeRenderer.end();
		
		batch.enableBlending();
		batch.begin();
		batch.draw(texGlow, 0, 0);
		batch.end();
		batch.disableBlending();
		
		// DRAW STARS
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(0.8f, 0.8f, 0.8f, 1f);
		
		for(int i=0; i<N_STARS; i++) {
			Vector3f star = stars[i];
			star.x -= star.z;
			if(star.x < 0) {
				star.x += cols*cellSize;
			}
			shapeRenderer.rect(star.x, star.y, 1, 1);
		}
		shapeRenderer.end();

		
		// DRAW GRID
		shapeRenderer.begin(ShapeType.Line);
		
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		
		int gridOffX = 0;
		int gridOffY = 0;

		Random random = new Random();
		
		if(shakeFrames > 0) {
			shakeFrames--;
			gridOffX = random.nextInt(shakeSize*2)-shakeSize;
			gridOffY = random.nextInt(shakeSize*2)-shakeSize;
		}
		
		for(int x=0; x<cols; x++) {
			for(int y=0; y<rows; y++) {
				shapeRenderer.setColor(1f, 1f, 1f, 0.2f);
				shapeRenderer.rect(gridOffX+x*cellSize, gridOffY+y*cellSize, cellSize, cellSize);
				shapeRenderer.setColor(0f, 0f, 1f, 0.3f);
				shapeRenderer.rect(gridOffX+x*cellSize+1, gridOffY+y*cellSize+1, cellSize-2, cellSize-2);
			}
		}
		shapeRenderer.end();
		Gdx.gl.glDisable(GL20.GL_BLEND);
		
		// DRAW OBJECTS
		batch.begin();
		batch.setShader(lightenShader);
		
		int srcFunc = batch.getBlendSrcFunc();
		int dstFunc = batch.getBlendDstFunc();
		
		batch.enableBlending();
		batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE);
		
		for(int x=0; x<cols; x++) {
			for(int y=0; y<rows; y++) {
				Cell cell = world.getCell(x, y);
				for(int i=0; i<cell.getEntities().size(); i++) {
					Entity e = cell.getEntities().get(i);
					
					batch.setColor(0f, 0f, 0f, 1f);
					
					if(e instanceof Player) {
						batch.setColor(0f, 1f, 1f, 1f);
						batch.draw(texPlayer, x*cellSize, y*cellSize, 100, 100);
					}
					
					if(e instanceof Enemy) {
						batch.setColor(1f, 0f, 1f, 1f);
						if(e instanceof CircleEnemy) {
							batch.draw(texEnemyCircle, x*cellSize, y*cellSize, 100, 100);
						}
						if(e instanceof BulletEnemy) {
							batch.draw(texEnemyBullet, x*cellSize, y*cellSize, 100, 100);
						}
						if(e instanceof LaserEnemy) {
							batch.draw(texEnemyLaser, x*cellSize, y*cellSize, 100, 100);
						}
					}
					
					if(e instanceof Bullet) {
						if( ((Bullet)e).source instanceof Player ) {
							batch.setColor(0f, 1f, 1f, 1f);
						} else {
							batch.setColor(1f, 0f, 1f, 1f);
						}
						batch.draw(texBullet, x*cellSize, y*cellSize, 100, 100);
					}
					if(e instanceof Bomb) {
						if( ((Bomb)e).source instanceof Player ) {
							batch.setColor(0f, 1f, 1f, 1f);
						} else {
							batch.setColor(1f, 0f, 1f, 1f);
						}
						batch.draw(texBomb, x*cellSize, y*cellSize, 100, 100);
					}
					if(e instanceof Laser) {
						
						if( ((Laser)e).source instanceof Player ) {
							batch.setColor(0f, 1f, 1f, 1f);
						} else {
							batch.setColor(1f, 0f, 1f, 1f);
						}
						
						int state = ((Laser)e).getState();
						if(state == 0) {
							if( ((Laser)e).source instanceof Player ) {
								batch.setColor(0f, 1f, 1f, 0.5f);
							} else {
								batch.setColor(1f, 0f, 1f, 0.5f);
							}
							batch.draw(texLaser, x*cellSize, y*cellSize+45, 100, 10);
						}
						
						if(state == 1) {
							batch.draw(texLaser, x*cellSize, y*cellSize, 100, 100);
						}
						
						if(state == 2) {
							batch.draw(texLaser, x*cellSize, y*cellSize+30, 100, 40);	
						}

					}
				}
			}
		}
		batch.end();
		batch.setBlendFunction(srcFunc, dstFunc);

		
		// draw particles
		particleMng.update(deltaMS);

		
		// draw GUI
		batch.begin();
		batch.setColor(1f, 1f, 1f, 1f);
		batch.setShader(defaultShader);
		
		font.draw(batch, "HULL:", 10, Gdx.graphics.getHeight()-4, 150, Align.right, false);
		font.draw(batch, player.health+"%", 170, Gdx.graphics.getHeight()-4, 100, Align.right, false);
		font.draw(batch, "ENERGY:", 10, Gdx.graphics.getHeight()-40, 150, Align.right, false);
		font.draw(batch, player.power+"%", 170, Gdx.graphics.getHeight()-40, 100, Align.right, false);

		font.draw(batch, "SCORE:", Gdx.graphics.getWidth()-300, Gdx.graphics.getHeight()-4, 150, Align.right, false);
		font.draw(batch, Player.score+"", Gdx.graphics.getWidth()-160, Gdx.graphics.getHeight()-4, 100, Align.right, false);
		font.draw(batch, "TURN:", Gdx.graphics.getWidth()-300, Gdx.graphics.getHeight()-40, 150, Align.right, false);
		font.draw(batch, turn+"", Gdx.graphics.getWidth()-160, Gdx.graphics.getHeight()-40, 100, Align.right, false);
		
		batch.draw(textureItems, 480, Gdx.graphics.getHeight()-90);
		
		batch.end();
		
		
		input.update();
	}

	
	
	
	
	@Override
	public void unload() {
		particleMng.unload();
		particleMng = null;
		texGlow.dispose();
		lightenShader.dispose();
		defaultShader.dispose();
		shapeRenderer.dispose();
		batch.dispose();
		font.dispose();
		textureObjects.dispose();
		textureItems.dispose();
	}

}
