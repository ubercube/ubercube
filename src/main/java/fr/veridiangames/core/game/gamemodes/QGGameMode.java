package fr.veridiangames.core.game.gamemodes;

import com.sun.javafx.geom.Vec3f;
import fr.veridiangames.client.Ubercube;
import fr.veridiangames.client.main.screens.gamemode.QGHudScreen;
import fr.veridiangames.client.main.screens.gamemode.QGPlayerListScreen;
import fr.veridiangames.client.rendering.guis.GuiCanvas;
import fr.veridiangames.core.GameCore;
import fr.veridiangames.core.game.Game;
import fr.veridiangames.core.game.data.GameData;
import fr.veridiangames.core.game.entities.Entity;
import fr.veridiangames.core.game.entities.player.NetworkedPlayer;
import fr.veridiangames.core.game.entities.player.Player;
import fr.veridiangames.core.game.world.Block;
import fr.veridiangames.core.game.world.Chunk;
import fr.veridiangames.core.game.world.World;
import fr.veridiangames.core.maths.Vec3;
import fr.veridiangames.core.maths.Vec3i;
import fr.veridiangames.core.network.NetworkableClient;
import fr.veridiangames.core.network.NetworkableServer;
import fr.veridiangames.core.network.packets.gamemode.qg.QGPlayerStatsPacket;
import fr.veridiangames.core.network.packets.gamemode.qg.QGScorePacket;
import fr.veridiangames.core.network.packets.gamemode.qg.QGSpawnPacket;
import fr.veridiangames.core.network.packets.gamemode.qg.QGTeamPacket;
import fr.veridiangames.core.network.packets.gamemode.tdm.TDMPlayerStatsPacket;
import fr.veridiangames.core.network.packets.gamemode.tdm.TDMScorePacket;
import fr.veridiangames.core.network.packets.gamemode.tdm.TDMSpawnPacket;
import fr.veridiangames.core.network.packets.gamemode.tdm.TDMTeamPacket;
import fr.veridiangames.core.utils.Color4f;
import fr.veridiangames.core.utils.Log;

import java.util.ArrayList;
import java.util.List;

public class QGGameMode implements GameMode
{
	private PlayerStats stats = new PlayerStats();

	private Team redTeam = new Team();
	public Team getRedTeam(){
		return redTeam;
	}
	private float redScore = 0;
	public float getRedScore()
	{
		return redScore;
	}
	public void setRedScore(int i){
		blueScore = i;
	}

	private Team blueTeam = new Team();
	public Team getBlueTeam(){
		return blueTeam;
	}
	private float blueScore = 0;
	public float getBlueScore()
	{
		return blueScore;
	}
	public void setBlueScore(int i){
		blueScore = i;
	}

	private Vec3f qgPosition = new Vec3f();
	private int qgRadius = 10;

	int worldSize;

	long lastScoreUpdate = System.currentTimeMillis();

	public QGGameMode(GameData data)
	{
		redTeam.setName("Red");
		redTeam.setColor(Color4f.RED);
		blueTeam.setName("Blue");
		blueTeam.setColor(Color4f.BLUE);
		worldSize = data.getWorldSize() * Chunk.SIZE;
		redTeam.setSpawn(new Vec3(50, 50, 50));
		blueTeam.setSpawn(new Vec3(worldSize - 50, 50, worldSize - 50));
	}

	@Override
	public void clientUpdate(NetworkableClient client)
	{

	}

	@Override
	public void serverUpdate(NetworkableServer server)
	{
		int redIn = 0;
		int blueIn = 0;

		Player p;
		for(Integer i : redTeam.getPlayers().getList())
		{
			p = (Player) GameCore.getInstance().getGame().getEntityManager().get(i);

			if(distance(new Vec3f(p.getPosition().x, p.getPosition().y, p.getPosition().z), qgPosition) <= qgRadius)
				redIn++;
		}

		for(Integer i : blueTeam.getPlayers().getList())
		{
			p = (Player) GameCore.getInstance().getGame().getEntityManager().get(i);

			if(distance(new Vec3f(p.getPosition().x, p.getPosition().y, p.getPosition().z), qgPosition) <= qgRadius)
				blueIn++;
		}

		if(redIn > blueIn)
			redScore += 1/60;

		if(blueIn > redIn)
			blueScore += 1/60;

		if(System.currentTimeMillis() - lastScoreUpdate > 1000)
		{
			server.tcpSendToAll(new QGScorePacket((int) redScore, (int) blueScore));
			lastScoreUpdate = System.currentTimeMillis();
		}
	}

	public float distance(Vec3f v1, Vec3f v2)
	{
		Vec3f v = new Vec3f();
		v.x = v1.x - v2.x;
		v.y = v1.y - v2.y;
		v.z = v1.z - v2.z;
		return (float)Math.sqrt(v.x*v.x + v.y*v.y + v.z*v.z);
	}

	@Override
	public Vec3 getPlayerSpawn(int id)
	{
		Vec3 sp = new Vec3();
		Vec3 offset = new Vec3();

		if (redTeam.getPlayers().contains(id))
		{
			sp = redTeam.getSpawn().copy();
		}
		else if (blueTeam.getPlayers().contains(id))
		{
			sp = blueTeam.getSpawn().copy();
		}
		else {
			Log.error("Player(" + id + ") has no team !");
		}

		offset.x = (int)(((Math.random() * 0.8 + 0.2) * 2.0 - 1.0) * 8);
		offset.z = (int)(((Math.random() * 0.8 + 0.2) * 2.0 - 1.0) * 8);

		sp.x += offset.x;
		sp.z += offset.z;

		//System.out.println(sp.x + " " + sp.y + " " + sp.z);
		sp.y = GameCore.getInstance().getGame().getWorld().getHeightAt((int)sp.x, (int)sp.z) + 2;

		return sp;
	}

	@Override
	public Team getPlayerTeam(int id)
	{
		if(redTeam.getPlayers().contains(id))
			return redTeam;

		if(blueTeam.getPlayers().contains(id))
			return blueTeam;

		return null;
	}

	@Override
	public List<Team> getTeams()
	{
		List<Team> t = new ArrayList<>();
		t.add(redTeam);
		t.add(blueTeam);
		return t;
	}

	@Override
	public GuiCanvas getPlayerListScreen(GuiCanvas parent)
	{
		return new QGPlayerListScreen(parent);
	}

	@Override
	public GuiCanvas getHudScreen(GuiCanvas parent)
	{
		return new QGHudScreen(parent);
	}

	@Override
	public PlayerStats getPlayerStats()
	{
		return stats;
	}

	@Override
	public void onWorldGeneration(World w)
	{
		w.addBlock((int)redTeam.getSpawn().x, w.getHeightAt((int)redTeam.getSpawn().x,(int)redTeam.getSpawn().z),(int)redTeam.getSpawn().z, Block.ROCK.getARGB());
		w.addBlock((int)redTeam.getSpawn().x, w.getHeightAt((int)redTeam.getSpawn().x,(int)redTeam.getSpawn().z)+1,(int)redTeam.getSpawn().z, Block.ROCK.getARGB());
		w.addBlock((int)redTeam.getSpawn().x, w.getHeightAt((int)redTeam.getSpawn().x,(int)redTeam.getSpawn().z)+2,(int)redTeam.getSpawn().z, Block.ROCK.getARGB());
		w.addBlock((int)redTeam.getSpawn().x, w.getHeightAt((int)redTeam.getSpawn().x,(int)redTeam.getSpawn().z)+3,(int)redTeam.getSpawn().z, Color4f.RED.getARGB());

		w.addBlock((int)blueTeam.getSpawn().x, w.getHeightAt((int)blueTeam.getSpawn().x,(int)blueTeam.getSpawn().z),(int)blueTeam.getSpawn().z, Block.ROCK.getARGB());
		w.addBlock((int)blueTeam.getSpawn().x, w.getHeightAt((int)blueTeam.getSpawn().x,(int)blueTeam.getSpawn().z)+1,(int)blueTeam.getSpawn().z, Block.ROCK.getARGB());
		w.addBlock((int)blueTeam.getSpawn().x, w.getHeightAt((int)blueTeam.getSpawn().x,(int)blueTeam.getSpawn().z)+2,(int)blueTeam.getSpawn().z, Block.ROCK.getARGB());
		w.addBlock((int)blueTeam.getSpawn().x, w.getHeightAt((int)blueTeam.getSpawn().x,(int)blueTeam.getSpawn().z)+3,(int)blueTeam.getSpawn().z, Color4f.BLUE.getARGB());

		qgPosition = new Vec3f(worldSize/2, w.getHeightAt(worldSize/2, worldSize/2), worldSize/2);
		w.addBlock((int)qgPosition.x, w.getHeightAt((int)qgPosition.x,(int)qgPosition.z),(int)qgPosition.z, Block.ROCK.getARGB());
		w.addBlock((int)qgPosition.x, w.getHeightAt((int)qgPosition.x,(int)qgPosition.z)+1,(int)qgPosition.z, Block.ROCK.getARGB());
		w.addBlock((int)qgPosition.x, w.getHeightAt((int)qgPosition.x,(int)qgPosition.z)+2,(int)qgPosition.z, Block.ROCK.getARGB());
		w.addBlock((int)qgPosition.x, w.getHeightAt((int)qgPosition.x,(int)qgPosition.z)+3,(int)qgPosition.z, Block.ROCK.getARGB());
		w.addBlock((int)qgPosition.x, w.getHeightAt((int)qgPosition.x,(int)qgPosition.z)+4,(int)qgPosition.z, Block.ROCK.getARGB());
		w.addBlock((int)qgPosition.x, w.getHeightAt((int)qgPosition.x,(int)qgPosition.z)+5,(int)qgPosition.z, Color4f.GREEN.getARGB());
	}

	@Override
	public void onPlayerConnect(int id, NetworkableServer server)
	{
		if(redTeam.getPlayers().getSize() < blueTeam.getPlayers().getSize()){
			redTeam.getPlayers().add(id);
			Log.println("r");
		}else{
			blueTeam.getPlayers().add(id);
			Log.println("b");
		}

		stats.set(id, PlayerStats.Stats.KILLS, 0);
		stats.set(id, PlayerStats.Stats.DEATHS, 0);

		server.tcpSendToAll(new QGScorePacket((int) redScore, (int) blueScore)); // All to player
		server.tcpSendToAll(new QGTeamPacket(redTeam, blueTeam));
		server.tcpSendToAll(new QGSpawnPacket(redTeam.getSpawn(), blueTeam.getSpawn()));
		server.tcpSendToAll(new QGPlayerStatsPacket(stats));
	}

	@Override
	public void onPlayerDisconnect(int id, NetworkableServer server)
	{
		if(!redTeam.getPlayers().remove(id))
			blueTeam.getPlayers().remove(id);

		stats.remove(id);

		server.tcpSendToAll(new QGTeamPacket(redTeam, blueTeam));
		server.tcpSendToAll(new QGPlayerStatsPacket(stats));
	}

	@Override
	public void onPlayerDeath(int victimId, int shooterId, NetworkableServer server)
	{
		if (victimId != shooterId)
			stats.set(shooterId, PlayerStats.Stats.KILLS, (int)stats.get(shooterId).get(PlayerStats.Stats.KILLS)+1);
		stats.set(victimId, PlayerStats.Stats.DEATHS, (int)stats.get(victimId).get(PlayerStats.Stats.DEATHS)+1);

		server.tcpSendToAll(new QGScorePacket((int)redScore, (int)blueScore));
		server.tcpSendToAll(new QGPlayerStatsPacket(stats));
	}

	@Override
	public void onPlayerSpawn(int id, NetworkableServer server)
	{

	}

	@Override
	public boolean canSpawnTree(float x, float z)
	{
		double d = Math.hypot(x-redTeam.getSpawn().x, z-redTeam.getSpawn().z);
		if(d < 10){
			return false;
		}

		d = Math.hypot(x-blueTeam.getSpawn().x, z-blueTeam.getSpawn().z);
		if(d < 10){
			return false;
		}

		d = Math.hypot(x-qgPosition.x, z-qgPosition.z);
		if(d < 30){
			return false;
		}
		return true;
	}
}
