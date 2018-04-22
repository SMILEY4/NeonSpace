package com.ruegnerlukas.ldgame.game.particles;

import com.badlogic.gdx.math.Vector3;

public class Particle {

	public float x, y, dx, dy;
	public boolean isPlayerColor;
	public boolean isBigChunk;
	public long timeStart;
	public long timeTotal;
	
	public boolean isAttracted = false;
	public float attX, attY;
	
}

