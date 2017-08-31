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

package fr.veridiangames.core.game.modes;

import fr.veridiangames.core.game.data.GameData;
import fr.veridiangames.core.game.entities.player.Player;
import fr.veridiangames.core.game.world.Chunk;
import fr.veridiangames.core.maths.Vec3;
import fr.veridiangames.core.network.NetworkableServer;
import fr.veridiangames.core.network.packets.gamemode.tdm.TDMScorePacket;
import fr.veridiangames.core.network.packets.gamemode.tdm.TDMSpawnPacket;
import fr.veridiangames.core.network.packets.gamemode.tdm.TDMTeamPacket;
import fr.veridiangames.core.utils.Color4f;

/**
 * Created by Jimi Vacarians on 24/07/2016.
 */
public class TDMGameMode implements GameMode
{
    public TDMGameMode(GameData data)
    {
        this.redTeam.setColor(Color4f.RED);
        this.blueTeam.setColor(Color4f.BLUE);
        int worldSize = data.getWorldSize() * Chunk.SIZE;
        this.redTeam.setSpawn(new Vec3(50, 50, 50));
        this.blueTeam.setSpawn(new Vec3(worldSize - 50, 50, worldSize - 50));
    }

    private Team redTeam = new Team();
    public Team getRedTeam(){
        return this.redTeam;
    }
    private int redScore = 0;
    public int getRedScore()
    {
        return this.redScore;
    }
    public void setRedScore(int i){
        this.redScore = i;
    }

    private Team blueTeam = new Team();
    public Team getBlueTeam(){
        return this.blueTeam;
    }
    private int blueScore = 0;
    public int getBlueScore()
    {
        return this.blueScore;
    }
    public void setBlueScore(int i){
        this.blueScore = i;
    }

    @Override
    public void update()
    {

    }

    @Override
    public Vec3 getPlayerSpawn(Player p) {
        if(this.redTeam.getPlayers().contains(p))
            return this.redTeam.getSpawn();

        if(this.blueTeam.getPlayers().contains(p))
            return this.blueTeam.getSpawn();

        return new Vec3();
    }

    @Override
    public Team getPlayerTeam(Player p) {
        if(this.redTeam.getPlayers().contains(p))
            return this.redTeam;

        if(this.blueTeam.getPlayers().contains(p))
            return this.blueTeam;

        return null;
    }

    @Override
    public void onPlayerConnect(Player p, NetworkableServer server) {
        if(this.redTeam.getPlayers().size() < this.blueTeam.getPlayers().size())
			this.redTeam.getPlayers().add(p);
		else
			this.blueTeam.getPlayers().add(p);
        server.tcpSendToAll(new TDMScorePacket(this.redScore, this.blueScore)); // All to player
        server.tcpSendToAll(new TDMTeamPacket(this.redTeam, this.blueTeam));
        server.tcpSendToAll(new TDMSpawnPacket(this.redTeam.getSpawn(), this.blueTeam.getSpawn()));
    }

    @Override
    public void onPlayerDisconnect(Player p, NetworkableServer server) {
        if(!this.redTeam.getPlayers().remove(p))
            this.blueTeam.getPlayers().remove(p);

        server.tcpSendToAll(new TDMTeamPacket(this.redTeam, this.blueTeam));
    }

    @Override
    public void onPlayerDeath(Player p, NetworkableServer server) {
        if(this.redTeam.getPlayers().contains(p))
			this.blueScore++;
		else if(this.blueTeam.getPlayers().contains(p))
			this.redScore++;

        server.tcpSendToAll(new TDMScorePacket(this.redScore, this.blueScore));
    }

    @Override
    public void onPlayerSpawn(Player p, NetworkableServer server) {

    }
}
