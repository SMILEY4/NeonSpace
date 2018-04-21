package com.ruegnerlukas.ldgame.game.entities.weapons;

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
		
		if(x == 0 || x == world.getWidth()-1) {
			currCell.remove(this);
		}
		
		Cell cellDst = null;
		int dir = source instanceof Player ? +1 : -1;
		cellDst = world.getCell(x+dir, y);

		if(cellDst != null) {
			world.getCell(x, y).remove(this);
			cellDst.add(this);
			onMove(currCell, cellDst);
		}
		
		return true;
	}
	
	
	
	
	
	
	public void onMove(Cell from, Cell to) {
		for(Entity e : to.getEntities()) {
			
			if(source instanceof Player) {
				if(e instanceof Enemy) {
					((Enemy)e).takeDamage(to, this, 1);
					to.remove(this);
				}
			}
			
			if(!(source instanceof Player)) {
				if(e instanceof Player) {
					((Player)e).takeDamage(this, 1);
					to.remove(this);
				}
			}
			
		}
	}
	
	
	
	
	
	
}
