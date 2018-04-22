package com.ruegnerlukas.ldgame.game.animation;

import com.ruegnerlukas.simplemath.interpolation.InterpolationSine;
import com.ruegnerlukas.simplemath.vectors.vec2.Vector2f;

public class MoveAnimation {

	public Vector2f posStart;
	public Vector2f posDst;
	public Vector2f posCurr;
	
	public int durMS;
	public long startMS;
	
	public boolean done = false;
	
	public InterpolationSine interpolation;
	
	
	public MoveAnimation(Vector2f posStart, Vector2f posDst, int durMS) {
		this.posStart = posStart;
		this.posDst = posDst;
		this.durMS = 100;//durMS;
		this.startMS = System.currentTimeMillis();
		this.posCurr = new Vector2f(posStart);
		this.interpolation = new InterpolationSine();
	}
	
	
	
	
	public void update(int deltaMS) {
		
		int time = (int) (System.currentTimeMillis()-startMS);
		float t = (float)time / (float)durMS;
		
		if(t >= 1f) {
			done = true;
		}
		
		t = Math.max(0f, Math.min(t, 1f));
		t = interpolation.interpolate(t);
		
		posCurr.x = posStart.x * (1f-t) + posDst.x * t;
		posCurr.y = posStart.y * (1f-t) + posDst.y * t;
		
	}
	
}








