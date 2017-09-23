package fr.veridiangames.client.rendering.guis.components;

import fr.veridiangames.client.Resource;
import fr.veridiangames.client.rendering.Display;
import fr.veridiangames.client.rendering.guis.GuiComponent;
import fr.veridiangames.client.rendering.guis.primitives.StaticPrimitive;
import fr.veridiangames.client.rendering.renderers.Renderer;
import fr.veridiangames.client.rendering.renderers.game.minimap.MinimapRenderer;
import fr.veridiangames.client.rendering.renderers.game.world.WorldRenderer;
import fr.veridiangames.client.rendering.renderers.models.Mesh;
import fr.veridiangames.client.rendering.renderers.models.OBJModel;
import fr.veridiangames.client.rendering.shaders.*;
import fr.veridiangames.client.rendering.textures.CubeMap;
import fr.veridiangames.client.rendering.textures.FrameBuffer;
import fr.veridiangames.client.rendering.textures.TextureLoader;
import fr.veridiangames.core.maths.Mat4;
import fr.veridiangames.core.maths.Vec3;
import fr.veridiangames.core.utils.Color4f;
import org.lwjgl.opengl.GL11;

import static org.lwjgl.opengl.GL11.*;

public class GuiRotatingModel extends GuiComponent
{
	private FrameBuffer modelFbo;
	private WeaponShader weaponShader;
	private OBJModel model;

	private float rotate;

	public GuiRotatingModel(int x, int y, int w, int h, Color4f color, OBJModel model)
	{
		super(x, y, w, h, color);

		this.model = model;
		this.modelFbo = new FrameBuffer(w, h);
		this.weaponShader = new WeaponShader();
	}

	@Override
	public void update()
	{
		super.update();
		rotate += 0.5f;
	}

	@Override
	public void render(GuiShader shader)
	{
		this.modelFbo.bind();
		glEnable(GL_DEPTH_TEST);
		glClearColor(0.2f, 0.2f, 0.2f, 0.5f);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

		Mat4 r = Mat4.rotate(0, rotate, 0);


		weaponShader.bind();
		weaponShader.setShaderBase(Mat4.perspective(70.0f, w / h, 0.1f, 100.0f), new Vec3(0, 0, 0), 100.0f);
		weaponShader.setModelViewMatrix(Mat4.translate(0, 0, 5).mul(r));

		Renderer.bindTextureCube(512);
		glDisable(GL11.GL_CULL_FACE);

		model.render();

		glEnable(GL11.GL_CULL_FACE);
		Renderer.bindTextureCube(0);

		weaponShader.unbind();

		glDisable(GL_DEPTH_TEST);
		this.modelFbo.unbind();


		shader.bind();
		shader.setColor(Color4f.WHITE);
		shader.setUseTexture(true);
		glBindTexture(GL_TEXTURE_2D, this.modelFbo.getColorTextureID());
		drawQuad(shader, x, y, w, h);
		glBindTexture(GL_TEXTURE_2D, 0);
		shader.setUseTexture(false);
		shader.setColor(Color4f.WHITE);
	}

	private void drawQuad(Shader shader, float x, float y, float w, float h)
	{
		StaticPrimitive.quadPrimitive().render(shader, x + w / 2, y + h / 2, 0, w / 2, h / 2,  1);
	}

	@Override
	public void dispose()
	{

	}
}
