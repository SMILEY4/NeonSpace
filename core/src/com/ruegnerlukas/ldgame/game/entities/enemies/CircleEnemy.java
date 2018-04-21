package com.ruegnerlukas.ldgame.game.entities.enemies;

import com.ruegnerlukas.ldgame.game.Cell;
import com.ruegnerlukas.ldgame.game.World;

public class CircleEnemy extends Enemy {

	

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
		
		Cell cellDst = null;
		cellDst = world.getCell(x-1, y);

		if(cellDst != null && canMoveTo(cellDst)) {
			world.getCell(x, y).remove(this);
			cellDst.add(this);
			onMove(currCell, cellDst);
		}
		
		return true;
	}
	
}
