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

package fr.veridiangames.client.rendering.renderers.cullings;

import fr.veridiangames.core.maths.Vec3;

public class DistanceCulling {
	private Vec3 cameraPosition;
	private float radius;

	public void update(Vec3 camPosition, float radius) {
		this.cameraPosition = camPosition;
		this.radius = radius * radius;
	}

	public boolean isInViewDistance(Vec3 pos, float rad) {
		float xx = this.cameraPosition.x - pos.x;
		float yy = this.cameraPosition.y - pos.y;
		float zz = this.cameraPosition.z - pos.z;

		return (xx * xx + yy * yy + zz * zz) <= this.radius + (rad * rad);
	}
}
