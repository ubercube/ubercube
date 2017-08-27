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

import fr.veridiangames.core.game.entities.player.Player;
import fr.veridiangames.core.game.modes.TDMGameMode;
import fr.veridiangames.core.game.modes.Team;
import fr.veridiangames.core.network.NetworkableClient;
import fr.veridiangames.core.network.NetworkableServer;
import fr.veridiangames.core.network.packets.Packet;
import fr.veridiangames.core.utils.DataBuffer;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jimi Vacarians on 25/07/2016.
 */
public class TDMTeamPacket extends Packet {
    private List<Integer> redTeam = new ArrayList<>();
    private List<Integer> blueTeam = new ArrayList<>();

    public TDMTeamPacket() {
        super(Packet.GAMEMODE_TDM_TEAM);
    }

    public TDMTeamPacket(Team red, Team blue) {
        super(Packet.GAMEMODE_TDM_TEAM);
        data.put(red.getPlayers().size());
        for(Player p : red.getPlayers()){
            data.put(p.getID());
        }
        data.put(blue.getPlayers().size());
        for(Player p : blue.getPlayers()){
            data.put(p.getID());
        }
		data.flip();
    }

    @Override
    public void read(DataBuffer buffer) {
        int size = buffer.getInt();
        for (int i=0;i<size;i++){
            redTeam.add(new Integer(buffer.getInt()));
        }

        size = buffer.getInt();
        for (int i=0;i<size;i++){
            blueTeam.add(new Integer(buffer.getInt()));
        }
    }

    @Override
    public void process(NetworkableServer server, InetAddress address, int port) {

    }

    @Override
    public void process(NetworkableClient client, InetAddress address, int port) {
        TDMGameMode mode = (TDMGameMode) client.getCore().getGame().getGameMode();
        mode.getRedTeam().getPlayers().clear();
        for(int i : redTeam){
            if(i != 0)
                mode.getRedTeam().getPlayers().add((Player) client.getCore().getGame().getEntityManager().get(i));
        }

        mode.getBlueTeam().getPlayers().clear();
        for(int i : blueTeam){
            if(i != 0)
                mode.getBlueTeam().getPlayers().add((Player) client.getCore().getGame().getEntityManager().get(i));
        }
    }
}