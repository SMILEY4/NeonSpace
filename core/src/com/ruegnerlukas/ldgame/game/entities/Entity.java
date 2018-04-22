package com.ruegnerlukas.ldgame.game.entities;

import com.ruegnerlukas.ldgame.game.World;
import com.ruegnerlukas.ldgame.game.animation.MoveAnimation;
import com.ruegnerlukas.simplemath.vectors.vec2.Vector2f;

public class Entity {
	
	public int x;
	public int y;
	
	public int lastX = 0;
	public int lastY = 0;
	
	public long lastUpdate;
	
	public MoveAnimation animation;
	public Vector2f pos;
	
	
	
	public Entity() {
		pos = new Vector2f();
	}
	
	
	public void updateAnimation(int deltaMS, World world) {
		
		if(x != lastX || y != lastY) {
			MoveAnimation anim = null;
			if(this.animation != null) {
				anim = new MoveAnimation(this.animation.posCurr.copy(), new Vector2f(x*100, y*100), 300);
			} else {
				anim = new MoveAnimation(new Vector2f(lastX*100, lastY*100), new Vector2f(x*100, y*100), 300);
			}
			this.animation = anim;
		}
		
		if(animation != null) {
			animation.update(deltaMS);
			pos.set(animation.posCurr);
			if(animation.done) {
				animation = null;
			}
		}
		
		if(animation == null) {
			pos.set(x*100, y*100);
		}
		
		lastX = x;
		lastY = y;
		
	}
	
	
	
	/**
	 * @return true, if entity did sth (->mainly for player actions)
	 * */
	public boolean update(long turnNum, int turnType, World world) {
		return false;
	}
	
	

}
