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
		
		// handle first collisions
		Cell cell0 = world.getCell(x, y);
		
		for(int i=0; i<cell0.getEntities().size(); i++) {
			Entity e = cell0.getEntities().get(i);
			
			if(e instanceof Enemy && source instanceof Player) {
				((Enemy)e).takeDamage(cell0, this, 1);
				cell0.removeNow(this);
			}
			
			if(e instanceof Player && !(source instanceof Player) ) {
				((Player)e).takeDamage(cell0, this, 1);
				cell0.removeNow(this);
			}
		}
		
		
		if(!cell0.getEntities().contains(this)) {
			return false;
		}
		
		// move
		Cell cellDst = null;
		int dir = source instanceof Player ? +1 : -1;
		cellDst = world.getCell(x+dir, y);
		if(cellDst != null) {
			world.getCell(x, y).removeNow(this);
			cellDst.add(this, false);
		}
		
		// handle collisions
		Cell cell1 = world.getCell(x, y);
		
		for(int i=0; i<cell1.getEntities().size(); i++) {
			Entity e = cell1.getEntities().get(i);
			
			if(e instanceof Enemy && source instanceof Player) {
				((Enemy)e).takeDamage(cell1, this, 1);
				cell1.removeNow(this);
			}
			
			if(e instanceof Player && !(source instanceof Player) ) {
				((Player)e).takeDamage(cell1, this, 1);
				cell1.removeNow(this);
			}
		}
		
		
		return false;
	}
	
	
	
	
}
