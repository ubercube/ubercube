package fr.veridiangames.client.rendering.renderers.game.sun;

import fr.veridiangames.client.rendering.player.PlayerViewport;
import fr.veridiangames.core.GameCore;
import fr.veridiangames.core.game.entities.player.Player;

public class SunShadowMap
{
	GameCore core;
	SunViewport sun;

	public SunShadowMap(GameCore core)
	{
		this.core = core;
		this.sun = new SunViewport(core);
	}

	public void update(PlayerViewport playerViewport)
	{
		sun.update(playerViewport);
	}

	public void render()
	{

	}

	public SunViewport getSun() {
		return sun;
	}
}
