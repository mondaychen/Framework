package cs4620.splines;
import java.util.ArrayList;

import cs4620.mesh.MeshData;
import egl.NativeMem;
import egl.math.Matrix4;
import egl.math.Vector2;
import egl.math.Vector3;
import egl.math.Vector3i;
import egl.math.Vector4;


public abstract class SplineCurve {
	private float epsilon;
	
	//Spline Control Points
	private ArrayList<Vector2> controlPoints;
	
	//Bezier Curves that make up this Spline
	private ArrayList<CubicBezier> bezierCurves;
	
	//Whether or not this curve is a closed curve
	private boolean isClosed;
	
	public static final float DIST_THRESH = 0.15f;
	public static final int MIN_OPEN_CTRL_POINTS= 4,
			                           MIN_CLOSED_CTRL_POINTS= 3,
			                           MAX_CTRL_POINTS= 20;

	public SplineCurve(ArrayList<Vector2> controlPoints, boolean isClosed, float epsilon) throws IllegalArgumentException {
		if(isClosed) {
			if(controlPoints.size() < MIN_CLOSED_CTRL_POINTS)
				throw new IllegalArgumentException("Closed Splines must have at least 3 control points.");
		} else {
			if(controlPoints.size() < MIN_OPEN_CTRL_POINTS)
				throw new IllegalArgumentException("Open Splines must have at least 4 control points.");
		}

		this.controlPoints = controlPoints;
		this.isClosed = isClosed;
		this.epsilon = epsilon;
		setBeziers();
	}
	
	public boolean isClosed() {
		return this.isClosed;
	}
	
	public boolean setClosed(boolean closed) {
		if(this.isClosed && this.controlPoints.size() == 3) {
			System.err.println("You must have at least 4 control points to make an open spline.");
			return false;
		}
		this.isClosed= closed;
		setBeziers();
		return true;
	}
	
	public ArrayList<Vector2> getControlPoints() {
		return this.controlPoints;
	}
	
	public void setControlPoint(int index, Vector2 point) {
		this.controlPoints.set(index, point);
		setBeziers();
	}
	
	public boolean addControlPoint(Vector2 point) {
		if(this.controlPoints.size() == MAX_CTRL_POINTS) {
			System.err.println("You can only have "+ SplineCurve.MAX_CTRL_POINTS + " control points per spline.");
			return false;
		}
		/* point= (x0, y0), prev= (x1, y1), curr= (x2,y2)
		 * 
		 * v= [ (y2-y1), -(x2-x1) ]
		 * 
		 * r= [ (x1-x0), (y1-y0) ]
		 * 
		 * distance between point and line prev -> curr is v . r
		 */
		Vector2 curr, prev;
		Vector2 r= new Vector2(), v= new Vector2();
		float distance= Float.POSITIVE_INFINITY;
		int index= -1;
		for(int i= 0; i < controlPoints.size(); i++) {
			curr= controlPoints.get(i);
			if(i == 0) {
				if(isClosed) {
					// add line between first and last ctrl points
					prev= controlPoints.get(controlPoints.size()-1);
				} else {
					continue;
				}
			} else {
				prev= controlPoints.get(i-1);
			}
			v.set(curr.y-prev.y, -(curr.x-prev.x)); v.normalize();
			r.set(prev.x-point.x, prev.y-point.y);
			float newDist = Math.abs(v.dot(r));
			Vector2 v2 = curr.clone().sub(prev);
			v2.mul(1.0f / v2.lenSq());
			float newParam = -v2.dot(r);
			if(newDist < DIST_THRESH && newDist <= distance && 0 < newParam && newParam < 1) {
				distance= newDist;
				index= i;
			}
		}
		
		if (index >= 0) {
			controlPoints.add(index, point);
			setBeziers();
			return true;
		}
		System.err.println("Invalid location, try selecting a point closer to the spline.");
		return false;
	}
	
	public boolean removeControlPoint(int index) {
		if(this.isClosed) {
			if(this.controlPoints.size() == MIN_CLOSED_CTRL_POINTS) {
				System.err.println("You must have at least "+MIN_CLOSED_CTRL_POINTS+" for a closed Spline.");
				return false;
			}
		} else {
			if(this.controlPoints.size() == MIN_OPEN_CTRL_POINTS) {
				System.err.println("You must have at least "+MIN_OPEN_CTRL_POINTS+" for an open Spline.");
				return false;
			}
		}
		this.controlPoints.remove(index);
		setBeziers();
		return true;
	}
	
	public void modifyEpsilon(float newEps) {
		epsilon = newEps;
		setBeziers();
	}
	
	public float getEpsilon() {
		return epsilon;
	}
	
	/**
	 * Returns the sequence of 2D vertices on this Spline specified by the sequence of Bezier curves
	 */
	public ArrayList<Vector2> getPoints() {
		ArrayList<Vector2> returnList = new ArrayList<Vector2>();
		for(CubicBezier b : bezierCurves)
			for(Vector2 p : b.getPoints())
				returnList.add(p.clone());
		return returnList;
	}
	
	/**
	 * Returns the sequence of normals on this Spline specified by the sequence of Bezier curves
	 */
	public ArrayList<Vector2> getNormals() {
		ArrayList<Vector2> returnList = new ArrayList<Vector2>();
		for(CubicBezier b : bezierCurves)
			for(Vector2 p : b.getNormals())
				returnList.add(p.clone());
		return returnList;
	}
	
	/**
	 * Returns the sequence of tangents on this Spline specified by the sequence of Bezier curves
	 */
	public ArrayList<Vector2> getTangents() {
		ArrayList<Vector2> returnList = new ArrayList<Vector2>();
		for(CubicBezier b : bezierCurves)
			for(Vector2 p : b.getTangents())
				returnList.add(p.clone());
		return returnList;
	}
	
	/**
	 * Using this.controlPoints, create the CubicBezier objects that make up this curve and
	 * save them to this.bezierCurves. Assure that the order of the Bezier curves that you
	 * add to bezierCurves is the order in which the overall Spline is chained together.
	 * If the spline is closed, include additional CubicBeziers to account for this.
	 */
	private void setBeziers() {
		//TODO A5
		this.bezierCurves = new ArrayList<CubicBezier>();
		for(int i = 1; i < controlPoints.size()-2; ++i) {
			this.bezierCurves.add(this.toBezier(controlPoints.get(i-1), controlPoints.get(i),
					controlPoints.get(i+1), controlPoints.get(i+2), epsilon));
		}
		// if it is a closed curve, add 3 curves using (-3,0), (-2,1) & (-1,2)
		if(isClosed) {
			int pointsSize = controlPoints.size();
			this.bezierCurves.add(this.toBezier(controlPoints.get(pointsSize-3), controlPoints.get(pointsSize-2),
					controlPoints.get(pointsSize-1), controlPoints.get(0), epsilon));
			this.bezierCurves.add(this.toBezier(controlPoints.get(pointsSize-2), controlPoints.get(pointsSize-1),
					controlPoints.get(0), controlPoints.get(1), epsilon));
			this.bezierCurves.add(this.toBezier(controlPoints.get(pointsSize-1), controlPoints.get(0),
					controlPoints.get(1), controlPoints.get(2), epsilon));
		}
	}
	
	/**
	 * Reverses the tangents and normals associated with this Spline
	 */
	public void reverseNormalsAndTangents() {
		for(CubicBezier b : bezierCurves) {
			for(Vector2 p : b.getNormalReferences())
				p.mul(-1);
			for(Vector2 p : b.getTangentReferences())
				p.mul(-1);
		}
	}
	
	//Debug code
	public double getMaxAngle() {
		ArrayList<Vector2> myPoints = getPoints();
		double max = 0;
		for(int i = 0; i < myPoints.size() - 2; ++i) {
			Vector2 A = myPoints.get(i);
			Vector2 B = myPoints.get(i+1);
			Vector2 C = myPoints.get(i+2);
			
			Vector2 v1 = B.clone().sub(A);
			Vector2 v2 = C.clone().sub(B);
			
			v1.normalize();
			v2.normalize();
			
			double cur = Math.acos(v1.dot(v2));
			if (cur > max) max = cur;
		}
		return max;
	}
	
	
	public abstract CubicBezier toBezier(Vector2 p0, Vector2 p1, Vector2 p2, Vector2 p3, float eps);
	
	
	/**
	 * Given a curve that defines the cross section along the axis, fill the three GLBuffer objects appropriately.
	 * Here, we revolve the crossSection curve about the positive Z-axis.
	 * @param crossSection, the 2D spline for which every point defines the cross section of the surface
	 * @param data, a MeshData where we will output our triangle mesh
	 * @param scale > 0, parameter that controls how much the resulting surface should be scaled
	 * @param sliceTolerance > 0, the maximum angle in radians between adjacent vertical slices.
	 */
	public static void build3DRevolution(SplineCurve crossSection, MeshData data, float scale, float sliceTolerance) {
		//TODO A5
		
		/* Initialize the buffers for data.positions, data.normals, data.indices, and data.uvs as
		 * you did for A1.  Although you will not be using uv's, you DO need to initialize the
		 * buffer with space.  Don't forget to initialize data.indexCount and data.vertexCount.
		 * 
		 * Then set the data of positions / normals / indices with what you have calculated.
		 */
		ArrayList<Vector2> csPoints = crossSection.getPoints();
		ArrayList<Vector2> csNormals = crossSection.getNormals();
		if (crossSection.isClosed) {
			csPoints.add(csPoints.get(0).clone());
			csNormals.add(csNormals.get(0).clone());
		}
		
		int circleDivision = (int)(Math.PI*2/sliceTolerance) + 1;
		float slice = (float)Math.PI*2 / circleDivision;
		
		ArrayList<Vector3> positions = new ArrayList<>();
		ArrayList<Vector3> normals = new ArrayList<>();
		
		// compute circle by circle
		for (int i = 0; i < csPoints.size(); i++) {
			Vector4 point = new Vector4(csPoints.get(i).x * scale, 0, csPoints.get(i).y * scale, 1);
			Vector4 normal = new Vector4(csNormals.get(i).x, 0, csNormals.get(i).y, 0);
			
			for (int j = 0; j <= circleDivision; j++) {
				float angel = slice * j;
				Matrix4 transformer = Matrix4.createRotationZ(angel);
				Vector4 p = point.clone();
				Vector4 n = normal.clone();
				transformer.mul(p);
				transformer.mul(n);
				positions.add(new Vector3(p.x, p.y, p.z));
				normals.add(new Vector3(n.x, n.y, n.z));
			}
		}
		data.vertexCount = positions.size();
		data.indexCount = (csPoints.size() - 1) * circleDivision * 2 * 3;
		// Create Storage Spaces
		data.positions = NativeMem.createFloatBuffer(data.vertexCount * 3);
		data.uvs = NativeMem.createFloatBuffer(data.vertexCount * 2);
		data.normals = NativeMem.createFloatBuffer(data.vertexCount * 3);
		data.indices = NativeMem.createIntBuffer(data.indexCount);
		for (Vector3 p: positions) {
			data.positions.put(p.x);
			data.positions.put(p.y);
			data.positions.put(p.z);
		}
		for (Vector3 n: normals) {
			data.normals.put(n.x);
			data.normals.put(n.y);
			data.normals.put(n.z);
		}
		// Create The Indices
		for (int i = 0; i < csPoints.size() - 1; i++) {
			for (int j = 0; j < circleDivision; j++) {
				int index = i * (circleDivision + 1) + j;
				data.indices.put(index);
				data.indices.put(index + circleDivision + 2);
				data.indices.put(index + circleDivision + 1);
				data.indices.put(index);
				data.indices.put(index + 1);
				data.indices.put(index + circleDivision + 2);
			}
		}

	}
}

