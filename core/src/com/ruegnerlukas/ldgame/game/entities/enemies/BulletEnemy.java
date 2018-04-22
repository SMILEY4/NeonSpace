package com.ruegnerlukas.ldgame.game.entities.enemies;

import java.util.Random;

import com.ruegnerlukas.ldgame.SoundManager;
import com.ruegnerlukas.ldgame.game.Cell;
import com.ruegnerlukas.ldgame.game.World;
import com.ruegnerlukas.ldgame.game.entities.Entity;
import com.ruegnerlukas.ldgame.game.entities.Player;
import com.ruegnerlukas.ldgame.game.entities.weapons.Bomb;
import com.ruegnerlukas.ldgame.game.entities.weapons.Bullet;
import com.ruegnerlukas.ldgame.game.entities.weapons.Laser;

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
			currCell.removeNow(this);
		}
		
		
		// decide to shoot
		boolean shoot = false;
		float a = random.nextFloat();
		if(a < 0.35f) {
			shoot = true;
		}
		if(nShots >= 3) {
			shoot = false;
		}
		
		// do action
		if(shoot) {
			Cell cellTarget = world.getCell(x-1, y);
			if(cellTarget != null) {
				Bullet b = new Bullet();
				b.source = this;
				cellTarget.add(b, true);
				nShots++;
				SoundManager.play("shootBullet");
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
				world.getCell(x, y).removeNow(this);
				cellDst.add(this, false);
				nShots=0;
				SoundManager.play("move");
			} else {
				
				Cell cellTarget = world.getCell(x-1, y);
				if(cellTarget != null) {
					Bullet b = new Bullet();
					b.source = this;
					cellTarget.add(b, true);
					SoundManager.play("shootBullet");
				}
				nShots++;
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
					((Bomb) e).exists = false;
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
		Player.score += 25;
		return super.takeDamage(cell, src, dmg);
	}
	
}
