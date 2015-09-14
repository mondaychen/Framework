package cs4620.ray1.surface;

import java.util.ArrayList;

import cs4620.mesh.MeshData;
import cs4620.mesh.OBJParser;
import cs4620.ray1.IntersectionRecord;
import cs4620.ray1.Ray;
import cs4620.ray1.RayTracer;
import egl.math.Vector2d;
import egl.math.Vector3d;
import egl.math.Vector3i;

/**
 * An interface between a MeshData and the ray tracer. When the Scene calls
 * appendRenderableSurfaces on this object, it appends all Triangles on the
 * mesh onto the given ArrayList. This way, the Scene has direct access to
 * all intersectable Surfaces in the scene.
 * 
 * @author eschweic
 *
 */
public class Mesh extends Surface {

	/** The underlying data of this Mesh. */
	private MeshData mesh = null;

	/**
	 * Default constructor; creates an empty mesh.
	 */
	public Mesh() { }

	/**
	 * Construct a Mesh from an existing MeshData.
	 * @param newMesh an existing MeshData.
	 */
	public Mesh(MeshData newMesh) {
		mesh = newMesh;
	}
	
	/**
	 * Set the data in this mesh to the data of a mesh on disk.
	 * @param fileName the name of a .obj file on disk.
	 */
	public void setData(String fileName) {
		System.out.println("Loading " + RayTracer.sceneWorkspace.resolve(fileName));
		mesh = (OBJParser.parse(RayTracer.sceneWorkspace.resolve(fileName))).flatten();
	}
	
	public boolean intersect(IntersectionRecord outRecord, Ray rayIn) {	return false; }

	public void appendRenderableSurfaces (ArrayList<Surface> in) {
		for (int i=0; i<mesh.indexCount/3; i++) {
			Vector3i triVec = new Vector3i(mesh.indices.get(3*i),
										   mesh.indices.get(3*i+1),
										   mesh.indices.get(3*i+2));
			Triangle t = new Triangle(this, new Vector3i(triVec), shader);
			in.add(t);
		}
	}
	
	/**
	 * @return True if the mesh has per-vertex normals specified.
	 */
	public boolean hasNormals() {
		return mesh.hasNormals();
	}
	
	/**
	 * @return True if the mesh has per-vertex UV coordinates.
	 */
	public boolean hasUVs() {
		return mesh.hasUVs();
	}
	
	/**
	 * Get the position of a vertex.
	 * @param index The vertex index.
	 * @return The position of the specified vertex.
	 */
	public Vector3d getPosition(int index) {
		return new Vector3d(mesh.positions.get(3*index),
							mesh.positions.get(3*index+1),
							mesh.positions.get(3*index+2));
	}
	
	/**
	 * Get the UV-coordinates of a vertex, assuming the coordinates exist.
	 * @param index The vertex index.
	 * @return The UV-coordinates of the specified vertex.
	 */
	public Vector2d getUV(int index) {
		return new Vector2d(mesh.uvs.get(2*index),
							mesh.uvs.get(2*index+1));
	}
	
	/**
	 * Get the normal of a vertex, assuming it exists.
	 * @param index The vertex index.
	 * @return The normal at the specified vertex.
	 */
	public Vector3d getNormal(int index) {
		return new Vector3d(mesh.normals.get(3*index),
							mesh.normals.get(3*index+1),
							mesh.normals.get(3*index+2));
	}
	
}
