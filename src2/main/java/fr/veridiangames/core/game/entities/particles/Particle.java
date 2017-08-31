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
    private boolean     rendered;

    public Particle(ParticleSystem system)
    {
        this.transform = new Transform();
        this.transform.setLocalPosition(system.getTransform().getPosition().copy());
        this.transform.setLocalPosition(system.getTransform().getPosition().copy().add(  system.getRandom().nextFloat() * system.getParticleSpawnBox().x - system.getParticleSpawnBox().x / 2,
                                                                                    system.getRandom().nextFloat() * system.getParticleSpawnBox().y - system.getParticleSpawnBox().y / 2,
                                                                                    system.getRandom().nextFloat() * system.getParticleSpawnBox().z - system.getParticleSpawnBox().z / 2));

        this.transform.setLocalScale(system.getTransform().getLocalScale().copy().mul(system.getRandom().nextFloat() * (system.getMaxScale() - system.getMinScale()) + system.getMinScale()));
        this.transform.setLocalRotation(Quat.euler(system.getRandom().nextFloat(), system.getRandom().nextFloat(), system.getRandom().nextFloat()));

        float colorRandomness = system.getRandom().nextFloat() * system.getParticleColorRandomness() - system.getParticleColorRandomness() / 2;
        this.color = system.getParticleColor().copy().add(new Color4f(colorRandomness, colorRandomness, colorRandomness, 0));

        this.lifeTime = system.getParticleLifeTime() + system.getRandom().nextInt(system.getParticleLifeTimeRandomness());

        this.velocity = system.getParticleVelocity().copy();
        this.velocity.add(   system.getRandom().nextFloat() * system.getParticleVelocityRandomness() - system.getParticleVelocityRandomness() / 2,
                        system.getRandom().nextFloat() * system.getParticleVelocityRandomness() - system.getParticleVelocityRandomness() / 2,
                        system.getRandom().nextFloat() * system.getParticleVelocityRandomness() - system.getParticleVelocityRandomness() / 2)
                        .normalize().mul(system.getParticleVelocity().magnitude() + system.getParticleVelocityRandomness() * system.getRandom().nextFloat());

        this.collision = false;
        this.gravity = system.getGravity();
    }

    public Transform getTransform()
    {
        return this.transform;
    }

    public Color4f getColor()
    {
        return this.color;
    }

    public Vec3 getVelocity()
    {
        return this.velocity;
    }

    public int getLifeTime()
    {
        return this.lifeTime;
    }

    public void setLifeTime(int lifeTime)
    {
        this.lifeTime = lifeTime;
    }

    public boolean isCollision()
    {
        return this.collision;
    }

    public void setCollision(boolean collision)
    {
        this.collision = collision;
    }

    public Vec3 getGravity() {
        return this.gravity;
    }

    public void setGravity(Vec3 gravity) {
        this.gravity = gravity;
    }

    public boolean isRendered()
    {
        return this.rendered;
    }

    public void setRendered(boolean rendered)
    {
        this.rendered = rendered;
    }
}
