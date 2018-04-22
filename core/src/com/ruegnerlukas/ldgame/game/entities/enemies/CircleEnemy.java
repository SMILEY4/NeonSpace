package com.ruegnerlukas.ldgame.game.entities.enemies;

import com.ruegnerlukas.ldgame.game.Cell;
import com.ruegnerlukas.ldgame.game.World;
import com.ruegnerlukas.ldgame.game.entities.Entity;
import com.ruegnerlukas.ldgame.game.entities.Player;
import com.ruegnerlukas.ldgame.game.entities.weapons.Bomb;
import com.ruegnerlukas.ldgame.game.entities.weapons.Bullet;
import com.ruegnerlukas.ldgame.game.entities.weapons.Laser;
import com.ruegnerlukas.ldgame.scenes.GameScene;

public class CircleEnemy extends Enemy {

	

	@Override
	public boolean update(long turnNum, int turnType, World world) {
		if(turnType != 2) {
			return false;
		}
		
		Cell currCell = world.getCell(x, y);
		
		// out of bounds
		if(x == 0) {
			currCell.removeNow(this);
		}
		
		
		// move
		Cell cellDst = null;
		cellDst = world.getCell(x-1, y);
		if(cellDst != null && canMoveTo(cellDst)) {
			world.getCell(x, y).removeNow(this);
			cellDst.add(this);
		}

		
		// handle collisions
		Cell cell = world.getCell(x, y);

		for(int i=0; i<cell.getEntities().size(); i++) {
			Entity e = cell.getEntities().get(i);
			
			if(e instanceof Bomb) {
				if( (((Bomb)e).source instanceof Player) ) {
					takeDamage(cell, e, 1);
					cell.getEntities().remove(e);
				}
			}
			if(e instanceof Bullet) {
				if( (((Bullet)e).source instanceof Player) ) {
					takeDamage(cell, e, 1);
					cell.getEntities().remove(e);
				}
			}
			if(e instanceof Laser) {
				if( (((Laser)e).source instanceof Player) && (((Laser)e).getState()==2 || ((Laser)e).getState()==1) ) {
//					takeDamage(cell, e, 1);
				}
			}
		}
		
		return false;
	}
	
	
	@Override
	public boolean takeDamage(Cell cell, Entity src, int dmg) {
		Player.score += 10;
		return super.takeDamage(cell, src, dmg);
	}
	
}
