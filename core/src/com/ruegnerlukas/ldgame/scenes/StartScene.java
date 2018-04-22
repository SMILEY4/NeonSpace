package com.ruegnerlukas.ldgame.scenes;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.utils.Align;
import com.ruegnerlukas.scenes.Scene;
import com.ruegnerlukas.scenes.SceneManager;
import com.ruegnerlukas.scenes.SceneManager.TransitionState;
import com.ruegnerlukas.scenes.transition.ColorFadeTransition;
import com.ruegnerlukas.simplemath.interpolation.InterpolationMath;
import com.ruegnerlukas.simplemath.interpolation.InterpolationSineIn;
import com.ruegnerlukas.simplemath.interpolation.InterpolationSineOut;
import com.ruegnerlukas.simplemath.vectors.vec3.Vector3f;
import com.ruegnerlukas.simplemath.vectors.vec4.Vector4f;

public class StartScene extends Scene {

	private ShapeRenderer shapeRenderer;
	private SpriteBatch batch;
	private BitmapFont font36, font12;
	
	private Texture texTitle, texGlow;
	
	private final int N_STARS = 800;
	private final float STARS_SPEED_MIN = 0.2f;
	private final float STARS_SPEED_MAX = 8f;
	private Vector3f[] stars;
	
	private boolean loaded = false;
	
	private int time = 0;
	private int textFadeIn = 3000;
	
	
	
	@Override
	public void load() {
		shapeRenderer = new ShapeRenderer();
		batch = new SpriteBatch();
		font36 = new BitmapFont(Gdx.files.internal("fonts/cornerstone36mod.fnt"));
		font12 = new BitmapFont(Gdx.files.internal("fonts/cornerstone12.fnt"));

		texTitle = new Texture(Gdx.files.internal("startSceneTitle.png"));
		texGlow = new Texture(Gdx.files.internal("glow.png"));

		Random random = new Random();
		
		stars = new Vector3f[N_STARS];
		for(int i=0; i<N_STARS; i++) {
			stars[i] = new Vector3f(
					random.nextInt(Gdx.graphics.getWidth()),
					random.nextInt(Gdx.graphics.getHeight()),
					random.nextFloat()*(STARS_SPEED_MAX-STARS_SPEED_MIN)+STARS_SPEED_MIN );
		}
		
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
		if(loaded) {
			
			Vector4f colorA = new Vector4f(0f, 1f, 1f, 0f);
			Vector4f colorB = new Vector4f(1f, 1f, 1f, 1f);
			
			int d = Math.min(Math.max(0, textFadeIn - time), textFadeIn);
			float t = 1f - ((float)d / (float)textFadeIn);
			float tIpl = InterpolationMath.sine(t);
			
			Vector4f color = new Vector4f();
			color.x = colorA.x * (1f-tIpl) + colorB.x * (tIpl);
			color.y = colorA.y * (1f-tIpl) + colorB.y * (tIpl);
			color.z = colorA.z * (1f-tIpl) + colorB.z * (tIpl);
			color.w = colorA.w * (1f-tIpl) + colorB.w * (tIpl);

			batch.enableBlending();
			batch.begin();
			
			font36.setColor(color.x, color.y, color.z, color.w);
			font36.draw(batch, "< Press any key to start >", 0, 150, Gdx.graphics.getWidth(), Align.center, false);
			
			font12.setColor(color.x, color.y, color.z, Math.min(color.w, 0.3f));
			font12.draw(batch, "a game by Lukas R<gner (SMILEY>4>) for Ludum Dare 41 (2018)", 0, 20, Gdx.graphics.getWidth()-10, Align.right, false);
			
			Color colorOld = batch.getColor();
			batch.setColor(1, 1, 1, color.w);
			batch.draw(texTitle, 0, 0);
			batch.setColor(colorOld);
			
			batch.end();
			batch.disableBlending();
			
		}
		
		
		if(Gdx.input.isKeyJustPressed(-1) && loaded && SceneManager.get().getTransitionState() != TransitionState.SCENE_OUT) {
			SceneManager.get().changeScene("menu_scene",
					new ColorFadeTransition(1000, new Vector4f(0,0,0,0f), new Vector4f(0,0,0,1f), new InterpolationSineOut()),
					new ColorFadeTransition(1000, new Vector4f(0,0,0,1f), new Vector4f(0,0,0,0), new InterpolationSineIn())
					);
		}
		

		
		
	}

	
	
	
	@Override
	public void unload() {
		texTitle.dispose();
		texGlow.dispose();
		shapeRenderer.dispose();
		batch.dispose();
		font36.dispose();
	}

}
