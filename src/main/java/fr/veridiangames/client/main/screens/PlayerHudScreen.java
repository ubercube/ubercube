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

import fr.veridiangames.client.Resource;
import fr.veridiangames.client.Ubercube;
import fr.veridiangames.client.audio.AudioPlayer;
import fr.veridiangames.client.inputs.Input;
import fr.veridiangames.client.main.minimap.MinimapHandler;
import fr.veridiangames.client.main.screens.gamemenu.GameMenuScreen;
import fr.veridiangames.client.rendering.Display;
import fr.veridiangames.client.rendering.guis.GuiCanvas;
import fr.veridiangames.client.rendering.guis.GuiComponent;
import fr.veridiangames.client.rendering.guis.components.GuiLabel;
import fr.veridiangames.client.rendering.guis.components.GuiMinimap;
import fr.veridiangames.client.rendering.guis.components.GuiPanel;
import fr.veridiangames.client.rendering.textures.Texture;
import fr.veridiangames.client.rendering.textures.TextureLoader;
import fr.veridiangames.core.GameCore;
import fr.veridiangames.client.main.screens.gamemode.TDMHudScreen;
import fr.veridiangames.core.game.entities.player.ClientPlayer;
import fr.veridiangames.core.game.entities.weapons.Weapon;
import fr.veridiangames.core.game.entities.weapons.explosiveWeapons.WeaponGrenade;
import fr.veridiangames.core.game.entities.weapons.fireWeapons.FireWeapon;
import fr.veridiangames.core.utils.Color4f;
import org.lwjgl.opengl.GL11;

import static fr.veridiangames.client.Resource.getResource;
import static org.lwjgl.opengl.GL11.GL_NEAREST;

/**
 * Created by Marc on 23/06/2016.
 */
public class PlayerHudScreen extends GuiCanvas
{
    private GameCore core;

    private GuiPanel playerHealth;
    private GuiLabel playerHealthText;
    private GuiLabel weaponStats;
    private GuiLabel gameFpsLabel;
    private GuiLabel playerPosition;
    private GuiLabel audioStatus;
    private GuiPanel damageEffect;
    private GuiLabel headshotLabel;
    private ConsoleScreen consoleScreen;
    private boolean showCrosshair;
    private GuiPanel crosshair;
    public GuiMinimap minimap;

    private int headshotTimer = 0;

    private int health;

    public PlayerHudScreen(GuiCanvas parent, Display display, GameCore core)
    {
        super(parent);
        this.core = core;
		this.showCrosshair = core.getGame().getPlayer().getWeaponComponent().getWeapon().getCrosshairTexture() != null;

		GuiPanel vignette = new GuiPanel(0, 0, Display.getInstance().getWidth(), display.getHeight());
		vignette.setTexture(TextureLoader.loadTexture(Resource.getResource("textures/vignette.png"), GL11.GL_LINEAR, false));
		vignette.setOrigin(GuiComponent.GuiOrigin.A);
		vignette.setScaleParent(GuiComponent.GuiScale.SCALED);
		super.add(vignette);

		headshotLabel = new GuiLabel("HEADSHOT", Display.getInstance().getWidth()/2, display.getHeight()/2/2*3, 25f);
		headshotLabel.setOrigin(GuiComponent.GuiOrigin.CENTER);
		headshotLabel.setScreenParent(GuiComponent.GuiCorner.BL);
		headshotLabel.setColor(Color4f.WHITE);
		headshotLabel.setDropShadow(3);
		headshotLabel.setDropShadowColor(new Color4f(0, 0, 0, 0.5f));
		super.add(headshotLabel);

        damageEffect = new GuiPanel(0, 0, Display.getInstance().getWidth(), Display.getInstance().getHeight());
        damageEffect.setColor(Color4f.RED);
        damageEffect.setOrigin(GuiComponent.GuiOrigin.A);
        damageEffect.setScaleParent(GuiComponent.GuiScale.SCALED);
        damageEffect.getColor().setAlpha(0);
        super.add(damageEffect);

        health = GameCore.getInstance().getGame().getPlayer().getLife();

        GuiPanel playerHealthShadow = new GuiPanel(35 + 2, display.getHeight() - 60 + 3, 300, 30);
        playerHealthShadow.setColor(new Color4f(0f, 0f, 0f, 0.3f));
        playerHealthShadow.setOrigin(GuiComponent.GuiOrigin.A);
        playerHealthShadow.setScreenParent(GuiComponent.GuiCorner.BL);
        super.add(playerHealthShadow);

        playerHealth = new GuiPanel(35, display.getHeight() - 60, 300, 30);
        playerHealth.setColor(new Color4f(0.7f, 0.1f, 0));
        playerHealth.setOrigin(GuiComponent.GuiOrigin.A);
        playerHealth.setScreenParent(GuiComponent.GuiCorner.BL);
        super.add(playerHealth);

        playerHealthText = new GuiLabel("100 HP", 35 + 2 + 150, display.getHeight() - 60 + 3 + 12, 25f);
        playerHealthText.setOrigin(GuiComponent.GuiOrigin.CENTER);
        playerHealthText.setScreenParent(GuiComponent.GuiCorner.BL);
        playerHealthText.setColor(Color4f.WHITE);
        playerHealthText.setDropShadow(3);
        playerHealthText.setDropShadowColor(new Color4f(0, 0, 0, 0.5f));
        super.add(playerHealthText);

        weaponStats = new GuiLabel("12/30", 60, display.getHeight() - 65 - 5 - 45 + 3, 45f);
        weaponStats.setOrigin(GuiComponent.GuiOrigin.A);
        weaponStats.setScreenParent(GuiComponent.GuiCorner.BL);
        weaponStats.setColor(Color4f.WHITE);
        weaponStats.setDropShadow(3);
        weaponStats.setDropShadowColor(new Color4f(0, 0, 0, 0.5f));
        super.add(weaponStats);

        crosshair = new GuiPanel(display.getWidth() / 2, display.getHeight() / 2, 100, 100);
		crosshair.setOrigin(GuiComponent.GuiOrigin.CENTER);
		crosshair.setScreenParent(GuiComponent.GuiCorner.CENTER);
		crosshair.setTexture(Texture.STD_CROSSHAIR);
        super.add(crosshair);

        GuiLabel gameVersionLabel = new GuiLabel(GameCore.GAME_NAME + " " + GameCore.GAME_VERSION_NAME, 10, 10, 20f);
        gameVersionLabel.setOrigin(GuiComponent.GuiOrigin.A);
        gameVersionLabel.setScreenParent(GuiComponent.GuiCorner.TL);
        gameVersionLabel.setColor(Color4f.WHITE);
        gameVersionLabel.setDropShadow(2);
        gameVersionLabel.setDropShadowColor(new Color4f(0, 0, 0, 0.5f));
        super.add(gameVersionLabel);

        gameFpsLabel = new GuiLabel("60 Fps", 10, 30, 20f);
        gameFpsLabel.setOrigin(GuiComponent.GuiOrigin.A);
        gameFpsLabel.setScreenParent(GuiComponent.GuiCorner.TL);
        gameFpsLabel.setColor(Color4f.WHITE);
        gameFpsLabel.setDropShadow(2);
        gameFpsLabel.setDropShadowColor(new Color4f(0, 0, 0, 0.5f));
        super.add(gameFpsLabel);

        audioStatus = new GuiLabel("Audio muted !", 10, 50, 20f);
        audioStatus.setOrigin(GuiComponent.GuiOrigin.A);
        audioStatus.setScreenParent(GuiComponent.GuiCorner.TL);
        audioStatus.setColor(Color4f.RED);
        audioStatus.setDropShadow(2);
        audioStatus.setDropShadowColor(new Color4f(0, 0, 0, 0.5f));
        audioStatus.setUseable(AudioPlayer.muteAudio);
        super.add(audioStatus);

        playerPosition = new GuiLabel("0 - 0 - 0", display.getWidth() / 2, 10, 20f);
        playerPosition.setOrigin(GuiComponent.GuiOrigin.TC);
        playerPosition.setScreenParent(GuiComponent.GuiCorner.TC);
        playerPosition.setColor(Color4f.WHITE);
        playerPosition.setDropShadow(2);
        playerPosition.setDropShadowColor(new Color4f(0, 0, 0, 0.5f));
        super.add(playerPosition);

        consoleScreen = new ConsoleScreen(this, display, core, 10, Display.getInstance().getHeight() - 130, 600, 450);
        super.addCanvas(consoleScreen);

        MinimapHandler minimapHandler = Ubercube.getInstance().getMinimapHandler();

		GuiPanel minimapShadow = new GuiPanel(
			Display.getInstance().getWidth() - minimapHandler.getPos().x + 2,
			Display.getInstance().getHeight() - minimapHandler.getPos().y + 3,
			minimapHandler.getSize().x, minimapHandler.getSize().y);
		minimapShadow.setOrigin(GuiComponent.GuiOrigin.C);
		minimapShadow.setScreenParent(GuiComponent.GuiCorner.BR);
		minimapShadow.setColor(new Color4f(0, 0, 0, 0.3f));
		super.add(minimapShadow);

        minimap = new GuiMinimap(
        	Display.getInstance().getWidth() - minimapHandler.getPos().x,
			Display.getInstance().getHeight() - minimapHandler.getPos().y,
			minimapHandler.getSize().x, minimapHandler.getSize().y);
		minimap.setOrigin(GuiComponent.GuiOrigin.C);
		minimap.setScreenParent(GuiComponent.GuiCorner.BR);
		minimap.setColor(new Color4f(1, 1, 1, 0.1f));
		super.add(minimap);

        GuiCanvas gc = GameCore.getInstance().getGame().getGameMode().getPlayerListScreen(this);
        super.addCanvas(gc);

        ProfilerScreen profilerScreen = new ProfilerScreen(this, display, core);
        super.addCanvas(profilerScreen);

        GuiCanvas gameMode = GameCore.getInstance().getGame().getGameMode().getHudScreen(this);
        super.addCanvas(gameMode);

        GameMenuScreen gameMenuGui = new GameMenuScreen(this, display, core);
        this.addCanvas(gameMenuGui);

        SpawnScreen deathScreen = new SpawnScreen(this, display, core);
        this.addCanvas(deathScreen);
    }

    public void update()
    {
        super.update();

        Weapon weapon = core.getGame().getPlayer().getWeaponComponent().getWeapon();
		this.showCrosshair = (weapon.getCrosshairTexture() != null && !(weapon instanceof FireWeapon)) ||
							(weapon.getCrosshairTexture() != null && weapon.isZoomed() && weapon instanceof FireWeapon);
		if (this.showCrosshair)
		{
			crosshair.setTexture(TextureLoader.loadTexture(getResource(core.getGame().getPlayer().getWeaponComponent().getWeapon().getCrosshairTexture()), GL_NEAREST, false));
			crosshair.setUseable(true);
		}
		else
			crosshair.setUseable(false);

        Display display = Ubercube.getInstance().getDisplay();
        gameFpsLabel.setText(display.getFps() + " Fps");

        ClientPlayer player = core.getGame().getPlayer();
        int life = player.getLife();

        playerHealthText.setText(life + " HP");

        float normalizedLife = (float) life / 100.0f;
        playerHealth.setW((int) (normalizedLife * 300));

        weaponStats.setUseable(false);
        if (weapon instanceof FireWeapon)
        {
            weaponStats.setUseable(true);
            weaponStats.setText(((FireWeapon) weapon).getBulletsLeft() + "/" + ((FireWeapon) weapon).getMaxBullets());
        }

        if (weapon instanceof WeaponGrenade)
        {
            weaponStats.setUseable(true);
            weaponStats.setText(((WeaponGrenade) weapon).getGrenadesLeft() + "/" + ((WeaponGrenade) weapon).getMaxGrenades());
        }

        if(life < health && damageEffect.getColor().getAlpha() < 0.75f)
        {
            damageEffect.getColor().setAlpha(damageEffect.getColor().getAlpha() + 0.25f);
        }
        if(damageEffect.getColor().getAlpha() > 0)
        {
            damageEffect.getColor().setAlpha(damageEffect.getColor().getAlpha() - 0.005f);
        }

        if (Display.getInstance().getInput().getKeyDown(Input.KEY_F1))
            AudioPlayer.muteAudio = !AudioPlayer.muteAudio;

        audioStatus.setUseable(AudioPlayer.muteAudio);
        audioStatus.setColor(Color4f.RED);

        health = player.getLife();

        int px = (int) player.getPosition().x;
        int py = (int) player.getPosition().y;
        int pz = (int) player.getPosition().z;
        playerPosition.setText(px + " - " + py + " - " + pz);

        if(headshotTimer > 0){
        	headshotLabel.setUseable(true);
        	headshotTimer--;
		}
		else
		{
			headshotLabel.setUseable(false);
		}
    }

    public ConsoleScreen getConsoleScreen()
    {
        return consoleScreen;
    }

    public void fireHeadshotHint()
	{
		headshotTimer = 60*3;
	}
}
