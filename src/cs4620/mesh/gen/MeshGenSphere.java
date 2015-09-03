package cs4620.mesh.gen;

import java.util.ArrayList;
import java.util.Vector;

import cs4620.mesh.MeshData;
import egl.NativeMem;
import egl.math.Vector2;
import egl.math.Vector3;

/**
 * Generates A Sphere Mesh
 * @author Cristian
 *
 */
public class MeshGenSphere extends MeshGenerator {
	@Override
	public void generate(MeshData outData, MeshGenOptions opt) {
		// SOLUTION START
		// Calculate Vertex And Index Count
		int m = opt.divisionsLatitude;
		int n = opt.divisionsLongitude;
		outData.vertexCount = n * (m + 1);
		outData.indexCount = (m - 1) * n * 2 + 2 * n;

		// Create Storage Spaces
		outData.positions = NativeMem.createFloatBuffer(outData.vertexCount * 3);
		outData.uvs = NativeMem.createFloatBuffer(outData.vertexCount * 2);
		outData.normals = NativeMem.createFloatBuffer(outData.vertexCount * 3);
		outData.indices = NativeMem.createIntBuffer(outData.indexCount);

		// getting points on the Greenwich meridian (0,y,z); size = m + 1
		ArrayList<Vector3> meridianGW = new ArrayList<>();
		for (Vector2 v2: generatePointsInCircle(m * 2, 1)) {
			// only the positive half, including north and south pole
			if (v2.x < 0) {
				break;
			}
			meridianGW.add(new Vector3(0.f, v2.y, v2.x));
		}

		// Create The Vertices
		// fixed y, using radius=z to calculate x and z; each size = n
		// old use the first half
		for (Vector3 vector: meridianGW) {
			ArrayList<Vector2> position2d = generatePointsInCircle(n, vector.z);
			for (Vector2 v2: position2d) {
				outData.positions.put(v2.x);
				outData.positions.put(vector.y);
				outData.positions.put(-v2.y);
			}
		}
		
		// Create The Indices
				
		// #SOLUTION END
	}

	private ArrayList<Vector2> generatePointsInCircle(int count, float radius) {
		ArrayList<Vector2> result = new ArrayList<Vector2>();
		for (int i = 0; i < count; i++) {
			float x = round(Math.sin(Math.PI * 2 / count * i), 5) * radius;
			float y = round(Math.cos(Math.PI * 2 / count * i), 5) * radius;
			result.add(new Vector2(x, y));
		}

		return result;
	}

	private static float round(double v, int scale) {
		String temp="0.";
		for (int i = 0; i < scale; i++)
		{
			temp += "0";
		}
		return Float.valueOf(new java.text.DecimalFormat(temp).format(v));
	}
}
