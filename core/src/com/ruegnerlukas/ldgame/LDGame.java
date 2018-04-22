package com.ruegnerlukas.ldgame;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.ruegnerlukas.ldgame.scenes.*;
import com.ruegnerlukas.scenes.SceneManager;
import com.ruegnerlukas.scenes.transition.ColorFadeTransition;
import com.ruegnerlukas.simplemath.interpolation.InterpolationSineIn;
import com.ruegnerlukas.simplemath.vectors.vec4.Vector4f;

public class LDGame extends ApplicationAdapter {

	
	private FrameBuffer buffer;
	private SpriteBatch batch;
	private ShaderProgram ppShader;
	private float timeSec = 0;
	
	
	
	@Override
	public void create() {
		
		buffer = new FrameBuffer(Pixmap.Format.RGB888, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
		batch = new SpriteBatch();
		
		String vertexShader = "attribute vec4 " + ShaderProgram.POSITION_ATTRIBUTE + ";\n" //
				+ "attribute vec4 " + ShaderProgram.COLOR_ATTRIBUTE + ";\n" //
				+ "attribute vec2 " + ShaderProgram.TEXCOORD_ATTRIBUTE + "0;\n" //
				+ "uniform mat4 u_projTrans;\n" //
				+ "varying vec4 v_color;\n" //
				+ "varying vec2 v_texCoords;\n" //
				+ "\n" //
				+ "void main()\n" //
				+ "{\n" //
				+ "   v_color = " + ShaderProgram.COLOR_ATTRIBUTE + ";\n" //
				+ "   v_color.a = v_color.a * (255.0/254.0);\n" //
				+ "   v_texCoords = " + ShaderProgram.TEXCOORD_ATTRIBUTE + "0;\n" //
				+ "   gl_Position =  u_projTrans * " + ShaderProgram.POSITION_ATTRIBUTE + ";\n" //
				+ "}\n";
		
			String fragmentShader = "#ifdef GL_ES\n" //
				+ "#define LOWP lowp\n" //
				+ "precision mediump float;\n" //
				+ "#else\n" //
				+ "#define LOWP \n" //
				+ "#endif\n" //
				+ "varying LOWP vec4 v_color;\n" //
				+ "varying vec2 v_texCoords;\n" //
				+ "uniform sampler2D u_texture;"
				+ "uniform float u_time;\n"
				
				+ "float rand(vec2 co) {\r\n" + 
				"    float a = 12.9898;\r\n" + 
				"    float b = 78.233;\r\n" + 
				"    float c = 43758.5453;\r\n" + 
				"    float dt= dot(co.xy ,vec2(a,b));\r\n" + 
				"    float sn= mod(dt,3.14);\r\n" + 
				"    return fract(sin(sn) * c);\r\n" + 
				"}\r\n" + 
				"\r\n" + 
				"void main() {\r\n" + 
				"\r\n" + 
				"    float scale = 0.2;\r\n" + 
				"\r\n" + 
				"    vec2 iResolution = vec2(1200.0, 701.0);\r\n" + 
				"\r\n" + 
				"    vec2 uv = gl_FragCoord.xy / iResolution.xy;\r\n" + 
				"    uv.xy = vec2(uv.x, uv.y);" +
				"    \r\n" + 
				"    // chromatic abberation\r\n" + 
				"    vec2 d = abs((uv - 0.5) * 2.0);\r\n" + 
				"    d = pow(d, vec2(2.0, 2.0));\r\n" + 
				"    d *= scale;\r\n" + 
				"\r\n" + 
				"    // x distortion\r\n" + 
				"    uv.x += rand(vec2(cos(u_time*0.01),sin(uv.y)))*0.001;\r\n" + 
				"\r\n" + 
				"    vec4 r = texture2D(u_texture, uv - d * 0.015);\r\n" + 
				"    vec4 g = texture2D(u_texture, uv);\r\n" + 
				"    vec4 b = texture2D(u_texture, uv);\r\n" + 
				"    \r\n" + 
				"    vec4 col = vec4(r.r, g.g, b.b, 1.0);\r\n" + 
				"\r\n" + 
				"    // static noise\r\n" + 
				"    col.rgb *= rand(vec2(uv.x*sin(u_time), uv.y*cos(u_time))) * 0.2 + 0.8;\r\n" + 
				"\r\n" + 
				"    // tv line\r\n" + 
				"    float scanline = sin(uv.y*800.0)*0.02;\r\n" + 
				"    col -= scanline;\r\n" + 
				"\r\n" + 
				"    gl_FragColor = col.rgba;\r\n" + 
				"}"
				;
			
		ppShader = new ShaderProgram(vertexShader, fragmentShader);
		
		
		SceneManager.get().addScene("start_scene", new StartScene());
		SceneManager.get().addScene("menu_scene", new MenuScene());
		SceneManager.get().addScene("game_scene", new GameScene());
		SceneManager.get().addScene("gameover_scene", new GameOverScene());
		SceneManager.get().addScene("exit_scene", new ExitScene());

		SceneManager.get().changeScene("start_scene", null, new ColorFadeTransition(2000, new Vector4f(0,0,0,1f), new Vector4f(0,0,0,0), new InterpolationSineIn()));

		SceneManager.get().addScene("overlay_scene", new OverlayScene());
		SceneManager.get().setStaticScene("overlay_scene");
		
	}


	
	
	@Override
	public void render() {
		int deltaMS = (int)(Gdx.graphics.getDeltaTime()*1000);
		
		timeSec += ((float)deltaMS/1000f);
		
		buffer.begin();
		SceneManager.get().update(deltaMS);
		buffer.end();
		
		batch.setShader(ppShader);
		batch.begin();
		ppShader.setUniformf("u_time", timeSec);
		batch.draw(buffer.getColorBufferTexture(), 0, 0);
		batch.end();
	}


	
	
	@Override
	public void dispose() {
		ppShader.dispose();
		buffer.dispose();
		batch.dispose();
		SceneManager.get().unloadAll();
	}

	
}
