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

package fr.veridiangames.core.game.entities.player;

import fr.veridiangames.core.game.entities.components.ECRigidbody;
import fr.veridiangames.core.maths.Quat;
import fr.veridiangames.core.maths.Vec3;
import fr.veridiangames.core.physics.colliders.AABoxCollider;

/**
 * Created by Marccspro on 23 f�vr. 2016.
 */
public class NetworkedPlayer extends Player
{
	public NetworkedPlayer(int id, String name, Vec3 position, Quat rotation, String address, int port)
	{
		super(id, name, position, rotation, address, port);
		super.add(new ECRigidbody(this, position, rotation, new AABoxCollider(new Vec3(0.3f, 2.5f * 0.5f, 0.3f)), true));
	}
}
