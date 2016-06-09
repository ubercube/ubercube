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

package fr.veridiangames.core.game.entities.components;

import fr.veridiangames.core.GameCore;
import fr.veridiangames.core.game.entities.Entity;
import fr.veridiangames.core.maths.Quat;
import fr.veridiangames.core.maths.Vec3;
import fr.veridiangames.core.physics.Rigidbody;
import fr.veridiangames.core.physics.colliders.Collider;

/**
 * Created by Marccspro on 28 f�vr. 2016.
 */
public class ECRigidbody extends EComponent
{
	private Rigidbody body;

	public ECRigidbody(Entity parent, Vec3 position, Quat rotation, Collider collider, boolean networkView)
	{
		super(RIGIDBODY);
		super.addDependencies(RENDER);

		body = new Rigidbody(parent, position, rotation, collider, networkView);
	}

	public void init(GameCore core)
	{
		core.getGame().getPhysics().addBody(body);
	}

	public void update(GameCore core)
	{

	}

	public Rigidbody getBody()
	{
		return body;
	}

	public void setBody(Rigidbody body)
	{
		this.body = body;
	}
}
