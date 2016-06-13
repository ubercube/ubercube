/*
 * Copyright (C) 2016 Team Ubercube
 *
 * This file is part of Ubercube.
 *
 *     Ubercube is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Ubercube is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Ubercube.  If not, see http://www.gnu.org/licenses/.
 */

package fr.veridiangames.client.rendering.renderers.guis;

import fr.veridiangames.client.guis.TrueTypeFont;
import fr.veridiangames.client.guis.TrueTypeFont.IntObject;
import fr.veridiangames.client.rendering.Camera;
import fr.veridiangames.client.rendering.buffers.Buffers;
import fr.veridiangames.client.rendering.shaders.Gui3DShader;
import fr.veridiangames.client.rendering.shaders.GuiShader;
import fr.veridiangames.client.rendering.textures.Texture;
import fr.veridiangames.core.maths.*;
import fr.veridiangames.core.utils.Color4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL13;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;

public class Font3DRenderer
{
	private int vbo;
	private FloatBuffer buffer;
	private int size;

	private TrueTypeFont font;
	private Transform transform;
	private int w, h;
	private String text;

	public Font3DRenderer(TrueTypeFont font, String text, Vec3 position) {
		this.font = font;
		this.transform = new Transform(position);
		this.text = new String(text);
		
		size = text.length() * 20;
		vbo = Buffers.createVertexBuffer();
		createBuffer();
	}
	
	public void setText(String text) {
		this.size = text.length() * 20;	
		if (this.text.length() != text.length()) {
			this.text = text; 
			createBuffer();			
		}else {
			this.text = text;
			updateBuffer();			
		}
	}
	
	public void setPosition(Vec3 position) {
		this.transform.setLocalPosition(position);
	}
	
	private void createBuffer() {
		buffer = BufferUtils.createFloatBuffer(size);
		
		IntObject intObject = null;
		int charCurrent;

		int totalwidth = 0;
		
		int line = 0;
		for (int i = 0; i < text.length(); i++) {
			charCurrent = text.charAt(i);
			if (charCurrent < 256) {
				intObject = font.charArray[charCurrent];
			} else {
				intObject = (IntObject)font.customChars.get( new Character( (char) charCurrent ) );
			} 
			
			if (charCurrent == '\n') {
				line++;
				totalwidth = 0;
			}
			
			if( intObject != null ) {
				buffer.put(quadData(totalwidth, 0 + line * intObject.height,
						(totalwidth + intObject.width),
						intObject.height + line * intObject.height, intObject.storedX,
						intObject.storedY, intObject.storedX + intObject.width,
						intObject.storedY + intObject.height));
				
				totalwidth += intObject.width;
			}
		}
		if (intObject != null) {
			w = totalwidth;
			h = intObject.height + line * intObject.height;
		}else {
			w = 0;
			h = 0;
		}
		
		line = 0;
		buffer.flip();
		
		glBindBuffer(GL_ARRAY_BUFFER, vbo);
		glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
		
		buffer.clear();
	}
	
	private float[] quadData(float drawX, float drawY, float drawX2, float drawY2, float srcX, float srcY, float srcX2, float srcY2) {
		float DrawWidth = drawX2 - drawX;
		float DrawHeight = drawY2 - drawY;
		float TextureSrcX = srcX / font.textureWidth;
		float TextureSrcY = srcY / font.textureHeight;
		float SrcWidth = srcX2 - srcX;
		float SrcHeight = srcY2 - srcY;
		float RenderWidth = (SrcWidth / font.textureWidth);
		float RenderHeight = (SrcHeight / font.textureHeight);
		
		float[] data = new float[] {
			drawX, drawY + DrawHeight, 0, TextureSrcX, TextureSrcY,
			drawX + DrawWidth, drawY + DrawHeight, 0, TextureSrcX + RenderWidth, TextureSrcY,
			drawX + DrawWidth, drawY, 0, TextureSrcX + RenderWidth, TextureSrcY + RenderHeight,
			drawX, drawY, 0, TextureSrcX, TextureSrcY + RenderHeight
		};
		
		return data;
	}
	
	private void updateBuffer() {
		if (buffer.capacity() == 0 && size != 0) {
			createBuffer();
			return;
		}
		
		
		IntObject intObject = null;
		int charCurrent;

		int totalwidth = 0;
		
		int line = 0;

		for (int i = 0; i < text.length(); i++) {
			charCurrent = text.charAt(i);
			if (charCurrent < 256) {
				intObject = font.charArray[charCurrent];
			} else {
				intObject = (IntObject)font.customChars.get( new Character( (char) charCurrent ) );
			} 
			
			if (charCurrent == '\n') {
				line++;
				totalwidth = 0;
			}
			
			if (intObject != null) {
				buffer.put(quadData(totalwidth, 0 + line * intObject.height,
						(totalwidth + intObject.width),
						intObject.height + line * intObject.height, intObject.storedX,
						intObject.storedY, intObject.storedX + intObject.width,
						intObject.storedY + intObject.height));
				
				totalwidth += intObject.width;
			}
		}
		if (intObject != null) {
			w = totalwidth;
			h = intObject.height + line * intObject.height;
		}else {
			w = 0;
			h = 0;
		}
		
		line = 0;
		buffer.flip();
		
		glBindBuffer(GL_ARRAY_BUFFER, vbo);
		glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
		
		buffer.clear();
	}

	public void render(Gui3DShader shader, Camera camera, Color4f color, float dropShadow) {
		glDisable(GL_CULL_FACE);
		glEnable(GL_TEXTURE_2D);
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		font.fontTexture.bind(shader);

		shader.setModelViewMatrix(transform.toMatrix());
		shader.setColor(color);
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		glBindBuffer(GL_ARRAY_BUFFER, vbo);
		glVertexAttribPointer(0, 3, GL_FLOAT, false, 5*4, 0);
		glVertexAttribPointer(1, 2, GL_FLOAT, false, 5*4, 12);
		glDrawArrays(GL_QUADS, 0, text.length() * 4);
		glEnableVertexAttribArray(1);
		glEnableVertexAttribArray(0);

		Texture.unbind(shader);
		glDisable(GL_TEXTURE_2D);
		glEnable(GL_CULL_FACE);
	}
	
	public void dispose() {
		Buffers.deleteVertexBuffer(vbo);
	}
	
	public int getWidth() {
		return w;
	}
	
	public int getHeight() {
		return h;
	}
	
	public IntObject getCharData(char c) {
		return font.charArray[c];
	}

	public Transform getTransform()
	{
		return transform;
	}
}