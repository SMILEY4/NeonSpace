package com.ruegnerlukas.ldgame.game.entities.weapons;

import com.ruegnerlukas.ldgame.SoundManager;
import com.ruegnerlukas.ldgame.game.Cell;
import com.ruegnerlukas.ldgame.game.World;
import com.ruegnerlukas.ldgame.game.entities.Entity;
import com.ruegnerlukas.ldgame.game.entities.Player;
import com.ruegnerlukas.ldgame.game.entities.enemies.Enemy;

public class Laser extends Entity {
	
	public Entity source;
	
	public long startTurn = 0;
	public int nTurns = 3;
	public int state = 0;


	public Laser(long startTurn) {
		this.startTurn = startTurn;
	}
	
	
	
	
	public int getState() {
		return this.state;
	}
	
	
	
	@Override
	public boolean update(long turnNum, int turnType, World world) {

		if(turnType != 1) {
			return false;
		}
		
		// calc state
		if(state == 2) {
			state = 4;
			world.getCell(x, y).removeNow(this);
		}
		if(state == 1) {
			state = 2;
		}
		if(state == 0) {
			SoundManager.play("shootLaser");
			state = 1;
		}
		
		// handle collisions
		Cell cell = world.getCell(x, y);

		for(int i=0; i<cell.getEntities().size(); i++) {
			Entity e = cell.getEntities().get(i);
			
			if(e instanceof Enemy && source instanceof Player && (state==1 || state==2)) {
				((Enemy)e).takeDamage(cell, this, 1);
			}
			
			if(e instanceof Player && !(source instanceof Player) && (state==1 || state==2)) {
				((Player)e).takeDamage(cell, this, 1);
			}
		}
		
		return false;
	}
	
	
	
	@Override
	public String toString() {
		return super.toString() + "  state=" + this.state; 
	}
	
	
	
	
}
