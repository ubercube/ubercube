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

package fr.veridiangames.client.main.screens;

import fr.veridiangames.client.Ubercube;
import fr.veridiangames.client.inputs.Input;
import fr.veridiangames.client.rendering.Display;
import fr.veridiangames.client.rendering.guis.GuiCanvas;
import fr.veridiangames.client.rendering.guis.GuiComponent;
import fr.veridiangames.client.rendering.guis.components.GuiPanel;
import fr.veridiangames.client.rendering.guis.components.GuiTextBox;
import fr.veridiangames.core.GameCore;
import fr.veridiangames.core.utils.Color4f;

/**
 * Created by Marc on 13/07/2016.
 */
public class ConsoleScreen extends GuiCanvas
{
    private GameCore core;

    private boolean console;
    private GuiPanel bg;
    private GuiTextBox write;
    private String message;

    public ConsoleScreen(Display display, GameCore core, int x, int y, int w, int h)
    {
        super();
        this.core = core;

//        gameFpsLabel = new GuiLabel("60 Fps", 10, 30, 20f);
//        gameFpsLabel.setOrigin(GuiComponent.GuiOrigin.A);
//        gameFpsLabel.setScreenParent(GuiComponent.GuiCorner.TL);
//        gameFpsLabel.setColor(Color4f.WHITE);
//        gameFpsLabel.setDropShadow(2);
//        gameFpsLabel.setDropShadowColor(new Color4f(0, 0, 0, 0.5f));
//        super.add(gameFpsLabel);

        bg = new GuiPanel(x, y, w, h);
        bg.setColor(new Color4f(0, 0, 0, 0.5f));
        bg.setScreenParent(GuiComponent.GuiCorner.BL);
        bg.setOrigin(GuiComponent.GuiOrigin.D);
        super.add(bg);

        write = new GuiTextBox(x, y, w, 64);
        write.setOrigin(GuiComponent.GuiOrigin.D);
        write.setScreenParent(GuiComponent.GuiCorner.BL);
        super.add(write);
    }

    public void update()
    {
        super.update();

        if (Display.getInstance().getInput().getKeyDown(Input.KEY_T))
            console = true;
        if (Display.getInstance().getInput().getKeyDown(Input.KEY_ESCAPE))
            console = false;
        Ubercube.getInstance().setConsole(console);
        bg.setUseable(console);
        write.setUseable(console);

        this.message = write.getText();
    }
}
