package com.ruegnerlukas.ldgame;

import com.badlogic.gdx.Gdx;
import com.ruegnerlukas.input.InputReciever;

public class GDXInputReciever implements InputReciever {

	@Override
	public void updatePost() {
	}
	
	
	
	
	@Override
	public void onAddReciever() {
	}
	
	
	

	@Override
	public void onRemoveReciever() {
	}
	
	
	

	@Override
	public boolean isKeyDown(int keycode) {
		return Gdx.input.isKeyPressed(keycode);
	}
	
	
	

	@Override
	public boolean isMouseDown(int button) {
		return Gdx.input.isButtonPressed(button);
	}
	
	
	

	@Override
	public int getMouseWheel() {
		return 0;
	}

}
