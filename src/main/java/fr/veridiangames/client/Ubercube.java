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

package fr.veridiangames.client;

import fr.veridiangames.client.audio.AudioListener;
import fr.veridiangames.client.audio.AudioSystem;
import fr.veridiangames.client.main.minimap.MinimapHandler;
import fr.veridiangames.client.main.screens.ConsoleScreen;
import fr.veridiangames.client.main.screens.PlayerHudScreen;
import fr.veridiangames.client.main.screens.LoadingScreen;
import fr.veridiangames.client.rendering.guis.GuiCanvas;
import fr.veridiangames.client.rendering.guis.GuiManager;
import fr.veridiangames.client.rendering.guis.GuiUtils;
import fr.veridiangames.core.GameCore;
import fr.veridiangames.core.game.entities.player.ClientPlayer;
import fr.veridiangames.core.maths.Quat;
import fr.veridiangames.core.maths.Vec3;
import fr.veridiangames.core.network.Protocol;
import fr.veridiangames.core.network.packets.ConnectPacket;
import fr.veridiangames.client.main.player.PlayerHandler;
import fr.veridiangames.client.network.NetworkClient;
import fr.veridiangames.client.rendering.Display;
import fr.veridiangames.client.rendering.renderers.MainRenderer;
import fr.veridiangames.core.network.packets.DisconnectPacket;
import fr.veridiangames.core.profiler.Profiler;
import fr.veridiangames.core.utils.Color4f;
import fr.veridiangames.core.utils.Log;

import javax.swing.*;

/**
 * Created by Marccspro on 28 janv. 2016.
 */
public class Ubercube
{
	private static Ubercube		instance;

	private GameCore			core;
	private Display				display;
	
	private MainRenderer		mainRenderer;
	private PlayerHandler		playerHandler;
	private MinimapHandler		minimapHandler;
	private NetworkClient		net;
	private GuiManager 			guiManager;
	private boolean 			connected;
	private boolean 			joinGame;
	private LoadingScreen       gameLoading;
	private boolean 			inConsole;
	private ConsoleScreen		console;
	private boolean				inMenu;
	private boolean				hasInitRendering;

	private Profiler			renderProfiler;
	private Profiler			updateProfiler;
	private Profiler			audioProfiler;
	private Profiler			guiProfiler;
	private Profiler			coreProfiler;
	private Profiler			playerProfiler;
	private Profiler			renderUpdateProfiler;
	private Profiler			physicsProfiler;
	private Profiler			sleepProfiler;

	private PlayerHudScreen 	playerHudScreen;

	public Ubercube()
	{
		Log.println("Starting " + GameCore.GAME_NAME + " " + GameCore.GAME_VERSION_NAME + " v" + GameCore.GAME_SUB_VERSION);
		/* *** AUDIO INITIALISATION *** */
		AudioSystem.init();
		AudioListener.init();
	}

	public void init()
	{
		instance = this;
		inMenu = false;

		/* *** INIT STUFF *** */
		this.playerHandler = new PlayerHandler(core, net);
		this.minimapHandler = new MinimapHandler(core);
		this.guiManager = new GuiManager();

		/* *** LOADING GUI *** */
		gameLoading = new LoadingScreen(null, display);
		this.guiManager.add(gameLoading);

		/* *** PLAYER HUD GUI *** */
		this.playerHudScreen = new PlayerHudScreen(null, display, core);
		this.console = playerHudScreen.getConsoleScreen();
		this.guiManager.add(playerHudScreen);

		/* *** PROFILER *** */
		this.renderProfiler = new Profiler("render", new Color4f(0.57f, 0.75f, 0.91f, 1f));
		this.updateProfiler = new Profiler("update", new Color4f(0.75f, 0.57f, 0.91f, 1f));
		this.physicsProfiler = new Profiler("physics", new Color4f(0.73f, 0.77f, 0.55f, 1f));
		this.audioProfiler = new Profiler("audio", true);
		this.guiProfiler = new Profiler("gui", true);
		this.coreProfiler = new Profiler("core", true);
		this.playerProfiler = new Profiler("player", true);
		this.renderUpdateProfiler = new Profiler("render_update", true);
		//this.sleepProfiler = new Profiler("sleep");
		Profiler.setResolution(5);
	}

	public void initRendering()
	{
		if (hasInitRendering)
			return;
		this.mainRenderer = new MainRenderer(this, core);
		hasInitRendering = true;
	}

	public void update()
	{
        core.setIgnoreAction(false);
		updateProfiler.start();

        audioProfiler.start();
			AudioSystem.update(core);
		audioProfiler.end();

		guiProfiler.start();
			guiManager.update();
		guiProfiler.end();

		gameLoading.update(this);

		if (core.getGame().getWorld() != null && core.getGame().getWorld().isGenerated())
			initRendering();

		if (net.isConnected() && core.getGame().getWorld().isGenerated())
		{
			if (!connected && net.isConnected())
				connected = true;
			if (!joinGame && gameLoading.hasJoinedGame())
			{
				display.getInput().getMouse().setGrabbed(true);
				guiManager.setCanvas(1);
				joinGame = true;
			}
			if (joinGame && !core.getGame().getPlayer().isKicked() && !core.getGame().getPlayer().isTimedOut())
			{
				coreProfiler.start();
					core.update();
				coreProfiler.end();

				playerProfiler.start();
					playerHandler.update(display.getInput());
				playerProfiler.end();

				minimapHandler.update();

				renderUpdateProfiler.start();
					mainRenderer.update();
				renderUpdateProfiler.end();

				AudioListener.setTransform(core.getGame().getPlayer().getEyeTransform());
			}
		}
		if (core.getGame().getPlayer().isKicked())
		{
			display.getInput().getMouse().setGrabbed(false);
			String msg = core.getGame().getPlayer().getKickMessage();
			joinGame = false;
			Object[] options = {"OK"};
			int n = GuiUtils.warning("You got kicked: " + msg, options);
			if (n == JOptionPane.YES_OPTION)
				exitGame();
		}
		if (core.getGame().getPlayer().isTimedOut())
		{
			display.getInput().getMouse().setGrabbed(false);
			joinGame = false;
			Object[] options = {"OK"};
			int n = GuiUtils.warning("Time out: connection lost !", options);
			if (n == JOptionPane.YES_OPTION)
				exitGame();
		}
		updateProfiler.end();
	}

	public void updatePhysics()
	{
		if (!net.isConnected())
			return;

		physicsProfiler.start();
		core.updatePhysics();
		physicsProfiler.end();
	}
	
	public void render()
	{
		if (!hasInitRendering)
			return;
		renderProfiler.start();
		mainRenderer.renderAll(display);
		guiManager.render(display);
		renderProfiler.end();
	}

	public void start()
	{
		init();

		fr.veridiangames.client.main.Timer timer = new fr.veridiangames.client.main.Timer();
		
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
				display.getInput().getMouse().update();
				display.getInput().getKeyboardCallback().update();
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
				Profiler.updateAll();
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
					Log.exception(e);
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
		disconnectAndExit();
	}

	public void exitGame()
	{
		net.setConnected(false);
		connected = false;
		net.stop();
		display.setDestroyed(true);
		AudioSystem.destroy();
		Log.println("Bye !");
		System.exit(0);
	}

	public void disconnectAndExit()
	{
		net.send(new DisconnectPacket(core.getGame().getPlayer().getID(), "Client closed the game"), Protocol.TCP);
		exitGame();
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
	
	public void openConnection(int clientID, String name, String address, int port)
	{
		net = new NetworkClient(clientID, address, port, this);

		float midWorld = core.getGame().getData().getWorldSize() / 2 * 16;
		ClientPlayer player = new ClientPlayer(clientID, name, new Vec3(midWorld, 30, midWorld), new Quat(), address, port);
		player.setNetwork(net);
		
		core.getGame().setPlayer(player);
		net.send(new ConnectPacket(player), Protocol.TCP);
	}

	public PlayerHandler getPlayerHandler()
	{
		return playerHandler;
	}

	public static Ubercube getInstance()
	{
		return instance;
	}

	public boolean isConnected()
	{
		return connected;
	}

	public NetworkClient getNet()
	{
		return net;
	}

	public GuiManager getGuiManager() { return guiManager; }

	public boolean isInMenu() { return inMenu; }
	public void setInMenu(boolean inMenu) { this.inMenu = inMenu; }

	public void setScreen(GuiCanvas canvas) // This is wrong
	{
		guiManager.add(canvas);
		guiManager.setCanvas(guiManager.getCanvases().indexOf(canvas));
	}

	public GuiCanvas getScreen(int i)
	{
		return guiManager.getCanvases().get(i);
	}

	public boolean isInConsole()
	{
		return inConsole;
	}

	public void setInConsole(boolean inConsole)
	{
		this.inConsole = inConsole;
	}

	public ConsoleScreen getConsole()
	{
		return console;
	}

	public MinimapHandler getMinimapHandler()
	{
		return minimapHandler;
	}

	public PlayerHudScreen getPlayerHudScreen()
	{
		return playerHudScreen;
	}
}