package cs4621.demos;

import blister.MainGame;
import blister.ScreenList;

public class OpenGLDemoApp extends MainGame {

	public OpenGLDemoApp(String title, int w, int h) {
		super(title, w, h);
		// TODO Auto-generated constructor stub
	}
	

	final int currentScreen = 0;
	@Override
	protected void buildScreenList() {
		screenList = new ScreenList(this, currentScreen,
		    new HelloWorldScreen(), 
			new GreenTriangleScreen(), 
			new TwistScreen(),
			new PositionColorScreen(), 
			new TwistColorScreen()
			);
	}

	@Override
	protected void fullInitialize() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void fullLoad() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void exit() {
		super.exit();
	}

	public static void main(String[] args) {
		OpenGLDemoApp app = new OpenGLDemoApp("CS4621 GLSL demos", 800, 800);
		app.run();
		app.dispose();
	}

}
