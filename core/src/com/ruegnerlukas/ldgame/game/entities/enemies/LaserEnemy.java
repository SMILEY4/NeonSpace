package com.ruegnerlukas.ldgame.game.entities.enemies;

import java.util.Random;

import com.ruegnerlukas.ldgame.game.Cell;
import com.ruegnerlukas.ldgame.game.World;
import com.ruegnerlukas.ldgame.game.entities.weapons.Bullet;
import com.ruegnerlukas.ldgame.game.entities.weapons.Laser;

public class LaserEnemy extends Enemy {

	private Random random = new Random();
	
	private int nShots = 0;
	
	private Laser laser;
	
	

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
		
		
		if(laser != null && laser.getState()==4) {
			laser = null;
		}
		if(laser != null) {
			return true;
		}
		
		
		boolean shoot = false;
		
		float a = random.nextFloat();
		if(a < 0.2f) {
			shoot = true;
		}
		if(nShots >= 2) {
			shoot = false;
		}
		
		if(shoot) {
			shoot(world, turnNum);
			
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
				shoot(world, turnNum);
			}
		}
		
		return true;
	}
	
	
	private void shoot(World world, long turnNum) {
		
		nShots++;
		
		for(int lx = this.x-1; lx>=0; lx--) {
			Cell cellTarget = world.getCell(lx, y);
			laser = new Laser(turnNum);
			laser.source = this;
			cellTarget.add(laser);
		}
		
	}
	
	
}
