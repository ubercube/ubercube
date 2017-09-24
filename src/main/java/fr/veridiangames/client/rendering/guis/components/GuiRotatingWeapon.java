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
import fr.veridiangames.core.game.entities.weapons.Weapon;
import fr.veridiangames.core.maths.Mat4;
import fr.veridiangames.core.maths.Vec3;
import fr.veridiangames.core.utils.Color4f;
import org.lwjgl.opengl.GL11;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created by Tybau on 23/09/2017.
 */
public class GuiRotatingWeapon extends GuiComponent
{
	private FrameBuffer modelFbo;
	private WeaponShader weaponShader;
	private OBJModel model;
	private Weapon weapon;

	private float rotate;

	public GuiRotatingWeapon(int x, int y, int w, int h, Color4f color, Weapon weapon)
	{
		super(x, y, w, h, color);

		this.weapon = weapon;
		this.modelFbo = new FrameBuffer(w, h);
		this.weaponShader = new WeaponShader();

		this.model = OBJModel.getModel(this.weapon.getModel());
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
		this.createFbo();

		shader.bind();
		shader.setColor(Color4f.WHITE);
		shader.setUseTexture(true);
		glBindTexture(GL_TEXTURE_2D, this.modelFbo.getColorTextureID());

		StaticPrimitive.quadPrimitive().render(shader, x + w / 2, y + h / 2, 0, w / 2, h / 2,  1);

		glBindTexture(GL_TEXTURE_2D, 0);
		shader.setUseTexture(false);
		shader.setColor(Color4f.WHITE);
	}

	private void createFbo()
	{
		this.modelFbo.bind();
		glEnable(GL_DEPTH_TEST);
		glClearColor(color.r, color.g, color.b, 0.7f);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

		Mat4 r = Mat4.rotate(0, rotate, 0).mul(weapon.getCenterPosition().toMatrix());

		weaponShader.bind();
		weaponShader.setShaderBase(Mat4.perspective(50.0f, w / h, 0.1f, 100.0f), new Vec3(0, 0, 0), 100.0f);
		weaponShader.setModelViewMatrix(Mat4.translate(0, 0, 5).mul(Mat4.scale(weapon.getPreviewScale()).mul(r)));

		Renderer.bindTextureCube(512);
		glDisable(GL11.GL_CULL_FACE);

		model.render();

		glEnable(GL11.GL_CULL_FACE);
		Renderer.bindTextureCube(0);

		weaponShader.unbind();

		glDisable(GL_DEPTH_TEST);
		this.modelFbo.unbind();
	}

	@Override
	public void dispose()
	{

	}
}
