package fr.veridiangames.client.rendering.renderers.game.minimap;

import fr.veridiangames.client.rendering.Display;
import fr.veridiangames.client.rendering.guis.primitives.StaticPrimitive;
import fr.veridiangames.client.rendering.shaders.MinimapShader;
import fr.veridiangames.client.rendering.shaders.WeaponFboShader;
import fr.veridiangames.client.rendering.shaders.WorldShader;
import fr.veridiangames.client.rendering.textures.FrameBuffer;
import fr.veridiangames.core.maths.Mat4;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.glEnable;

public class MinimapFramebuffer
{
	private FrameBuffer fbo;
	private MinimapRenderer renderer;

	private WeaponFboShader fboShader;
	private MinimapShader worldShader;

	public MinimapFramebuffer()
	{
		this.fbo = new FrameBuffer(1280, 720);
		this.fboShader = new WeaponFboShader();
		this.renderer = new MinimapRenderer();
		this.worldShader = new MinimapShader();
	}

	public void update()
	{
		renderer.update();
	}

	public void render()
	{
		fbo.bind();
		glClearColor(0, 0, 0, 0);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		worldShader.bind();
		worldShader.setProjectionMatrix(Mat4.orthographic(Display.getInstance().getWidth(), 0, 0, Display.getInstance().getHeight(), -1, 1));
		renderer.render(worldShader);
		fbo.unbind();

		fboShader.bind();
		fboShader.setProjectionMatrix(Mat4.orthographic(Display.getInstance().getWidth(), 0, 0, Display.getInstance().getHeight(), -1, 1));
		glBindTexture(GL_TEXTURE_2D, fbo.getColorTextureID());
		glDisable(GL_CULL_FACE);
		StaticPrimitive.quadPrimitive().render(fboShader,
			Display.getInstance().getWidth() / 2,
			Display.getInstance().getHeight() / 2,0,
			Display.getInstance().getWidth() / 2,
			-Display.getInstance().getHeight() / 2, 0);
		glEnable(GL_CULL_FACE);
	}
}
