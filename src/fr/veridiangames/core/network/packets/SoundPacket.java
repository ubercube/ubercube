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

package fr.veridiangames.core.network.packets;

import fr.veridiangames.core.game.entities.audio.AudioSource;
import fr.veridiangames.core.game.entities.player.Player;
import fr.veridiangames.core.game.entities.player.ServerPlayer;
import fr.veridiangames.core.maths.Vec3;
import fr.veridiangames.core.network.NetworkableClient;
import fr.veridiangames.core.network.NetworkableServer;
import fr.veridiangames.core.utils.DataBuffer;

import java.net.InetAddress;

/**
 * Created by Marc on 19/06/2016.
 */
public class SoundPacket extends Packet
{
    private int     clientID;
    private int     sound;
    private Vec3    position;

    public SoundPacket()
    {
        super(AUDIO);
    }

    public SoundPacket(int client, AudioSource source)
    {
        super(AUDIO);

        data.put(client);
        data.put(source.getSound());
        data.put(source.getPosition().x);
        data.put(source.getPosition().y);
        data.put(source.getPosition().z);
        data.flipped();
    }

    public SoundPacket(SoundPacket packet)
    {
        super(AUDIO);
        data.put(packet.clientID);
        data.put(packet.sound);
        data.put(packet.position.x);
        data.put(packet.position.y);
        data.put(packet.position.z);
        data.flipped();
    }

    public void read(DataBuffer buffer)
    {
        this.clientID = buffer.getInt();
        this.sound = buffer.getInt();
        this.position = new Vec3(buffer.getFloat(), buffer.getFloat(), buffer.getFloat());
    }

    public void process(NetworkableServer server, InetAddress address, int port)
    {
        server.udpSendToAny(new SoundPacket(clientID, new AudioSource(sound, position)), clientID);
    }

    public void process(NetworkableClient client, InetAddress address, int port)
    {
        client.playSound(new AudioSource(sound, position));
    }
}
