package com.ruegnerlukas.ldgame.game.particles;

import java.util.List;
import java.util.Random;

import com.ruegnerlukas.simplemath.vectors.vec2.Vector2f;

public class ExplosionEffect extends ParticleEffect {


	private int x;
	private int y;
	private boolean isPlayerColor;
	private int ticks = 0;
	
	private final float MIN_SPEED = 1;
	private final float MAX_SPEED = 25;

	
	public ExplosionEffect(int x, int y, boolean isPlayerColor) {
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
		Vector2f tmp = new Vector2f();
		
		for(int i=0; i<2000; i++) {
			Particle p = new Particle();
			p.x = x;
			p.y = y;
			
//			p.dx = (random.nextFloat()*2-1) * MAX_SPEED + MIN_SPEED;
//			p.dy = (random.nextFloat()*2-1) * MAX_SPEED + MIN_SPEED;
			
			p.dx = (random.nextFloat()*2-1);
			p.dy = (random.nextFloat()*2-1);
			tmp.set(p.dx, p.dy);
			tmp.normalize();
			tmp.setLength(random.nextFloat()*(MAX_SPEED-MIN_SPEED)+MIN_SPEED);
			
			p.dx = tmp.x;
			p.dy = tmp.y;
			
			p.isPlayerColor = isPlayerColor;
			p.isBigChunk = random.nextFloat() < 0.2f;
			p.timeStart = System.currentTimeMillis();
			float f = random.nextFloat();
			p.timeTotal = (int)((f*f)*500+100);
			particles.add(p);
		}
	
	}

	
	
}













