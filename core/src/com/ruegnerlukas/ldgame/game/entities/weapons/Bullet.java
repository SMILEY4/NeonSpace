package com.ruegnerlukas.ldgame.game.entities.weapons;

import java.util.ListIterator;

import com.ruegnerlukas.ldgame.game.Cell;
import com.ruegnerlukas.ldgame.game.World;
import com.ruegnerlukas.ldgame.game.entities.Entity;
import com.ruegnerlukas.ldgame.game.entities.Player;
import com.ruegnerlukas.ldgame.game.entities.enemies.Enemy;

public class Bullet extends Entity {
	
	public Entity source;
	
	
	

	
	@Override
	public boolean update(long turnNum, int turnType, World world) {
		if(turnType != 1) {
			return false;
		}
		
		Cell currCell = world.getCell(x, y);
		
		// out of bounds 
		if(x == 0 || x == world.getWidth()-1) {
			currCell.removeNow(this);
		}
		
		// move
		Cell cellDst = null;
		int dir = source instanceof Player ? +1 : -1;
		cellDst = world.getCell(x+dir, y);
		if(cellDst != null) {
			world.getCell(x, y).removeNow(this);
			cellDst.add(this);
		}
		
		// handle collisions
		Cell cell = world.getCell(x, y);
		
		for(int i=0; i<cell.getEntities().size(); i++) {
			Entity e = cell.getEntities().get(i);
			
			if(e instanceof Enemy && source instanceof Player) {
				((Enemy)e).takeDamage(cell, this, 1);
				cell.removeNow(this);
			}
			
			if(e instanceof Player && !(source instanceof Player) ) {
				((Player)e).takeDamage(cell, this, 1);
				cell.removeNow(this);
			}
		}
		
		
		return false;
	}
	
	
	
	
}
