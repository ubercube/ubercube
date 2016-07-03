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

package fr.veridiangames.client.inputs;

import java.util.ArrayList;

import fr.veridiangames.core.maths.Vec2;
import org.lwjgl.glfw.GLFW;

import fr.veridiangames.client.rendering.Display;

import static java.awt.SystemColor.window;
import static org.lwjgl.glfw.GLFW.glfwSetCursorPos;

/**
 * Created by Marccspro on 9 f�vr. 2016.
 */
public class Mouse
{
	public static final int NUM_MOUSEBUTTONS = 5;

	private MousePosition	position	= new MousePosition();
	private MouseButton		buttons		= new MouseButton();
	private MouseScroll		scroll		= new MouseScroll();

	private boolean grabbed = false;

	private float	lx, ly;
	private float	cx, cy;
	private float	dx, dy;

	private float dsx, dsy;

	private ArrayList<Integer>	currentMouse	= new ArrayList<>();
	private ArrayList<Integer>	downMouse		= new ArrayList<>();
	private ArrayList<Integer>	upMouse			= new ArrayList<>();

	private Display display;

	public Mouse(Display display)
	{
		this.display = display;
	}

	public void update()
	{
		if (dsx != 0)
			dsx = 0;
		if (dsy != 0)
			dsy = 0;

		dsx += scroll.xOffs;
		dsy += scroll.yOffs;

		if (scroll.xOffs != 0)
			scroll.xOffs = 0;
		if (scroll.yOffs != 0)
			scroll.yOffs = 0;

		cx = position.x - lx;
		dx = cx;
		lx = position.x;

		cy = position.y - ly;
		dy = cy;
		ly = position.y;

		if (grabbed)
		{
			GLFW.glfwSetInputMode(display.getWindow(), GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_DISABLED);
		}
		else
		{
			GLFW.glfwSetInputMode(display.getWindow(), GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_NORMAL);
		}

		upMouse.clear();

		for (int i = 0; i < NUM_MOUSEBUTTONS; i++)
		{
			if (!getButton(i) && currentMouse.contains(i))
			{
				upMouse.add(i);
			}
		}

		downMouse.clear();

		for (int i = 0; i < NUM_MOUSEBUTTONS; i++)
		{
			if (getButton(i) && !currentMouse.contains(i))
			{
				downMouse.add(i);
			}
		}

		currentMouse.clear();

		for (int i = 0; i < NUM_MOUSEBUTTONS; i++)
		{
			if (getButton(i))
			{
				currentMouse.add(i);
			}
		}
	}

	public int getX()
	{
		return (int) position.x;
	}

	public int getY()
	{
		return (int) position.y;
	}

	public float getDX()
	{
		return dx;
	}

	public float getDY()
	{
		return dy;
	}

	public float getDWheel()
	{
		return (dsx + dsy) / 2.0f;
	}

	public void setMousePosition(Vec2 position)
	{
		glfwSetCursorPos(display.getWindow(), position.x, position.y);
	}

	public boolean getButton(int button)
	{
		return buttons.buttons[button];
	}

	public boolean getButtonDown(int button)
	{
		return downMouse.contains(button);
	}

	public boolean getButtonUp(int button)
	{
		return upMouse.contains(button);
	}

	public boolean isGrabbed()
	{
		return grabbed;
	}

	public void setGrabbed(boolean grabbed)
	{
		this.grabbed = grabbed;
	}

	public MousePosition getCursorPosCallback()
	{
		return position;
	}

	public MouseButton getMouseButtonCallback()
	{
		return buttons;
	}

	public MouseScroll getScrollCallback()
	{
		return scroll;
	}
}