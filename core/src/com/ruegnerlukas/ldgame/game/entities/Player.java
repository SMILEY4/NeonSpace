package com.ruegnerlukas.ldgame.game.entities;

import java.util.ListIterator;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Circle;
import com.ruegnerlukas.input.InputManager;
import com.ruegnerlukas.input.InputManager.InputState;
import com.ruegnerlukas.input.actions.MultiKeyAction;
import com.ruegnerlukas.input.actions.InputAction.MultiMode;
import com.ruegnerlukas.ldgame.GDXInputReciever;
import com.ruegnerlukas.ldgame.SoundManager;
import com.ruegnerlukas.ldgame.game.Cell;
import com.ruegnerlukas.ldgame.game.World;
import com.ruegnerlukas.ldgame.game.entities.enemies.BulletEnemy;
import com.ruegnerlukas.ldgame.game.entities.enemies.CircleEnemy;
import com.ruegnerlukas.ldgame.game.entities.enemies.Enemy;
import com.ruegnerlukas.ldgame.game.entities.enemies.LaserEnemy;
import com.ruegnerlukas.ldgame.game.entities.weapons.Bomb;
import com.ruegnerlukas.ldgame.game.entities.weapons.Bullet;
import com.ruegnerlukas.ldgame.game.entities.weapons.Laser;
import com.ruegnerlukas.ldgame.scenes.GameScene;
import com.ruegnerlukas.simplemath.vectors.vec3.Vector3i;

public class Player extends Entity {
	
	private InputManager input;
	
	public static int score = 0;
	public int health = 100;
	public int power = 5;
	
	private long lastTurn = 0;
	
	public Bomb bomb = null;
	public Laser laser = null;
	
	
	
	
	public Player() {
		score = 0;
		input = new InputManager();
		input.setReciever(new GDXInputReciever());
		input.addAction(new MultiKeyAction("moveUp", InputState.RELEASED, new int[]{Keys.W,Keys.UP}, MultiMode.ONE));
		input.addAction(new MultiKeyAction("moveDown", InputState.RELEASED, new int[]{Keys.S,Keys.DOWN}, MultiMode.ONE));
		input.addAction(new MultiKeyAction("moveLeft", InputState.RELEASED, new int[]{Keys.A,Keys.LEFT}, MultiMode.ONE));
		input.addAction(new MultiKeyAction("moveRight", InputState.RELEASED, new int[]{Keys.D,Keys.RIGHT}, MultiMode.ONE));
		input.addAction(new MultiKeyAction("shootBullet", InputState.RELEASED, new int[]{Keys.F}, MultiMode.ONE));
		input.addAction(new MultiKeyAction("shootBomb", InputState.RELEASED, new int[]{Keys.G}, MultiMode.ONE));
		input.addAction(new MultiKeyAction("shootLaser", InputState.RELEASED, new int[]{Keys.H}, MultiMode.ONE));
//		input.addAction(new MultiKeyAction("nothing", InputState.RELEASED, new int[]{Keys.SPACE}, MultiMode.ONE));

	}

	
	
	
	@Override
	public boolean update(long turnNum, int turnType, World world) {
		if(bomb != null && !bomb.exists) {
			bomb = null;
		}
		
		if(turnType != 0) {
			return false;
		}
		
		// replenish power
		if(turnNum != lastTurn) {
			power = Math.max(0, Math.min(100, power+1));
			lastTurn = turnNum;
		}
		
		boolean usedAction = false;

		
		// handle bomb, laser
		if(bomb != null && !bomb.exists) {
			bomb = null;
		}
		if(laser != null && laser.getState()==4) {
			laser = null;
		}
		if(laser != null) {
			usedAction = true;
		}
		
		// handle input
		if(input.action("nothing") && !usedAction && laser == null) {
			usedAction = true;
		}
		
		if(input.action("moveUp") && !usedAction && laser == null) {
			Cell cellDst = world.getCell(x, y+1);
			if(cellDst != null && canMoveTo(cellDst)) {
				SoundManager.play("move");
				world.getCell(x, y).removeNow(this);
				cellDst.add(this, false);
				usedAction = true;
			}
		}
		
		if(input.action("moveDown") && !usedAction && laser == null) {
			Cell cellDst = world.getCell(x, y-1);
			if(cellDst != null && canMoveTo(cellDst)) {
				SoundManager.play("move");
				world.getCell(x, y).removeNow(this);
				cellDst.add(this, false);
				usedAction = true;
			}
		}
		
		if(input.action("moveRight") && !usedAction && laser == null) {
			Cell cellDst = world.getCell(x+1, y);
			if(cellDst != null && canMoveTo(cellDst)) {
				SoundManager.play("move");
				world.getCell(x, y).removeNow(this);
				cellDst.add(this, false);
				usedAction = true;
			}
		}
		
		if(input.action("moveLeft") && !usedAction && laser == null) {
			Cell cellDst = world.getCell(x-1, y);
			if(cellDst != null && canMoveTo(cellDst)) {
				SoundManager.play("move");
				world.getCell(x, y).removeNow(this);
				cellDst.add(this, false);
				usedAction = true;
			}
		}
		
		if(input.action("shootBullet") && !usedAction && bomb==null && laser == null) {
			if(power >= 2) {
				Cell cellTarget = world.getCell(x+1, y);
				if(cellTarget != null) {
					Bullet b = new Bullet();
					b.source = this;
					cellTarget.add(b, true);
					usedAction = true;
					power -= 2;
					SoundManager.play("shootBullet");
				}
			} else {
				SoundManager.play("error");
				GameScene.errorMessage = "Not enough energy ("+power+"/2)";
				GameScene.errorMsgTime = System.currentTimeMillis();
			}
		}
		
		if(input.action("shootLaser") && !usedAction && bomb==null && laser == null) {
			if(power >= 15) {
				for(int lx = this.x+1; lx<world.getWidth(); lx++) {
					Cell cellTarget = world.getCell(lx, y);
					laser = new Laser(turnNum);
					laser.source = this;
					cellTarget.add(laser, true);
				}
				GameScene.particleMng.spawnLaser((x+1)*100+50, (world.getWidth()-(x+1))*100+50, y*100+50, 0, true, laser);
				usedAction = true;
				power -= 15;
			} else {
				SoundManager.play("error");
				GameScene.errorMessage = "Not enough energy ("+power+"/15)";
				GameScene.errorMsgTime = System.currentTimeMillis();
			}
			
		}
		
		if(input.action("shootBomb") && !usedAction && bomb==null && laser == null) {
			if(power >= 8) {
				Cell cellTarget = world.getCell(x+1, y);
				if(cellTarget != null) {
					Bomb b = new Bomb();
					b.source = this;
					cellTarget.add(b, true);
					usedAction = true;
					this.bomb = b;
					SoundManager.play("shootBomb");

				}
				power -= 8;
			} else {
				SoundManager.play("error");
				GameScene.errorMessage = "Not enough energy ("+power+"/8)";
				GameScene.errorMsgTime = System.currentTimeMillis();
			}		
		}
		
		if(input.action("shootBomb") && !usedAction && bomb!=null && laser == null) {
			bomb.explode();
			bomb = null;
			usedAction = true;
		}
		
		
		
		// handle collisions
		Cell cell = world.getCell(x, y);

		for(int i=0; i<cell.getEntities().size(); i++) {
			Entity e = cell.getEntities().get(i);
			
			if(e instanceof Enemy) {
				takeDamage(cell, e, 1);
				((Enemy) e).takeDamage(cell, this, 1);
				cell.getEntities().remove(e);
			}
			if(e instanceof Bomb) {
				if( !(((Bomb)e).source instanceof Player) ) {
					takeDamage(cell, e, 1);
					cell.getEntities().remove(e);
				}
			}
			if(e instanceof Bullet) {
				if( !(((Bullet)e).source instanceof Player) ) {
					takeDamage(cell, e, 1);
					cell.getEntities().remove(e);
				}
			}
			if(e instanceof Laser) {
				if( !(((Laser)e).source instanceof Player) && (((Laser)e).getState()==2 || ((Laser)e).getState()==1) ) {
//					takeDamage(cell, e, 1);
				}
			}
		}
		
		// update input
		input.update();

		// return true, if player ended turn
		return usedAction;
	}
	
	
	
	
	public boolean canMoveTo(Cell cell) {
		return true;
	}
	
	
	
	
	public void takeDamage(Cell cell, Entity src, int dmg) {
		
		GameScene.particleMng.spawnHit(x*100+50, y*100+50, true, 1);
		GameScene.screenShake.add(new Vector3i(6, 2, 0));
		
		SoundManager.play("hit");
		
		if( (src instanceof CircleEnemy) || (src instanceof BulletEnemy) || (src instanceof LaserEnemy)) {
			health -= 40;
		}
		if( (src instanceof Bullet) ) {
			health -= 40;
		}
		if( (src instanceof Bomb) ) {
			health -= 60;
		}
		if( (src instanceof Laser) ) {
			health -= 90;
		}
		
		health = Math.max(0, health);
		if(health <= 0) {
			SoundManager.play("explosion");
			GameScene.particleMng.spawnExplosionDeath(x*100+50, y*100+50, true);
			cell.removeNow(this);
			GameScene.screenShake.add(new Vector3i(16, 10, 0));

		}
	}
	
	
}
