/*
 *   Copyright (C) 2016 Team Ubercube
 *
 *   This file is part of Ubercube.
 *
 *       Ubercube is free software: you can redistribute it and/or modify
 *       it under the terms of the GNU General Public License as published by
 *       the Free Software Foundation, either version 3 of the License, or
 *       (at your option) any later version.
 *
 *       Ubercube is distributed in the hope that it will be useful,
 *       but WITHOUT ANY WARRANTY; without even the implied warranty of
 *       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *       GNU General Public License for more details.
 *
 *       You should have received a copy of the GNU General Public License
 *       along with Ubercube.  If not, see <http://www.gnu.org/licenses/>.
 */

package fr.veridiangames.client.main.console;

import static org.lwjgl.opengl.GL11.*;

import java.awt.Font;
import java.util.ArrayList;
import java.util.List;


import fr.veridiangames.core.maths.Mat4;
import fr.veridiangames.core.utils.Color4f;
import fr.veridiangames.client.guis.TrueTypeFont;
import fr.veridiangames.client.guis.components.GuiLabel;
import fr.veridiangames.client.rendering.Display;
import fr.veridiangames.client.rendering.shaders.GuiShader;

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
		lines.add(msg);
	}
	
	public void error(String msg)
	{
		lines.add(msg);
	}
	
	public void print(String msg, Color4f color)
	{
		lines.add(msg);
	}
	
	public void update()
	{
		clines.clear();
		for (int i = 0; i < lines.size(); i++)
		{
			clines.add(new CLine(lines.get(i) , Color4f.WHITE, 5, 5 + clines.size() * 12, font));
		}
	}
	
	public void render(Display display)
	{
		glDisable(GL_DEPTH_TEST);
		glDisable(GL_CULL_FACE);
		
		shader.bind();
		shader.setOrtho(display.getWidth(), 0, 0, display.getHeight(), -1, 1);
		shader.setModelViewMatrix(Mat4.identity());
		shader.setColor(1, 1, 1, 1);
		shader.enableVColor(false);
		
		for (CLine c : clines)
		{
			c.getLabel().render(shader);
		}
	}
	
	class CLine
	{
		int time;
		GuiLabel label;
		
		CLine(String text, Color4f color, int x, int y, TrueTypeFont font)
		{
			label = new GuiLabel(text, x, y, font);
			label.setColor(color);
			label.setShadowDistance(1);
		}
		
		void update()
		{
			time++;
		}
		
		public GuiLabel getLabel()
		{
			return label;
		}
	}
}
