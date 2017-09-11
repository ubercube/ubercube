package fr.veridiangames.client.main.minimap;

import fr.veridiangames.client.Ubercube;
import fr.veridiangames.client.rendering.textures.Texture;
import fr.veridiangames.core.maths.Vec2;
import fr.veridiangames.core.maths.Vec3;
import fr.veridiangames.core.utils.Color4f;

import static fr.veridiangames.core.maths.Mathf.cos;
import static fr.veridiangames.core.maths.Mathf.sin;

public class MinimapObject
{
	public enum MinimapObjectType
	{
		STATIC, RELATIVE
	}

	private MinimapHandler minimap;
	private Texture icon;
	private Vec2 position;
	private Color4f color;
	private MinimapObjectType type;

	private Vec2 minimapCorrectPosition;

	public MinimapObject(MinimapHandler minimap, Texture icon, Vec3 worldPosition, Color4f color, MinimapObjectType type)
	{
		this.icon = icon;
		this.position = worldPosition.xz();
		this.color = color;
		this.color.a = 0.7f;
		this.type = type;
		this.minimap = minimap;
		this.minimapCorrectPosition = new Vec2();
	}

	public void update(Vec3 playerPosition, float playerYRotation)
	{
		Vec2 p = playerPosition.xz();
		float yRot = playerYRotation;
		float scale = minimap.getScale();
		float width = minimap.getSize().x;
		float height = minimap.getSize().y;

		float relx = p.x;
		float rely = p.y;

		if (type == MinimapObject.MinimapObjectType.RELATIVE)
			relx = rely = -0;

		float x = -(relx - position.x) * scale;
		float y = (rely - position.y) * scale;

		float rx = (x * sin(yRot) + y * cos(yRot));
		float ry = (y * sin(yRot) - x * cos(yRot));

		float padding = 5;
		if (ry < -height / 2 + padding)
		{
			float a = -rx;
			float b = -ry;
			float c = -height / 2 + padding;
			rx = c * (a / b);
			ry = -height / 2 + padding;
		}
		if (ry > height / 2 - padding)
		{
			float a = -rx;
			float b = -ry;
			float c = height / 2 - padding;
			rx = c * (a / b);
			ry = height / 2 - padding;
		}
		if (rx < -width / 2 + padding)
		{
			float a = -ry;
			float b = -rx;
			float c = -width / 2 + padding;
			rx = -width / 2 + padding;
			ry = c * (a / b);
		}
		if (rx > width / 2 - padding)
		{
			float a = -ry;
			float b = -rx;
			float c = width / 2 - padding;
			rx = width / 2 - padding;
			ry = c * (a / b);
		}
		minimapCorrectPosition.x = rx;
		minimapCorrectPosition.y = ry;
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

	public Vec2 getMinimapCorrectedPosition() {
		return minimapCorrectPosition;
	}
}
