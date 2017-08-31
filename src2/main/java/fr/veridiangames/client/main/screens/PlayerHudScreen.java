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
import fr.veridiangames.client.audio.AudioPlayer;
import fr.veridiangames.client.inputs.Input;
import fr.veridiangames.client.main.screens.gamemenu.GameMenuScreen;
import fr.veridiangames.client.main.screens.gamemode.TDMGameModeScreen;
import fr.veridiangames.client.rendering.Display;
import fr.veridiangames.client.rendering.guis.GuiCanvas;
import fr.veridiangames.client.rendering.guis.GuiComponent;
import fr.veridiangames.client.rendering.guis.components.GuiLabel;
import fr.veridiangames.client.rendering.guis.components.GuiPanel;
import fr.veridiangames.core.GameCore;
import fr.veridiangames.core.game.entities.player.ClientPlayer;
import fr.veridiangames.core.game.entities.weapons.Weapon;
import fr.veridiangames.core.game.entities.weapons.explosiveWeapons.WeaponGrenade;
import fr.veridiangames.core.game.entities.weapons.fireWeapons.FireWeapon;
import fr.veridiangames.core.utils.Color4f;

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
    private ConsoleScreen consoleScreen;
    private GuiPanel minimap;

    private int health;

    public PlayerHudScreen(GuiCanvas parent, Display display, GameCore core)
    {
        super(parent);
        this.core = core;

        this.damageEffect = new GuiPanel(0, 0, Display.getInstance().getWidth(), Display.getInstance().getHeight());
        this.damageEffect.setColor(Color4f.RED);
        this.damageEffect.setOrigin(GuiComponent.GuiOrigin.A);
        this.damageEffect.setScreenParent(GuiComponent.GuiCorner.SCALED);
        this.damageEffect.getColor().setAlpha(0);
        super.add(this.damageEffect);

        this.health = GameCore.getInstance().getGame().getPlayer().getLife();

        GuiPanel playerHealthShadow = new GuiPanel(35 + 2, display.getHeight() - 60 + 3, 300, 30);
        playerHealthShadow.setColor(new Color4f(0f, 0f, 0f, 0.3f));
        playerHealthShadow.setOrigin(GuiComponent.GuiOrigin.A);
        playerHealthShadow.setScreenParent(GuiComponent.GuiCorner.BL);
        super.add(playerHealthShadow);

        this.playerHealth = new GuiPanel(35, display.getHeight() - 60, 300, 30);
        this.playerHealth.setColor(new Color4f(0.7f, 0.1f, 0));
        this.playerHealth.setOrigin(GuiComponent.GuiOrigin.A);
        this.playerHealth.setScreenParent(GuiComponent.GuiCorner.BL);
        super.add(this.playerHealth);

        this.playerHealthText = new GuiLabel("100 HP", 35 + 2 + 150, display.getHeight() - 60 + 3 + 12, 25f);
        this.playerHealthText.setOrigin(GuiComponent.GuiOrigin.CENTER);
        this.playerHealthText.setScreenParent(GuiComponent.GuiCorner.BL);
        this.playerHealthText.setColor(Color4f.WHITE);
        this.playerHealthText.setDropShadow(3);
        this.playerHealthText.setDropShadowColor(new Color4f(0, 0, 0, 0.5f));
        super.add(this.playerHealthText);

        this.weaponStats = new GuiLabel("12/30", 60, display.getHeight() - 65 - 5 - 45 + 3, 45f);
        this.weaponStats.setOrigin(GuiComponent.GuiOrigin.A);
        this.weaponStats.setScreenParent(GuiComponent.GuiCorner.BL);
        this.weaponStats.setColor(Color4f.WHITE);
        this.weaponStats.setDropShadow(3);
        this.weaponStats.setDropShadowColor(new Color4f(0, 0, 0, 0.5f));
        super.add(this.weaponStats);

        GuiPanel crosshairBack = new GuiPanel(display.getWidth() / 2, display.getHeight() / 2, 4, 4);
        crosshairBack.setOrigin(GuiComponent.GuiOrigin.CENTER);
        crosshairBack.setScreenParent(GuiComponent.GuiCorner.CENTER);
        crosshairBack.setColor(new Color4f(0, 0, 0, 0.5f));
        super.add(crosshairBack);

        GuiPanel crosshairFront = new GuiPanel(display.getWidth() / 2, display.getHeight() / 2, 2, 2);
        crosshairFront.setOrigin(GuiComponent.GuiOrigin.CENTER);
        crosshairFront.setScreenParent(GuiComponent.GuiCorner.CENTER);
        super.add(crosshairFront);

        GuiLabel gameVersionLabel = new GuiLabel(GameCore.GAME_NAME + " " + GameCore.GAME_VERSION_NAME, 10, 10, 20f);
        gameVersionLabel.setOrigin(GuiComponent.GuiOrigin.A);
        gameVersionLabel.setScreenParent(GuiComponent.GuiCorner.TL);
        gameVersionLabel.setColor(Color4f.WHITE);
        gameVersionLabel.setDropShadow(2);
        gameVersionLabel.setDropShadowColor(new Color4f(0, 0, 0, 0.5f));
        super.add(gameVersionLabel);

        this.gameFpsLabel = new GuiLabel("60 Fps", 10, 30, 20f);
        this.gameFpsLabel.setOrigin(GuiComponent.GuiOrigin.A);
        this.gameFpsLabel.setScreenParent(GuiComponent.GuiCorner.TL);
        this.gameFpsLabel.setColor(Color4f.WHITE);
        this.gameFpsLabel.setDropShadow(2);
        this.gameFpsLabel.setDropShadowColor(new Color4f(0, 0, 0, 0.5f));
        super.add(this.gameFpsLabel);

        this.audioStatus = new GuiLabel("Audio muted !", 10, 50, 20f);
        this.audioStatus.setOrigin(GuiComponent.GuiOrigin.A);
        this.audioStatus.setScreenParent(GuiComponent.GuiCorner.TL);
        this.audioStatus.setColor(Color4f.RED);
        this.audioStatus.setDropShadow(2);
        this.audioStatus.setDropShadowColor(new Color4f(0, 0, 0, 0.5f));
        this.audioStatus.setUseable(AudioPlayer.muteAudio);
        super.add(this.audioStatus);

        this.playerPosition = new GuiLabel("0 - 0 - 0", display.getWidth() / 2, 10, 20f);
        this.playerPosition.setOrigin(GuiComponent.GuiOrigin.TC);
        this.playerPosition.setScreenParent(GuiComponent.GuiCorner.TC);
        this.playerPosition.setColor(Color4f.WHITE);
        this.playerPosition.setDropShadow(2);
        this.playerPosition.setDropShadowColor(new Color4f(0, 0, 0, 0.5f));
        super.add(this.playerPosition);

		GuiPanel minimapShadow = new GuiPanel(display.getWidth() - 35 + 2, display.getHeight() - 30 + 3, 200, 150);
		minimapShadow.setOrigin(GuiComponent.GuiOrigin.C);
		minimapShadow.setScreenParent(GuiComponent.GuiCorner.BR);
		minimapShadow.setColor(new Color4f(0.0f, 0.0f, 0.0f, 0.3f));
		super.add(minimapShadow);

        this.minimap = new GuiPanel(display.getWidth() - 35, display.getHeight() - 30, 200, 150);
        this.minimap.setOrigin(GuiComponent.GuiOrigin.C);
        this.minimap.setScreenParent(GuiComponent.GuiCorner.BR);
        this.minimap.setColor(new Color4f(0, 0.5f, 0, 1f));
        super.add(this.minimap);

        GuiLabel minimapSoon = new GuiLabel("Minimap soon !", display.getWidth() - 35 - 100, display.getHeight() - 30 - 75);
        minimapSoon.setDropShadow(2);
        minimapSoon.setOrigin(GuiComponent.GuiOrigin.CENTER);
        minimapSoon.setScreenParent(GuiComponent.GuiCorner.BR);
		minimapSoon.setColor(new Color4f(1,1,1,1f));
        super.add(minimapSoon);

        this.consoleScreen = new ConsoleScreen(this, display, core, 10, Display.getInstance().getHeight() - 130, 600, 450);
        super.addCanvas(this.consoleScreen);

        PlayerListScreen playerListScreen = new PlayerListScreen(this, display, core, Display.getInstance().getWidth() / 2, Display.getInstance().getHeight() / 2);
        super.addCanvas(playerListScreen);

        ProfilerScreen profilerScreen = new ProfilerScreen(this, display, core);
        super.addCanvas(profilerScreen);

        TDMGameModeScreen gameMode = new TDMGameModeScreen(this);
        super.addCanvas(gameMode);

        GameMenuScreen gameMenuGui = new GameMenuScreen(this, display, core);
        this.addCanvas(gameMenuGui);

        DeathScreen deathScreen = new DeathScreen(this, display, core);
        this.addCanvas(deathScreen);
    }

    @Override
	public void update()
    {
        super.update();

        Display display = Ubercube.getInstance().getDisplay();
        this.gameFpsLabel.setText(display.getFps() + " Fps");

        ClientPlayer player = this.core.getGame().getPlayer();
        int life = player.getLife();

        this.playerHealthText.setText(life + " HP");

        float normalizedLife = life / 100.0f;
        this.playerHealth.setW((int) (normalizedLife * 300));

        Weapon weapon = player.getWeaponManager().getWeapon();
        this.weaponStats.setUseable(false);
        if (weapon instanceof FireWeapon)
        {
            this.weaponStats.setUseable(true);
            this.weaponStats.setText(((FireWeapon) weapon).getBulletsLeft() + "/" + ((FireWeapon) weapon).getMaxBullets());
        }

        if (weapon instanceof WeaponGrenade)
        {
            this.weaponStats.setUseable(true);
            this.weaponStats.setText(((WeaponGrenade) weapon).getGrenadesLeft() + "/" + ((WeaponGrenade) weapon).getMaxGrenades());
        }

        if(life < this.health && this.damageEffect.getColor().getAlpha() < 0.75f)
			this.damageEffect.getColor().setAlpha(this.damageEffect.getColor().getAlpha() + 0.25f);
        if(this.damageEffect.getColor().getAlpha() > 0)
			this.damageEffect.getColor().setAlpha(this.damageEffect.getColor().getAlpha() - 0.005f);

        if (Display.getInstance().getInput().getKeyDown(Input.KEY_F1))
            AudioPlayer.muteAudio = !AudioPlayer.muteAudio;

        this.audioStatus.setUseable(AudioPlayer.muteAudio);
        this.audioStatus.setColor(Color4f.RED);

        this.health = player.getLife();

        int px = (int) player.getPosition().x;
        int py = (int) player.getPosition().y;
        int pz = (int) player.getPosition().z;
        this.playerPosition.setText(px + " - " + py + " - " + pz);
    }

    public ConsoleScreen getConsoleScreen()
    {
        return this.consoleScreen;
    }
}
