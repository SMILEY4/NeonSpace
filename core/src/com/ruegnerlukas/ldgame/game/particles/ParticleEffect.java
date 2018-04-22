package com.ruegnerlukas.ldgame.game.particles;

import java.util.List;

public abstract class ParticleEffect {

	/**
	 * @return true to remove effect
	 * */
	public abstract boolean update(int deltaMS);
	
	
	public abstract void spawnParticles(List<Particle> particles);

	
}
