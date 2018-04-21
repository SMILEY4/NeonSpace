package com.ruegnerlukas.ldgame.game.entities.enemies;

import com.ruegnerlukas.ldgame.game.Cell;
import com.ruegnerlukas.ldgame.game.World;
import com.ruegnerlukas.ldgame.game.entities.Entity;
import com.ruegnerlukas.ldgame.game.entities.Player;
import com.ruegnerlukas.ldgame.game.entities.weapons.Bomb;
import com.ruegnerlukas.ldgame.game.entities.weapons.Bullet;
import com.ruegnerlukas.ldgame.game.entities.weapons.Laser;

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
					to.remove(b);
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
				((Player)e).takeDamage(this, 1);
				if(takeDamage(to, e, 1)) {
					return;
				}
			}
		}
	}
	
	
	
	public boolean takeDamage(Cell cell, Entity src, int dmg) {
		cell.remove(this);
		if(src instanceof Bomb) {
			cell.remove(src);
		}
		System.out.println("Enemy took " + dmg + " damage from " + src);
		return true;
	}
	
	
}
