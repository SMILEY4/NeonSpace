package com.ruegnerlukas.ldgame.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.ruegnerlukas.ldgame.LDGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 1200;
		config.height = 701;
		config.foregroundFPS = 60;
		config.vSyncEnabled = true;
		config.resizable = false;
		new LwjglApplication(new LDGame(), config);
	}
}
