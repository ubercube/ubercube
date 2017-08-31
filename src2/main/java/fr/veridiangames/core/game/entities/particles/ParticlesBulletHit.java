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

package fr.veridiangames.core.game.entities.particles;

import fr.veridiangames.core.maths.Vec3;
import fr.veridiangames.core.utils.Color4f;
import fr.veridiangames.core.utils.Indexer;

/**
 * Created by Tybau on 11/06/2016.
 */
public class ParticlesBulletHit extends ParticleSystem {

    public ParticlesBulletHit()
    {
        this(Indexer.getUniqueID(), new Vec3(), new Color4f(0.3f, 0.15f, 0f));
    }

    public ParticlesBulletHit(int id, Vec3 position, Color4f color)
    {
        super(id, ParticlesManager.BULLET_HIT, position);
        super.useCollision(true);
        super.setGravity(new Vec3(0, -0.002f, 0));
        super.setScaleInterval(0.01f, 0.1f);
        super.setParticleColor(color);
        super.setImpulsion(true);
        super.setDuration(0);
        super.setParticleCount(30);
        super.setParticleLifeTime(120);
        super.setParticleLifeTimeRandomness(120);
        super.setParticleVelocityRandomness(0.1f);
    }
}
