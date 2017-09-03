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
import fr.veridiangames.core.network.NetworkableClient;
import fr.veridiangames.core.network.NetworkableServer;
import fr.veridiangames.core.network.packets.Packet;
import fr.veridiangames.core.utils.DataBuffer;

import java.net.InetAddress;

/**
 * Created by Jimi Vacarians on 25/07/2016.
 */
public class TDMScorePacket extends Packet {
    private int redTeam;
    private int blueTeam;

    public TDMScorePacket() {
        super(Packet.GAMEMODE_TDM_SCORE);
    }

    public TDMScorePacket(int red, int blue) {
        super(Packet.GAMEMODE_TDM_SCORE);
        data.put(red);
        data.put(blue);
        data.flip();
    }

    @Override
    public void read(DataBuffer buffer) {
        redTeam = buffer.getInt();
        blueTeam = buffer.getInt();
    }

    @Override
    public void process(NetworkableServer server, InetAddress address, int port) {

    }

    @Override
    public void process(NetworkableClient client, InetAddress address, int port) {
        TDMGameMode mode = (TDMGameMode) client.getCore().getGame().getGameMode();
        mode.setRedScore(redTeam);
        mode.setBlueScore(blueTeam);
    }
}