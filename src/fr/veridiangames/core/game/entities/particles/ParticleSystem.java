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
 *     along with Ubercube.  If not, see <http://www.gnu.org/licenses/>.
 */

package fr.veridiangames.core.game.entities.particles;

import fr.veridiangames.core.GameCore;
import fr.veridiangames.core.game.entities.Entity;
import fr.veridiangames.core.game.entities.components.*;
import fr.veridiangames.core.maths.Quat;
import fr.veridiangames.core.maths.Transform;
import fr.veridiangames.core.maths.Vec3;
import fr.veridiangames.core.utils.Color4f;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ParticleSystem extends Entity
{
	private Random			random;

	private int				particleCount;

	private Vec3 			particleSpawnBox;

	private int 			particleLifeTime;
	private int				particleLifeTimeRandomness;

	private float 			particleSize;
	private float 			particleSizeRandomness;

	private Color4f[] 		particleColors;
	private float			particleColorRandomness;

	private Vec3 			particleVelocity;
	private float 			particleVelocityRandomness;

	private boolean 		collision;
	private Vec3			gravity;
	private boolean			activate;

	private List<Particle> 	particles;

	public ParticleSystem(int id, String name, Vec3 position, float scale, Color4f... color)
	{
		super(id);
		super.add(new ECName(name));
		super.add(new ECRender(position, new Quat(), new Vec3(scale)));

		this.random = new Random();
		this.particles = new ArrayList<>();

		this.particleCount = 1;

		this.particleSpawnBox = new Vec3();

		this.particleLifeTime = 50;
		this.particleColors = color;
		this.particleVelocity = new Vec3();

		this.particleLifeTimeRandomness = 10;
		this.particleColorRandomness = 0.1f;
		this.particleVelocityRandomness = 0.01f;

		this.collision = false;
		this.gravity = new Vec3();
		this.activate = true;
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

			if(!collision || !p.isCollision())
			{
				p.getVelocity().add(p.getGravity());
				p.getTransform().getLocalPosition().add(p.getVelocity());
			}

			if(p.getLifeTime() < 0)
				particles.remove(p);
		}
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

	public Color4f[] getParticleColors() {
		return particleColors;
	}

	public ParticleSystem setParticleColors(Color4f... particleColors) {
		this.particleColors = particleColors;
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
}