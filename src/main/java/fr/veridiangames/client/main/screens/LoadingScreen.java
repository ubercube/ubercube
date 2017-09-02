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
import fr.veridiangames.client.rendering.Display;
import fr.veridiangames.client.rendering.guis.GuiCanvas;
import fr.veridiangames.client.rendering.guis.GuiComponent;
import fr.veridiangames.client.rendering.guis.StaticFont;
import fr.veridiangames.client.rendering.guis.components.GuiButton;
import fr.veridiangames.client.rendering.guis.components.GuiLabel;
import fr.veridiangames.client.rendering.guis.components.GuiPanel;
import fr.veridiangames.client.rendering.guis.components.GuiTextArea;
import fr.veridiangames.client.rendering.guis.listeners.GuiActionListener;
import fr.veridiangames.client.rendering.textures.TextureLoader;
import fr.veridiangames.core.GameCore;
import fr.veridiangames.core.game.entities.player.ClientPlayer;
import fr.veridiangames.core.network.Protocol;
import fr.veridiangames.core.network.packets.RespawnPacket;
import fr.veridiangames.core.utils.Color4f;

import java.awt.*;

import static fr.veridiangames.client.FileManager.getResource;
import static org.lwjgl.opengl.GL11.GL_LINEAR;

/**
 * Created by Marc on 02/07/2016.
 */
public class LoadingScreen extends GuiCanvas
{
    GuiLabel loadingInfo;
    GuiButton joinGameButton;

    private boolean joinGame;

    public LoadingScreen(GuiCanvas parent, Display display)
    {
        super(parent);
        this.joinGame = false;

        GuiPanel bg = new GuiPanel(0, 0, display.getWidth(), display.getHeight());
        bg.setTexture(TextureLoader.loadTexture(getResource("textures/LoadingBG.png"), GL_LINEAR, false));
        bg.setOrigin(GuiComponent.GuiOrigin.A);
        bg.setScreenParent(GuiComponent.GuiCorner.SCALED);
        super.add(bg);

        loadingInfo = new GuiLabel("Connecting...", display.getWidth() / 2, display.getHeight() - 80, 20f);
        loadingInfo.setOrigin(GuiComponent.GuiOrigin.CENTER);
        loadingInfo.setScreenParent(GuiComponent.GuiCorner.BC);
        super.add(loadingInfo);

        joinGameButton = new GuiButton("Join game", display.getWidth() / 2, display.getHeight() - 50, 200, new GuiActionListener()
        {
            public void onAction()
            {
                loadingInfo.setText("Joining game...");
				ClientPlayer p = GameCore.getInstance().getGame().getPlayer();
				p.getNet().send(new RespawnPacket(p), Protocol.TCP);
                joinGame = true;
            }
        });
        joinGameButton.centerText();
        joinGameButton.setClickable(false);
        joinGameButton.setOrigin(GuiComponent.GuiOrigin.CENTER);
        joinGameButton.setScreenParent(GuiComponent.GuiCorner.BC);
        super.add(joinGameButton);

        String creditsText =
                "The Team:\n" +
                " - Marccspro (Creator, Engineer)\n" +
                " - Tybau (Engineer)\n" +
                " - Jimi Vacarians (Engineer)\n" +
                " - Nik' (Art/Design)\n" +
                " - MrDev023 (Engineer)\n" +
                "\n" +
                "\n" +
                "Credits to:\n" +
                " - Mimus Angel for helping in the beginning with the game \nand for his awesome BufferData class !\n" +
                " - Arthur for helping with the game management and for \nhis awesome help with the physics !\n" +
                " - Freezee for helping with gameplay ideas and  with game  \nmanagement !\n" +
                " - Every one else who created art and models !\n" +
                " - The person who proposed the name \"Ubercube\" for the  \ngame (Give me your name ;) ) !\n";

        GuiTextArea creditsTextArea = new GuiTextArea(creditsText, display.getWidth() / 2, display.getHeight() / 2 - 20, 600, 500, StaticFont.Kroftsmann(0, 20));
        creditsTextArea.setOrigin(GuiComponent.GuiOrigin.CENTER);
        creditsTextArea.setScreenParent(GuiComponent.GuiCorner.BC);
        super.add(creditsTextArea);

        GuiLabel creditsTitle = new GuiLabel("Credits", display.getWidth() / 2, 30, 42f);
        creditsTitle.setOrigin(GuiComponent.GuiOrigin.TC);
        creditsTitle.setScreenParent(GuiComponent.GuiCorner.TC);
        creditsTitle.setColor(Color4f.WHITE);
        super.add(creditsTitle);
    }

    public void update(Ubercube main)
    {
        if (Ubercube.getInstance().isConnected())
        {
            loadingInfo.setText("Connected !");
            joinGameButton.setClickable(true);
        }
    }

    public boolean hasJoinedGame()
    {
        return joinGame;
    }
}
