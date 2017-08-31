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

package fr.veridiangames.client.rendering;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

import java.nio.ByteBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

import fr.veridiangames.client.inputs.Input;
import fr.veridiangames.client.rendering.renderers.Renderer;
import fr.veridiangames.core.utils.Log;

/**
 * Created by Marccspro on 29 janv. 2016.
 */
public class Display
{
	private static Display 	instance;

	private long		window;
	private boolean		closed;
	private boolean		destroy;
	private boolean		resized;
	private float 		aspect;
	private String		title;
	private int			width;
	private int			height;
	private int			lastWidth;
	private int			lastHeight;
	private ByteBuffer	windowSizeBuffer;
	private Input 		input;
	private int			fps;
	private int			tps;

	public Display(String title, int width, int height)
	{
		instance = this;
		this.title = title;
		this.width = width;
		this.height = height;
		this.closed = false;
		this.windowSizeBuffer = BufferUtils.createByteBuffer(4);
		this.init();
	}

	private void init()
	{
		Log.println("glfwInit...");
		if (!glfwInit())
			throw new IllegalStateException("Unable to initialize GLFW");

		glfwDefaultWindowHints();
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
		glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
		glfwWindowHint(GLFW_SAMPLES, 4);

		Log.println("glfwCreateWindow...");
		this.window = glfwCreateWindow(this.width, this.height, this.title, NULL, NULL);
		if (this.window == NULL)
			throw new RuntimeException("Failed to create the GLFW window");

		long monitor = glfwGetPrimaryMonitor();
		if (monitor != 0)
		{
			Log.println("glfwSetWindowPos...");
			GLFWVidMode vidmode = glfwGetVideoMode(monitor);
			glfwSetWindowPos(this.window, (vidmode.width() - this.width) / 2, (vidmode.height() - this.height) / 2);
		}

		glfwDefaultWindowHints();
		Log.println("glfw inputs callbacks...");
		this.input = new Input(this);
		glfwSetKeyCallback(this.window, this.input.getKeyboardCallback());
		glfwSetCursorPosCallback(this.window, this.input.getMouse().getCursorPosCallback());
		glfwSetMouseButtonCallback(this.window, this.input.getMouse().getMouseButtonCallback());
		glfwSetScrollCallback(this.window, this.input.getMouse().getScrollCallback());

		Log.println("glfwMakeContextCurrent...");
		glfwMakeContextCurrent(this.window);
		glfwShowWindow(this.window);

		Log.println("glfwSwapInterval...");
		glfwSwapInterval(0);

		Log.println("GL.createCapabilities()...");
		GL.createCapabilities();

		Renderer.setDX11();
	}

	public void update()
	{
		this.closed = glfwWindowShouldClose(this.window);

		if (this.destroy)
		{
			this.destroy();
			return;
		}

		glfwPollEvents();
		glfwSwapBuffers(this.window);

		glfwGetWindowSize(this.window, this.windowSizeBuffer.asIntBuffer(), null);
		this.width = this.windowSizeBuffer.getInt(0);

		glfwGetWindowSize(this.window, null, this.windowSizeBuffer.asIntBuffer());
		this.height = this.windowSizeBuffer.getInt(0);

		this.resized = false;

		if (this.width != this.lastWidth || this.height != this.lastHeight)
		{
			this.lastWidth = this.width;
			this.lastHeight = this.height;
			this.aspect = (float)this.width / (float)this.height;
			this.resized = true;
		}

		if (this.resized)
			GL11.glViewport(0, 0, this.width, this.height);
	}

	private void destroy()
	{
		glfwDestroyWindow(this.window);
		glfwTerminate();
		Log.println("GLFW Display terminated !");
	}

	public void enableVSync()
	{
		glfwSwapInterval(1);
	}

	public void setResizable(boolean resizable)
	{
		glfwWindowHint(GLFW_RESIZABLE, resizable ? GL_TRUE : GL_FALSE);
	}

	public void setVisible(boolean visible)
	{
		glfwWindowHint(GLFW_VISIBLE, visible ? GL_TRUE : GL_FALSE);
	}

	public long getWindow()
	{
		return this.window;
	}

	public void setWindow(long window)
	{
		this.window = window;
	}

	public boolean isClosed()
	{
		return this.closed;
	}

	public void setClosed(boolean closed)
	{
		this.closed = closed;
	}

	public boolean isDestroyed()
	{
		return this.destroy;
	}

	public void setDestroyed(boolean destroy)
	{
		this.destroy = destroy;
		if (this.destroy)
			this.destroy();
	}

	public boolean wasResized()
	{
		return this.resized;
	}

	public String getTitle()
	{
		return this.title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public void displayTitle(String title)
	{
		glfwSetWindowTitle(this.window, title);
	}

	public int getWidth()
	{
		return this.width;
	}

	public int getHeight()
	{
		return this.height;
	}

	public float getAspect()
	{
		return this.aspect;
	}

	public Input getInput()
	{
		return this.input;
	}

	public int getFps()
	{
		return this.fps;
	}

	public void setFps(int fps)
	{
		this.fps = fps;
	}

	public int getTps()
	{
		return this.tps;
	}

	public void setTps(int tps)
	{
		this.tps = tps;
	}

	public static Display getInstance()
	{
		return instance;
	}
}