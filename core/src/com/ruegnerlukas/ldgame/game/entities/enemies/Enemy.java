package com.ruegnerlukas.ldgame.game.entities.enemies;

import com.ruegnerlukas.ldgame.game.Cell;
import com.ruegnerlukas.ldgame.game.World;
import com.ruegnerlukas.ldgame.game.entities.Entity;
import com.ruegnerlukas.ldgame.game.entities.Player;
import com.ruegnerlukas.ldgame.game.entities.weapons.Bomb;
import com.ruegnerlukas.ldgame.game.entities.weapons.Bullet;
import com.ruegnerlukas.ldgame.game.entities.weapons.Laser;
import com.ruegnerlukas.ldgame.scenes.GameScene;

public abstract class Enemy extends Entity {
	
	
	
	
	
	
	public boolean canMoveTo(Cell cell) {
		for(Entity e : cell.getEntities()) {
			if(e instanceof Enemy) {
				return false;
			}
		}
		return true;
	}
	
	
	public void onMove(Cell from, Cell to) {
		for(Entity e : to.getEntities()) {
			if(e instanceof Bullet) {
				Bullet b = (Bullet)e;
				if(b.source instanceof Player) {
					takeDamage(to, b, 1);
					to.removeLater(b);
					return;
				}
			}
			if(e instanceof Laser) {
				Laser l = (Laser)e;
				if(l.source instanceof Player && l.getState()==1) {
					if(takeDamage(to, l, 1)) {
						return;
					}
				}
			}
			if(e instanceof Bomb) {
				Bomb b = (Bomb)e;
				if(b.source instanceof Player && b.exists) {
					if(takeDamage(to, b, 1)) {
						return;
					}
				}
			}
			if(e instanceof Player) {
				((Player)e).takeDamage(to, this, 1);
				if(takeDamage(to, e, 1)) {
					return;
				}
			}
		}
	}
	
	
	
	public boolean takeDamage(Cell cell, Entity src, int dmg) {
		GameScene.particleMng.spawnHit(x*100+50, y*100+50, false, 0);
		cell.removeNow(this);
		if(src instanceof Bomb) {
			cell.removeNow(src);
		}
		GameScene.particleMng.spawnExplosionDeath(x*100+50, y*100+50, false);
		GameScene.shakeFrames = Math.max(GameScene.shakeFrames, 5);
		GameScene.shakeSize = Math.max(GameScene.shakeSize, 12);
		return true;
	}
	
	
}
