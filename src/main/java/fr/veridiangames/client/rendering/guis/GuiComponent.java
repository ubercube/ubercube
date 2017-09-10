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

package fr.veridiangames.client.rendering.guis;

import java.util.ArrayList;
import java.util.List;

import fr.veridiangames.client.rendering.Display;
import fr.veridiangames.client.rendering.shaders.GuiShader;
import fr.veridiangames.core.utils.Color4f;


public abstract class GuiComponent
{
	public enum GuiOrigin
	{
		A, B, C, D, TC, BC, LC, RC, CENTER
	}

	public enum GuiCorner
	{
		BL, BR, TL, TR, BC, TC, LC, RC, CENTER, SCALED
	}

	protected boolean useable = true;
	protected boolean rendered = true;
	protected boolean mouseFocused;
	protected boolean mouseEnter, mouseIn, mouseExit;
	protected boolean mouseButtonDown, mouseButtonPressed, mouseButtonUp;
	protected boolean action;
	protected boolean hasMoved = false;

	protected static boolean mouseInUse = false;

	protected int bx, by;
	protected int x, y;
	protected int w, h;
	protected Color4f color;
	protected int xOrigin, yOrigin;
	protected GuiOrigin origin;
	protected GuiCorner corner;
	protected int xScreen, yScreen;
	protected GuiCanvas canvas;

	protected GuiComponent parent;
	protected List<GuiComponent> nodes;

	public GuiComponent(int x, int y, int w, int h, Color4f color)
	{
		this.x = x;
		this.y = y;
		this.bx = x;
		this.by = y;
		this.w = w;
		this.h = h;
		this.color = color;
		this.origin = GuiOrigin.A;
		this.corner = GuiCorner.CENTER;
		xScreen = Display.getInstance().getWidth();
		yScreen = Display.getInstance().getHeight();
		this.nodes = new ArrayList<GuiComponent>();

		processOrigin();
	}

	public void setOrigin(GuiOrigin origin)
	{
		this.origin = origin;
		processOrigin();
	}

	public void setScreenParent(GuiCorner corner)
	{
		this.corner = corner;
		processCornerParent();
	}

	public void setParent(GuiComponent parent)
	{
		this.parent = parent;
		processParent();
	}

	protected void processOrigin()
	{
		x += xOrigin;
		y += yOrigin;

		switch (origin)
		{
		case A:
			xOrigin = 0;
			yOrigin = 0;
			break;
		case B:
			xOrigin = w;
			yOrigin = 0;
			break;
		case C:
			xOrigin = w;
			yOrigin = h;
			break;
		case D:
			xOrigin = 0;
			yOrigin = h;
			break;
		case TC:
			xOrigin = w / 2;
			yOrigin = 0;
			break;
		case BC:
			xOrigin = w / 2;
			yOrigin = h;
			break;
		case LC:
			xOrigin = 0;
			yOrigin = h / 2;
			break;
		case RC:
			xOrigin = w;
			yOrigin = h / 2;
			break;
		case CENTER:
			xOrigin = w / 2;
			yOrigin = h / 2;
			break;
		}
		x -= xOrigin;
		y -= yOrigin;
	}

	protected void processCornerParent()
	{
		Display display = Display.getInstance();
		switch (corner)
		{
		case BL:
			y += display.getHeight() - yScreen;
			break;
		case BR:
			x += display.getWidth() - xScreen;
			y += display.getHeight() - yScreen;
			break;
		case TL:
			break;
		case TR:
			x += display.getWidth() - xScreen;
			break;

		case BC:
			y += display.getHeight() - yScreen;
			x += (display.getWidth() - xScreen) / 2;
			break;
		case TC:
			x += (display.getWidth() - xScreen) / 2;
			break;
		case LC:
			y += (display.getHeight() - yScreen) / 2;
			break;
		case RC:
			x += display.getWidth() - xScreen;
			y += (display.getHeight() - yScreen) / 2;
			break;

		case CENTER:
			x += (display.getWidth() - xScreen) / 2;
			y += (display.getHeight() - yScreen) / 2;

			break;

		case SCALED:
			w += (display.getWidth() - xScreen) + 1;
			h += (display.getHeight() - yScreen) + 1;

			break;
		}

		xScreen = display.getWidth();
		yScreen = display.getHeight();
	}

	protected void processParent()
	{
		if (parent == null)
			return;

		x += parent.x;
		y += parent.y;
	}

	public void update()
	{
		Display display = Display.getInstance();

		hasMoved = false;
		if (xScreen != display.getWidth() || yScreen != display.getHeight())
		{
			processCornerParent();
		}

		if (display.getInput().getMouse().getX() >= x
				&& display.getInput().getMouse().getY() >= y
				&& display.getInput().getMouse().getX() <= x + w 
				&& display.getInput().getMouse().getY() <= y + h)
		{
			if (!mouseIn)
			{
				mouseEnter = true;
				mouseIn = true;
			}
			else
			{
				mouseEnter = false;
			}
		}
		else
		{
			mouseEnter = false;
			if (mouseIn)
			{
				mouseExit = true;
				mouseIn = false;
			}
			else
			{
				mouseExit = false;
			}
		}

		if (mouseIn && display.getInput().getMouse().getButton(0))
		{
			mouseFocused = true;
		}
		if (mouseFocused && display.getInput().getMouse().getButtonUp(0))
		{
			mouseFocused = false;
		}

		if (mouseFocused)
		{
			if (!mouseButtonPressed)
			{
				mouseButtonDown = true;
				mouseButtonPressed = true;
			}
			else
			{
				mouseButtonDown = false;
			}
		}
		else
		{
			if (mouseButtonPressed)
			{
				mouseButtonUp = true;
				mouseButtonPressed = false;
			}
			else
			{
				mouseButtonUp = false;
			}
		}

		if (mouseIn && mouseButtonUp)
		{
			action = true;
		}
		else
		{
			action = false;
		}
	}

	public void renderSteps(GuiShader shader)
	{
		if (!rendered)
			return;
		render(shader);
	}

	public abstract void render(GuiShader shader);

	public abstract void dispose();

	public int getBx()
	{
		return bx;
	}

	public int getBy()
	{
		return by;
	}

	public int getX()
	{
		return x;
	}

	public int getY()
	{
		return y;
	}

	public int getW()
{
	return w;
}

	public int getH()
	{
		return h;
	}

	public void setX(int x)
	{
		this.x = x;
	}

	public void setY(int y)
	{
		this.y = y;
	}

	public void setW(int w)
	{
		this.w = w;
	}

	public void setH(int h)
	{
		this.h = h;
	}

	public Color4f getColor()
	{
		return color;
	}

	public void setColor(Color4f color)
	{
		this.color = new Color4f(color);
	}

	public int getxOrigin()
	{
		return xOrigin;
	}

	public int getyOrigin()
	{
		return yOrigin;
	}

	public GuiCorner getCorner()
	{
		return corner;
	}

	public GuiComponent getParent()
	{
		return parent;
	}

	public int getxScreen()
	{
		return xScreen;
	}

	public int getyScreen()
	{
		return yScreen;
	}

	public boolean hasMoved()
	{
		return hasMoved;
	}

	public boolean isUseable()
	{
		return useable;
	}

	public void setUseable(boolean useable)
	{
		this.useable = useable;
	}

	public void setCanvas(GuiCanvas canvas) {
		this.canvas = canvas;
	}

	public GuiCanvas getCanvas() {
		return canvas;
	}

	public void setRendered(boolean rendered) {
		this.rendered = rendered;
	}

	public boolean isRendered() {
		return rendered;
	}
}
