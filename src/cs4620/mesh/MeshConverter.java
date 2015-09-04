package cs4620.mesh;

import java.util.ArrayList;

import egl.NativeMem;
import egl.math.Vector3;
import egl.math.Vector3i;

/**
 * Performs Normals Reconstruction Upon A Mesh Of Positions
 * @author Cristian
 *
 */
public class MeshConverter {
	/**
	 * Reconstruct A Mesh's Normals So That It Appears To Have Sharp Creases
	 * @param positions List Of Positions
	 * @param tris List Of Triangles (A Group Of 3 Values That Index Into The Positions List)
	 * @return A Mesh With Normals That Lie Normal To Faces
	 */
	public static MeshData convertToFaceNormals(ArrayList<Vector3> positions, ArrayList<Vector3i> tris) {
		MeshData data = new MeshData();

		// Notice
		System.out.println("This Feature Has Been Removed For The Sake Of Assignment Consistency");
		System.out.println("This Feature Will Be Added In A Later Assignment");
		
		// Please Do Not Fill In This Function With Code
		
		// After You Turn In Your Assignment, Chuck Norris Will
		// Substitute This Function With His Fiery Will Of Steel
		
		// TODO#A1 SOLUTION START
				
		// #SOLUTION END

		return data;
	}
	/**
	 * Reconstruct A Mesh's Normals So That It Appears To Be Smooth
	 * @param positions List Of Positions
	 * @param tris List Of Triangles (A Group Of 3 Values That Index Into The Positions List)
	 * @return A Mesh With Normals That Extrude From Vertices
	 */
	public static MeshData convertToVertexNormals(ArrayList<Vector3> positions, ArrayList<Vector3i> tris) {
		MeshData data = new MeshData();

		// #A1 SOLUTION START
		data.vertexCount = positions.size();
		data.indexCount = tris.size() * 3;

		// Create Storage Spaces
		data.positions = NativeMem.createFloatBuffer(data.vertexCount * 3);
		data.normals = NativeMem.createFloatBuffer(data.vertexCount * 3);
		data.indices = NativeMem.createIntBuffer(data.indexCount);

		for (Vector3 vector: positions) {
			data.positions.put(vector.x);
			data.positions.put(vector.y);
			data.positions.put(vector.z);
		}

		ArrayList<Vector3> normals = new ArrayList<>();
		for (Vector3 vector: positions) {
			normals.add(new Vector3(0, 0, 0));
		}
		for (Vector3i tri: tris) {
			Vector3 v1 = positions.get(tri.x);
			Vector3 v2 = positions.get(tri.y);
			Vector3 v3 = positions.get(tri.z);
			Vector3 triNormal = v1.clone().sub(v2).cross(v1.clone().sub(v3));
			normals.get(tri.x).add(triNormal);
			normals.get(tri.y).add(triNormal);
			normals.get(tri.z).add(triNormal);
		}
		for (Vector3 normal: normals) {
			normal.normalize();
			data.normals.put(normal.x);
			data.normals.put(normal.y);
			data.normals.put(normal.z);
		}
		for (Vector3i tri: tris) {
			data.indices.put(tri.x);
			data.indices.put(tri.y);
			data.indices.put(tri.z);
		}
		
		// #SOLUTION END
		
		return data;
	}
}
