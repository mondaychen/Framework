package cs4621.Particles;

import org.lwjgl.opengl.ContextAttribs;

import blister.MainGame;
import blister.ScreenList;

public class ParticlesApp extends MainGame {
    public ParticlesApp(String title, int w, int h) {
        super(title, w, h, 
                System.getProperty("os.name").equalsIgnoreCase("Mac OS X") ? new ContextAttribs(3,2).withProfileCore(true) : null,
                null
                );
    }

    @Override
    protected void buildScreenList() {
        screenList = new ScreenList(this, 0, new ParticleScreen());
    }
    @Override
    protected void fullInitialize() {
        // Empty
    }
    @Override
    protected void fullLoad() {
        // Empty
    }

    public static void main(String[] args) {
        ParticlesApp app = new ParticlesApp("CS 4621 Particles", 800, 800);
        app.run();
        app.dispose();
    }
}