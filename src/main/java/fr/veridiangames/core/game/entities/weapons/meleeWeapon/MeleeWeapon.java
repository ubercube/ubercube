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

package fr.veridiangames.core.game.entities.weapons.meleeWeapon;

import fr.veridiangames.core.GameCore;
import fr.veridiangames.core.game.entities.components.ECKeyMovement;
import fr.veridiangames.core.game.entities.player.ClientPlayer;
import fr.veridiangames.core.game.entities.weapons.Weapon;
import fr.veridiangames.core.maths.Vec3;

/**
 * Created by Marc on 11/07/2016.
 */
public class MeleeWeapon extends Weapon
{
    boolean attack = false;
	private Vec3 attackRotation;
	private Vec3 runRotation;
	private int attackTime;

	public MeleeWeapon(int id)
    {
        super(id);
        this.attackRotation = new Vec3(90, -40, -40);
        this.runRotation = new Vec3(-5, 0, -5);
        this.attackTime = 0;
    }

    public void update(GameCore core)
	{
		super.update(core);
		ClientPlayer player = core.getGame().getPlayer();
		if (holder.getID() == player.getID() && !zoomed)
		{
			ECKeyMovement movement = player.getKeyComponent();
			float dx = player.getMouseComponent().getDx();
			float dy = player.getMouseComponent().getDy();
			Vec3 movementVelocity = new Vec3(movement.getVelocity(1)).mul(1, 0, 1);
			if (movement.isRun())
			{
				super.rotationFactor.add(runRotation.copy().mul(0.01f));
				super.updateBobbing(movementVelocity.magnitude(), 0.4f, 0.3f);
			}
			else
			{
				super.updateBobbing(movementVelocity.magnitude(), 0.15f, 0.2f);
				super.updateWeaponVelocity(movement.getVelocity(1), dx, dy, 0.0008f);
			}
		}
		if (attack && attackTime < 5)
		{
			super.rotationFactor.add(attackRotation.copy().mul(0.01f));
			attackTime++;
		}
		else
		{
			attack = false;
			attackTime = 0;
		}
	}

	public void onAction() {

	}

	public void onActionUp() {

	}

	public void onActionDown()
	{
		System.out.println("LOL");
		attack = true;
	}
}
