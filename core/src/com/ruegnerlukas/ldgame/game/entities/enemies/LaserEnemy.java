package com.ruegnerlukas.ldgame.game.entities.enemies;

import java.util.Random;

import com.ruegnerlukas.ldgame.game.Cell;
import com.ruegnerlukas.ldgame.game.World;
import com.ruegnerlukas.ldgame.game.entities.Entity;
import com.ruegnerlukas.ldgame.game.entities.Player;
import com.ruegnerlukas.ldgame.game.entities.weapons.Bomb;
import com.ruegnerlukas.ldgame.game.entities.weapons.Bullet;
import com.ruegnerlukas.ldgame.game.entities.weapons.Laser;
import com.ruegnerlukas.ldgame.scenes.GameScene;

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
			currCell.removeNow(this);
		}
		
		
		// is shooting ?
		boolean isLocked = false;
		if(laser != null && laser.getState()==4) {
			laser = null;
		}
		if(laser != null) {
			isLocked = true;
		}
		
		
		// decide to shoot
		boolean shoot = false;
		float a = random.nextFloat();
		if(a < 0.2f) {
			shoot = true;
		}
		if(nShots >= 2) {
			shoot = false;
		}
		
		
		
		
		// do action
		if(!isLocked) {
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
					world.getCell(x, y).removeNow(this);
					cellDst.add(this, false);
					nShots=0;
				} else {
					shoot(world, turnNum);
				}
			}
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
	
	
	
	private void shoot(World world, long turnNum) {
		nShots++;
		for(int lx = this.x-1; lx>=0; lx--) {
			Cell cellTarget = world.getCell(lx, y);
			laser = new Laser(turnNum);
			laser.source = this;
			cellTarget.add(laser, true);
		}
		GameScene.particleMng.spawnLaser((x-1)*100+50, 50, y*100+50, 1, false, laser);

	}
	
	
	@Override
	public boolean takeDamage(Cell cell, Entity src, int dmg) {
		Player.score += 50;
		return super.takeDamage(cell, src, dmg);
	}
	
}
