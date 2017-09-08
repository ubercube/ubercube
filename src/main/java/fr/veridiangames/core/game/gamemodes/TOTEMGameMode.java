package fr.veridiangames.core.game.gamemodes;

import fr.veridiangames.client.rendering.guis.GuiCanvas;
import fr.veridiangames.core.game.world.World;
import fr.veridiangames.core.maths.Vec3;
import fr.veridiangames.core.network.NetworkableServer;

import java.util.List;

/**
 * Created by Jimi Vacarians on 06/09/2017.
 */
public class TOTEMGameMode implements GameMode {
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
	public List<Team> getTeams() {
		return null;
	}

	@Override
	public GuiCanvas getPlayerListScreen(GuiCanvas parent) {
		return null;
	}

	@Override
	public PlayerStats getPlayerStats() {
		return null;
	}

	@Override
	public void onWorldGeneration(World w) {

	}

	@Override
	public void onPlayerConnect(int id, NetworkableServer server) {

	}

	@Override
	public void onPlayerDisconnect(int id, NetworkableServer server) {

	}

	@Override
	public void onPlayerDeath(int victimId, int shooterId, NetworkableServer server) {

	}

	@Override
	public void onPlayerSpawn(int id, NetworkableServer server) {

	}

	@Override
	public boolean canSpawnTree(float x, float z) {
		return false;
	}
}
