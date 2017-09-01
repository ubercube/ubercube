package fr.veridiangames.core.game.modes;

import fr.veridiangames.client.Ubercube;
import fr.veridiangames.core.game.Game;
import fr.veridiangames.core.maths.Vec3;
import fr.veridiangames.core.network.NetworkableServer;

import java.util.List;

/**
 * Created by Jimi Vacarians on 31/08/2017.
 */
public class TOTEMGameMode implements GameMode {

	private Team redTeam = new Team();
	public Team getRedTeam(){
		return redTeam;
	}

	private Team blueTeam = new Team();
	public Team getBlueTeam(){
		return blueTeam;
	}

	public TOTEMGameMode(){

	}

	@Override
	public void update() {

	}

	@Override
	public Vec3 getPlayerSpawn(int id) {
		return null;
	}

	@Override
	public Team getPlayerTeam(int id) {
		return null;
	}

	@Override
	public void onPlayerConnect(int id, NetworkableServer server) {

	}

	@Override
	public void onPlayerDisconnect(int id, NetworkableServer server) {

	}

	@Override
	public void onPlayerDeath(int id, NetworkableServer server) {

	}

	@Override
	public void onPlayerSpawn(int id, NetworkableServer server) {

	}

	@Override
	public boolean canSpawnTree(float x, float z) {
		return false;
	}

}
