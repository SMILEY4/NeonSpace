package com.ruegnerlukas.ldgame.game.particles;

import java.util.List;
import java.util.Random;

public class HitEffect extends ParticleEffect {


	private int x;
	private int y;
	private boolean isPlayerColor;
	private int ticks = 0;
	private int direction;
	
	private final float MIN_SPEED = 1;
	private final float MAX_SPEED = 15;

	
	public HitEffect(int x, int y, boolean isPlayerColor, int direction) {
		this.x = x;
		this.y = y;
		this.isPlayerColor = isPlayerColor;
		this.direction = direction;
	}
	
	
	
	
	
	
	@Override
	public boolean update(int deltaMS) {
		ticks++;
		if(ticks>=2) {
			return true;
		} else {
			return false;
		}
	}
	
	
	

	@Override
	public void spawnParticles(List<Particle> particles) {
	
		Random random = new Random();
		
		for(int i=0; i<100; i++) {
			Particle p = new Particle();
			p.x = x;
			p.y = y;
			
			if(direction == 0) {
				p.dx = (-random.nextFloat()) * MAX_SPEED + MIN_SPEED;
			}
			if(direction == 1) {
				p.dx = (random.nextFloat()) * MAX_SPEED + MIN_SPEED;
			}
			if(direction == 2) {
				p.dx = (random.nextFloat()*2-1) * MAX_SPEED + MIN_SPEED;
			}
			
			p.dy = ( (random.nextFloat()*0.7f)*2-1) * MAX_SPEED + MIN_SPEED;
			p.isPlayerColor = isPlayerColor;
			p.isBigChunk = random.nextFloat() < 0.1f;
			p.timeStart = System.currentTimeMillis();
			p.timeTotal = random.nextInt(600)+100;
			particles.add(p);
		}
	
	}

	
	
}













