package com.ruegnerlukas.ldgame.game.entities.weapons;

import java.util.ListIterator;

import com.ruegnerlukas.ldgame.game.Cell;
import com.ruegnerlukas.ldgame.game.World;
import com.ruegnerlukas.ldgame.game.entities.Entity;
import com.ruegnerlukas.ldgame.game.entities.Player;
import com.ruegnerlukas.ldgame.game.entities.enemies.Enemy;
import com.ruegnerlukas.ldgame.scenes.GameScene;

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
			currCell.removeNow(this);
		}
		
		Cell cellDst = null;
		int dir = source instanceof Player ? +1 : -1;
		cellDst = world.getCell(x+dir, y);

		if(cellDst != null) {
			world.getCell(x, y).removeLater(this);
			cellDst.add(this);
		}
		
		// handle collisions
		Cell cell = world.getCell(x, y);

		for(int i=0; i<cell.getEntities().size(); i++) {
			Entity e = cell.getEntities().get(i);
			
			if(e instanceof Enemy && source instanceof Player) {
				System.out.println("BOOM");
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
	
	
	
	
	
	
	
	public void explode() {
		Cell currentCell = world.getCell(x, y);
		currentCell.removeNow(this);
		this.exists = false;
		
		for(int cx=0; cx<world.getWidth(); cx++) {
			for(int cy=0; cy<world.getHeight(); cy++) {
				
				int dx = Math.abs(cx-x);
				int dy = Math.abs(cy-y);
				int d = (int) Math.sqrt(dx*dx + dy*dy);
				
				if(d <= 1) {
					
					Cell cell = world.getCell(cx, cy);
					
					for(int i=0; i<cell.getEntities().size(); i++) {
						Entity e = cell.getEntities().get(i);
						if(source instanceof Player) {
							if(e instanceof Enemy) {
								((Enemy)e).takeDamage(cell, this, 1);
							}
						} else {
							if(e instanceof Player) {
								((Player)e).takeDamage(cell, this, 1);
							}
						}
					}
					
					
				}

				
			}
			
			GameScene.shakeFrames = Math.max(GameScene.shakeFrames, 4);
			GameScene.shakeSize = Math.max(GameScene.shakeSize, 16);
			GameScene.particleMng.spawnBomb(x*100, y*100, true);
		}
		
		
	}
	
	
	
}
