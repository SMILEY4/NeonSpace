package com.ruegnerlukas.ldgame.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.ruegnerlukas.ldgame.game.entities.enemies.BombEnemy;
import com.ruegnerlukas.ldgame.game.entities.enemies.BulletEnemy;
import com.ruegnerlukas.ldgame.game.entities.enemies.CircleEnemy;
import com.ruegnerlukas.ldgame.game.entities.enemies.Enemy;
import com.ruegnerlukas.ldgame.game.entities.enemies.LaserEnemy;

public class WaveGenerator {

	
//	public static void main(String[] args) {
//		
//		for(int i=0; i<1; i++) {
//			
//			WaveGenerator gen = new WaveGenerator();
//			
//			for(int j=0; j<30; j++) {
//				
//				System.out.println();
//				System.out.println();
//				System.out.println("WAVE: " + j);
//				
//				List<Enemy> enemies = gen.getNext();
//				for(Enemy e : enemies) {
//					System.out.println(e);
//				}
//				
//			}
//			
//		}
//		
//	}
//	
	
	
	private int currentWave = 1;
	
	
	
	public List<Enemy> getNext() {
		
		
		int points = (int) eq(currentWave);
		List<Enemy> enemies = new ArrayList<Enemy>();
		Random random = new Random();
		
		if(currentWave == 1) {
			CircleEnemy enemy = new CircleEnemy();
			boolean placed = setPosition(enemies, enemy, random);
			enemies.add(enemy);
			currentWave++;
			return enemies;
		}
		
		if(currentWave == 2) {
			CircleEnemy enemy1 = new CircleEnemy();
			CircleEnemy enemy2 = new CircleEnemy();
			boolean placed1 = setPosition(enemies, enemy1, random);
			boolean placed2 = setPosition(enemies, enemy2, random);
			enemies.add(enemy1);
			enemies.add(enemy2);
			currentWave++;
			return enemies;
		}
		
		
		
		while(points > 0) {
			
			int pEnemy = random.nextInt(Math.min(points, 3))+1;
			points -= pEnemy;
			
			if(pEnemy == 1) {
				CircleEnemy enemy = new CircleEnemy();
				boolean placed = setPosition(enemies, enemy, random);
				if(placed) {
					enemies.add(enemy);
				}
			}
			
			boolean bomb = random.nextFloat() < 0.4f;
			if(pEnemy == 2 && !bomb) {
				BulletEnemy enemy = new BulletEnemy();
				boolean placed = setPosition(enemies, enemy, random);
				if(placed) {
					enemies.add(enemy);
				}
			}
			if(pEnemy == 2 && bomb) {
				BombEnemy enemy = new BombEnemy();
				boolean placed = setPosition(enemies, enemy, random);
				if(placed) {
					enemies.add(enemy);
				}
			}
			
			if(pEnemy == 3) {
				LaserEnemy enemy = new LaserEnemy();
				boolean placed = setPosition(enemies, enemy, random);
				if(placed) {
					enemies.add(enemy);
				}
			}
			
			
		}
		

		
		currentWave++;
		return enemies;
	}
	
	
	private boolean setPosition(List<Enemy> enemies, Enemy enemy, Random random) {
		
		boolean placed = true;
		
		int cnt = 0;
		while(true) {
			cnt++;
			enemy.x = random.nextInt(5)+6;
			enemy.y = random.nextInt(6);
			placed = true;
			
			for(Enemy e : enemies) {
				if(e.x == enemy.x && e.y == enemy.y) {
					placed = false;
					break;
				}
			}
			if(placed || cnt>60) {
				break;
			}
		}
		
		return placed;
	}
	
	

	private float eq(float x) {
		return (float) Math.pow((Math.log(x) + 1), 1.5f) + x*0.3f;
	}
	
	
	
}


