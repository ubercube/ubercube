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

package fr.veridiangames.client.rendering.renderers;

import fr.veridiangames.client.Ubercube;
import fr.veridiangames.client.rendering.Display;
import fr.veridiangames.client.rendering.renderers.game.GameRenderer;
import fr.veridiangames.core.GameCore;

/**
 * Created by Marccspro on 3 févr. 2016.
 */
public class MainRenderer
{
	private Renderer		renderer;
	private GameRenderer	gameRenderer;

	public MainRenderer(Ubercube main, GameCore core)
	{
		this.renderer = new Renderer();
		this.gameRenderer = new GameRenderer(main, core);
	}

	public void update()
	{
		this.gameRenderer.update();
	}

	public void renderAll(Display display)
	{
		this.renderer.start();

		this.renderer.prepare3D();
		this.render();

		this.renderer.prepare2D();
		this.renderGui(display);
	}

	private void render()
	{
		this.gameRenderer.render();
	}

	private void renderGui(Display display)
	{

	}
}
