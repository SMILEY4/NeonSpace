package com.ruegnerlukas.ldgame.game.entities.weapons;

import com.ruegnerlukas.ldgame.game.Cell;
import com.ruegnerlukas.ldgame.game.World;
import com.ruegnerlukas.ldgame.game.entities.Entity;

public class Laser extends Entity {
	
	public Entity source;
	
	private long startTurn = 0;
	private int nTurns = 3;
	private int state = 0;


	public Laser(long startTurn) {
		this.startTurn = startTurn;
	}
	
	
	
	
	public int getState() {
		return this.state;
	}
	
	
	
	@Override
	public boolean update(long turnNum, int turnType, World world) {
		if(turnType != 1) {
			return false;
		}
		
		
		
		int turn = (int)(turnNum-startTurn);
		
		if(turn == 1) {
			state = 1;
		}
		if(turn == nTurns-1) {
			state = 2;
		}
		
		if(turn >= nTurns) {
			Cell currCell = world.getCell(x, y);
			currCell.remove(this);
			state = 4;
		}
		
		
		
		return true;
	}
	
	
	
	
	
	
}
