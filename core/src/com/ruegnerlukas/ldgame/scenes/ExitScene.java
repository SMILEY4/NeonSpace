package com.ruegnerlukas.ldgame.scenes;

import com.badlogic.gdx.Gdx;
import com.ruegnerlukas.scenes.Scene;

public class ExitScene extends Scene {

	@Override
	public void load() {
	}

	@Override
	public void update(int deltaMS) {
		Gdx.app.exit();
	}

	@Override
	public void unload() {
	}

}
