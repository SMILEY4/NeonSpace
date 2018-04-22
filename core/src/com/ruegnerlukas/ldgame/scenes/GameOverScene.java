package com.ruegnerlukas.ldgame.scenes;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.utils.Align;
import com.ruegnerlukas.input.InputManager;
import com.ruegnerlukas.input.InputManager.InputState;
import com.ruegnerlukas.input.actions.MultiKeyAction;
import com.ruegnerlukas.input.actions.InputAction.MultiMode;
import com.ruegnerlukas.ldgame.GDXInputReciever;
import com.ruegnerlukas.ldgame.SoundManager;
import com.ruegnerlukas.ldgame.game.entities.Player;
import com.ruegnerlukas.scenes.Scene;
import com.ruegnerlukas.scenes.SceneManager;
import com.ruegnerlukas.scenes.SceneManager.TransitionState;
import com.ruegnerlukas.scenes.transition.ColorFadeTransition;
import com.ruegnerlukas.simplemath.interpolation.InterpolationMath;
import com.ruegnerlukas.simplemath.interpolation.InterpolationSineIn;
import com.ruegnerlukas.simplemath.interpolation.InterpolationSineOut;
import com.ruegnerlukas.simplemath.vectors.vec3.Vector3f;
import com.ruegnerlukas.simplemath.vectors.vec4.Vector4f;

public class GameOverScene extends Scene {

	private ShapeRenderer shapeRenderer;
	private SpriteBatch batch;
	private BitmapFont font24, font36;
	
	private Texture texGlow, texGameOver;
	
	private InputManager input;
	
	private final int N_STARS = 800;
	private final float STARS_SPEED_MIN = 0.2f;
	private final float STARS_SPEED_MAX = 8f;
	private Vector3f[] stars;
	
	private boolean loaded = false;
	
	private int time = 0;
	private int textFadeIn = 3000;
	
	private int iSelected = 0;
	
	
	
	@Override
	public void load() {
		
		shapeRenderer = new ShapeRenderer();
		batch = new SpriteBatch();
		font24 = new BitmapFont(Gdx.files.internal("fonts/cornerstone24.fnt"));
		font36 = new BitmapFont(Gdx.files.internal("fonts/cornerstone36mod.fnt"));

		texGlow = new Texture(Gdx.files.internal("glow.png"));
		texGameOver = new Texture(Gdx.files.internal("gameOver.png"));

		Random random = new Random();
		
		stars = new Vector3f[N_STARS];
		for(int i=0; i<N_STARS; i++) {
			stars[i] = new Vector3f(
					random.nextInt(Gdx.graphics.getWidth()),
					random.nextInt(Gdx.graphics.getHeight()),
					random.nextFloat()*(STARS_SPEED_MAX-STARS_SPEED_MIN)+STARS_SPEED_MIN );
		}
		
		input = new InputManager();
		input.setReciever(new GDXInputReciever());
		input.addAction(new MultiKeyAction("up", InputState.RELEASED, new int[]{Keys.W,Keys.UP}, MultiMode.ONE));
		input.addAction(new MultiKeyAction("down", InputState.RELEASED, new int[]{Keys.S,Keys.DOWN}, MultiMode.ONE));
		input.addAction(new MultiKeyAction("accept", InputState.RELEASED, new int[]{Keys.ENTER, Keys.A, Keys.D, Keys.LEFT, Keys.RIGHT}, MultiMode.ONE));

		
	}

	
	
	
	@Override
	public void update(int deltaMS) {
		
		if(loaded) {
			time += deltaMS;
		}
		if(SceneManager.get().getTransitionState() == TransitionState.NO_TRANSITION && !loaded) {
			loaded = true;
		}
		
		
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
				star.x += Gdx.graphics.getWidth();
			}
			shapeRenderer.rect(star.x, star.y, 1, 1);
		}
		shapeRenderer.end();
		
		
		// draw text
		if(loaded && SceneManager.get().getTransitionState() != TransitionState.SCENE_OUT) {
			
			Vector4f colorA = new Vector4f(0f, 1f, 1f, 0f);
			Vector4f colorB = new Vector4f(1f, 1f, 1f, 1f);
			Vector4f colorC = new Vector4f(1f, 0f, 1f, 1f);

			
			int d = Math.min(Math.max(0, textFadeIn - time), textFadeIn);
			float t = 1f - ((float)d / (float)textFadeIn);
			float tIpl = InterpolationMath.sine(t);
			
			Vector4f colorWhite = new Vector4f();
			colorWhite.x = colorA.x * (1f-tIpl) + colorB.x * (tIpl);
			colorWhite.y = colorA.y * (1f-tIpl) + colorB.y * (tIpl);
			colorWhite.z = colorA.z * (1f-tIpl) + colorB.z * (tIpl);
			colorWhite.w = colorA.w * (1f-tIpl) + colorB.w * (tIpl);

			Vector4f colorSelection = new Vector4f();
			colorSelection.x = colorA.x * (1f-tIpl) + colorC.x * (tIpl);
			colorSelection.y = colorA.y * (1f-tIpl) + colorC.y * (tIpl);
			colorSelection.z = colorA.z * (1f-tIpl) + colorC.z * (tIpl);
			colorSelection.w = colorA.w * (1f-tIpl) + colorC.w * (tIpl);
		

			
			batch.enableBlending();
			batch.begin();
			
			font36.setColor(colorWhite.x, colorWhite.y, colorWhite.z, colorWhite.w);
			font36.draw(batch, "Score: " + Player.score, 0, Gdx.graphics.getHeight()-400, Gdx.graphics.getWidth(), Align.center, false);

			
			if(iSelected == 0) {
				font24.setColor(colorSelection.x, colorSelection.y, colorSelection.z, colorSelection.w);
				font24.draw(batch, "< Retry >", 0, Gdx.graphics.getHeight()-530, Gdx.graphics.getWidth(), Align.center, false);
			} else {
				font24.setColor(colorWhite.x, colorWhite.y, colorWhite.z, colorWhite.w);
				font24.draw(batch, "Retry", 0, Gdx.graphics.getHeight()-530, Gdx.graphics.getWidth(), Align.center, false);
			}
			
			if(iSelected == 1) {
				font24.setColor(colorSelection.x, colorSelection.y, colorSelection.z, colorSelection.w);
				font24.draw(batch, "< Return to Main menu >", 0, Gdx.graphics.getHeight()-555, Gdx.graphics.getWidth(), Align.center, false);
			} else {
				font24.setColor(colorWhite.x, colorWhite.y, colorWhite.z, colorWhite.w);
				font24.draw(batch, "Return to Main menu", 0, Gdx.graphics.getHeight()-555, Gdx.graphics.getWidth(), Align.center, false);
			}
			

			Color colorOld = batch.getColor();
			batch.setColor(1, 1, 1, colorWhite.w);
			batch.draw(texGameOver, 0, 0);
			batch.setColor(colorOld);
			
			batch.end();
			batch.disableBlending();
			
		}
		
		
		
		if(isLoaded()) {
			
			if(input.action("up")) {
				iSelected--;
				if(iSelected<0) { iSelected = 1; };
				SoundManager.play("menu_click_1");
			}
			
			if(input.action("down")) {
				iSelected++;
				if(iSelected>=2) { iSelected = 0; };
				SoundManager.play("menu_click_1");
			}
			
			if(input.action("accept")) {
				SoundManager.play("menu_click_2");
				if(iSelected == 0) {
					SceneManager.get().changeScene("game_scene",
							new ColorFadeTransition(1000, new Vector4f(0,0,0,0f), new Vector4f(0,0,0,1f), new InterpolationSineOut()),
							new ColorFadeTransition( 500, new Vector4f(0,0,0,1f), new Vector4f(0,0,0,0), new InterpolationSineIn())
							);
				}
				if(iSelected == 1) {
					SceneManager.get().changeScene("menu_scene",
							new ColorFadeTransition(1000, new Vector4f(0,0,0,0f), new Vector4f(0,0,0,1f), new InterpolationSineOut()),
							new ColorFadeTransition( 500, new Vector4f(0,0,0,1f), new Vector4f(0,0,0,0), new InterpolationSineIn())
							);
				}
			}
			
		}
		
		
		input.update();
		
	}
	
	
	
	

	@Override
	public void unload() {
		texGlow.dispose();
		texGameOver.dispose();
		shapeRenderer.dispose();
		batch.dispose();
		font36.dispose();
		font24.dispose();
	}

}
