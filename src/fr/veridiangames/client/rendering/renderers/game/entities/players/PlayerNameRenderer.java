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

package fr.veridiangames.client.rendering.renderers.game.entities.players;

import fr.veridiangames.client.guis.TrueTypeFont;
import fr.veridiangames.client.rendering.Camera;
import fr.veridiangames.client.rendering.renderers.guis.Font3DRenderer;
import fr.veridiangames.client.rendering.shaders.Gui3DShader;
import fr.veridiangames.core.GameCore;
import fr.veridiangames.core.utils.Color4f;

import java.awt.*;

/**
 * Created by Marc on 13/06/2016.
 */
public class PlayerNameRenderer
{
    private Font3DRenderer fontRenderer;

    public PlayerNameRenderer()
    {
        TrueTypeFont font = new TrueTypeFont(new Font("Arial", Font.PLAIN, 20), true);
        fontRenderer = new Font3DRenderer(font, "MDR GROS CONNARD", GameCore.getInstance().getGame().getPlayer().getPosition().copy().add(0, -3, 0));
    }

    public void render(Gui3DShader shader, Camera camera)
    {
        fontRenderer.render(shader, camera, Color4f.BLUE, 0);
    }
}
