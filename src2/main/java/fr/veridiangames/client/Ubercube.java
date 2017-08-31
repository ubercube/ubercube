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

import javax.swing.JOptionPane;

import fr.veridiangames.client.audio.AudioListener;
import fr.veridiangames.client.audio.AudioSystem;
import fr.veridiangames.client.main.player.PlayerHandler;
import fr.veridiangames.client.main.screens.ConsoleScreen;
import fr.veridiangames.client.main.screens.LoadingScreen;
import fr.veridiangames.client.main.screens.PlayerHudScreen;
import fr.veridiangames.client.network.NetworkClient;
import fr.veridiangames.client.rendering.Display;
import fr.veridiangames.client.rendering.guis.GuiCanvas;
import fr.veridiangames.client.rendering.guis.GuiManager;
import fr.veridiangames.client.rendering.guis.GuiUtils;
import fr.veridiangames.client.rendering.renderers.MainRenderer;
import fr.veridiangames.core.GameCore;
import fr.veridiangames.core.game.entities.player.ClientPlayer;
import fr.veridiangames.core.maths.Quat;
import fr.veridiangames.core.maths.Vec3;
import fr.veridiangames.core.network.Protocol;
import fr.veridiangames.core.network.packets.ConnectPacket;
import fr.veridiangames.core.network.packets.DisconnectPacket;
import fr.veridiangames.core.profiler.Profiler;
import fr.veridiangames.core.utils.Color4f;
import fr.veridiangames.core.utils.Log;

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
		this.inMenu = false;

		/* *** INIT STUFF *** */
		this.playerHandler = new PlayerHandler(this.core, this.net);
		this.guiManager = new GuiManager();

		/* *** LOADING GUI *** */
		this.gameLoading = new LoadingScreen(null, this.display);
		this.guiManager.add(this.gameLoading);

		/* *** PLAYER HUD GUI *** */
		PlayerHudScreen playerHudGui = new PlayerHudScreen(null, this.display, this.core);
		this.console = playerHudGui.getConsoleScreen();
		this.guiManager.add(playerHudGui);

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
		if (this.hasInitRendering)
			return;
		this.mainRenderer = new MainRenderer(this, this.core);
		this.hasInitRendering = true;
	}

	public void update()
	{
        this.core.setIgnoreAction(false);
		this.updateProfiler.start();

        this.audioProfiler.start();
			AudioSystem.update(this.core);
		this.audioProfiler.end();

		this.guiProfiler.start();
			this.guiManager.update();
		this.guiProfiler.end();

		this.gameLoading.update(this);

		if (this.core.getGame().getWorld() != null && this.core.getGame().getWorld().isGenerated())
			this.initRendering();

		if (this.net.isConnected() && this.core.getGame().getWorld().isGenerated())
		{
			if (!this.connected && this.net.isConnected())
				this.connected = true;
			if (!this.joinGame && this.gameLoading.hasJoinedGame())
			{
				this.display.getInput().getMouse().setGrabbed(true);
				this.guiManager.setCanvas(1);
				this.joinGame = true;
			}
			if (this.joinGame && !this.core.getGame().getPlayer().isKicked() && !this.core.getGame().getPlayer().isTimedOut())
			{
				this.coreProfiler.start();
					this.core.update();
				this.coreProfiler.end();

				this.playerProfiler.start();
					this.playerHandler.update(this.display.getInput());
				this.playerProfiler.end();

				this.renderUpdateProfiler.start();
					this.mainRenderer.update();
				this.renderUpdateProfiler.end();

				AudioListener.setTransform(this.core.getGame().getPlayer().getEyeTransform());
			}
		}
		if (this.core.getGame().getPlayer().isKicked())
		{
			this.display.getInput().getMouse().setGrabbed(false);
			String msg = this.core.getGame().getPlayer().getKickMessage();
			this.joinGame = false;
			Object[] options = {"OK"};
			int n = GuiUtils.warning("You got kicked: " + msg, options);
			if (n == JOptionPane.YES_OPTION)
				this.exitGame();
		}
		if (this.core.getGame().getPlayer().isTimedOut())
		{
			this.display.getInput().getMouse().setGrabbed(false);
			this.joinGame = false;
			Object[] options = {"OK"};
			int n = GuiUtils.warning("Time out: connection lost !", options);
			if (n == JOptionPane.YES_OPTION)
				this.exitGame();
		}
		this.updateProfiler.end();
	}

	public static boolean warning(String msg)
	{
		Object[] options = {"OK"};
		int n = GuiUtils.warning(msg, options);
		if (n == JOptionPane.YES_OPTION)
			return true;
		return false;
	}

	public void updatePhysics()
	{
		if (!this.net.isConnected())
			return;

		this.physicsProfiler.start();
		this.core.updatePhysics();
		this.physicsProfiler.end();
	}

	public void render()
	{
		if (!this.hasInitRendering)
			return;
		this.renderProfiler.start();
		this.mainRenderer.renderAll(this.display);
		this.guiManager.render(this.display);
		this.renderProfiler.end();
	}

	public void start()
	{
		this.init();

		fr.veridiangames.client.main.Timer timer = new fr.veridiangames.client.main.Timer();

		double tickTime = 1000000000.0 / 60.0;
		double renderTime = 1000000000.0 / 9000.0;
		double updatedTime = 0.0;
		double renderedTime = 0.0;

		int secondTime = 0;
		boolean second;
		int frames = 0;
		int ticks = 0;
		while (!this.display.isClosed())
		{
			second = false;
			if (timer.getElapsed() - updatedTime >= tickTime)
			{
				this.display.getInput().getMouse().update();
				this.display.getInput().getKeyboardCallback().update();
				this.update();
				this.updatePhysics();
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
				this.render();
				frames++;
				this.display.update();
				Profiler.updateAll();
				renderedTime += renderTime;
			} else
				try
				{
					Thread.sleep(1);
				}
				catch (InterruptedException e)
				{
					Log.exception(e);
				}
			if (second)
			{
				this.display.setFps(frames);
				this.display.setTps(ticks);
				this.display.displayTitle(this.display.getTitle() + " | " + ticks + " tps, " + frames + " fps");
				frames = 0;
				ticks = 0;
			}
		}
		this.disconnectAndExit();
	}

	public void exitGame()
	{
		this.net.setConnected(false);
		this.connected = false;
		this.net.stop();
		this.display.setDestroyed(true);
		AudioSystem.destroy();
		Log.println("Bye !");
		System.exit(0);
	}

	public void disconnectAndExit()
	{
		this.net.send(new DisconnectPacket(this.core.getGame().getPlayer().getID(), "Client closed the game"), Protocol.TCP);
		this.exitGame();
	}

	public GameCore getGameCore()
	{
		return this.core;
	}

	public void setGameCore(GameCore core)
	{
		this.core = core;
	}

	public Display getDisplay()
	{
		return this.display;
	}

	public void setDisplay(Display display)
	{
		this.display = display;
	}

	public void openConnection(int clientID, String name, String address, int port)
	{
		this.net = new NetworkClient(clientID, address, port, this);

		float midWorld = this.core.getGame().getData().getWorldSize() / 2 * 16;
		ClientPlayer player = new ClientPlayer(clientID, name, new Vec3(midWorld, 30, midWorld), new Quat(), address, port);
		player.setNetwork(this.net);

		this.core.getGame().setPlayer(player);
		this.net.send(new ConnectPacket(player), Protocol.TCP);
	}

	public PlayerHandler getPlayerHandler()
	{
		return this.playerHandler;
	}

	public static Ubercube getInstance()
	{
		return instance;
	}

	public boolean isConnected()
	{
		return this.connected;
	}

	public NetworkClient getNet()
	{
		return this.net;
	}

	public GuiManager getGuiManager() { return this.guiManager; }

	public boolean isInMenu() { return this.inMenu; }
	public void setInMenu(boolean inMenu) { this.inMenu = inMenu; }

	public void setScreen(GuiCanvas canvas) // This is wrong
	{
		this.guiManager.add(canvas);
		this.guiManager.setCanvas(this.guiManager.getCanvases().indexOf(canvas));
	}

	public GuiCanvas getScreen(int i)
	{
		return this.guiManager.getCanvases().get(i);
	}

	public boolean isInConsole()
	{
		return this.inConsole;
	}

	public void setInConsole(boolean inConsole)
	{
		this.inConsole = inConsole;
	}

	public ConsoleScreen getConsole()
	{
		return this.console;
	}
}