package fr.veridiangames.client.rendering.renderers.models;

import fr.veridiangames.core.maths.Vec3;

public class Vertex {
	public static final int BUFFER_SIZE = 3 + 3;
	
	private Vec3 pos;
	private Vec3 normal;

	public Vertex(Vec3 pos, Vec3 normal) {
		this.pos = new Vec3(pos);
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
}
