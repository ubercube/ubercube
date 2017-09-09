package fr.veridiangames.client.main.minimap;

import fr.veridiangames.client.rendering.textures.Texture;
import fr.veridiangames.core.maths.Vec2;
import fr.veridiangames.core.maths.Vec3;
import fr.veridiangames.core.utils.Color4f;

public class MinimapObject
{
	public enum MinimapObjectType
	{
		STATIC, RELATIVE
	}

	private Texture icon;
	private Vec2 position;
	private Color4f color;
	private MinimapObjectType type;

	public MinimapObject(Texture icon, Vec3 worldPosition, Color4f color, MinimapObjectType type)
	{
		this.icon = icon;
		this.position = worldPosition.xz();
		this.color = color;
		this.color.a = 0.7f;
		this.type = type;
	}

	public Color4f getColor() {
		return color;
	}

	public Texture getIcon() {
		return icon;
	}

	public Vec2 getPosition() {
		return position;
	}

	public void setColor(Color4f color) {
		this.color = color;
	}

	public void setIcon(Texture icon) {
		this.icon = icon;
	}

	public void setPosition(Vec2 position) {
		this.position = position;
	}

	public MinimapObjectType getType() {
		return type;
	}
}
