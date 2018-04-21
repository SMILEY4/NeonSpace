package com.ruegnerlukas.ldgame.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.ruegnerlukas.input.InputManager;
import com.ruegnerlukas.input.InputManager.InputState;
import com.ruegnerlukas.input.actions.InputAction.MultiMode;
import com.ruegnerlukas.input.actions.MultiKeyAction;
import com.ruegnerlukas.ldgame.GDXInputReciever;
import com.ruegnerlukas.scenes.Scene;

public class MenuScene extends Scene {

	private ShapeRenderer shapeRenderer;
	private SpriteBatch batch;
	private BitmapFont font;
	
	private InputManager input;
	
	private String[] lines;
	private int selected = 0;
	
	
	@Override
	public void load() {
		shapeRenderer = new ShapeRenderer();
		batch = new SpriteBatch();
		font = new BitmapFont();
		
		input = new InputManager();
		input.setReciever(new GDXInputReciever());
		input.addAction(new MultiKeyAction("up", InputState.RELEASED, new int[]{Keys.W,Keys.UP}, MultiMode.ONE));
		input.addAction(new MultiKeyAction("down", InputState.RELEASED, new int[]{Keys.S,Keys.DOWN}, MultiMode.ONE));
		input.addAction(new MultiKeyAction("left", InputState.RELEASED, new int[]{Keys.A,Keys.LEFT}, MultiMode.ONE));
		input.addAction(new MultiKeyAction("right", InputState.RELEASED, new int[]{Keys.D,Keys.RIGHT}, MultiMode.ONE));
		input.addAction(new MultiKeyAction("accept", InputState.RELEASED, new int[]{Keys.ENTER}, MultiMode.ONE));

		lines = new String[]{
				"Start",
				"< Normal >",
				"Quit",
				"",
				"Help:",
				"  W/A/S/D:  move",
				"  1/2/3/4:  actions"
		};
	
	}

	
	
	
	@Override
	public void update(int deltaMS) {
		
		if(input.action("up")) { selected--; }
		if(input.action("down")) { selected++; }
		if(selected == -1) { selected = 2; }
		if(selected == 3) { selected = 0; }

		if(input.action("accept")) {
			if(selected == 0) {
				System.out.println("start game");
			}
			if(selected == 1) {
				System.out.println("select diffuculty");
			}
			if(selected == 2) {
				System.out.println("exit game");
			}
		}
		
		
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(0.8f, 0.8f, 0.8f, 1f);
		shapeRenderer.rect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		shapeRenderer.end();
		
		batch.begin();
		
		int y = 460;
		for(int i=0; i<lines.length; i++) {
			if(i == selected) {
				font.setColor(1f, 0f, 0f, 1f);
			} else {
				font.setColor(0f, 0f, 0f, 1f);
			}
			
			font.draw(batch, lines[i], 100, y);
			
			y-=20;
		}
		
		batch.end();
		
		input.update();
	}

	
	
	
	@Override
	public void unload() {
		shapeRenderer.dispose();
		batch.dispose();
		font.dispose();
	}

}
