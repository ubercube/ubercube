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

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL13;

import fr.veridiangames.client.guis.TrueTypeFont;
import fr.veridiangames.client.guis.TrueTypeFont.IntObject;
import fr.veridiangames.client.rendering.Camera;
import fr.veridiangames.client.rendering.buffers.Buffers;
import fr.veridiangames.client.rendering.shaders.Gui3DShader;
import fr.veridiangames.client.rendering.textures.Texture;
import fr.veridiangames.core.maths.Mat4;
import fr.veridiangames.core.maths.Transform;
import fr.veridiangames.core.maths.Vec3;
import fr.veridiangames.core.utils.Color4f;

public class Font3DRenderer
{
	private int vbo;
	private FloatBuffer buffer;
	private int bufferSize;

	private TrueTypeFont font;
	private Transform transform;
	private int w, h;
	private String text;
	private Color4f color = Color4f.WHITE;

	public Font3DRenderer(TrueTypeFont font, String text, Vec3 position) {
		this.font = font;
		this.transform = new Transform(position);
		this.text = new String(text);
		this.bufferSize = text.length() * 20;
		this.vbo = Buffers.createVertexBuffer();
		this.createBuffer();
	}

	public void setText(String text) {
		this.bufferSize = text.length() * 20;
		if (this.text.length() != text.length()) {
			this.text = text;
			this.createBuffer();
		}else {
			this.text = text;
			this.updateBuffer();
		}
	}

	public void setPosition(Vec3 position) {
		this.transform.setLocalPosition(position);
	}

	private void createBuffer() {
		this.buffer = BufferUtils.createFloatBuffer(this.bufferSize);

		IntObject intObject = null;
		int charCurrent;

		int totalwidth = 0;

		int line = 0;
		for (int i = 0; i < this.text.length(); i++) {
			charCurrent = this.text.charAt(i);
			if (charCurrent < 256)
				intObject = this.font.charArray[charCurrent];
			else
				intObject = this.font.customChars.get( new Character( (char) charCurrent ) );

			if (charCurrent == '\n') {
				line++;
				totalwidth = 0;
			}

			if( intObject != null ) {
				this.buffer.put(this.quadData(totalwidth, 0 + line * intObject.height,
						(totalwidth + intObject.width),
						intObject.height + line * intObject.height, intObject.storedX,
						intObject.storedY, intObject.storedX + intObject.width,
						intObject.storedY + intObject.height));

				totalwidth += intObject.width;
			}
		}
		if (intObject != null) {
			this.w = totalwidth;
			this.h = intObject.height + line * intObject.height;
		}else {
			this.w = 0;
			this.h = 0;
		}

		line = 0;
		this.buffer.flip();

		glBindBuffer(GL_ARRAY_BUFFER, this.vbo);
		glBufferData(GL_ARRAY_BUFFER, this.buffer, GL_STATIC_DRAW);

		this.buffer.clear();
	}

	private float[] quadData(float drawX, float drawY, float drawX2, float drawY2, float srcX, float srcY, float srcX2, float srcY2) {
		float DrawWidth = (drawX2 - drawX);
		float DrawHeight = (drawY2 - drawY);
		float TextureSrcX = srcX / this.font.textureWidth;
		float TextureSrcY = srcY / this.font.textureHeight;
		float SrcWidth = srcX2 - srcX;
		float SrcHeight = srcY2 - srcY;
		float RenderWidth = (SrcWidth / this.font.textureWidth);
		float RenderHeight = (SrcHeight / this.font.textureHeight);

		float halfDrawX = drawX;
		float halfDrawY = drawY;

		float[] data = new float[] {
				halfDrawX, halfDrawY + DrawHeight, 0, TextureSrcX, TextureSrcY,
				halfDrawX + DrawWidth, halfDrawY + DrawHeight, 0, TextureSrcX + RenderWidth, TextureSrcY,
				halfDrawX + DrawWidth, halfDrawY, 0, TextureSrcX + RenderWidth, TextureSrcY + RenderHeight,
				halfDrawX, halfDrawY, 0, TextureSrcX, TextureSrcY + RenderHeight
		};

		return data;
	}

	private void updateBuffer() {
		if (this.buffer.capacity() == 0 && this.bufferSize != 0) {
			this.createBuffer();
			return;
		}


		IntObject intObject = null;
		int charCurrent;

		int totalwidth = 0;

		int line = 0;

		for (int i = 0; i < this.text.length(); i++) {
			charCurrent = this.text.charAt(i);
			if (charCurrent < 256)
				intObject = this.font.charArray[charCurrent];
			else
				intObject = this.font.customChars.get( new Character( (char) charCurrent ) );

			if (charCurrent == '\n') {
				line++;
				totalwidth = 0;
			}

			if (intObject != null) {
				this.buffer.put(this.quadData(totalwidth, 0 + line * intObject.height,
						(totalwidth + intObject.width),
						intObject.height + line * intObject.height, intObject.storedX,
						intObject.storedY, intObject.storedX + intObject.width,
						intObject.storedY + intObject.height));

				totalwidth += intObject.width;
			}
		}
		if (intObject != null) {
			this.w = totalwidth;
			this.h = intObject.height + line * intObject.height;
		}else {
			this.w = 0;
			this.h = 0;
		}

		line = 0;
		this.buffer.flip();

		glBindBuffer(GL_ARRAY_BUFFER, this.vbo);
		glBufferData(GL_ARRAY_BUFFER, this.buffer, GL_STATIC_DRAW);

		this.buffer.clear();
	}

	public void render(Gui3DShader shader, Camera camera, Color4f color, float dropShadow) {
		glDisable(GL_CULL_FACE);
		glEnable(GL_TEXTURE_2D);
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		this.font.fontTexture.bind(shader);

		camera.getTransform().getPosition().copy().sub(this.transform.getPosition()).magnitude();

		this.transform.setLocalRotation(camera.getTransform().getRotation());
		this.transform.setLocalScale(new Vec3(10.0f / this.font.fontTexture.getHeight() * 0.5f));

		if (dropShadow > 0)
		{
			shader.setModelViewMatrix(this.transform.toMatrix().mul(Mat4.translate(-this.w / 2 + dropShadow, -this.h / 2 - dropShadow, 0.3f)));
			shader.setColor(new Color4f(0, 0, 0, 0.8f));
			glEnableVertexAttribArray(0);
			glEnableVertexAttribArray(1);
			glBindBuffer(GL_ARRAY_BUFFER, this.vbo);
			glVertexAttribPointer(0, 3, GL_FLOAT, false, 5*4, 0);
			glVertexAttribPointer(1, 2, GL_FLOAT, false, 5*4, 12);
			glDrawArrays(GL_QUADS, 0, this.text.length() * 4);
			glEnableVertexAttribArray(1);
			glEnableVertexAttribArray(0);
		}

		shader.setModelViewMatrix(this.transform.toMatrix().mul(Mat4.translate(-this.w / 2, -this.h / 2, 0)));
		shader.setColor(color);
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		glBindBuffer(GL_ARRAY_BUFFER, this.vbo);
		glVertexAttribPointer(0, 3, GL_FLOAT, false, 5*4, 0);
		glVertexAttribPointer(1, 2, GL_FLOAT, false, 5*4, 12);
		glDrawArrays(GL_QUADS, 0, this.text.length() * 4);
		glEnableVertexAttribArray(1);
		glEnableVertexAttribArray(0);

		Texture.unbind(shader);
		glDisable(GL_TEXTURE_2D);
		glEnable(GL_CULL_FACE);
	}

	public void dispose() {
		Buffers.deleteVertexBuffer(this.vbo);
	}

	public int getWidth() {
		return this.w;
	}

	public int getHeight() {
		return this.h;
	}

	public IntObject getCharData(char c) {
		return this.font.charArray[c];
	}

	public Transform getTransform()
	{
		return this.transform;
	}

	public void setColor(Color4f color) { this.color = color; }

	public Color4f getColor() { return this.color; }
}