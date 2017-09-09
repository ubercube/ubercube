package fr.veridiangames.client.rendering.renderers.models;

import fr.veridiangames.core.maths.Vec3;
import fr.veridiangames.core.utils.Color4f;

public class Vertex {
	public static final int BUFFER_SIZE = 3 + 4 + 3;
	
	private Vec3 pos;
	private Color4f color;
	private Vec3 normal;

	public Vertex(Vec3 pos, Color4f color, Vec3 normal) {
		this.pos = new Vec3(pos);
		this.color = color.copy();
		this.normal = new Vec3(normal);
	}

	public Vec3 getPosition() {
		return pos;
	}

	public void setPosition(Vec3 pos) {
		this.pos = pos;
	}

	public Vec3 getNormal() {
		return normal;
	}

	public void setNormal(Vec3 normal) {
		this.normal = normal;
	}

	public Color4f getColor() {
		return color;
	}

	public void setColor(Color4f color) {
		this.color = color;
	}
}
