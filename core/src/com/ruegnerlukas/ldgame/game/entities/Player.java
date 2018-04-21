package com.ruegnerlukas.ldgame.game.entities;

import com.badlogic.gdx.Input.Keys;
import com.ruegnerlukas.input.InputManager;
import com.ruegnerlukas.input.InputManager.InputState;
import com.ruegnerlukas.input.actions.MultiKeyAction;
import com.ruegnerlukas.input.actions.InputAction.MultiMode;
import com.ruegnerlukas.ldgame.GDXInputReciever;
import com.ruegnerlukas.ldgame.game.Cell;
import com.ruegnerlukas.ldgame.game.World;
import com.ruegnerlukas.ldgame.game.entities.enemies.Enemy;
import com.ruegnerlukas.ldgame.game.entities.weapons.Bomb;
import com.ruegnerlukas.ldgame.game.entities.weapons.Bullet;
import com.ruegnerlukas.ldgame.game.entities.weapons.Laser;

public class Player extends Entity {
	
	private InputManager input;
	
	public int health = 100;
	public int power = 50;
	public int score = 1024;
	
	private long lastTurn = 0;
	
	private Bomb bomb = null;
	private Laser laser = null;
	
	
	
	
	public Player() {
		input = new InputManager();
		input.setReciever(new GDXInputReciever());
		input.addAction(new MultiKeyAction("moveUp", InputState.RELEASED, new int[]{Keys.W,Keys.UP}, MultiMode.ONE));
		input.addAction(new MultiKeyAction("moveDown", InputState.RELEASED, new int[]{Keys.S,Keys.DOWN}, MultiMode.ONE));
		input.addAction(new MultiKeyAction("moveLeft", InputState.RELEASED, new int[]{Keys.A,Keys.LEFT}, MultiMode.ONE));
		input.addAction(new MultiKeyAction("moveRight", InputState.RELEASED, new int[]{Keys.D,Keys.RIGHT}, MultiMode.ONE));
		input.addAction(new MultiKeyAction("shootBullet", InputState.RELEASED, new int[]{Keys.F}, MultiMode.ONE));
		input.addAction(new MultiKeyAction("shootLaser", InputState.RELEASED, new int[]{Keys.G}, MultiMode.ONE));
		input.addAction(new MultiKeyAction("shootBomb", InputState.RELEASED, new int[]{Keys.H}, MultiMode.ONE));

	}

	
	
	
	@Override
	public boolean update(long turnNum, int turnType, World world) {
	
		if(turnNum != lastTurn) {
			power = Math.max(0, Math.min(100, power+1));
			lastTurn = turnNum;
		}
		
		Cell currCell = world.getCell(x, y);
		
		onMove(currCell, currCell);
		
		boolean usedAction = false;

		
		if(bomb != null && !bomb.exists) {
			bomb = null;
		}
		
		if(laser != null && laser.getState()==4) {
			laser = null;
		}
		if(laser != null) {
			usedAction = true;
		}
		
		
		if(input.action("moveUp") && !usedAction && laser == null) {
			Cell cellDst = world.getCell(x, y+1);
			if(cellDst != null && canMoveTo(cellDst)) {
				world.getCell(x, y).remove(this);
				cellDst.add(this);
				onMove(currCell, cellDst);
				usedAction = true;
			}
		}
		
		if(input.action("moveDown") && !usedAction && laser == null) {
			Cell cellDst = world.getCell(x, y-1);
			if(cellDst != null && canMoveTo(cellDst)) {
				world.getCell(x, y).remove(this);
				cellDst.add(this);
				onMove(currCell, cellDst);
				usedAction = true;
			}
		}
		
		if(input.action("moveLeft") && !usedAction && laser == null) {
			Cell cellDst = world.getCell(x-1, y);
			if(cellDst != null && canMoveTo(cellDst)) {
				world.getCell(x, y).remove(this);
				cellDst.add(this);
				onMove(currCell, cellDst);
				usedAction = true;
			}
		}
		
		if(input.action("moveRight") && !usedAction && laser == null) {
			Cell cellDst = world.getCell(x+1, y);
			if(cellDst != null && canMoveTo(cellDst)) {
				world.getCell(x, y).remove(this);
				cellDst.add(this);
				onMove(currCell, cellDst);
				usedAction = true;
			}
		}
		
		if(input.action("shootBullet") && !usedAction && bomb==null && laser == null) {
			Cell cellTarget = world.getCell(x+1, y);
			if(cellTarget != null) {
				Bullet b = new Bullet();
				b.source = this;
				cellTarget.add(b);
				usedAction = true;
			}
		}
		
		if(input.action("shootLaser") && !usedAction && bomb==null && laser == null) {
			for(int lx = this.x+1; lx<world.getWidth(); lx++) {
				Cell cellTarget = world.getCell(lx, y);
				laser = new Laser(turnNum);
				laser.source = this;
				cellTarget.add(laser);
			}
			usedAction = true;
		}
		
		if(input.action("shootBomb") && !usedAction && bomb==null && laser == null) {
			Cell cellTarget = world.getCell(x+1, y);
			if(cellTarget != null) {
				Bomb b = new Bomb();
				b.source = this;
				cellTarget.add(b);
				usedAction = true;
				this.bomb = b;
			}
		}
		if(input.action("shootBomb") && !usedAction && bomb!=null && laser == null) {
			bomb.explode();
			bomb = null;
			usedAction = true;
		}
		
		
		input.update();
		
		return usedAction;
	}
	
	
	
	
	public boolean canMoveTo(Cell cell) {
		return true;
	}
	
	
	
	
	public void onMove(Cell from, Cell to) {
		for(Entity e : to.getEntities()) {
			if(e instanceof Bullet) {
				Bullet b = (Bullet)e;
				if( !(b.source instanceof Player)) {
					takeDamage(b, 1);
				}
				to.remove(b);
			}
			if(e instanceof Laser) {
				Laser l = (Laser)e;
				if( !(l.source instanceof Player) && l.getState()==1) {
					takeDamage(l, 1);
				}
			}
			if(e instanceof Enemy) {
				takeDamage(e, 1);
				((Enemy)e).takeDamage(to, this, 1);
			}
		}
	}
	
	
	
	
	public void takeDamage(Entity src, int dmg) {
		health = Math.max(0, health-100);
	}
	
	
}
