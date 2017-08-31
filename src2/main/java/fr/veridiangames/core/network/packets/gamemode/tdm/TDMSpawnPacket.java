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

package fr.veridiangames.core.network.packets.gamemode.tdm;

import java.net.InetAddress;

import fr.veridiangames.core.game.modes.TDMGameMode;
import fr.veridiangames.core.maths.Vec3;
import fr.veridiangames.core.network.NetworkableClient;
import fr.veridiangames.core.network.NetworkableServer;
import fr.veridiangames.core.network.packets.Packet;
import fr.veridiangames.core.utils.DataBuffer;

/**
 * Created by Jimi Vacarians on 25/07/2016.
 */
public class TDMSpawnPacket extends Packet {

    private Vec3 redSpawn = new Vec3();
    private Vec3 blueSpawn = new Vec3();

    public TDMSpawnPacket() {
        super(Packet.GAMEMODE_TDM_SPAWN);
    }

    public TDMSpawnPacket(Vec3 red, Vec3 blue) {
        super(Packet.GAMEMODE_TDM_SPAWN);
        this.data.put(red.x);
        this.data.put(red.y);
        this.data.put(red.z);
        this.data.put(blue.x);
        this.data.put(blue.y);
        this.data.put(blue.z);
		this.data.flip();
    }

    @Override
    public void read(DataBuffer buffer) {
        this.redSpawn.x = this.data.getFloat();
        this.redSpawn.y = this.data.getFloat();
        this.redSpawn.z = this.data.getFloat();
        this.blueSpawn.x = this.data.getFloat();
        this.blueSpawn.y = this.data.getFloat();
        this.blueSpawn.z = this.data.getFloat();
    }

    @Override
    public void process(NetworkableServer server, InetAddress address, int port) {

    }

    @Override
    public void process(NetworkableClient client, InetAddress address, int port) {
        TDMGameMode mode = (TDMGameMode) client.getCore().getGame().getGameMode();
        mode.getRedTeam().setSpawn(this.redSpawn);
        mode.getBlueTeam().setSpawn(this.blueSpawn);
    }
}
