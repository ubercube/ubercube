package fr.veridiangames.client.rendering.renderers.game.minimap;

import fr.veridiangames.client.Resource;
import fr.veridiangames.client.Ubercube;
import fr.veridiangames.client.main.minimap.MinimapHandler;
import fr.veridiangames.client.main.minimap.MinimapObject;
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

	private MinimapHandler minimap;

	public MinimapFramebuffer()
	{
		this.minimap = Ubercube.getInstance().getMinimapHandler();
		this.width = minimap.getSize().x;
		this.height = minimap.getSize().y;
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
		glClearColor(0.2f, 0.2f, 0.2f, 1f);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		worldShader.bind();
		worldShader.setProjectionMatrix(Mat4.orthographic(width, 0, 0, height, -1, 1));
		float scale = height / 30 / 2;
		minimap.setScale(scale);
		renderer.render(worldShader, scale);
		fbo.unbind();

		fboShader.bind();
		fboShader.endColor();
		fboShader.setProjectionMatrix(Mat4.orthographic(Display.getInstance().getWidth(), 0, 0, Display.getInstance().getHeight(), -1, 1));
		glBindTexture(GL_TEXTURE_2D, shadowColor.getId());
		glDisable(GL_CULL_FACE);
		StaticPrimitive.quadPrimitive().render(fboShader,
			Display.getInstance().getWidth() - minimap.getPos().x - width / 2 + 2,
			Display.getInstance().getHeight() - minimap.getPos().y - height / 2 + 3,0,
			-width / 2,
			-height / 2, 1);
		glBindTexture(GL_TEXTURE_2D, fbo.getColorTextureID());
		StaticPrimitive.quadPrimitive().render(fboShader,
			Display.getInstance().getWidth() - minimap.getPos().x - width / 2,
			Display.getInstance().getHeight() - minimap.getPos().y - height / 2,0,
			-width / 2,
			-height / 2, 1);
		glBindTexture(GL_TEXTURE_2D, playerPosition.getId());
		StaticPrimitive.quadPrimitive().render(fboShader,
			Display.getInstance().getWidth() - minimap.getPos().x - width / 2,
			Display.getInstance().getHeight() - minimap.getPos().y - height / 2,0,
			75,
			75, 1);
		for (MinimapObject obj : minimap.getMinimapObjects())
		{
			float rx = obj.getMinimapCorrectedPosition().x;
			float ry = obj.getMinimapCorrectedPosition().y;

			glBindTexture(GL_TEXTURE_2D, obj.getIcon().getId());
			fboShader.startColor(obj.getColor());
			StaticPrimitive.quadPrimitive().render(fboShader,
				Display.getInstance().getWidth() - minimap.getPos().x - width / 2 + rx,
				Display.getInstance().getHeight() - minimap.getPos().y - height / 2 + ry,0,
				10,
				10, 1);
		}
		fboShader.endColor();
		glBindTexture(GL_TEXTURE_2D, 0);
		glEnable(GL_CULL_FACE);
	}
}
