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
		this.xScreen = Display.getInstance().getWidth();
		this.yScreen = Display.getInstance().getHeight();
		this.nodes = new ArrayList<GuiComponent>();

		this.processOrigin();
	}

	public void setOrigin(GuiOrigin origin)
	{
		this.origin = origin;
		this.processOrigin();
	}

	public void setScreenParent(GuiCorner corner)
	{
		this.corner = corner;
		this.processCornerParent();
	}

	public void setParent(GuiComponent parent)
	{
		this.parent = parent;
		this.processParent();
	}

	protected void processOrigin()
	{
		this.x += this.xOrigin;
		this.y += this.yOrigin;

		switch (this.origin)
		{
		case A:
			this.xOrigin = 0;
			this.yOrigin = 0;
			break;
		case B:
			this.xOrigin = this.w;
			this.yOrigin = 0;
			break;
		case C:
			this.xOrigin = this.w;
			this.yOrigin = this.h;
			break;
		case D:
			this.xOrigin = 0;
			this.yOrigin = this.h;
			break;
		case TC:
			this.xOrigin = this.w / 2;
			this.yOrigin = 0;
			break;
		case BC:
			this.xOrigin = this.w / 2;
			this.yOrigin = this.h;
			break;
		case LC:
			this.xOrigin = 0;
			this.yOrigin = this.h / 2;
			break;
		case RC:
			this.xOrigin = this.w;
			this.yOrigin = this.h / 2;
			break;
		case CENTER:
			this.xOrigin = this.w / 2;
			this.yOrigin = this.h / 2;
			break;
		}
		this.x -= this.xOrigin;
		this.y -= this.yOrigin;
	}

	protected void processCornerParent()
	{
		Display display = Display.getInstance();
		switch (this.corner)
		{
		case BL:
			this.y += display.getHeight() - this.yScreen;
			break;
		case BR:
			this.x += display.getWidth() - this.xScreen;
			this.y += display.getHeight() - this.yScreen;
			break;
		case TL:
			break;
		case TR:
			this.x += display.getWidth() - this.xScreen;
			break;

		case BC:
			this.y += display.getHeight() - this.yScreen;
			this.x += (display.getWidth() - this.xScreen) / 2;
			break;
		case TC:
			this.x += (display.getWidth() - this.xScreen) / 2;
			break;
		case LC:
			this.y += (display.getHeight() - this.yScreen) / 2;
			break;
		case RC:
			this.x += display.getWidth() - this.xScreen;
			this.y += (display.getHeight() - this.yScreen) / 2;
			break;

		case CENTER:
			this.x += (display.getWidth() - this.xScreen) / 2;
			this.y += (display.getHeight() - this.yScreen) / 2;

			break;

		case SCALED:
			this.w += (display.getWidth() - this.xScreen) + 1;
			this.h += (display.getHeight() - this.yScreen) + 1;

			break;
		}

		this.xScreen = display.getWidth();
		this.yScreen = display.getHeight();
	}

	protected void processParent()
	{
		if (this.parent == null)
			return;

		this.x += this.parent.x;
		this.y += this.parent.y;
	}

	public void update()
	{
		Display display = Display.getInstance();

		this.hasMoved = false;
		if (this.xScreen != display.getWidth() || this.yScreen != display.getHeight())
			this.processCornerParent();

		if (display.getInput().getMouse().getX() >= this.x
				&& display.getInput().getMouse().getY() >= this.y
				&& display.getInput().getMouse().getX() <= this.x + this.w
				&& display.getInput().getMouse().getY() <= this.y + this.h)
		{
			if (!this.mouseIn)
			{
				this.mouseEnter = true;
				this.mouseIn = true;
			} else
				this.mouseEnter = false;
		}
		else
		{
			this.mouseEnter = false;
			if (this.mouseIn)
			{
				this.mouseExit = true;
				this.mouseIn = false;
			} else
				this.mouseExit = false;
		}

		if (this.mouseIn && display.getInput().getMouse().getButton(0))
			this.mouseFocused = true;
		if (this.mouseFocused && display.getInput().getMouse().getButtonUp(0))
			this.mouseFocused = false;

		if (this.mouseFocused)
		{
			if (!this.mouseButtonPressed)
			{
				this.mouseButtonDown = true;
				this.mouseButtonPressed = true;
			} else
				this.mouseButtonDown = false;
		} else if (this.mouseButtonPressed)
		{
			this.mouseButtonUp = true;
			this.mouseButtonPressed = false;
		} else
			this.mouseButtonUp = false;

		if (this.mouseIn && this.mouseButtonUp)
			this.action = true;
		else
			this.action = false;
	}

	public void renderSteps(GuiShader shader)
	{
		this.render(shader);
	}

	public abstract void render(GuiShader shader);

	public abstract void dispose();

	public int getBx()
	{
		return this.bx;
	}

	public int getBy()
	{
		return this.by;
	}

	public int getX()
	{
		return this.x;
	}

	public int getY()
	{
		return this.y;
	}

	public int getW()
{
	return this.w;
}

	public int getH()
	{
		return this.h;
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
		return this.color;
	}

	public void setColor(Color4f color)
	{
		this.color = new Color4f(color);
	}

	public int getxOrigin()
	{
		return this.xOrigin;
	}

	public int getyOrigin()
	{
		return this.yOrigin;
	}

	public GuiCorner getCorner()
	{
		return this.corner;
	}

	public GuiComponent getParent()
	{
		return this.parent;
	}

	public int getxScreen()
	{
		return this.xScreen;
	}

	public int getyScreen()
	{
		return this.yScreen;
	}

	public boolean hasMoved()
	{
		return this.hasMoved;
	}

	public boolean isUseable()
	{
		return this.useable;
	}

	public void setUseable(boolean useable)
	{
		this.useable = useable;
	}


}
