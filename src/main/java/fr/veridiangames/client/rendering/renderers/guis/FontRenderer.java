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

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL13;

import fr.veridiangames.core.maths.Mat4;
import fr.veridiangames.core.utils.Color4f;
import fr.veridiangames.client.guis.TrueTypeFont;
import fr.veridiangames.client.guis.TrueTypeFont.IntObject;
import fr.veridiangames.client.rendering.buffers.Buffers;
import fr.veridiangames.client.rendering.shaders.GuiShader;
import fr.veridiangames.client.rendering.textures.Texture;

public class FontRenderer {
	private int vao, vbo;
	private FloatBuffer buffer;
	private int size;
	
	private TrueTypeFont font;
	private int x, y;
	private int w, h;
	private String text;

	public FontRenderer(TrueTypeFont font, String text, int x, int y) {
		this.font = font;
		this.x = x;
		this.y = y;
		this.text = new String(text);
		
		size = text.length() * 6 * 5;
		vao = Buffers.createVertexArray();
		vbo = Buffers.createVertexBuffer();
		createBuffer();
	}
	
	public void setText(String text) {
		this.size = text.length() * 6 * 5;
		if (this.text.length() != text.length()) {
			this.text = text; 
			createBuffer();			
		}else {
			this.text = text;
			updateBuffer();			
		}
	}
	
	public void setPosition(int x, int y) {
		this.x = x;
		this.y = y;
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

		glBindVertexArray(vao);

		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);

		glBindBuffer(GL_ARRAY_BUFFER, vbo);
		glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
		glVertexAttribPointer(0, 3, GL_FLOAT, false, 5*4, 0);
		glVertexAttribPointer(1, 2, GL_FLOAT, false, 5*4, 12);

		glBindVertexArray(0);

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
			drawX, drawY, 0, TextureSrcX, TextureSrcY,
			drawX + DrawWidth, drawY, 0, TextureSrcX + RenderWidth, TextureSrcY,
			drawX, drawY + DrawHeight, 0, TextureSrcX, TextureSrcY + RenderHeight,

			drawX + DrawWidth, drawY, 0, TextureSrcX + RenderWidth, TextureSrcY,
			drawX + DrawWidth, drawY + DrawHeight, 0, TextureSrcX + RenderWidth, TextureSrcY + RenderHeight,
			drawX, drawY + DrawHeight, 0, TextureSrcX, TextureSrcY + RenderHeight
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

		glBindVertexArray(vao);

		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);

		glBindBuffer(GL_ARRAY_BUFFER, vbo);
		glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
		glVertexAttribPointer(0, 3, GL_FLOAT, false, 5*4, 0);
		glVertexAttribPointer(1, 2, GL_FLOAT, false, 5*4, 12);

		glBindVertexArray(0);

		buffer.clear();
	}
	
	public void render(GuiShader shader, Color4f color, float dropShadow) {
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		font.fontTexture.bind(shader);

		if (dropShadow != 0)
		{
			shader.setModelViewMatrix(Mat4.translate(x + dropShadow, y + dropShadow, 0));
			shader.setColor(0, 0, 0, 0.8f * color.getAlpha());
			glBindVertexArray(vao);
			glDrawArrays(GL_TRIANGLES, 0, text.length() * 6);
			glBindVertexArray(0);
		}

		shader.setModelViewMatrix(Mat4.translate(x, y, 0));
		shader.setColor(color);
		glBindVertexArray(vao);
		glDrawArrays(GL_TRIANGLES, 0, text.length() * 6);
		glBindVertexArray(0);

		Texture.unbind(shader);
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
}