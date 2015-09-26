package cs4621.GPUray;

import cs4620.ray1.RayTracer.ScenePath;
import blister.MainGame;
import blister.ScreenList;

public class GPURayApp extends MainGame {

	/**
	 * The Workspace For The Scene
	 */
	public static ScenePath sceneWorkspace = null;
	
	/**
	 * This directory precedes the arguments passed in via the command line.
	 */
	public static final String directory = "data/scenes/gpuray";
	
	
    public GPURayApp(String title, int w, int h) {
        super(title, w, h, System.getProperty("os.name").equalsIgnoreCase("Mac OS X") ?
        			new org.lwjgl.opengl.ContextAttribs(3,2).withProfileCore(true) :
        			null, null);
    }
    
    @Override
    protected void buildScreenList() {
        screenList = new ScreenList(this, 0, new RayTracerScreen());
    }

    @Override
    protected void fullInitialize() {
        // Empty
    }

    @Override
    protected void fullLoad() {
        // Empty
    }
    
    @Override
    public void exit() {
        super.exit();
    }

    public static void main(String[] args) {
        GPURayApp app = new GPURayApp("Real-Time Ray Tracer", 800, 800);
        sceneWorkspace = new ScenePath(directory,"Test.xml");
        app.run();
        app.dispose();
    }

}
