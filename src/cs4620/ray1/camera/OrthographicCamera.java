package cs4620.ray1.camera;

import cs4620.ray1.Ray;
import egl.math.Vector3d;

public class OrthographicCamera extends Camera {
    
    /*
     * Derived values that are computed before ray generation.
     * basisU, basisV, and basisW form an orthonormal basis.
     * basisW is parallel to projNormal.
     */
    protected final Vector3d basisU = new Vector3d();
    protected final Vector3d basisV = new Vector3d();
    protected final Vector3d basisW = new Vector3d();
    
    // Has the view been initialized?
    protected boolean initialized = false;
    
    /**
     * Initialize the derived view variables to prepare for using the camera.
     */
    public void initView() {
        // TODO#A2: Fill in this function.
        // 1) Set basisU, basisV, basisW to be the 3 basis vectors, 
        //    based on viewDir and viewUp
      
        initialized = true;
    }

    /**
     * Set outRay to be a ray from the camera through a point in the image.
     *
     * @param outRay The output ray (not normalized)
     * @param inU The u coord of the image point (range [0,1])
     * @param inV The v coord of the image point (range [0,1])
     */
    public void getRay(Ray outRay, double inU, double inV) {
        // TODO#A2: Fill in this function.
        // 1) If the view is not yet initialized, initialize it.
        // 2) Transform inU so that it lies between [-viewWidth / 2, +viewWidth / 2] 
        //    instead of [0, 1]. Similarly, transform inV so that its range is
        //    [-vieHeight / 2, +viewHeight / 2]
        // 3) Set the origin field of outRay for an orthographic camera. 
        //    In an orthographic camera, the origin should depend on your transformed
        //    inU and inV and basisU and basisV.
        // 4) Set the direction field of outRay for an orthographic camera.
    }

}
