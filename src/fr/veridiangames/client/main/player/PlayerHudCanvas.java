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

package fr.veridiangames.client.main.player;

import fr.veridiangames.client.rendering.Display;
import fr.veridiangames.client.rendering.guis.GuiCanvas;
import fr.veridiangames.client.rendering.guis.GuiComponent;
import fr.veridiangames.client.rendering.guis.components.GuiLabel;
import fr.veridiangames.client.rendering.guis.components.GuiPanel;
import fr.veridiangames.core.GameCore;
import fr.veridiangames.core.game.entities.player.ClientPlayer;
import fr.veridiangames.core.game.entities.player.Player;
import fr.veridiangames.core.game.entities.weapons.Weapon;
import fr.veridiangames.core.game.entities.weapons.fire_weapons.FireWeapon;
import fr.veridiangames.core.utils.Color4f;

/**
 * Created by Marc on 23/06/2016.
 */
public class PlayerHudCanvas extends GuiCanvas
{
    private GameCore core;

    private GuiPanel playerHealth;
    private GuiLabel playerHealthText;
    private GuiLabel weaponStats;

    public PlayerHudCanvas(Display display, GameCore core)
    {
        super();
        this.core = core;

        GuiPanel playerHealthShadow = new GuiPanel(35 + 2, display.getHeight() - 60 + 3, 300, 30);
        playerHealthShadow.setColor(new Color4f(0f, 0f, 0f, 0.5f));
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

        weaponStats = new GuiLabel("12/30", 60, display.getHeight() - 65 - 5 - 45, 45f);
        weaponStats.setOrigin(GuiComponent.GuiOrigin.A);
        weaponStats.setScreenParent(GuiComponent.GuiCorner.BL);
        weaponStats.setColor(Color4f.WHITE);
        weaponStats.setDropShadow(3);
        weaponStats.setDropShadowColor(new Color4f(0, 0, 0, 0.5f));
        super.add(weaponStats);

        GuiPanel crosshairBack = new GuiPanel(display.getWidth() / 2, display.getHeight() / 2, 4, 4);
        crosshairBack.setOrigin(GuiComponent.GuiOrigin.CENTER);
        crosshairBack.setScreenParent(GuiComponent.GuiCorner.CENTER);
        crosshairBack.setColor(new Color4f(0, 0, 0, 0.5f));
        super.add(crosshairBack);

        GuiPanel crosshairFront = new GuiPanel(display.getWidth() / 2, display.getHeight() / 2, 2, 2);
        crosshairFront.setOrigin(GuiComponent.GuiOrigin.CENTER);
        crosshairFront.setScreenParent(GuiComponent.GuiCorner.CENTER);
        super.add(crosshairFront);

    }

    public void update()
    {
        super.update();

        ClientPlayer player = core.getGame().getPlayer();
        int life = player.getLife();

        playerHealthText.setText(life + " HP");

        float normalizedLife = (float) life / 100.0f;
        playerHealth.setW((int) (normalizedLife * 300));

        Weapon weapon = player.getWeaponManager().getWeapon();
        weaponStats.setUseable(false);
        if (weapon instanceof FireWeapon)
        {
            weaponStats.setUseable(true);
            weaponStats.setText(((FireWeapon) weapon).getBulletsLeft() + "/" + ((FireWeapon) weapon).getMaxBullets());
        }
    }
}
