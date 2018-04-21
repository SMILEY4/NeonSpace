package com.ruegnerlukas.ldgame.game.entities.enemies;

import java.util.Random;

import com.ruegnerlukas.ldgame.game.Cell;
import com.ruegnerlukas.ldgame.game.World;
import com.ruegnerlukas.ldgame.game.entities.weapons.Bullet;

public class BulletEnemy extends Enemy {

	private Random random = new Random();
	
	private int nShots = 0;
	

	@Override
	public boolean update(long turnNum, int turnType, World world) {
		if(turnType != 2) {
			return false;
		}
		
		Cell currCell = world.getCell(x, y);
		
		if(x == 0) {
			currCell.remove(this);
		}
		
		onMove(currCell, currCell);
		
		
		boolean shoot = false;
		
		float a = random.nextFloat();
		if(a < 0.35f) {
			shoot = true;
		}
		if(nShots >= 3) {
			shoot = false;
		}
		
		if(shoot) {
			Cell cellTarget = world.getCell(x-1, y);
			if(cellTarget != null) {
				Bullet b = new Bullet();
				b.source = this;
				cellTarget.add(b);
				nShots++;
			}
			
		} else {
			
			int dir = random.nextInt(4);
			
			Cell cellDst = null;
			
			if(dir == 0) {
				cellDst = world.getCell(x-1, y);
			}
			if(dir == 1) {
				cellDst = world.getCell(x+1, y);
			}
			if(dir == 2) {
				cellDst = world.getCell(x, y-1);
			}
			if(dir == 3) {
				cellDst = world.getCell(x, y+1);
			}
			
			if(cellDst != null && canMoveTo(cellDst)) {
				world.getCell(x, y).remove(this);
				cellDst.add(this);
				onMove(currCell, cellDst);
				nShots=0;
			} else {
				
				Cell cellTarget = world.getCell(x-1, y);
				if(cellTarget != null) {
					Bullet b = new Bullet();
					b.source = this;
					cellTarget.add(b);
				}
				nShots++;
			}
		}
		
		return true;
	}
	
}
