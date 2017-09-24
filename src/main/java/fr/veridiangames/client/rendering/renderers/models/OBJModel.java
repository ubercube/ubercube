package fr.veridiangames.client.rendering.renderers.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import fr.veridiangames.core.game.entities.Model;
import fr.veridiangames.core.maths.Vec2;
import fr.veridiangames.core.maths.Vec3;
import fr.veridiangames.core.utils.Color4f;
import org.lwjgl.opengl.GL11;

import static fr.veridiangames.client.Resource.getResource;

public class OBJModel {
	public static final OBJModel GRENADE_RENDERER = ObjModelLoader.loadModel(getResource("weapons/GRENADE.obj"));
	public static final OBJModel AK47_RENDERER = ObjModelLoader.loadModel(getResource("weapons/ak47/AK47.obj"));
	public static final OBJModel AWP_RENDERER = ObjModelLoader.loadModel(getResource("weapons/awp/AWP.obj"));
	public static final OBJModel SHOVEL_RENDERER = ObjModelLoader.loadModel(getResource("weapons/SHOVEL.obj"));
	public static final OBJModel MEDICBAG_RENDERER = ObjModelLoader.loadModel(getResource("weapons/medicbag/MEDICBAG.obj"));

	public static OBJModel getModel(int id)
	{
		switch (id)
		{
			case Model.AK47:
				return AK47_RENDERER;
			case Model.AWP:
				return AWP_RENDERER;
			case Model.GRENADE:
				return GRENADE_RENDERER;
			case Model.MEDICBAG:
				return MEDICBAG_RENDERER;
			case Model.SHOVEL:
				return SHOVEL_RENDERER;
		}
		return null;
	}

	private List<Vec3> verticesList = new ArrayList<Vec3>();
	private List<Vec3> normalsList = new ArrayList<Vec3>();
	private List<OBJIndex> indicesList = new ArrayList<OBJIndex>();
	private HashMap<String, Color4f> colorMap = new HashMap<>();

	private Mesh mesh;

	public void parseModel() {
		Vertex[] r = new Vertex[indicesList.size()];

		Vec3[] nPositions = new Vec3[indicesList.size()];
		Vec3[] nNormals = new Vec3[indicesList.size()];
		int[] nIndices = new int[indicesList.size()];

		for (int i = 0; i < r.length; i++) {
			OBJIndex index = indicesList.get(i);
			nPositions[i] = new Vec3(verticesList.get(index.positionIndex));
			nNormals[i] = new Vec3(normalsList.get(index.normalIndex));
			nIndices[i] = i;
		}

		for (int i = 0; i < r.length; i++) {
			OBJIndex index = indicesList.get(i);
			r[i] = new Vertex(verticesList.get(index.positionIndex), colorMap.get(index.material), normalsList.get(index.normalIndex));
		}

		mesh = new Mesh(r, nIndices);

		verticesList.clear();
		normalsList.clear();
		indicesList.clear();
	}

	public void render() {
		mesh.render();
	}

	public void setVertices(List<Vec3> vertices) {
		verticesList = vertices;
	}

	public void setIndices(List<OBJIndex> indices) {
		indicesList = indices;
	}


	public void setNormals(List<Vec3> normals) {
		normalsList = normals;
	}

	public void setColorMap(HashMap<String, Color4f> colorMap) {
		this.colorMap = colorMap;
	}

	public Mesh getMesh() {
		return mesh;
	}
}
