package com.ruegnerlukas.ldgame.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.ruegnerlukas.scenes.Scene;
import com.ruegnerlukas.scenes.SceneManager;
import com.ruegnerlukas.scenes.SceneManager.TransitionState;
import com.ruegnerlukas.scenes.transition.ColorFadeTransition;
import com.ruegnerlukas.simplemath.interpolation.InterpolationSineIn;
import com.ruegnerlukas.simplemath.interpolation.InterpolationSineOut;
import com.ruegnerlukas.simplemath.vectors.vec4.Vector4f;

public class StartScene extends Scene {

	private ShapeRenderer shapeRenderer;
	private SpriteBatch batch;
	private BitmapFont font;
	
	private boolean loaded = false;
	
	
	
	@Override
	public void load() {
		shapeRenderer = new ShapeRenderer();
		batch = new SpriteBatch();
		font = new BitmapFont();
	}

	
	
	
	@Override
	public void update(int deltaMS) {
		
		if(SceneManager.get().getTransitionState() == TransitionState.NO_TRANSITION && !loaded) {
			System.out.println("load global resources");
			loaded = true;
		}
		
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(0.8f, 0.8f, 0.8f, 1f);
		shapeRenderer.rect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		shapeRenderer.end();

		if(loaded) {
			batch.begin();
			font.setColor(0, 0, 0, 1f);
			font.draw(batch, "Press any key to start.", 100, 100);
			batch.end();
		}
		
		if(Gdx.input.isKeyJustPressed(-1) && loaded) {
			SceneManager.get().changeScene("menu_scene",
					new ColorFadeTransition(600, new Vector4f(0,0,0,0f), new Vector4f(0,0,0,1f), new InterpolationSineOut()),
					new ColorFadeTransition(600, new Vector4f(0,0,0,1f), new Vector4f(0,0,0,0), new InterpolationSineIn())
					);
		}
		

		
		
	}

	
	
	
	@Override
	public void unload() {
		shapeRenderer.dispose();
		batch.dispose();
		font.dispose();
	}

}
