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

import fr.veridiangames.client.main.screens.gamemode.TDMPlayerListScreen;
import fr.veridiangames.client.rendering.guis.GuiCanvas;
import fr.veridiangames.core.GameCore;
import fr.veridiangames.core.game.data.GameData;
import fr.veridiangames.core.game.world.Block;
import fr.veridiangames.core.game.world.Chunk;
import fr.veridiangames.core.game.world.World;
import fr.veridiangames.core.maths.Vec3;
import fr.veridiangames.core.network.NetworkableServer;
import fr.veridiangames.core.network.packets.gamemode.tdm.TDMScorePacket;
import fr.veridiangames.core.network.packets.gamemode.tdm.TDMSpawnPacket;
import fr.veridiangames.core.network.packets.gamemode.tdm.TDMTeamPacket;
import fr.veridiangames.core.utils.Color4f;
import fr.veridiangames.core.utils.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Jimi Vacarians on 24/07/2016.
 */
public class TDMGameMode implements GameMode
{
    public TDMGameMode(GameData data)
    {
    	redTeam.setName("Red");
        redTeam.setColor(Color4f.RED);
        blueTeam.setName("Blue");
        blueTeam.setColor(Color4f.BLUE);
        int worldSize = data.getWorldSize() * Chunk.SIZE;
        redTeam.setSpawn(new Vec3(50, 50, 50));
        blueTeam.setSpawn(new Vec3(worldSize - 50, 50, worldSize - 50));
	}

    private Team redTeam = new Team();
    public Team getRedTeam(){
        return redTeam;
    }
    private int redScore = 0;
    public int getRedScore()
    {
        return redScore;
    }
    public void setRedScore(int i){
        redScore = i;
    }

    private Team blueTeam = new Team();
    public Team getBlueTeam(){
        return blueTeam;
    }
    private int blueScore = 0;
    public int getBlueScore()
    {
        return blueScore;
    }
    public void setBlueScore(int i){
        blueScore = i;
    }

    @Override
    public void update()
    {

    }

    @Override
    public Vec3 getPlayerSpawn(int id) {
    	Vec3 sp = new Vec3();
    	Vec3 offset = new Vec3();
        if(redTeam.getPlayers().contains(id))
			sp = redTeam.getSpawn();
        else if(blueTeam.getPlayers().contains(id))
			sp = blueTeam.getSpawn();
        else {
			Log.error("Player(" + id + ") hose no team !");
		}

		offset.x = (int)(((Math.random() * 0.8 + 0.2) * 2.0 - 1.0) * 8);
		offset.z = (int)(((Math.random() * 0.8 + 0.2) * 2.0 - 1.0) * 8);

		sp.x += offset.x;
		sp.z += offset.z;

		sp.y = GameCore.getInstance().getGame().getWorld().getHeightAt((int)sp.x, (int)sp.z) + 2;
		System.out.println(sp.x + " " + sp.y + " " + sp.z);

        return sp;
    }

    @Override
    public Team getPlayerTeam(int id) {
        if(redTeam.getPlayers().contains(id))
            return redTeam;

        if(blueTeam.getPlayers().contains(id))
            return blueTeam;

        return null;
    }

	@Override
	public List<Team> getTeams() {
    	List<Team> t = new ArrayList<>();
    	t.add(redTeam);
    	t.add(blueTeam);
		return t;
	}

	@Override
	public GuiCanvas getPlayerListScreen(GuiCanvas parent) {
		return new TDMPlayerListScreen(parent);
	}

	@Override
	public void onWorldGeneration(World w) {
		w.addBlock((int)redTeam.getSpawn().x, w.getHeightAt((int)redTeam.getSpawn().x,(int)redTeam.getSpawn().z),(int)redTeam.getSpawn().z, Block.ROCK.getARGB());
		w.addBlock((int)redTeam.getSpawn().x, w.getHeightAt((int)redTeam.getSpawn().x,(int)redTeam.getSpawn().z)+1,(int)redTeam.getSpawn().z, Block.ROCK.getARGB());
		w.addBlock((int)redTeam.getSpawn().x, w.getHeightAt((int)redTeam.getSpawn().x,(int)redTeam.getSpawn().z)+2,(int)redTeam.getSpawn().z, Block.ROCK.getARGB());
		w.addBlock((int)redTeam.getSpawn().x, w.getHeightAt((int)redTeam.getSpawn().x,(int)redTeam.getSpawn().z)+3,(int)redTeam.getSpawn().z, Color4f.RED.getARGB());

		w.addBlock((int)blueTeam.getSpawn().x, w.getHeightAt((int)blueTeam.getSpawn().x,(int)blueTeam.getSpawn().z),(int)blueTeam.getSpawn().z, Block.ROCK.getARGB());
		w.addBlock((int)blueTeam.getSpawn().x, w.getHeightAt((int)blueTeam.getSpawn().x,(int)blueTeam.getSpawn().z)+1,(int)blueTeam.getSpawn().z, Block.ROCK.getARGB());
		w.addBlock((int)blueTeam.getSpawn().x, w.getHeightAt((int)blueTeam.getSpawn().x,(int)blueTeam.getSpawn().z)+2,(int)blueTeam.getSpawn().z, Block.ROCK.getARGB());
		w.addBlock((int)blueTeam.getSpawn().x, w.getHeightAt((int)blueTeam.getSpawn().x,(int)blueTeam.getSpawn().z)+3,(int)blueTeam.getSpawn().z, Color4f.BLUE.getARGB());
	}

	@Override
    public void onPlayerConnect(int id, NetworkableServer server) {
        if(redTeam.getPlayers().getSize() < blueTeam.getPlayers().getSize()){
            redTeam.getPlayers().add(id);
        }else{
            blueTeam.getPlayers().add(id);
        }
        server.tcpSendToAll(new TDMScorePacket(redScore, blueScore)); // All to player
        server.tcpSendToAll(new TDMTeamPacket(redTeam, blueTeam));
        Log.println("Sending Red team spawn: " + redTeam.getSpawn());
        Log.println("Sending Blue team spawn: " + blueTeam.getSpawn());
        server.tcpSendToAll(new TDMSpawnPacket(redTeam.getSpawn(), blueTeam.getSpawn()));
    }

    @Override
    public void onPlayerDisconnect(int id, NetworkableServer server) {
        if(!redTeam.getPlayers().remove(id))
            blueTeam.getPlayers().remove(id);

        server.tcpSendToAll(new TDMTeamPacket(redTeam, blueTeam));
    }

    @Override
    public void onPlayerDeath(int id, NetworkableServer server) {
        if(redTeam.getPlayers().contains(id)){
            blueScore++;
        }else if(blueTeam.getPlayers().contains(id)){
            redScore++;
        }

        server.tcpSendToAll(new TDMScorePacket(redScore, blueScore));
    }

    @Override
    public void onPlayerSpawn(int id, NetworkableServer server) {

    }

	@Override
	public boolean canSpawnTree(float x, float z) {
    	double d = Math.hypot(x-redTeam.getSpawn().x, z-redTeam.getSpawn().z);
    	if(d < 10){
    		return false;
		}

		d = Math.hypot(x-blueTeam.getSpawn().x, z-blueTeam.getSpawn().z);
		if(d < 10){
			return false;
		}

		return true;
	}

}
