package fr.veridiangames.client.rendering.renderers.game.minimap;

import fr.veridiangames.client.Resource;
import fr.veridiangames.client.rendering.Display;
import fr.veridiangames.client.rendering.guis.primitives.StaticPrimitive;
import fr.veridiangames.client.rendering.shaders.MinimapFboShader;
import fr.veridiangames.client.rendering.shaders.MinimapShader;
import fr.veridiangames.client.rendering.textures.FrameBuffer;
import fr.veridiangames.client.rendering.textures.Texture;
import fr.veridiangames.client.rendering.textures.TextureLoader;
import fr.veridiangames.core.maths.Mat4;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.glEnable;

public class MinimapFramebuffer
{
	private int width, height;
	private FrameBuffer fbo;
	private MinimapRenderer renderer;

	private MinimapFboShader fboShader;
	private MinimapShader worldShader;

	private Texture playerPosition;
	private Texture shadowColor;

	public MinimapFramebuffer()
	{
		this.width = 300;
		this.height = 200;
		this.fbo = new FrameBuffer(width, height);
		this.fboShader = new MinimapFboShader();
		this.renderer = new MinimapRenderer(width, height);
		this.worldShader = new MinimapShader();
		this.playerPosition = TextureLoader.loadTexture(Resource.getResource("textures/player_minimap.png"), GL_LINEAR, false);
		this.shadowColor = TextureLoader.loadTexture(Resource.getResource("textures/shadow.png"), GL_NEAREST, false);
	}

	public void update()
	{
		renderer.update();
	}

	public void render()
	{
		glDisable(GL_DEPTH_TEST);
		fbo.bind();
		glClearColor(0.2f, 0.2f, 0.2f, 0.5f);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		worldShader.bind();
		worldShader.setProjectionMatrix(Mat4.orthographic(width, 0, 0, height, -1, 1));
		renderer.render(worldShader, height / 30 / 2);
		fbo.unbind();

		fboShader.bind();
		fboShader.setProjectionMatrix(Mat4.orthographic(Display.getInstance().getWidth(), 0, 0, Display.getInstance().getHeight(), -1, 1));
		glBindTexture(GL_TEXTURE_2D, shadowColor.getId());
		glDisable(GL_CULL_FACE);
		StaticPrimitive.quadPrimitive().render(fboShader,
			Display.getInstance().getWidth() - 35 - width / 2 + 2,
			Display.getInstance().getHeight() - 30 - height / 2 + 3,0,
			-width / 2,
			-height / 2, 1);
		glBindTexture(GL_TEXTURE_2D, fbo.getColorTextureID());
		StaticPrimitive.quadPrimitive().render(fboShader,
			Display.getInstance().getWidth() - 35 - width / 2,
			Display.getInstance().getHeight() - 30 - height / 2,0,
			-width / 2,
			-height / 2, 1);
		glBindTexture(GL_TEXTURE_2D, playerPosition.getId());
		StaticPrimitive.quadPrimitive().render(fboShader,
			Display.getInstance().getWidth() - 35 - width / 2,
			Display.getInstance().getHeight() - 30 - height / 2,0,
			35,
			35, 1);
		glBindTexture(GL_TEXTURE_2D, 0);
		glEnable(GL_CULL_FACE);
	}
}
