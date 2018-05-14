package fr.veridiangames.client.rendering.renderers.game;

import fr.veridiangames.client.Ubercube;
import fr.veridiangames.client.rendering.Display;
import fr.veridiangames.client.rendering.guis.primitives.StaticPrimitive;
import fr.veridiangames.client.rendering.renderers.game.sun.SunShadowMap;
import fr.veridiangames.client.rendering.shaders.FramebufferShader;
import fr.veridiangames.client.rendering.shaders.WeaponFboShader;
import fr.veridiangames.client.rendering.textures.FrameBuffer;
import fr.veridiangames.core.GameCore;
import fr.veridiangames.core.maths.Mat4;
import fr.veridiangames.core.utils.Color4f;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.glEnable;

/**
 * Created by Marc on 12/11/2017.
 */
public class GameBufferRenderer
{
	private WeaponFboShader			weaponFboShader;
	private FramebufferShader		framebufferShader;

	private FrameBuffer				weaponFbo;
	private FrameBuffer				framebuffer;

	private GameRenderer			gameRenderer;
	private SunShadowMap			sunShadowMap;

	private int						sampledWidth;
	private int						sampledHeight;

	private int						samples;
	private int						lastSamples;

	private boolean					renderShadows;

	public GameBufferRenderer(Ubercube main, GameCore core, int samples)
	{
		this.samples = samples;
		this.gameRenderer = new GameRenderer(main, core);
		this.weaponFboShader = new WeaponFboShader();
		this.framebufferShader = new FramebufferShader();
		this.sunShadowMap = new SunShadowMap(core);
		this.renderShadows = true;
	}

	public void update()
	{
		gameRenderer.update();
		sunShadowMap.update(gameRenderer.getPlayerViewport());
	}

	public void render()
	{
		updatedFramebuffers();
		if (renderShadows)
			renderShadows();
		renderWorld();
		renderClientView();
	}

	private void updatedFramebuffers()
	{
		if (lastSamples != samples || framebuffer == null || weaponFbo == null || Display.getInstance().wasResized())
		{
			sampledWidth = (Display.getInstance().getWidth()) * samples;
			sampledHeight = (Display.getInstance().getHeight()) * samples;

			if (framebuffer != null)
			{
				this.framebuffer.destroy();
				this.framebuffer = null;
			}
			if (weaponFbo != null)
			{
				this.weaponFbo.destroy();
				this.weaponFbo = null;
			}
			this.framebuffer = new FrameBuffer(sampledWidth, sampledHeight);
			this.weaponFbo = new FrameBuffer(sampledWidth, sampledHeight);
		}
		lastSamples = samples;
	}

	private void renderWorld()
	{
		framebuffer.bind();
		glEnable(GL_DEPTH_TEST);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		gameRenderer.renderWorld(gameRenderer.getPlayerViewport().getCamera(), sunShadowMap);
		glDisable(GL_DEPTH_TEST);
		framebuffer.unbind();

		framebufferShader.bind();
		framebufferShader.setProjectionMatrix(Mat4.orthographic(Display.getInstance().getWidth(), 0, 0, Display.getInstance().getHeight(), -1, 1));

		glBindTexture(GL_TEXTURE_2D, framebuffer.getColorTextureID());
		glDisable(GL_CULL_FACE);
		StaticPrimitive.quadPrimitive().render(framebufferShader,
			Display.getInstance().getWidth() / 2,
			Display.getInstance().getHeight() / 2,0,
			Display.getInstance().getWidth() / 2,
			-Display.getInstance().getHeight() / 2, 0);
		glBindTexture(GL_TEXTURE_2D, 0);
	}

	private void renderShadows()
	{
		sunShadowMap.render(gameRenderer);
	}

	private void renderClientView()
	{
		weaponFbo.bind();
		glEnable(GL_DEPTH_TEST);
		glClearColor(0, 0, 0, 0);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		gameRenderer.renderClientView(gameRenderer.getPlayerViewport().getCamera());
		glDisable(GL_DEPTH_TEST);
		weaponFbo.unbind();

		weaponFboShader.bind();
		weaponFboShader.setProjectionMatrix(Mat4.orthographic(Display.getInstance().getWidth(), 0, 0, Display.getInstance().getHeight(), -1, 1));

		glBindTexture(GL_TEXTURE_2D, weaponFbo.getColorTextureID());
		glDisable(GL_CULL_FACE);
		StaticPrimitive.quadPrimitive().render(weaponFboShader,
			Display.getInstance().getWidth() / 2,
			Display.getInstance().getHeight() / 2,0,
			Display.getInstance().getWidth() / 2,
			-Display.getInstance().getHeight() / 2, 0);
		glEnable(GL_CULL_FACE);

//		framebufferShader.bind();
//		framebufferShader.setProjectionMatrix(Mat4.orthographic(Display.getInstance().getWidth(), 0, 0, Display.getInstance().getHeight(), -1, 1));
//		framebufferShader.setColor(Color4f.WHITE);
//		if (renderShadows)
//			for (int i = 0; i < sunShadowMap.getSun().getCascadesCount(); i++)
//			{
//				glBindTexture(GL_TEXTURE_2D, sunShadowMap.getShadowMaps()[i].getDepthTextureID());
//				glDisable(GL_CULL_FACE);
//				StaticPrimitive.quadPrimitive().render(framebufferShader,
//					200 / 2,
//					200 / 2 + 200 * i,0,
//					200 / 2,
//					-200 / 2, 0);
//				glBindTexture(GL_TEXTURE_2D, 0);
//			}
	}

	public void setSamples(int samples)
	{
		this.samples = samples;
	}

	public int getSamples()
	{
		return samples;
	}

	public boolean isRenderShadows() {
		return renderShadows;
	}

	public void setRenderShadows(boolean renderShadows) {
		this.renderShadows = renderShadows;
	}
}