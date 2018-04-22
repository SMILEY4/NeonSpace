package com.ruegnerlukas.ldgame.game.particles;

import java.util.List;
import java.util.Random;

import com.ruegnerlukas.ldgame.game.entities.weapons.Laser;

public class LaserEffect extends ParticleEffect {


	private int x0, x1;
	private int y;
	private boolean isPlayerColor;
	private int dir;
	
	private Laser laser;
	
	private final float MIN_SPEED = 15;
	private final float MAX_SPEED = 30;

	
	public LaserEffect(int x0, int x1, int y, int dir, boolean isPlayerColor, Laser laser) {
		this.x0 = x0;
		this.x1 = x1;
		this.y = y;
		this.dir = dir;
		this.isPlayerColor = isPlayerColor;
		this.laser = laser;
	}
	
	
	
	
	
	
	@Override
	public boolean update(int deltaMS) {
		boolean done = laser.getState() == 4;
		return done;
	}
	
	
	

	@Override
	public void spawnParticles(List<Particle> particles) {
	
		Random random = new Random();
		
		int n = 0;
		if(laser.getState() == 0) {
			n = 1;
		}
		if(laser.getState() == 1) {
			n = 20;
		}
		if(laser.getState() == 2) {
			n = 10;
		}
		
		for(int i=0; i<n; i++) {
			Particle p = new Particle();
			
			
			if(dir == 0) {
				p.x = random.nextInt(Math.abs(x1-x0)) + x0;
			} else {
				p.x = random.nextInt(Math.abs(x1-x0));
			}
			p.y = random.nextInt(100) + (y-50);
			
			p.dx = (random.nextFloat()*(MAX_SPEED-MIN_SPEED) + MIN_SPEED) * (dir == 0 ? 1 : -1);
			p.dy = 0;
			
			p.isPlayerColor = isPlayerColor;
			p.isBigChunk = random.nextFloat() < 0.1f;
			p.timeStart = System.currentTimeMillis();
			p.timeTotal = random.nextInt(500)+10;
			particles.add(p);
		}
	
	}

	
	
}













