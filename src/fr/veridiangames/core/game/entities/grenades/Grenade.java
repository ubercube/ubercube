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
import fr.veridiangames.core.game.entities.Entity;
import fr.veridiangames.core.game.entities.Model;
import fr.veridiangames.core.game.entities.components.*;
import fr.veridiangames.core.game.entities.particles.ParticlesExplosion;
import fr.veridiangames.core.maths.Quat;
import fr.veridiangames.core.maths.Vec3;
import fr.veridiangames.core.network.NetworkableClient;
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
    private boolean             throwed;
    private NetworkableClient   net;
    private Vec3                startPosition = new Vec3();
    int timer;

    public Grenade(int id, int holderID, Vec3 spawnPoint, Quat direction, float force)
    {
        super(id);
        super.add(new ECName("Grenade"));
        super.add(new ECRender(spawnPoint, direction, new Vec3(1f)));
        super.add(new ECModel(Model.GRENADE));
        super.add(new ECRigidbody(this, spawnPoint, direction, new AABoxCollider(new Vec3(0.2f, 0.8f, 0.2f)), false));
        super.addTag("Grenade");
        this.getRender().getTransform();

        this.getBody().useGravity(true);
        this.getBody().setIgnoreOthers(true);
        this.getBody().setAirDragFactor(1f);
        this.getBody().setFrictionFactor(0.8f);
        this.getBody().setBounceFactor(0.2f);
        this.getBody().applyForce(direction.getForward().copy(), force);

        this.holderID = holderID;
        startPosition.set(this.getPosition());

        this.force = force;
    }

    public void update(GameCore core)
    {
        super.update(core);

        timer++;

        if (timer > 60 * 2)
        {
            explode();
        }
    }

    private void explode()
    {
        getCore().getGame().spawn(new ParticlesExplosion(Indexer.getUniqueID(), getPosition().copy()));

        if (holderID == getCore().getGame().getPlayer().getID())
            net.tcpSend(new DamageForcePacket(getPosition().copy(), 4));

        getCore().getGame().getWorld().explosion(getPosition(), 2f);

        this.destroy();
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
        return force;
    }
}
