package com.ruegnerlukas.ldgame.game;

import java.util.ArrayList;
import java.util.List;

import com.ruegnerlukas.ldgame.game.entities.Entity;
import com.ruegnerlukas.ldgame.game.entities.Player;
import com.ruegnerlukas.ldgame.game.entities.enemies.Enemy;


public class Cell {

	public final int x;
	public final int y;
	private List<Entity> entities;
	private List<Entity> toRemove;
	
	
	
	public Cell(int x, int y) {
		this.x = x;
		this.y = y;
		this.entities = new ArrayList<Entity>();
		this.toRemove = new ArrayList<Entity>();

	}
	
	
	
	public boolean blockedFor(Entity e) {
		if(e instanceof Player) {
			for(Entity entity : entities) {
				if(entity instanceof Enemy) {
					return true;
				}
			}
		}
		if(e instanceof Enemy) {
			for(Entity entity : entities) {
				if(entity instanceof Player) {
					return true;
				}
				if(entity instanceof Enemy) {
					return true;
				}
			}
		}
		return false;
	}
	
	
	
	
	public void add(Entity e) {
		e.x = x;
		e.y = y;
		entities.add(e);
	}
	
	
	
	
	public void removeNow(Entity e) {
		entities.remove(e);
	} 
	
	
	
	
	public void removeLater(Entity e) {
		toRemove.add(e);
	} 
	
	
	public void removeAllDead() {
		entities.removeAll(toRemove);
		toRemove.clear();
	}
	
	
	
	public List<Entity> getEntities() {
		return entities;
	}
	
	
}
