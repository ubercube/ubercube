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

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Tybau on 11/06/2016.
 */
public class ParticlesManager {

    public static final String BLOOD = "ParticleBlood";
    public static final String BULLET_HIT = "ParticleBulletHit";

    private static Map<String, Class<? extends ParticleSystem>> particles;

    static
    {
        particles = new HashMap<>();
        particles.put(BLOOD, ParticlesBlood.class);
        particles.put(BULLET_HIT, ParticlesBulletHit.class);
    }

    public static ParticleSystem getParticleSystem(String particleName)
    {
        try
        {
            return (ParticleSystem) particles.get(particleName).newInstance();
        }
        catch (InstantiationException e)
        {
            e.printStackTrace();
            return null;
        }
        catch (IllegalAccessException e)
        {
            e.printStackTrace();
            return null;
        }
    }
}
