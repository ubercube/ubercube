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

import fr.veridiangames.core.maths.Quat;
import fr.veridiangames.core.maths.Transform;
import fr.veridiangames.core.maths.Vec3;
import fr.veridiangames.core.utils.Color4f;

/**
 * Created by Marc on 07/06/2016.
 */
public class Particle
{
    private Transform   transform;
    private Color4f     color;
    private int         lifeTime;
    private Vec3        velocity;
    private boolean     collision;
    private Vec3        gravity;

    public Particle(ParticleSystem system)
    {
        transform = new Transform();
        transform.setLocalPosition(system.getTransform().getPosition().copy());
        transform.setLocalPosition(system.getTransform().getPosition().copy().add(  system.getRandom().nextFloat() * system.getParticleSpawnBox().x - system.getParticleSpawnBox().x / 2,
                                                                                    system.getRandom().nextFloat() * system.getParticleSpawnBox().y - system.getParticleSpawnBox().y / 2,
                                                                                    system.getRandom().nextFloat() * system.getParticleSpawnBox().z - system.getParticleSpawnBox().z / 2));

        transform.setLocalScale(system.getTransform().getLocalScale().copy().mul(system.getRandom().nextFloat() * (system.getMaxScale() - system.getMinScale()) + system.getMinScale()));
        transform.setLocalRotation(Quat.euler(system.getRandom().nextFloat(), system.getRandom().nextFloat(), system.getRandom().nextFloat()));

        float colorRandomness = system.getRandom().nextFloat() * system.getParticleColorRandomness() - system.getParticleColorRandomness() / 2;
        color = system.getParticleColors()[system.getRandom().nextInt(system.getParticleColors().length)].copy().add(new Color4f(colorRandomness, colorRandomness, colorRandomness, 0));

        lifeTime = system.getParticleLifeTime() + system.getRandom().nextInt(system.getParticleLifeTimeRandomness());

        velocity = system.getParticleVelocity().copy();
        velocity.add(   system.getRandom().nextFloat() * system.getParticleVelocityRandomness() - system.getParticleVelocityRandomness() / 2,
                        system.getRandom().nextFloat() * system.getParticleVelocityRandomness() - system.getParticleVelocityRandomness() / 2,
                        system.getRandom().nextFloat() * system.getParticleVelocityRandomness() - system.getParticleVelocityRandomness() / 2)
                        .normalize().mul(system.getParticleVelocity().magnitude() + system.getParticleVelocityRandomness() * system.getRandom().nextFloat());

        collision = false;
        gravity = system.getGravity();
    }

    public Transform getTransform()
    {
        return transform;
    }

    public Color4f getColor()
    {
        return color;
    }

    public Vec3 getVelocity()
    {
        return velocity;
    }

    public int getLifeTime()
    {
        return lifeTime;
    }

    public void setLifeTime(int lifeTime)
    {
        this.lifeTime = lifeTime;
    }

    public boolean isCollision()
    {
        return collision;
    }

    public void setCollision(boolean collision)
    {
        this.collision = collision;
    }

    public Vec3 getGravity() {
        return gravity;
    }

    public void setGravity(Vec3 gravity) {
        this.gravity = gravity;
    }
}
