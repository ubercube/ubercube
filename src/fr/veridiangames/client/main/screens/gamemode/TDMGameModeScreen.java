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

package fr.veridiangames.client.main.screens.gamemode;

import fr.veridiangames.client.rendering.Display;
import fr.veridiangames.client.rendering.guis.GuiCanvas;
import fr.veridiangames.client.rendering.guis.GuiComponent;
import fr.veridiangames.client.rendering.guis.components.GuiLabel;
import fr.veridiangames.core.GameCore;
import fr.veridiangames.core.game.modes.TDMGameMode;
import fr.veridiangames.core.utils.Color4f;

/**
 * Created by Jimi Vacarians on 25/07/2016.
 */
public class TDMGameModeScreen extends GuiCanvas {
    private GameCore core;

    GuiLabel redLabel;
    GuiLabel blueLabel;

    public TDMGameModeScreen(GuiCanvas parent, Display display, GameCore core) {
        super(parent);
        this.core = core;

        redLabel = new GuiLabel("Blue : 0", display.getWidth() / 2-40, 30, 20f);
        redLabel.setOrigin(GuiComponent.GuiOrigin.TC);
        redLabel.setScreenParent(GuiComponent.GuiCorner.TC);
        redLabel.setColor(Color4f.RED);
        redLabel.setDropShadow(1);
        redLabel.setDropShadowColor(new Color4f(0, 0, 0, 0.5f));
        super.add(redLabel);

        blueLabel = new GuiLabel("Blue : 0", display.getWidth() / 2 +40, 30, 20f);
        blueLabel.setOrigin(GuiComponent.GuiOrigin.TC);
        blueLabel.setScreenParent(GuiComponent.GuiCorner.TC);
        blueLabel.setColor(Color4f.BLUE);
        blueLabel.setDropShadow(1);
        blueLabel.setDropShadowColor(new Color4f(0, 0, 0, 0.5f));
        super.add(blueLabel);
    }

    public void update(){
        super.update();
        TDMGameMode mode = (TDMGameMode) core.getGame().getGameMode();
        redLabel.setText("Red : " + mode.getRedScore());
        blueLabel.setText("Blue : " + mode.getBlueScore());
    }
}
