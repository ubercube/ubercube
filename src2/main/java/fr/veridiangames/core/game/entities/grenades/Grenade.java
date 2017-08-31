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

package fr.veridiangames.core.game.entities.grenades;

import fr.veridiangames.core.GameCore;
import fr.veridiangames.core.audio.Sound;
import fr.veridiangames.core.game.entities.Entity;
import fr.veridiangames.core.game.entities.Model;
import fr.veridiangames.core.game.entities.audio.AudioSource;
import fr.veridiangames.core.game.entities.components.ECModel;
import fr.veridiangames.core.game.entities.components.ECName;
import fr.veridiangames.core.game.entities.components.ECRender;
import fr.veridiangames.core.game.entities.components.ECRigidbody;
import fr.veridiangames.core.game.entities.components.EComponent;
import fr.veridiangames.core.game.entities.particles.ParticlesExplosion;
import fr.veridiangames.core.maths.Quat;
import fr.veridiangames.core.maths.Transform;
import fr.veridiangames.core.maths.Vec3;
import fr.veridiangames.core.network.NetworkableClient;
import fr.veridiangames.core.network.Protocol;
import fr.veridiangames.core.network.packets.DamageForcePacket;
import fr.veridiangames.core.physics.Rigidbody;
import fr.veridiangames.core.physics.colliders.AABoxCollider;
import fr.veridiangames.core.utils.Indexer;

/**
 * Created by Marc on 11/07/2016.
 */
public class Grenade extends Entity
{
    private int holderID;
    private float				force;
    private NetworkableClient   net;
    private Vec3                startPosition = new Vec3();
    private Vec3                rotationAxis;
    int timer;

    public Grenade(int id, int holderID, Vec3 spawnPoint, Quat direction, float force)
    {
        super(id);
        super.add(new ECName("Grenade"));
        super.add(new ECRender(spawnPoint, direction, new Vec3(1f)));
        super.add(new ECModel(Model.GRENADE));
        super.add(new ECRigidbody(this, spawnPoint, direction, new AABoxCollider(new Vec3(0.2f, 0.2f, 0.2f)), false));
        super.addTag("Grenade");

        this.getBody().useGravity(true);
        this.getBody().setIgnoreOthers(false);
        this.getBody().setAirDragFactor(0.906f);
        this.getBody().setFrictionFactor(0.88f);
        this.getBody().setBounceFactor(1f);
        this.getBody().applyForce(direction.getForward().copy(), force);

        this.holderID = holderID;
        this.startPosition.set(this.getPosition());

        this.rotationAxis = Vec3.random();

        this.force = force;
    }

    @Override
	public void update(GameCore core)
    {
        super.update(core);

        this.timer++;

        this.getTransform().rotate(this.rotationAxis, 5f / (this.timer * 0.2f));

        if (this.timer > 60 * 2)
			this.explose();
    }

    private void explose()
    {
        GameCore.getInstance().getGame().spawn(new AudioSource(Sound.EXPLODE, this.getPosition().copy()));
        GameCore.getInstance().getGame().spawn(new ParticlesExplosion(Indexer.getUniqueID(), this.getPosition().copy()));
        if(this.holderID == GameCore.getInstance().getGame().getPlayer().getID())
            this.net.send(new DamageForcePacket(this.getPosition().copy(), 4), Protocol.TCP);
        this.destroy();
        this.getCore().getGame().getPhysics().removeBody(this.getBody());
    }

    public Rigidbody getBody() {return ((ECRigidbody) this.get(EComponent.RIGIDBODY)).getBody(); }

    public ECRender getRender() { return (ECRender) this.get(EComponent.RENDER); }

    public Vec3 getPosition()
    {
        return ((ECRigidbody) this.get(EComponent.RIGIDBODY)).getBody().getPosition();
    }

    public Quat getRotation()
    {
        return ((ECRender) this.get(EComponent.RENDER)).getTransform().getRotation();
    }

    public Transform getTransform() { return ((ECRender) this.get(EComponent.RENDER)).getTransform(); }

    public Entity setNetwork(NetworkableClient net)
    {
        this.net = net;
        return this;
    }

    public String getName()
    {
        return ((ECName) this.get(EComponent.NAME)).getName();
    }

    public float getForce()
    {
        return this.force;
    }

    public int getHolderID ()
    {
        return this.holderID;
    }
}
