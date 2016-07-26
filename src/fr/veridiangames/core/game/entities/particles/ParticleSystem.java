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

import fr.veridiangames.core.GameCore;
import fr.veridiangames.core.game.entities.Entity;
import fr.veridiangames.core.game.entities.components.*;
import fr.veridiangames.core.maths.Quat;
import fr.veridiangames.core.maths.Transform;
import fr.veridiangames.core.maths.Vec3;
import fr.veridiangames.core.network.NetworkableClient;
import fr.veridiangames.core.network.packets.ParticlesRemovePacket;
import fr.veridiangames.core.network.packets.ParticlesSpawnPacket;
import fr.veridiangames.core.utils.Color4f;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ParticleSystem extends Entity
{
	private Random			    random;
    private NetworkableClient   net;

    private int                 particleType;

	private int				    particleCount;

	private Vec3 			    particleSpawnBox;

	private float			    minScale;
	private float			    maxScale;

	private int 			    particleLifeTime;
	private int				    particleLifeTimeRandomness;

	private Color4f 			particleColor;
	private float			    particleColorRandomness;

	private Vec3 			    particleVelocity;
	private float 			    particleVelocityRandomness;

	private float 				drag;

	private boolean 		    collision;
	private Vec3			    gravity;
	private boolean			    activate;
	private boolean				impulsion;
	private int					duration;

	private List<Particle> 	    particles;

	public ParticleSystem(int id, String name, Vec3 position)
	{
		super(id);
		super.add(new ECName(name));
		super.add(new ECRender(position, new Quat(), new Vec3(1)));
        super.addTag("ParticleSystem");

		this.random = new Random();
		this.particles = new ArrayList<>();

        this.minScale = 0;
        this.maxScale = 0.1f;

		this.particleCount = 1;

		this.particleSpawnBox = new Vec3();

		this.particleLifeTime = 50;
		this.particleColor = new Color4f(1.0f, 0.0f, 1.0f);
		this.particleVelocity = new Vec3();
		this.drag = 1f;

		this.particleLifeTimeRandomness = 10;
		this.particleColorRandomness = 0.1f;
		this.particleVelocityRandomness = 0.01f;

		this.collision = false;
		this.gravity = new Vec3();
		this.activate = true;
		this.impulsion = false;
		this.duration = 0;
	}

    public ParticleSystem(int id, ParticleSystem system)
    {
        super(id);
        super.add(new ECName(system.getName()));
        super.add(new ECRender(system.getTransform().getPosition(), new Quat(), new Vec3(1)));
        super.addTag("ParticleSystem");

        this.random = system.getRandom();
        this.particles = new ArrayList<>();

        this.minScale = system.getMinScale();
        this.maxScale = system.getMaxScale();

        this.particleCount = system.getParticleCount();

        this.particleSpawnBox = system.getParticleSpawnBox();

        this.particleLifeTime = system.getParticleLifeTime();
        this.particleColor = system.getParticleColor();
        this.particleVelocity = system.getParticleVelocity();

        this.particleLifeTimeRandomness = system.getParticleLifeTimeRandomness();
        this.particleColorRandomness = system.getParticleColorRandomness();
        this.particleVelocityRandomness = system.getParticleVelocityRandomness();

        this.collision = system.hasCollision();
        this.gravity = system.getGravity();
        this.activate = system.isActivate();
		this.impulsion = system.isImpulsion();
		this.duration = system.getDuration();
    }

	public void update(GameCore core)
	{
		super.update(core);

		for(int i = 0; i < particleCount; i++)
		{
			if(activate)
				particles.add(new Particle(this));
		}

		for(int i = 0; i < particles.size(); i++)
		{
			Particle p = particles.get(i);
			p.setLifeTime(p.getLifeTime() - 1);

			if(collision && core.getGame().getWorld().getBlock((int) p.getTransform().getPosition().x, (int) p.getTransform().getPosition().y, (int) p.getTransform().getPosition().z) != 0)
				p.setCollision(true);
			else
				p.setCollision(false);

			if(!collision || !p.isCollision())
			{
				p.setVelocity(p.getVelocity().mul(drag));
				p.getVelocity().add(p.getGravity());
				p.getTransform().getLocalPosition().add(p.getVelocity());
			}

			if(p.getLifeTime() < 0)
				particles.remove(p);
		}

		if(particles.size() == 0)
			destroy();

		if(impulsion)
			duration--;

		if(duration < 0)
			activate = false;
	}

    public void destroy() {
		if(net != null)
        	net.tcpSend(new ParticlesRemovePacket(this));
		else
			getCore().getGame().getEntityManager().remove(getID());
    }

    public int getParticleCount()
    {
        return particleCount;
    }

    public ParticleSystem setParticleCount(int particleCount)
    {
        this.particleCount = particleCount;
        return this;
    }

    public Vec3 getParticleSpawnBox()
	{
		return particleSpawnBox;
	}

	public ParticleSystem setParticleSpawnBox(Vec3 particleSpawnBox)
	{
		this.particleSpawnBox = particleSpawnBox;
		return this;
	}

	public float getMinScale()
	{
		return minScale;
	}

	public ParticleSystem setMinScale(float minScale)
	{
		this.minScale = minScale;
		return this;
	}

	public float getMaxScale()
	{
		return maxScale;
	}

	public ParticleSystem setMaxScale(float maxScale)
	{
		this.maxScale = maxScale;
		return this;
	}

    public ParticleSystem setScaleInterval(float min, float max)
    {
        this.minScale = min;
        this.maxScale = max;
        return this;
    }

	public Color4f getParticleColor()
	{
		return particleColor;
	}

	public ParticleSystem setParticleColor(Color4f particleColor) {
		this.particleColor = particleColor;
		return this;
	}

	public float getParticleColorRandomness()
	{
		return particleColorRandomness;
	}

	public ParticleSystem setParticleColorRandomness(float particleColorRandomness)
	{
		this.particleColorRandomness = particleColorRandomness;
		return this;
	}

	public int getParticleLifeTime() {
		return particleLifeTime;
	}

	public ParticleSystem setParticleLifeTime(int particleLifeTime)
	{
		this.particleLifeTime = particleLifeTime;
		return this;
	}

	public int getParticleLifeTimeRandomness()
	{
		return particleLifeTimeRandomness;
	}

	public ParticleSystem setParticleLifeTimeRandomness(int particleLifeTimeRandomness)
	{
		this.particleLifeTimeRandomness = particleLifeTimeRandomness;
		return this;
	}

	public Vec3 getParticleVelocity()
	{
		return particleVelocity;
	}

	public ParticleSystem setParticleVelocity(Vec3 particleVelocity)
	{
		this.particleVelocity = particleVelocity;
		return this;
	}

	public float getParticleVelocityRandomness()
	{
		return particleVelocityRandomness;
	}

	public ParticleSystem setParticleVelocityRandomness(float particleVelocityRandomness)
	{
		this.particleVelocityRandomness = particleVelocityRandomness;
		return this;
	}

	public Random getRandom() {
		return random;
	}

	public List<Particle> getParticles()
	{
		return particles;
	}

	public Transform getTransform()
	{
		return ((ECRender) this.get(EComponent.RENDER)).getTransform();
	}

	public ParticleSystem setPosition(Vec3 position)
	{
		getTransform().setLocalPosition(position);
		return this;
	}

	public boolean hasCollision()
	{
		return collision;
	}

	public ParticleSystem useCollision(boolean useCollision)
	{
		this.collision = useCollision;
		return this;
	}

	public Vec3 getGravity()
	{
		return gravity;
	}

	public ParticleSystem setGravity(Vec3 gravity)
	{
		this.gravity = gravity;
		return this;
	}

	public boolean isActivate()
	{
		return activate;
	}

	public ParticleSystem setActivate(boolean activate)
	{
		this.activate = activate;
		return this;
	}

    public ParticleSystem setNetwork(NetworkableClient net) {
        this.net = net;
        this.net.udpSend(new ParticlesSpawnPacket(this));
        return this;
    }

    public String getName(){
        return ((ECName) this.get(EComponent.NAME)).getName();
    }

	public boolean isImpulsion()
	{
		return impulsion;
	}

	public ParticleSystem setImpulsion(boolean impulsion)
	{
		this.impulsion = impulsion;
		return this;
	}

	public int getDuration()
	{
		return duration;
	}

	public ParticleSystem setDuration(int duration)
	{
		this.duration = duration;
		return this;
	}

	public void setDrag(float drag) {
		this.drag = drag;
	}

	public float getDrag() {
		return drag;
	}
}