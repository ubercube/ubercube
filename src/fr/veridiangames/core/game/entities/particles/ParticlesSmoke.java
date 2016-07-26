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
 * Created by Aiko on 26/07/2016.
 */
public class ParticlesSmoke extends ParticleSystem {
    public ParticlesSmoke()
    {
        this(Indexer.getUniqueID(), new Vec3(), new Vec3());
    }

    public ParticlesSmoke(int id, Vec3 position, Vec3 vel) {
        super(id, ParticlesManager.SMOKE, position);
        super.useCollision(false);
        super.setGravity(new Vec3(0, 0.001f, 0));
        super.setParticleLifeTime(10);
        super.setParticleLifeTimeRandomness(180);
        super.setParticleColorRandomness(0.4f);
        super.setParticleColor(new Color4f(0.5f, 0.5f, 0.5f, 0.7f));
        super.setScaleInterval(0.02f, 0.2f);
        super.setImpulsion(true);
        super.setDuration(1000);
        super.setParticleCount(10);
        super.setParticleVelocity(new Vec3(-vel.x, -vel.y, -vel.z).mul(0.9f));
        super.setParticleVelocityRandomness(0.1f);
        super.setDrag(0.8f);
    }
}