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

import fr.veridiangames.client.audio.Audio;
import fr.veridiangames.client.audio.AudioManager;
import fr.veridiangames.client.guis.TrueTypeFont;
import fr.veridiangames.client.main.player.PlayerHudCanvas;
import fr.veridiangames.client.rendering.guis.GuiCanvas;
import fr.veridiangames.client.rendering.guis.GuiComponent;
import fr.veridiangames.client.rendering.guis.GuiManager;
import fr.veridiangames.client.rendering.guis.StaticFont;
import fr.veridiangames.client.rendering.guis.components.GuiLabel;
import fr.veridiangames.client.rendering.guis.components.GuiPanel;
import fr.veridiangames.client.rendering.guis.components.GuiProgressBar;
import fr.veridiangames.core.GameCore;
import fr.veridiangames.core.game.entities.components.ECAudioSource;
import fr.veridiangames.core.game.entities.player.ClientPlayer;
import fr.veridiangames.core.maths.Quat;
import fr.veridiangames.core.maths.Vec3;
import fr.veridiangames.core.network.packets.ConnectPacket;
import fr.veridiangames.client.main.console.Console;
import fr.veridiangames.client.main.player.PlayerHandler;
import fr.veridiangames.client.network.NetworkClient;
import fr.veridiangames.client.rendering.Display;
import fr.veridiangames.client.rendering.renderers.MainRenderer;
import fr.veridiangames.core.network.packets.DisconnectPacket;
import fr.veridiangames.core.utils.Color4f;

import java.awt.*;

/**
 * Created by Marccspro on 28 janv. 2016.
 */
public class Main
{
	private static Main main;

	private GameCore		core;
	private Display			display;
	
	private MainRenderer	mainRenderer;
	private PlayerHandler	playerHandler;
	private NetworkClient	net;
	private Console			console;
	private GuiManager 		guiManager;
	private boolean 		connected;

	public void init()
	{
		main = this;

		/* *** AUDIO INITIALISATION *** */
		AudioManager.init();
		ECAudioSource.addSound("AK47_BULLET_SHOT", Audio.AK47_BULLET_SHOT);

		/* *** INIT STUFF *** */
		this.playerHandler = new PlayerHandler(core, net);
		this.mainRenderer = new MainRenderer(this, core);
		this.guiManager = new GuiManager();

		/* *** LOADING GUI *** */
		GuiCanvas startGui = new GuiCanvas();

		GuiPanel bg = new GuiPanel(0, 0, display.getWidth(), display.getHeight());
		bg.setColor(Color4f.DARK_GRAY);
		bg.setOrigin(GuiComponent.GuiOrigin.A);
		bg.setScreenParent(GuiComponent.GuiCorner.SCALED);
		startGui.add(bg);

		GuiLabel loadingInfo = new GuiLabel("Loading game...", display.getWidth() / 2, display.getHeight() / 2, 42f);
		loadingInfo.setOrigin(GuiComponent.GuiOrigin.CENTER);
		loadingInfo.setScreenParent(GuiComponent.GuiCorner.CENTER);
		startGui.add(loadingInfo);

		this.guiManager.add(startGui);


		/* *** PLAYER HUD GUI *** */
		GuiCanvas playerHudGui = new PlayerHudCanvas(display, core);
		this.guiManager.add(playerHudGui);
	}

	public void update()
	{
		AudioManager.update(core);
		guiManager.update();
		if (!connected && net.isConnected())
		{
			display.getInput().getMouse().setGrabbed(true);
			connected = true;
		}
		if (net.isConnected())
		{
			guiManager.setCanvas(1);
			//console.update();
			core.update();
			playerHandler.update(display.getInput());
			mainRenderer.update();
		}
	}

	public void updatePhysics()
	{
		if (!net.isConnected())
			return;

		core.updatePhysics();
	}
	
	public void render()
	{
		mainRenderer.renderAll(display);
		guiManager.render(display);
//		if (net.isConnected())
//		{
//			console.render(display);
//		}
	}

	public void start()
	{
		init();
		
		Timer timer = new Timer();
		
		double tickTime = 1000000000.0 / 60.0;
		double renderTime = 1000000000.0 / 9000.0;
		double updatedTime = 0.0;
		double renderedTime = 0.0;
		
		int secondTime = 0;
		boolean second;
		int frames = 0;
		int ticks = 0;
		while (!display.isClosed())
		{
			second = false;
			if (timer.getElapsed() - updatedTime >= tickTime)
			{
				display.getInput().update();
				update();
				updatePhysics();
				ticks++;

				secondTime++;
				if (secondTime % 60 == 0)
				{
					second = true;
					secondTime = 0;
				}
				updatedTime += tickTime;
			}
			else if (timer.getElapsed() - renderedTime >= renderTime)
			{
				render();
				frames++;
				display.update();
				renderedTime += renderTime;
			}
			else
			{
				try
				{
					Thread.sleep(1);
				}
				catch (InterruptedException e)
				{
					e.printStackTrace();
				}
			}
			if (second)
			{
				display.setFps(frames);
				display.setTps(ticks);
				display.displayTitle(display.getTitle() + " | " + ticks + " tps, " + frames + " fps");
				frames = 0;
				ticks = 0;
			}
		}
		net.send(new DisconnectPacket(core.getGame().getPlayer().getID()));
		display.setDestroyed(true);
		AudioManager.destroy();
		System.exit(0);
	}

	public GameCore getGameCore()
	{
		return core;
	}

	public void setGameCore(GameCore core)
	{
		this.core = core;
	}

	public Display getDisplay()
	{
		return display;
	}

	public void setDisplay(Display display)
	{
		this.display = display;
	}
	
	public void openConnection(int clientID, String address, int port)
	{
		this.console = new Console();
		net = new NetworkClient(clientID, address, port, this);
		
		float midWorld = core.getGame().getData().getWorldSize() / 2 * 16;
		ClientPlayer player = new ClientPlayer(clientID, new String("ID" + clientID).substring(0, 6), new Vec3(midWorld, 30, midWorld), new Quat(), address, port);
		player.setNetwork(net);
		
		core.getGame().setPlayer(player);
		net.send(new ConnectPacket(player));
	}

	public PlayerHandler getPlayerHandler()
	{
		return playerHandler;
	}
	
	public Console getConsole()
	{
		return console;
	}
	
	public static Main getMain()
	{
		return main;
	}
}