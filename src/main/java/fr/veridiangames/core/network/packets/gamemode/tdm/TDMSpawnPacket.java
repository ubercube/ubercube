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

import fr.veridiangames.core.game.modes.TDMGameMode;
import fr.veridiangames.core.maths.Vec3;
import fr.veridiangames.core.network.NetworkableClient;
import fr.veridiangames.core.network.NetworkableServer;
import fr.veridiangames.core.network.packets.Packet;
import fr.veridiangames.core.utils.DataBuffer;
import fr.veridiangames.core.utils.Log;

import java.net.InetAddress;

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
        data.put(red.x);
        data.put(red.y);
        data.put(red.z);
        data.put(blue.x);
        data.put(blue.y);
        data.put(blue.z);
		data.flip();
    }

    @Override
    public void read(DataBuffer buffer) {
        redSpawn.x = buffer.getFloat();
        redSpawn.y = buffer.getFloat();
        redSpawn.z = buffer.getFloat();
        blueSpawn.x = buffer.getFloat();
        blueSpawn.y = buffer.getFloat();
        blueSpawn.z = buffer.getFloat();
    }

    @Override
    public void process(NetworkableServer server, InetAddress address, int port) {

    }

    @Override
    public void process(NetworkableClient client, InetAddress address, int port) {
        TDMGameMode mode = (TDMGameMode) client.getCore().getGame().getGameMode();
		Log.println("Receiving Red team spawn: " + redSpawn);
		Log.println("Receiving Blue team spawn: " + blueSpawn);
        mode.getRedTeam().setSpawn(redSpawn);
        mode.getBlueTeam().setSpawn(blueSpawn);
    }
}
