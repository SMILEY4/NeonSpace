package com.ruegnerlukas.ldgame.game.entities;

import com.ruegnerlukas.ldgame.game.World;

public class Entity {
	
	public int x;
	public int y;
	
	public long lastUpdate;
	
	/**
	 * @return true, if entity did sth (->mainly for player actions)
	 * */
	public boolean update(long turnNum, int turnType, World world) {
		return false;
	}

}
