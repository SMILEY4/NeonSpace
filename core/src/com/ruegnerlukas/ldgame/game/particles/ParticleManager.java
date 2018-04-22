package com.ruegnerlukas.ldgame.game.particles;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.ruegnerlukas.ldgame.game.entities.weapons.Laser;

public class ParticleManager {
	
//	private ShaderProgram glowShader;
//	private SpriteBatch batch;
	
	private ShapeRenderer renderer;

	private List<ParticleEffect> effects;
	private List<Particle> particles;

	
	
	
	public ParticleManager() {
		renderer = new ShapeRenderer();
		effects = new ArrayList<ParticleEffect>();
		particles = new ArrayList<Particle>();
	}

	
	
	
	
	public void spawnExplosionDeath(int x, int y, boolean isPlayerColor) {
		ExplosionEffect e = new ExplosionEffect(x, y, isPlayerColor);
		effects.add(e);
	}
	
	
	public void spawnHit(int x, int y, boolean isPlayerColor, int dir) {
		HitEffect e = new HitEffect(x, y, isPlayerColor, dir);
		effects.add(e);
	}
	
	public void spawnBomb(int x, int y, boolean isPlayerColor) {
		BombEffect e = new BombEffect(x, y, isPlayerColor);
		effects.add(e);
	}
	
	
	public void spawnLaser(int x0, int x1, int y, int dir, boolean isPlayerColor, Laser laser) {
		LaserEffect e = new LaserEffect(x0, x1, y, dir, isPlayerColor, laser);
		effects.add(e);
	}
	
	
	
	public void update(int deltaMS) {

		for(int i=0; i<effects.size(); i++) {
			ParticleEffect e = effects.get(i);
			boolean remove = e.update(deltaMS);
			if(remove) {
				effects.remove(i);
			} else {
				e.spawnParticles(particles);
			}
		}
	
		
		renderer.begin(ShapeType.Filled);
		
		for(int i=0; i<particles.size(); i++) {
			Particle p = particles.get(i);

			if(p.isBigChunk) {
				renderer.rect(p.x-2, p.y-2, 4, 4);
			} else {
				renderer.rect(p.x, p.y, 1, 1);
			}

			p.x += p.dx;
			p.y += p.dy;
			
			if(p.timeTotal < System.currentTimeMillis()-p.timeStart) {
				particles.remove(i);
			}
			
		}
		
		renderer.end();
		
	}
	
	
	
	
//	public void setGlowShader(ShaderProgram glowShader) {
//		this.glowShader = glowShader;
//	}
	
	
	
	
	
	public void unload() {
		renderer.dispose();
//		batch.dispose();
		particles.clear();
		effects.clear();
	}
	

}
