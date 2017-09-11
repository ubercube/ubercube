package fr.veridiangames.client.rendering.renderers.game.minimap;

import fr.veridiangames.client.Resource;
import fr.veridiangames.client.Ubercube;
import fr.veridiangames.client.main.minimap.MinimapHandler;
import fr.veridiangames.client.main.minimap.MinimapObject;
import fr.veridiangames.client.rendering.Display;
import fr.veridiangames.client.rendering.guis.primitives.StaticPrimitive;
import fr.veridiangames.client.rendering.shaders.GuiShader;
import fr.veridiangames.client.rendering.shaders.MinimapFboShader;
import fr.veridiangames.client.rendering.shaders.MinimapShader;
import fr.veridiangames.client.rendering.shaders.Shader;
import fr.veridiangames.client.rendering.textures.FrameBuffer;
import fr.veridiangames.client.rendering.textures.Texture;
import fr.veridiangames.client.rendering.textures.TextureLoader;
import fr.veridiangames.core.maths.Mat4;
import fr.veridiangames.core.utils.Color4f;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.glEnable;


public class MinimapFramebuffer
{
	private int x, y;
	private int width, height;
	private FrameBuffer fbo;
	private MinimapRenderer renderer;

	private MinimapFboShader fboShader;
	private MinimapShader worldShader;

	private Texture playerPosition;
	private Texture shadowColor;

	private MinimapHandler minimap;

	public MinimapFramebuffer(int x, int y, int w, int h)
	{
		this.minimap = Ubercube.getInstance().getMinimapHandler();
		this.x = x;
		this.y = y;
		this.width = w;
		this.height = h;
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

	public void render(GuiShader shader, float scale, boolean drawClient)
	{
		glDisable(GL_DEPTH_TEST);
		fbo.bind();
		glClearColor(0.2f, 0.2f, 0.2f, 0.5f);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		worldShader.bind();
		worldShader.setProjectionMatrix(Mat4.orthographic(width, 0, 0, height, -1, 1));
		if (scale < 0)
			scale = height / 30 / 2;
		minimap.setScale(scale);
		renderer.render(worldShader, scale);
		fbo.unbind();

		shader.bind();
		shader.setUseTexture(true);
		shader.setColor(Color4f.WHITE);
		shader.setProjectionMatrix(Mat4.orthographic(Display.getInstance().getWidth(), 0, 0, Display.getInstance().getHeight(), -1, 1));
		glBindTexture(GL_TEXTURE_2D, shadowColor.getId());
		glDisable(GL_CULL_FACE);
		glBindTexture(GL_TEXTURE_2D, fbo.getColorTextureID());
		drawQuad(shader, x + width, y + height, -width, -height);
		if (drawClient)
		{
			glBindTexture(GL_TEXTURE_2D, playerPosition.getId());
			drawQuad(shader, x + width / 2 - 75, y + height / 2 - 75, 150, 150);
		}
		for (MinimapObject obj : minimap.getMinimapObjects())
		{
			float rx = obj.getMinimapCorrectedPosition().x;
			float ry = obj.getMinimapCorrectedPosition().y;

			glBindTexture(GL_TEXTURE_2D, obj.getIcon().getId());
			shader.setColor(obj.getColor());
			drawQuad(shader, x + width / 2 + rx - 10, y + height / 2 + ry - 10, 20, 20);
		}
		shader.setColor(Color4f.WHITE);
		shader.setUseTexture(false);
		glBindTexture(GL_TEXTURE_2D, 0);
		glEnable(GL_CULL_FACE);
	}

	private void drawQuad(Shader shader, float x, float y, float w, float h)
	{
		StaticPrimitive.quadPrimitive().render(shader, x + w / 2, y + h / 2, 0, w / 2, h / 2,  1);
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public void setHeight(int height) {
		this.height = height;
	}
}
