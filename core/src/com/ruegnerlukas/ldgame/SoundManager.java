package com.ruegnerlukas.ldgame;

import java.security.KeyStore.Entry;
import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.sun.media.jfxmedia.events.NewFrameEvent;

public class SoundManager {

	
	public static Map<String, Sound> sounds;
	
	public static boolean enabled = true;
	
	
	public static void create() {
		sounds = new HashMap<String, Sound>();
		
		sounds.put("menu_click_1", Gdx.audio.newSound(Gdx.files.internal("sounds/MenuClick3.wav")));
		sounds.put("menu_click_2", Gdx.audio.newSound(Gdx.files.internal("sounds/MenuClick.wav")));
		sounds.put("explosion", Gdx.audio.newSound(Gdx.files.internal("sounds/Explosion.wav")));
		sounds.put("hit", Gdx.audio.newSound(Gdx.files.internal("sounds/Hit.wav")));
		sounds.put("shootBullet", Gdx.audio.newSound(Gdx.files.internal("sounds/ShootGun.wav")));
		sounds.put("shootBomb", Gdx.audio.newSound(Gdx.files.internal("sounds/Bomb.wav")));
		sounds.put("shootLaser", Gdx.audio.newSound(Gdx.files.internal("sounds/ShootLaser.wav")));
		sounds.put("move", Gdx.audio.newSound(Gdx.files.internal("sounds/Move.wav")));
		sounds.put("error", Gdx.audio.newSound(Gdx.files.internal("sounds/Error.wav")));
		sounds.put("spawn", Gdx.audio.newSound(Gdx.files.internal("sounds/Spawn.wav")));

	}
	
	
	public static void play(String name) {
		if(!enabled) {
			return;
		}
		
		Sound sound = sounds.get(name);
		if(sound != null) {
			
			long id = sound.play();
				
			if(name.equalsIgnoreCase("explosion")) {
				float pitch = 1f + (float)(Math.random()*0.4f) - 0.2f;
				sound.setPitch(id, pitch);
			}
			if(name.equalsIgnoreCase("hit")) {
				float pitch = 1f + (float)(Math.random()*0.4f) - 0.2f;
				sound.setPitch(id, pitch);
			}
			if(name.equalsIgnoreCase("shootBullet")) {
				float pitch = 1f + (float)(Math.random()*0.2f) - 0.1f;
				sound.setPitch(id, pitch);
			}
			if(name.equalsIgnoreCase("shootBomb")) {
				float pitch = 1f + (float)(Math.random()*0.2f) - 0.1f;
				sound.setPitch(id, pitch);
			}
			if(name.equalsIgnoreCase("shootLaser")) {
				float pitch = 1f + (float)(Math.random()*0.2f) - 0.1f;
				sound.setPitch(id, pitch);
			}
			if(name.equalsIgnoreCase("move")) {
				float pitch = 1f + (float)(Math.random()*0.2f) - 0.1f;
				sound.setPitch(id, pitch);
			}
			if(name.equalsIgnoreCase("spawn")) {
				float pitch = 1f + (float)(Math.random()*0.2f) - 0.1f;
				sound.setPitch(id, pitch);
			}
			
		}
	}
	
	
	
	public static void dispose() {
		for(Map.Entry<String, Sound> entry : sounds.entrySet()) {
			entry.getValue().dispose();
		}
	}
	
}
