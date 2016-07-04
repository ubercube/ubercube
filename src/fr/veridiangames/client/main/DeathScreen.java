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

package fr.veridiangames.client.main;

import fr.veridiangames.client.rendering.Display;
import fr.veridiangames.client.rendering.guis.GuiCanvas;
import fr.veridiangames.client.rendering.guis.GuiComponent;
import fr.veridiangames.client.rendering.guis.components.GuiButton;
import fr.veridiangames.client.rendering.guis.components.GuiLabel;
import fr.veridiangames.client.rendering.guis.components.GuiPanel;
import fr.veridiangames.client.rendering.guis.listeners.GuiActionListener;
import fr.veridiangames.client.rendering.textures.TextureLoader;
import fr.veridiangames.core.utils.Color4f;

import static org.lwjgl.opengl.GL11.GL_LINEAR;

/**
 * Created by Marc on 02/07/2016.
 */
public class DeathScreen extends GuiCanvas
{
    GuiButton respawnButton;

    private boolean respawn;

    public DeathScreen(Display display)
    {
        this.respawn = false;

        GuiPanel bg = new GuiPanel(0, 0, display.getWidth(), display.getHeight());
        bg.setTexture(TextureLoader.loadTexture("res/textures/LoadingBG.png", GL_LINEAR, false));
        bg.setColor(Color4f.WHITE);
        bg.setOrigin(GuiComponent.GuiOrigin.A);
        bg.setScreenParent(GuiComponent.GuiCorner.SCALED);
        super.add(bg);

        GuiLabel dieText = new GuiLabel("You died !", display.getWidth() / 2, display.getHeight() / 2, 42f);
        super.add(dieText);

        respawnButton = new GuiButton("Respawn", display.getWidth() / 2, display.getHeight() - 50, 200, new GuiActionListener()
        {
            public void onAction()
            {
                respawn = true;
            }
        });
        respawnButton.centerText();
        respawnButton.setClickable(false);
        respawnButton.setOrigin(GuiComponent.GuiOrigin.CENTER);
        respawnButton.setScreenParent(GuiComponent.GuiCorner.BC);
        super.add(respawnButton);
    }

    public boolean isRespawn()
    {
        return respawn;
    }
}
