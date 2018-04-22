package com.ruegnerlukas.ldgame.game.entities.enemies;

import com.ruegnerlukas.ldgame.SoundManager;
import com.ruegnerlukas.ldgame.game.Cell;
import com.ruegnerlukas.ldgame.game.World;
import com.ruegnerlukas.ldgame.game.entities.Entity;
import com.ruegnerlukas.ldgame.game.entities.Player;
import com.ruegnerlukas.ldgame.game.entities.weapons.Bomb;
import com.ruegnerlukas.ldgame.game.entities.weapons.Bullet;
import com.ruegnerlukas.ldgame.game.entities.weapons.Laser;
import com.ruegnerlukas.ldgame.scenes.GameScene;
import com.ruegnerlukas.simplemath.vectors.vec3.Vector3i;

public abstract class Enemy extends Entity {
	
	
	
	public boolean hasShield = false;
	
	
	public boolean canMoveTo(Cell cell) {
		for(Entity e : cell.getEntities()) {
			if(e instanceof Enemy) {
				return false;
			}
		}
		return true;
	}
	
	
	
	
	public boolean takeDamage(Cell cell, Entity src, int dmg) {
		GameScene.particleMng.spawnHit(x*100+50, y*100+50, false, 0);
		if(hasShield) {
			hasShield = false;
			SoundManager.play("hit");
		} else {
			cell.removeNow(this);
			if(src instanceof Bomb) {
				cell.removeNow(src);
			}
			SoundManager.play("explosion");
			GameScene.particleMng.spawnExplosionDeath(x*100+50, y*100+50, false);
			GameScene.screenShake.add(new Vector3i(12, 5, 0));
		}
		return true;
	}
	
	
}
