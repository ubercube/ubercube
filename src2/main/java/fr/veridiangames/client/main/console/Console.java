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

package fr.veridiangames.client.main.console;

import static org.lwjgl.opengl.GL11.*;

import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

import fr.veridiangames.client.guis.TrueTypeFont;
import fr.veridiangames.client.rendering.Display;
import fr.veridiangames.client.rendering.guis.components.GuiLabel;
import fr.veridiangames.client.rendering.shaders.GuiShader;
import fr.veridiangames.core.maths.Mat4;
import fr.veridiangames.core.utils.Color4f;

public class Console
{
	private GuiShader 		shader;
	private TrueTypeFont 	font;
	private List<String> 	lines;
	private List<CLine> 	clines;

	public Console()
	{
		this.font = new TrueTypeFont(new Font("Arial", 0, 12), true);
		this.lines = new ArrayList<String>();
		this.clines = new ArrayList<CLine>();
		this.shader = new GuiShader();
	}

	public void print(String msg)
	{
		this.lines.add(msg);
	}

	public void error(String msg)
	{
		this.lines.add(msg);
	}

	public void print(String msg, Color4f color)
	{
		this.lines.add(msg);
	}

	public void update()
	{
		this.clines.clear();
		for (int i = 0; i < this.lines.size(); i++)
			this.clines.add(new CLine(this.lines.get(i) , new Color4f(0.2f, 0.75f, 1f, 1f), 5, 5 + this.clines.size() * 12, this.font));
	}

	public void render(Display display)
	{
		glDisable(GL_DEPTH_TEST);
		glDisable(GL_CULL_FACE);

		this.shader.bind();
		this.shader.setOrtho(display.getWidth(), 0, 0, display.getHeight(), -1, 1);
		this.shader.setModelViewMatrix(Mat4.identity());
		this.shader.setColor(1, 1, 1, 1);

		for (CLine c : this.clines)
			c.getLabel().render(this.shader);
	}

	class CLine
	{
		int time;
		GuiLabel label;

		CLine(String text, Color4f color, int x, int y, TrueTypeFont font)
		{
			this.label = new GuiLabel(text, x, y, 12);
			this.label.setColor(color);
		}

		void update()
		{
			this.time++;
		}

		public GuiLabel getLabel()
		{
			return this.label;
		}
	}
}
