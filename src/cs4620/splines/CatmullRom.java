/**
 * @author Jimmy, Andrew 
 */

package cs4620.splines;
import java.util.ArrayList;

import egl.math.Matrix4;
import egl.math.Vector2;
import egl.math.Vector4;

public class CatmullRom extends SplineCurve {

	public CatmullRom(ArrayList<Vector2> controlPoints, boolean isClosed,
			float epsilon) throws IllegalArgumentException {
		super(controlPoints, isClosed, epsilon);
	}

	@Override
	public CubicBezier toBezier(Vector2 p0, Vector2 p1, Vector2 p2, Vector2 p3, float eps) {
		//TODO A5
		//SOLUTION
		Matrix4 beztoher = new Matrix4(1, 0, 0, 0, 0, 0, 0,  1, 
				                      -3, 3, 0, 0, 0, 0, -3, 3);
		
		Matrix4 hertober = beztoher.invert();
		
		Matrix4 berzier = new Matrix4(-1, 3, -3, 1, 3, -6, 3, 0,
				                       -3, 3, 0, 0, 1, 0, 0, 0);
		
	    Matrix4 Romtoher = new Matrix4(0,   1, 0,    0, 0,    0,  1,   0,
	    		                     -0.5f, 0, 0.5f, 0, 0, -0.5f, 0, 0.5f);
		
	    
	    Matrix4 trans = berzier.mulBefore(hertober).mulBefore(Romtoher);
	   
	    Vector4 xaxis = new Vector4(p0.x, p1.x, p2.x, p3.x);
	    Vector4 yaxis = new Vector4(p0.y, p1.y, p2.y, p3.y);
	    
	    Vector4 newx = trans.mul(xaxis);
	    Vector4 newy = trans.mul(yaxis);
	       
		return new CubicBezier(new Vector2(newx.x, newy.x), new Vector2(newx.y, newy.y), 
				               new Vector2(newx.z, newy.z), new Vector2(newx.w, newy.w), eps);
		
		//END SOLUTION
	}
}
