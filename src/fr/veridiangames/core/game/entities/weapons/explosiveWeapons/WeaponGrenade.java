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

package fr.veridiangames.core.game.entities.weapons.explosiveWeapons;

import fr.veridiangames.core.GameCore;
import fr.veridiangames.core.game.entities.grenades.Grenade;
import fr.veridiangames.core.maths.Transform;
import fr.veridiangames.core.maths.Vec3;
import fr.veridiangames.core.utils.Indexer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Marc on 11/07/2016.
 */
public class WeaponGrenade extends ExplosiveWeapon
{
    private int grenadetCount;
    private List<Grenade> grenades;

    public WeaponGrenade(int num)
    {
        super(GRENADE);
        this.transform.setLocalScale(new Vec3(1, 1, -1));
        this.setIdlePosition(new Transform(new Vec3(0.3f, -0.05f, 0)));
        this.setHidePosition(new Transform(new Vec3(0.3f, -0.05f - 1f, 0)));
        this.setZoomPosition(new Transform(new Vec3(0, 0, 0)));
        this.setPosition(0);
        this.grenadetCount = num;
        this.grenades = new ArrayList<>();
    }

    public void onAction()
    {

    }

    public void onActionUp()
    {
        GameCore.getInstance().getGame().spawn(new Grenade(Indexer.getUniqueID(), this.holder.getID(), transform.getPosition().copy().add(this.holder.getTransform().getForward().copy()), transform.getRotation(), 1f));
    }

    public void onActionDown()
    {

    }
}