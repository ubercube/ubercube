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
public class ParticlesBlood extends ParticleSystem{

    public ParticlesBlood()
    {
        this(Indexer.getUniqueID(), new Vec3());
    }

    public ParticlesBlood(int id, Vec3 position)
    {
        super(id, ParticlesManager.BLOOD, position);
        super.useCollision(true);
        super.setGravity(new Vec3(0, -0.001f, 0));
        super.setParticleLifeTime(200);
        super.setScaleInterval(0.01f, 0.1f);
        super.setParticleColors(new Color4f(0.8f, 0f, 0f), new Color4f(0.65f, 0f, 0f));
        super.setImpulsion(true);
        super.setDuration(30);
    }
}
