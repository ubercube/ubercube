package fr.veridiangames.client.rendering.renderers.models;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import fr.veridiangames.core.maths.Vec3;
import fr.veridiangames.core.utils.Log;

public class ObjModelLoader {
	
	public static OBJModel loadModel(String modelFile) {
		Log.println("Loading model: [" + modelFile + "]");
		
		List<Vec3> position = new ArrayList<Vec3>();
		List<OBJIndex> indices = new ArrayList<OBJIndex>();
		List<Vec3> normals = new ArrayList<Vec3>();

		try {
			BufferedReader reader = new BufferedReader(new FileReader(modelFile));
			String line = "";
			while ((line = reader.readLine()) != null) {
				String[] value = line.split(" ");
				String prefix = value[0];

				if (prefix.equals("v")) {
					float x = Float.valueOf(value[1]);
					float y = Float.valueOf(value[2]);
					float z = Float.valueOf(value[3]);

					position.add(new Vec3(x, y, z));
				} else if (prefix.equals("vn")) {
					float x = Float.valueOf(value[1]);
					float y = Float.valueOf(value[2]);
					float z = Float.valueOf(value[3]);

					normals.add(new Vec3(x, y, z));
				} else if (prefix.equals("f")) {
					OBJIndex v1 = new OBJIndex();
					v1.positionIndex = Integer.parseInt(value[1].split("/")[0]) - 1;
					v1.normalIndex = Integer.parseInt(value[1].split("/")[2]) - 1;
					indices.add(v1);

					OBJIndex v2 = new OBJIndex();
					v2.positionIndex = Integer.parseInt(value[2].split("/")[0]) - 1;
					v2.normalIndex = Integer.parseInt(value[2].split("/")[2]) - 1;
					indices.add(v2);

					OBJIndex v3 = new OBJIndex();
					v3.positionIndex = Integer.parseInt(value[3].split("/")[0]) - 1;
					v3.normalIndex = Integer.parseInt(value[3].split("/")[2]) - 1;
					indices.add(v3);
				}
			}
			reader.close();
		} catch (IOException e) {
			Log.println("[ERROR]: unable to load model ! [" + modelFile + "]");
			e.printStackTrace();
		}

		OBJModel model = new OBJModel();
		model.setVertices(position);
		model.setIndices(indices);
		model.setNormals(normals);
		model.parseModel();

		position.clear();
		indices.clear();
		normals.clear();
		
		Log.println("Model loaded !");
		return model;
	}
}
