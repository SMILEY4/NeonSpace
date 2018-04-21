package com.ruegnerlukas.ldgame.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.ruegnerlukas.scenes.Scene;
import com.ruegnerlukas.scenes.SceneManager;
import com.ruegnerlukas.scenes.SceneManager.TransitionState;
import com.ruegnerlukas.scenes.transition.ColorFadeTransition;
import com.ruegnerlukas.scenes.transition.SceneTransition;
import com.ruegnerlukas.simplemath.vectors.vec4.Vector4f;

public class OverlayScene extends Scene {

	private ShapeRenderer shapeRenderer;

	
	
	
	@Override
	public void load() {
		shapeRenderer = new ShapeRenderer();
	}

	
	
	
	@Override
	public void update(int deltaMS) {
		
		if(SceneManager.get().getTransitionState() != TransitionState.NO_TRANSITION) {
			
			SceneTransition transition = SceneManager.get().getActiveTransition();
			if(transition != null && transition instanceof ColorFadeTransition) {
				
				ColorFadeTransition cfTransition = (ColorFadeTransition)transition;
				Vector4f color = cfTransition.getCurrentColor();
				
				Gdx.gl.glEnable(GL20.GL_BLEND);
				Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
				
				shapeRenderer.begin(ShapeType.Filled);
				shapeRenderer.setColor(color.x, color.y, color.z, color.w);
				shapeRenderer.rect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
				shapeRenderer.end();
				
				Gdx.gl.glDisable(GL20.GL_BLEND);
			}
			
		}
		
	}

	
	
	
	@Override
	public void unload() {
		shapeRenderer.dispose();	
	}

}
