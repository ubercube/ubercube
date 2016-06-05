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
 *     along with Ubercube.  If not, see <http://www.gnu.org/licenses/>.
 */

package fr.veridiangames.client.rendering;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;

import java.nio.ByteBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

import fr.veridiangames.core.utils.Log;
import fr.veridiangames.client.inputs.Input;
import fr.veridiangames.client.rendering.renderers.Renderer;

/**
 * Created by Marccspro on 29 janv. 2016.
 */
public class Display
{
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
		this.title = title;
		this.width = width;
		this.height = height;
		this.closed = false;
		this.windowSizeBuffer = BufferUtils.createByteBuffer(4);
		this.input = new Input(this);
		this.init();
	}

	private void init()
	{
		Log.println("====== Loading Display... ======");

		Log.println("glfwInit...");
		glfwInit();

		glfwDefaultWindowHints();
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
		glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
		glfwWindowHint(GLFW_SAMPLES, 4);

		Log.println("glfwCreateWindow...");
		window = glfwCreateWindow(width, height, title, NULL, NULL);
		
		long monitor = glfwGetPrimaryMonitor();
		if (monitor != 0)
		{
			Log.println("glfwSetWindowPos...");
			GLFWVidMode vidmode = glfwGetVideoMode(monitor);
			glfwSetWindowPos(window, (vidmode.width() - width) / 2, (vidmode.height() - height) / 2);
		}

		glfwDefaultWindowHints();
		Log.println("glfw inputs callbacks...");
		glfwSetKeyCallback(window, input.getKeyboardCallback());
		glfwSetCursorPosCallback(window, input.getMouse().getCursorPosCallback());
		glfwSetMouseButtonCallback(window, input.getMouse().getMouseButtonCallback());
		glfwSetScrollCallback(window, input.getMouse().getScrollCallback());

		Log.println("glfwMakeContextCurrent...");
		glfwMakeContextCurrent(window);
		glfwShowWindow(window);

		Log.println("glfwSwapInterval...");
		glfwSwapInterval(0);

		Log.println("GL.createCapabilities()...");
		GL.createCapabilities();

		Log.println("====== Display loaded ! ========");
		
		Renderer.setDX11();
	}

	public void update()
	{
		closed = (glfwWindowShouldClose(window) == GL_TRUE);

		if (destroy)
		{
			destroy();
			return;
		}
		
		glfwPollEvents();
		glfwSwapBuffers(window);

		glfwGetWindowSize(window, windowSizeBuffer, null);
		width = windowSizeBuffer.getInt(0);

		glfwGetWindowSize(window, null, windowSizeBuffer);
		height = windowSizeBuffer.getInt(0);

		resized = false;

		if (width != lastWidth || height != lastHeight)
		{
			lastWidth = width;
			lastHeight = height;
			aspect = (float)width / (float)height;
			resized = true;
		}
		
		if (resized)
		{
			GL11.glViewport(0, 0, width, height);
		}
	}
	
	private void destroy()
	{
		glfwDestroyWindow(window);
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
		return window;
	}

	public void setWindow(long window)
	{
		this.window = window;
	}

	public boolean isClosed()
	{
		return closed;
	}

	public void setClosed(boolean closed)
	{
		this.closed = closed;
	}

	public boolean isDestroyed()
	{
		return destroy;
	}

	public void setDestroyed(boolean destroy)
	{
		this.destroy = destroy;
		if (this.destroy)
			destroy();
	}

	public boolean wasResized()
	{
		return resized;
	}

	public String getTitle()
	{
		return title;
	}
	
	public void setTitle(String title)
	{
		this.title = title;
	}
	
	public void displayTitle(String title)
	{
		glfwSetWindowTitle(window, title);
	}

	public int getWidth()
	{
		return width;
	}

	public int getHeight()
	{
		return height;
	}
	
	public float getAspect()
	{
		return aspect;
	}
	
	public Input getInput()
	{
		return input;
	}

	public int getFps()
	{
		return fps;
	}

	public void setFps(int fps)
	{
		this.fps = fps;
	}

	public int getTps()
	{
		return tps;
	}

	public void setTps(int tps)
	{
		this.tps = tps;
	}
}