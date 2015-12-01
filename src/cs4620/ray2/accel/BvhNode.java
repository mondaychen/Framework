package cs4620.ray2.accel;

import cs4620.ray2.Ray;
import egl.math.Vector3d;

/**
 * A class representing a node in a bounding volume hierarchy.
 * 
 * @author pramook 
 */
public class BvhNode {

	/** The current bounding box for this tree node.
	 *  The bounding box is described by 
	 *  (minPt.x, minPt.y, minPt.z) - (maxBound.x, maxBound.y, maxBound.z).
	 */
	public final Vector3d minBound, maxBound;
	
	/**
	 * The array of children.
	 * child[0] is the left child.
	 * child[1] is the right child.
	 */
	public final BvhNode child[];

	/**
	 * The index of the first surface under this node. 
	 */
	public int surfaceIndexStart;
	
	/**
	 * The index of the surface next to the last surface under this node.	 
	 */
	public int surfaceIndexEnd; 
	
	/**
	 * Default constructor
	 */
	public BvhNode()
	{
		minBound = new Vector3d();
		maxBound = new Vector3d();
		child = new BvhNode[2];
		child[0] = null;
		child[1] = null;		
		surfaceIndexStart = -1;
		surfaceIndexEnd = -1;
	}
	
	/**
	 * Constructor where the user can specify the fields.
	 * @param minBound
	 * @param maxBound
	 * @param leftChild
	 * @param rightChild
	 * @param start
	 * @param end
	 */
	public BvhNode(Vector3d minBound, Vector3d maxBound, BvhNode leftChild, BvhNode rightChild, int start, int end) 
	{
		this.minBound = new Vector3d();
		this.minBound.set(minBound);
		this.maxBound = new Vector3d();
		this.maxBound.set(maxBound);
		this.child = new BvhNode[2];
		this.child[0] = leftChild;
		this.child[1] = rightChild;		   
		this.surfaceIndexStart = start;
		this.surfaceIndexEnd = end;
	}
	
	/**
	 * @return true if this node is a leaf node
	 */
	public boolean isLeaf()
	{
		return child[0] == null && child[1] == null; 
	}
	
	/** 
	 * Check if the ray intersects the bounding box.
	 * @param ray
	 * @return true if ray intersects the bounding box
	 */
	public boolean intersects(Ray ray) {
		// TODO#A7: fill in this function.
		
		// http://www.cs.utah.edu/~awilliam/box/box.pdf
		double tmin,tmax,tymin,tymax,tzmin,tzmax;
		Vector3d inv_direction = new Vector3d(1/ray.direction.x,
				1/ray.direction.y, 1/ray.direction.z);
		Vector3d[] bounds = {this.minBound, this.maxBound};
		int[] sign = {
				inv_direction.x < 0 ? 0 : 1,
				inv_direction.y < 0 ? 0 : 1,
				inv_direction.z < 0 ? 0 : 1
		};
		tmin=(bounds[sign[0]].x-ray.origin.x)*inv_direction.x;
		tmax=(bounds[1-sign[0]].x-ray.origin.x)*inv_direction.x;
		tymin=(bounds[sign[1]].y-ray.origin.y)*inv_direction.y;
		tymax=(bounds[1-sign[1]].y-ray.origin.y)*inv_direction.y;
		if((tmin>tymax)||(tymin>tmax))
			return false;
		if(tymin>tmin)
			tmin=tymin;
		if(tymax<tmax)
			tmax=tymax;
		tzmin=(bounds[sign[2]].z-ray.origin.z)*inv_direction.z;
		tzmax=(bounds[1-sign[2]].z-ray.origin.z)*inv_direction.z;
		if((tmin>tzmax)||(tzmin>tmax))
			return false;
		if(tzmin>tmin)
			tmin=tzmin;
		if(tzmax<tmax)
			tmax=tzmax;
		
		//return ((tmin<t1)&&(tmax>t0));
		return tmin < tmax;
	}
}
