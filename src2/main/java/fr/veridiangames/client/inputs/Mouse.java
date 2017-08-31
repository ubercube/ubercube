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

import static org.lwjgl.glfw.GLFW.glfwSetCursorPos;

import java.util.ArrayList;

import org.lwjgl.glfw.GLFW;

import fr.veridiangames.client.rendering.Display;
import fr.veridiangames.core.maths.Vec2;

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
		if (this.dsx != 0)
			this.dsx = 0;
		if (this.dsy != 0)
			this.dsy = 0;

		this.dsx += this.scroll.xOffs;
		this.dsy += this.scroll.yOffs;

		if (this.scroll.xOffs != 0)
			this.scroll.xOffs = 0;
		if (this.scroll.yOffs != 0)
			this.scroll.yOffs = 0;

		this.cx = this.position.x - this.lx;
		this.dx = this.cx;
		this.lx = this.position.x;

		this.cy = this.position.y - this.ly;
		this.dy = this.cy;
		this.ly = this.position.y;

		if (this.grabbed)
			GLFW.glfwSetInputMode(this.display.getWindow(), GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_DISABLED);
		else
			GLFW.glfwSetInputMode(this.display.getWindow(), GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_NORMAL);

		this.upMouse.clear();

		for (int i = 0; i < NUM_MOUSEBUTTONS; i++)
			if (!this.getButton(i) && this.currentMouse.contains(i))
				this.upMouse.add(i);

		this.downMouse.clear();

		for (int i = 0; i < NUM_MOUSEBUTTONS; i++)
			if (this.getButton(i) && !this.currentMouse.contains(i))
				this.downMouse.add(i);

		this.currentMouse.clear();

		for (int i = 0; i < NUM_MOUSEBUTTONS; i++)
			if (this.getButton(i))
				this.currentMouse.add(i);
	}

	public int getX()
	{
		return (int) this.position.x;
	}

	public int getY()
	{
		return (int) this.position.y;
	}

	public float getDX()
	{
		return this.dx;
	}

	public float getDY()
	{
		return this.dy;
	}

	public float getDWheel()
	{
		return (this.dsx + this.dsy) / 2.0f;
	}

	public void setMousePosition(Vec2 position)
	{
		glfwSetCursorPos(this.display.getWindow(), position.x, position.y);
	}

	public void center() { glfwSetCursorPos(this.display.getWindow(), this.display.getWidth() / 2, this.display.getHeight() / 2); }

	public boolean getButton(int button)
	{
		return this.buttons.buttons[button];
	}

	public boolean getButtonDown(int button)
	{
		return this.downMouse.contains(button);
	}

	public boolean getButtonUp(int button)
	{
		return this.upMouse.contains(button);
	}

	public boolean isGrabbed()
	{
		return this.grabbed;
	}

	public void setGrabbed(boolean grabbed)
	{
		this.grabbed = grabbed;
	}

	public MousePosition getCursorPosCallback()
	{
		return this.position;
	}

	public MouseButton getMouseButtonCallback()
	{
		return this.buttons;
	}

	public MouseScroll getScrollCallback()
	{
		return this.scroll;
	}
}