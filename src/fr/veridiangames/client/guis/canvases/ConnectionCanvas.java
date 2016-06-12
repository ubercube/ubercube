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

package fr.veridiangames.client.guis.canvases;

import java.awt.Font;

import fr.veridiangames.core.utils.Color4f;
import fr.veridiangames.client.guis.GuiCanvas;
import fr.veridiangames.client.guis.TrueTypeFont;
import fr.veridiangames.client.guis.components.GuiLabel;
import fr.veridiangames.client.guis.components.GuiPanel;
import fr.veridiangames.client.rendering.Display;

public class ConnectionCanvas extends GuiCanvas
{
	public ConnectionCanvas(String address, int port, Display display)
	{
		super();
		super.add(new GuiPanel(0, 0, display.getWidth(), display.getHeight()).setColor(new Color4f(0.3f, 0.3f, 0.3f, 1f)));
		super.add(new GuiLabel("Connecting to " + address + ":" + port + "...", 30, 30, new TrueTypeFont(new Font("Arial", 0, 20), true)).setShadowDistance(1));
	}
}
