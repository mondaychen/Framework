package cs4620.splines;

import java.util.ArrayList;

import egl.math.Vector2;
/*
 * Cubic Bezier class for the splines assignment
 */

public class CubicBezier {
	
	//This Bezier's control points
	public Vector2 p0, p1, p2, p3;
	
	//Control parameter for curve smoothness
	float epsilon;
	
	//The points on the curve represented by this Bezier
	private ArrayList<Vector2> curvePoints;
	
	//The normals associated with curvePoints
	private ArrayList<Vector2> curveNormals;
	
	//The tangent vectors of this bezier
	private ArrayList<Vector2> curveTangents;
	
	
	/**
	 * 
	 * Cubic Bezier Constructor
	 * 
	 * Given 2-D BSpline Control Points correctly set self.{p0, p1, p2, p3},
	 * self.uVals, self.curvePoints, and self.curveNormals
	 * 
	 * @param bs0 First Bezier Spline Control Point
	 * @param bs1 Second Bezier Spline Control Point
	 * @param bs2 Third Bezier Spline Control Point
	 * @param bs3 Fourth Bezier Spline Control Point
	 * @param eps Maximum angle between line segments
	 */
	public CubicBezier(Vector2 p0, Vector2 p1, Vector2 p2, Vector2 p3, float eps) {
		curvePoints = new ArrayList<Vector2>();
		curveTangents = new ArrayList<Vector2>();
		curveNormals = new ArrayList<Vector2>();
		epsilon = eps;
		
		this.p0 = new Vector2(p0);
		this.p1 = new Vector2(p1);
		this.p2 = new Vector2(p2);
		this.p3 = new Vector2(p3);
		
		tessellate();
	}
	

    /**
     * Approximate a Bezier segment with a number of vertices, according to an appropriate
     * smoothness criterion for how many are needed.  The points on the curve are written into the
     * array self.curvePoints, the tangents into self.curveTangents, and the normals into self.curveNormals.
     * The final point, p3, is not included, because cubic Beziers will be "strung together".
     */
    private void tessellate() {
    	 // TODO A5
    	//SOLUTION
    	//Compute curve points and tangents
    	tessellateHelper(p0, p1, p2, p3, 0, this.curvePoints, this.curveTangents);  	
    	//Compute normals using tangents and curve points
    	computeNormals(this.curveTangents, this.curveNormals);
    	//END SOLUTION
    	//END SOLUTION
    }
    
    private void computeNormals(ArrayList<Vector2> tangents, ArrayList<Vector2> outNormals) {    	

    	for(int i = 0; i < tangents.size(); ++i) {
    		Vector2 tangent = tangents.get(i);
    		
    		Vector2 normal = new Vector2(tangent.y / tangent.len(), -tangent.x / tangent.len());
    		outNormals.add(normal);
    	}
    }
    
    /**
     * Recursive helper for adaptive curve tessellation.  Tessellate the segment with the
     * given control points, proceeding by recursive subdivision until the sharpest angle in
     * the control polygon is below epsilon.  The points on the curve are written into the
     * array outPoints, and output tangents are written to outTangents.
     */
    private static final int MAX_LEVEL = 10;
    private void tessellateHelper(Vector2 p0, Vector2 p1, Vector2 p2, Vector2 p3, int level,
    								ArrayList<Vector2> outPoints, ArrayList<Vector2> outTangents) {

		// the three segments of the control polygon
		Vector2 v0 = p1.clone().sub(p0);
		Vector2 v1 = p2.clone().sub(p1);
		Vector2 v2 = p3.clone().sub(p2);

    	// find the maximum of the two angles between the segments.
    	float maxAngle = Math.max(Math.abs(v0.angle(v1)), Math.abs(v1.angle(v2)));

    	// Subdivide further if the angle is too high, but stop after a fixed maximum
    	// number of subdivisions.
    	if (level <= MAX_LEVEL && maxAngle > epsilon/2f) {
    		Vector2 p10 = new Vector2();
    		Vector2 p11 = new Vector2();
    		Vector2 p12 = new Vector2();
    		Vector2 p20 = new Vector2();
    		Vector2 p21 = new Vector2();
    		Vector2 p30 = new Vector2();
    		
    		// Use de Casteljeau's algorithm to compute the point on this segment at
    		// t = 0.5.  This produces the control points for the left and right halves 
    		// of the spline.
    		
    		p10.set(p0);
    		p11.set(p1);
    		p12.set(p2);
    		p10.lerp(p1, .5f);
    		p11.lerp(p2, .5f);
    		p12.lerp(p3, .5f);
    		
    		p20.set(p10);
    		p21.set(p11);
    		p20.lerp(p11, .5f);
    		p21.lerp(p12, .5f);
    		
    		p30.set(p20);
    		p30.lerp(p21, .5f);
    		
    		tessellateHelper(p0, p10, p20, p30, level + 1, outPoints, outTangents);
    		tessellateHelper(p30, p21, p12, p3, level + 1, outPoints, outTangents);
    	} else {
    		// Not subdividing, just tessellating this curve with a single vertex.
    		outPoints.add(new Vector2(p0));
    		//if(v0.dot(new Vector2(0,1)) < 0) v0.mul(-1);
    		outTangents.add(new Vector2(v0).normalize());
    	}
    }
	
    
    /**
     * @return The points on this cubic bezier
     */
    public ArrayList<Vector2> getPoints() {
    	ArrayList<Vector2> returnList = new ArrayList<Vector2>();
    	for(Vector2 p : curvePoints) returnList.add(p.clone());
    	return returnList;
    }
    
    /**
     * @return The tangents on this cubic bezier
     */
    public ArrayList<Vector2> getTangents() {
    	ArrayList<Vector2> returnList = new ArrayList<Vector2>();
    	for(Vector2 p : curveTangents) returnList.add(p.clone());
    	return returnList;
    }
    
    /**
     * @return The normals on this cubic bezier
     */
    public ArrayList<Vector2> getNormals() {
    	ArrayList<Vector2> returnList = new ArrayList<Vector2>();
    	for(Vector2 p : curveNormals) returnList.add(p.clone());
    	return returnList;
    }
    
    
    /**
     * @return The references to points on this cubic bezier
     */
    public ArrayList<Vector2> getPointReferences() {
    	ArrayList<Vector2> returnList = new ArrayList<Vector2>();
    	for(Vector2 p : curvePoints) returnList.add(p);
    	return returnList;
    }
    
    /**
     * @return The references to tangents on this cubic bezier
     */
    public ArrayList<Vector2> getTangentReferences() {
    	ArrayList<Vector2> returnList = new ArrayList<Vector2>();
    	for(Vector2 p : curveTangents) returnList.add(p);
    	return returnList;
    }
    
    /**
     * @return The references to normals on this cubic bezier
     */
    public ArrayList<Vector2> getNormalReferences() {
    	ArrayList<Vector2> returnList = new ArrayList<Vector2>();
    	for(Vector2 p : curveNormals) returnList.add(p);
    	return returnList;
    }
}
