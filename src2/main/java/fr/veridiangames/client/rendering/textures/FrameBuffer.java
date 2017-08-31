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

package fr.veridiangames.client.rendering.textures;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL14.GL_DEPTH_COMPONENT24;
import static org.lwjgl.opengl.GL30.*;

import java.nio.ByteBuffer;

import org.lwjgl.opengl.GL12;

import fr.veridiangames.client.Ubercube;

public class FrameBuffer
{
	private int frameBufferID;
	private int colorTextureID;
	private int depthTextureID;
	private int depthBufferID;

	private int width, height;

	public FrameBuffer(int width, int height)
	{
		this.width = width;
		this.height = height;

		this.createFrameBuffer();
	}

	private void createFrameBuffer()
	{
		this.frameBufferID = glGenFramebuffers();
		glBindFramebuffer(GL_FRAMEBUFFER, this.frameBufferID);
		glDrawBuffer(GL_COLOR_ATTACHMENT0);

		this.createColorTextureAttachement();
		this.createDepthTextureAttachement();
		this.createDepthBufferAttachement();

		this.unbind();
	}

	private void createColorTextureAttachement()
	{
		this.colorTextureID = glGenTextures();
		glBindTexture(GL_TEXTURE_2D, this.colorTextureID);

		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);

		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, this.width, this.height, 0, GL_RGBA, GL_UNSIGNED_BYTE, (ByteBuffer) null);
		glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, this.colorTextureID, 0);
	}

	private void createDepthTextureAttachement()
	{
		this.depthTextureID = glGenTextures();
		glBindTexture(GL_TEXTURE_2D, this.depthTextureID);

		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);

		glTexImage2D(GL_TEXTURE_2D, 0, GL_DEPTH_COMPONENT24, this.width, this.height, 0, GL_DEPTH_COMPONENT, GL_UNSIGNED_BYTE, (ByteBuffer) null);
		glFramebufferTexture2D(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_TEXTURE_2D, this.depthTextureID, 0);
	}

	private void createDepthBufferAttachement()
	{
		this.depthBufferID = glGenRenderbuffers();
		glBindRenderbuffer(GL_RENDERBUFFER, this.depthBufferID);

		glRenderbufferStorage(GL_RENDERBUFFER, GL_DEPTH_COMPONENT, this.width, this.height);
		glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_RENDERBUFFER, this.depthBufferID);
	}

	public void bind()
	{
		glBindFramebuffer(GL_DRAW_FRAMEBUFFER, this.frameBufferID);
		glViewport(0, 0, this.width, this.height);
	}

	public void unbind()
	{
		glBindFramebuffer(GL_FRAMEBUFFER, 0);
		glViewport(0, 0, Ubercube.getInstance().getDisplay().getWidth(), Ubercube.getInstance().getDisplay().getHeight());
	}

	public int getFrameBufferID()
	{
		return this.frameBufferID;
	}

	public int getColorTextureID()
	{
		return this.colorTextureID;
	}

	public int getDepthTextureID()
	{
		return this.depthTextureID;
	}

	public int getDepthBufferID()
	{
		return this.depthBufferID;
	}

	public int getWidth()
	{
		return this.width;
	}

	public int getHeight()
	{
		return this.height;
	}

	public void destroy()
	{
		glDeleteFramebuffers(this.frameBufferID);
		glDeleteTextures(this.colorTextureID);
		glDeleteTextures(this.depthTextureID);
		glDeleteRenderbuffers(this.depthBufferID);
	}
}
