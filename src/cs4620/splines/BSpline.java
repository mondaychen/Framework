package cs4620.splines;
import java.util.ArrayList;

import egl.math.Matrix4;
import egl.math.Vector2;

public class BSpline extends SplineCurve{

	public BSpline(ArrayList<Vector2> controlPoints, boolean isClosed,
			float epsilon) throws IllegalArgumentException {
		super(controlPoints, isClosed, epsilon);
	}

	@Override
	public CubicBezier toBezier(Vector2 p0, Vector2 p1, Vector2 p2, Vector2 p3,
			float eps) {
		//TODO A5 (Extra Credit)
		//SOLUTION
        
		Matrix4 bezToHer = new Matrix4(-1f, 3f, -3f, 1f, 
				                        3f, -6, 3f, 0f, 
				                        -3f, 3f, 0f, 0f, 
				                        1f, 0f, 0f, 0f);
		
		Matrix4 bspToHer = new Matrix4(-1/6f, 3/6f, -3/6f, 1/6f, 
				                        3/6f, -6/6f, 3/6f, 0/6f, 
				                        -3/6f, 0/6f, 3/6f, 0/6f, 
				                         1/6f, 4/6f, 1/6f, 0/6f);
        
		Matrix4 bspToBez = bezToHer.invert().mulBefore(bspToHer); 

		//Making a position matrix;
		Matrix4 bspPoints = new Matrix4(p0.x, p0.y, 0, 0,
										p1.x, p1.y, 0, 0,
										p2.x, p2.y, 0, 0,
										p3.x, p3.y, 0, 0);
		
		Matrix4 bezPoints = bspToBez.mulBefore(bspPoints);
				
		p0 = new Vector2(bezPoints.m[0], bezPoints.m[4]);
		p1 = new Vector2(bezPoints.m[1], bezPoints.m[5]);
		p2 = new Vector2(bezPoints.m[2], bezPoints.m[6]);
		p3 = new Vector2(bezPoints.m[3], bezPoints.m[7]);
		return new CubicBezier(p0, p1, p2, p3, eps);
		//END SOLUTION
	}
	

		
	
}
