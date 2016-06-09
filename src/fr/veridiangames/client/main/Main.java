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

package fr.veridiangames.client.main;

import fr.veridiangames.core.GameCore;
import fr.veridiangames.core.game.entities.player.ClientPlayer;
import fr.veridiangames.core.maths.Quat;
import fr.veridiangames.core.maths.Vec3;
import fr.veridiangames.core.network.packets.ConnectPacket;
import fr.veridiangames.core.network.packets.DisconnectPacket;
import fr.veridiangames.core.utils.Color4f;
import fr.veridiangames.client.guis.GuiCanvas;
import fr.veridiangames.client.guis.GuiManager;
import fr.veridiangames.client.guis.canvases.ConnectionCanvas;
import fr.veridiangames.client.guis.canvases.PlayerHUD;
import fr.veridiangames.client.guis.components.GuiPanel;
import fr.veridiangames.client.main.console.Console;
import fr.veridiangames.client.main.player.PlayerHandler;
import fr.veridiangames.client.network.NetworkClient;
import fr.veridiangames.client.rendering.Display;
import fr.veridiangames.client.rendering.renderers.MainRenderer;

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
	private GuiManager		guiManager;
	private Console			console;
	private boolean 		connected;
	private PlayerHUD		playerHUD;
	
	public void init()
	{
		main = this;
		this.guiManager = new GuiManager();
		
		GuiCanvas connectionCanvas = new ConnectionCanvas(net.getAddress().getHostAddress(), net.getPort(), display);
		
		GuiCanvas canvas = new GuiCanvas();
		canvas.add(new GuiPanel(display.getWidth() / 2 - 1, display.getHeight() / 2 - 1, 2, 2).setColor(Color4f.WHITE).setBorder(3, new Color4f(0, 0, 0, 0.3f)));
		
		playerHUD = new PlayerHUD(display.getWidth(), display.getHeight());
		
		this.guiManager.add(connectionCanvas);
		this.guiManager.add(canvas);
		this.guiManager.setCanvas(0);
		
		this.playerHandler = new PlayerHandler(core, net);
		this.mainRenderer = new MainRenderer(this, core);
	}

	public void update()
	{
		//playerHUD.update(display);
		if (!connected && net.isConnected())
		{
			display.getInput().getMouse().setGrabbed(true);
			connected = true;
		}
		if (net.isConnected())
		{
			guiManager.setCanvas(1);
			console.update();
			core.update();
			playerHandler.update(display.getInput());
			mainRenderer.update();
		}
	}
	
	public void render()
	{
		mainRenderer.renderAll(display);
		//playerHUD.renderBlock();
		if (net.isConnected())
		{
			console.render(display);
		}
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
		ClientPlayer player = new ClientPlayer(clientID, new String("ID" + clientID).substring(0, 6), new Vec3(midWorld, 20, midWorld), new Quat(), address, port);
		player.setNetwork(net);
		
		core.getGame().setPlayer(player);
		net.send(new ConnectPacket(player));
	}

	public GuiManager getGuiManager()
	{
		return guiManager;
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