package com.ruegnerlukas.ldgame.game.particles;

import java.util.List;
import java.util.Random;

import com.ruegnerlukas.simplemath.vectors.vec2.Vector2f;

public class SpawnEffect extends ParticleEffect {


	private int x;
	private int y;
	private boolean isPlayerColor;
	private int ticks = 0;
	
	private final float MIN_DIST = 100;
	private final float MAX_DIST = 300;

	private final float MIN_SPEED = 3;
	private final float MAX_SPEED = 5;
	
	public SpawnEffect(int x, int y, boolean isPlayerColor) {
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
		
		for(int i=0; i<300; i++) {
			Particle p = new Particle();
			
			p.isAttracted = true;
			p.attX = x;
			p.attY = y;
			
			p.x = (random.nextFloat()*2-1);
			p.y = (random.nextFloat()*2-1);
			tmp.set(p.x, p.y);
			tmp.normalize();
			tmp.setLength(random.nextFloat()*(MAX_DIST-MIN_DIST)+MIN_DIST);
			
			p.x = x + tmp.x;
			p.y = y + tmp.y;

			p.dx = random.nextFloat() * (MAX_SPEED-MIN_SPEED) + MIN_SPEED;
			p.dy = p.dx;
			
			p.isPlayerColor = isPlayerColor;
			p.isBigChunk = random.nextFloat() < 0.2f;
			p.timeStart = System.currentTimeMillis();
			p.timeTotal = random.nextInt(1500)+100;
			particles.add(p);
		}
	
	}

	
	
}













