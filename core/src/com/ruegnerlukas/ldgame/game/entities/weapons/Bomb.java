package com.ruegnerlukas.ldgame.game.entities.weapons;

import com.ruegnerlukas.ldgame.game.Cell;
import com.ruegnerlukas.ldgame.game.World;
import com.ruegnerlukas.ldgame.game.entities.Entity;
import com.ruegnerlukas.ldgame.game.entities.Player;
import com.ruegnerlukas.ldgame.game.entities.enemies.Enemy;

public class Bomb extends Entity {
	
	public Entity source;
	
	public boolean exists = true;
	
	private World world;

	
	@Override
	public boolean update(long turnNum, int turnType, World world) {
		this.world = world;
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
					this.exists = false;
					to.remove(this);
					((Enemy)e).takeDamage(to, this, 1);
				}
			}
			
			if(!(source instanceof Player)) {
				if(e instanceof Player) {
					this.exists = false;
					to.remove(this);
					((Player)e).takeDamage(this, 1);
				}
			}
			
		}
	}
	
	
	
	
	public void explode() {
		Cell currentCell = world.getCell(x, y);
		currentCell.remove(this);
		this.exists = false;
		
		for(int cx=0; cx<world.getWidth(); cx++) {
			for(int cy=0; cy<world.getHeight(); cy++) {
				
				int dx = Math.abs(cx-x);
				int dy = Math.abs(cy-y);
				int d = (int) Math.sqrt(dx*dx + dy*dy);
				
				if(d <= 1) {
					
					Cell cell = world.getCell(cx, cy);
					
					for(Entity e : cell.getEntities()) {
						if(source instanceof Player) {
							if(e instanceof Enemy) {
								cell.remove(e);
							}
						} else {
							if(e instanceof Player) {
								cell.remove(e);
							}
						}
					}
					
					
				}

				
			}
		}
		
		
	}
	
	
	
}
