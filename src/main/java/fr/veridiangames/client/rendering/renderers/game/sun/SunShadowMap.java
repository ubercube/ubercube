package fr.veridiangames.client.rendering.renderers.game.sun;

import fr.veridiangames.client.rendering.player.PlayerViewport;
import fr.veridiangames.client.rendering.renderers.game.GameRenderer;
import fr.veridiangames.client.rendering.textures.FrameBuffer;
import fr.veridiangames.core.GameCore;

import static org.lwjgl.opengl.GL11.*;

public class SunShadowMap
{
	private FrameBuffer[] 	shadowMaps;
	private GameCore 		core;
	private SunViewport 	sun;

	public SunShadowMap(GameCore core)
	{
		this.core = core;
		this.sun = new SunViewport(core);
		this.shadowMaps = new FrameBuffer[sun.getCascadesCount()];
		for (int i = 0; i < shadowMaps.length; i++)
			this.shadowMaps[i] = new FrameBuffer(4096, 4096);
	}

	public void update(PlayerViewport playerViewport)
	{
		sun.update(playerViewport);
	}

	public void render(GameRenderer gameRenderer)
	{
		for (int i = 0; i < shadowMaps.length; i++)
		{
			shadowMaps[i].bindDepth();
			glEnable(GL_DEPTH_TEST);
			glClear(GL_DEPTH_BUFFER_BIT | GL_COLOR_BUFFER_BIT);
			gameRenderer.renderShadowMap(sun.getLightMatrix()[i]);
			glDisable(GL_DEPTH_TEST);
			shadowMaps[i].unbind();
		}
	}

	public SunViewport getSun() {
		return sun;
	}

	public FrameBuffer[] getShadowMaps() {
		return shadowMaps;
	}
}
