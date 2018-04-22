package com.ruegnerlukas.ldgame.game.particles;

import java.util.List;
import java.util.Random;

public class BombEffect extends ParticleEffect {



	private int x;
	private int y;
	private boolean isPlayerColor;
	private int ticks = 0;
	

	
	public BombEffect(int x, int y, boolean isPlayerColor) {
		this.x = x;
		this.y = y;
		this.isPlayerColor = isPlayerColor;
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
		
		for(int i=0; i<80; i++) {
			Particle p = new Particle();
			
			
			p.x = random.nextInt(300) + (x-100);
			p.y = random.nextInt(300) + (y-100);
			
			p.dx = 0;
			p.dy = 0;
			p.isPlayerColor = isPlayerColor;
			p.isBigChunk = random.nextFloat()<0.05f;
			p.timeStart = System.currentTimeMillis();
			p.timeTotal = random.nextInt(1000)+100;
			particles.add(p);
		}
	
	}

	
	
}













