package cs4620.ray1.camera;

import cs4620.ray1.Ray;
import egl.math.Vector3d;

/**
 * Represents a camera object. This class is responsible for generating rays that are intersected
 * with the scene.
 */

public abstract class Camera {
	/*
	 * Fields that are read in from the input file to describe the camera.
	 * You'll probably want to store some derived values to make ray generation easy.
	 */
	
	/**
	 * The position of the eye.
	 */
	protected final Vector3d viewPoint = new Vector3d();
	public void setViewPoint(Vector3d viewPoint) { this.viewPoint.set(viewPoint); }
	
	/**
	 * The direction the eye is looking.
	 */
	protected final Vector3d viewDir = new Vector3d(0, 0, -1);
	public void setViewDir(Vector3d viewDir) { this.viewDir.set(viewDir); }
	
	/**
	 * The upwards direction from the viewer's perspective.
	 */
	protected final Vector3d viewUp = new Vector3d(0, 1, 0);
	public void setViewUp(Vector3d viewUp) { this.viewUp.set(viewUp); }
	
	/**
	 * The width of the viewing window.
	 */
	protected double viewWidth = 1.0;
	public void setViewWidth(double viewWidth) { this.viewWidth = viewWidth; }
	
	/**
	 * The height of the viewing window.
	 */
	protected double viewHeight = 1.0;
	public void setViewHeight(double viewHeight) { this.viewHeight = viewHeight; }
	
	/**
	 * Generate a ray that points out into the scene for the given (u,v) coordinate.
	 * This coordinate corresponds to a point on the viewing window, where (0,0) is the
	 * lower left corner and (1,1) is the upper right.
	 * @param outRay A space to return the output ray
	 * @param u The horizontal coordinate (0 is left, 1 is right)
	 * @param v The vertical coordinate (0 is bottom, 1 is top)
	 */
	public abstract void getRay(Ray outRay, double u, double v);
	
	/**
	 * Code for unit testing of cameras.
	 */
	public void testGetRay(Ray correctRay, double u, double v) {
		Ray testRay = new Ray();
		getRay(testRay, u, v);
		if (!raysEquivalent(testRay, correctRay)) {
			 System.err.println("test failed");
			 System.err.println("testRay: " + testRay.origin + " + t * " + testRay.direction);
			 System.err.println("correctRay: " + correctRay.origin + " + t * " + correctRay.direction);
			 System.exit(-1);
		}
	}
	
	private static boolean raysEquivalent(Ray ray1, Ray ray2) {
		Vector3d dir1 = new Vector3d(ray1.direction);
		Vector3d dir2 = new Vector3d(ray2.direction);
		dir1.normalize();
		dir2.normalize();
		dir1.sub(dir2);
		return ray1.origin.dist(ray2.origin) < 1e-6 && dir1.len() < 1e-6; 
	}

	public static void main(String args[]) {
	    Vector3d viewPoint = new Vector3d(1, 0.5, 2);
	    Vector3d viewDir = new Vector3d(15.23, -1.854, 65.221);
	    viewDir.normalize();
	    Vector3d viewUp = new Vector3d(1, 0, 0);
	    
	    OrthographicCamera orthoCam = new OrthographicCamera();
	    orthoCam.setViewPoint(viewPoint);
	    orthoCam.setViewDir(viewDir);
	    orthoCam.setViewUp(viewUp);
	    
	    PerspectiveCamera perspectiveCam = new PerspectiveCamera();
	    perspectiveCam.setViewPoint(viewPoint);
	    perspectiveCam.setViewDir(viewDir);
	    perspectiveCam.setViewUp(viewUp);
	    
	    float u = 0.37123f;
	    float v = 0.11343f;
	    Ray correctRay0 = new Ray(new Vector3d(0.6235493799051484, 0.36878515466141304, 2.084176425089877), 
	                              new Vector3d(0.22730915261287413, -0.027671120744863338, 0.9734294315537928));
	    orthoCam.testGetRay(correctRay0, u, v);
	    
	    Ray correctRay1 = new Ray(new Vector3d(1.0, 0.5, 2.0),
	                              new Vector3d(-0.13811656557506483, -0.14714072701594028, 0.9794250460177998));
	    perspectiveCam.testGetRay(correctRay1, u, v);
	    

	    u = 0.00234f;
	    v = 0.9832f;
	    Ray correctRay2 = new Ray(new Vector3d(1.4705511166404994, 0.005661926354425948, 1.8760675810709173),
	                              new Vector3d(0.22730915261287413, -0.027671120744863338, 0.9734294315537928));
	    orthoCam.testGetRay(correctRay2, u, v);
	    
	    Ray correctRay3 = new Ray(new Vector3d(1.0, 0.5, 2.0),
	                              new Vector3d(0.5734153111840217, -0.4289226336993767, 0.698011644029039));
	    perspectiveCam.testGetRay(correctRay3, u, v);
	    
	    
	    u = 0.2345f;
	    v = 0.78201f;
	    Ray correctRay4 = new Ray(new Vector3d(1.2746277432055346, 0.2364287075632546, 1.928378256923414),
                                  new Vector3d(0.22730915261287413, -0.027671120744863338, 0.9734294315537928));
	    orthoCam.testGetRay(correctRay4, u, v);
	    
	    Ray correctRay5 = new Ray(new Vector3d(1.0, 0.5, 2.0),
	                              new Vector3d(0.46805451959738786, -0.27158260116709737, 0.8409327306198586));
	    perspectiveCam.testGetRay(correctRay5, u, v);
	    

	    u = 0.55523f;
	    v = 0.12555f;
	    Ray correctRay6 = new Ray(new Vector3d(0.635352111386452, 0.552789156075998, 2.0866509013806787),
                                  new Vector3d(0.22730915261287413, -0.027671120744863338, 0.9734294315537928));
	    orthoCam.testGetRay(correctRay6, u, v);
	    
	    Ray correctRay7 = new Ray(new Vector3d(1.0, 0.5, 2.0),
	                              new Vector3d(-0.12844581009091482, 0.02349159814594461, 0.991438257627089));
	    perspectiveCam.testGetRay(correctRay7, u, v);
	}
	
}
