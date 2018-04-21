package com.ruegnerlukas.ldgame;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.ruegnerlukas.ldgame.scenes.*;
import com.ruegnerlukas.scenes.SceneManager;
import com.ruegnerlukas.scenes.transition.ColorFadeTransition;
import com.ruegnerlukas.simplemath.interpolation.InterpolationSineIn;
import com.ruegnerlukas.simplemath.vectors.vec4.Vector4f;

public class LDGame extends ApplicationAdapter {

	
	
	
	@Override
	public void create() {
		
		SceneManager.get().addScene("start_scene", new StartScene());
		SceneManager.get().addScene("menu_scene", new MenuScene());
		SceneManager.get().addScene("game_scene", new GameScene());
		SceneManager.get().addScene("gameover_scene", new GameOverScene());
		SceneManager.get().addScene("overlay_scene", new OverlayScene());
		
		SceneManager.get().setStaticScene("overlay_scene");
//		SceneManager.get().changeScene("start_scene", null, new ColorFadeTransition(2000, new Vector4f(0,0,0,1f), new Vector4f(0,0,0,0), new InterpolationSineIn()));
		
		SceneManager.get().changeScene("game_scene", null, null);

	}


	
	
	@Override
	public void render() {
		int deltaMS = (int)(Gdx.graphics.getDeltaTime()*1000);
		SceneManager.get().update(deltaMS);
	}


	
	
	@Override
	public void dispose() {
		SceneManager.get().unloadAll();
	}

	
}
