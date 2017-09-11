package fr.veridiangames.client.rendering.guis.components;


import fr.veridiangames.client.rendering.guis.GuiComponent;
import fr.veridiangames.client.rendering.renderers.game.minimap.MinimapFramebuffer;
import fr.veridiangames.client.rendering.shaders.GuiShader;
import fr.veridiangames.core.utils.Color4f;

public class GuiMinimap extends GuiComponent
{

	private MinimapFramebuffer minimap;
	private float scale;
	private boolean drawClient;

	public GuiMinimap(int x, int y, int w, int h)
	{
		super(x, y, w, h, Color4f.WHITE);
		setOrigin(GuiComponent.GuiOrigin.A);
		this.minimap = new MinimapFramebuffer(x, y, w, h);
		this.scale = -1;
		this.drawClient = true;
	}

	public void update()
	{
		super.update();
		this.minimap.setX(x);
		this.minimap.setY(y);
		this.minimap.update();
	}

	public void render(GuiShader shader)
	{
		shader.setColor(color);
		this.minimap.render(shader, scale, drawClient);
	}

	public void dispose()
	{

	}

	public void setScale(float scale)
	{
		this.scale = scale;
	}

	public float getScale()
	{
		return scale;
	}

	public boolean isDrawClient()
	{
		return drawClient;
	}

	public void setDrawClient(boolean drawClient)
	{
		this.drawClient = drawClient;
	}
}
