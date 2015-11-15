package cs4621.Particles;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import blister.GameScreen;
import blister.GameTime;
import blister.input.KeyboardEventDispatcher;
import blister.input.KeyboardKeyEventArgs;
import blister.input.MouseButton;
import blister.input.MouseButtonEventArgs;
import blister.input.MouseEventDispatcher;
import blister.input.MouseWheelEventArgs;
import blister.input.MouseMoveEventArgs;
import cs4620.mesh.MeshData;
import egl.GL.BufferTarget;
import egl.GL.BufferUsageHint;
import egl.GL.GLType;
import egl.GL.PrimitiveType;
import egl.GL.TextureUnit;
import egl.ArrayBind;
import egl.GLBuffer;
import egl.GLProgram;
import egl.GLUniform;
import egl.NativeMem;
import egl.RasterizerState;
import egl.Semantic;
import egl.ShaderInterface;
import egl.math.*;
import ext.csharp.ACEventFunc;

/**
 * Particle Screen for displaying and animating a basic particle system.
 * 
 * Refactored and added to CS 4621. Originally written by Asher Dunn (ad488).
 * Rewritten for CS 4621 (Introduction to Computer Graphics Practicum)
 * Copyright (c) 2015, Computer Science Department, Cornell University.
 * 
 * @author Eric Gao
 * @date 2015-11-01
 *
 */
public final class ParticleScreen extends GameScreen{
    /* Constants */
    private final int MAX_PARTICLES = 1024;
    
    /* For calculating FPS */
    private final float FPS_ALPHA = 0.5f;
    private float mFps = 0;
    
    /* Vertex shader inputs */
    private GLBuffer rasterVerts;
    private GLBuffer ibTris;
    private GLBuffer wireframeVerts;
    private GLBuffer velocityVerts;
    private GLBuffer ibWireframe;
    private GLBuffer ibVelocities;
    
    /* Mesh state */
    private FloatBuffer vBuf;
    private FloatBuffer vBufWireframe;
    private FloatBuffer vBufVelocities;
    
    /* Shader state/uniforms with state */
    private GLProgram program;
    private GLProgram linesProgram;
    private ShaderInterface particlesSI;
    private ShaderInterface linesSI;
    private boolean showWireFrames = false;
    private boolean showVelocities = false;
    
    /* Camera information */
    private Matrix4 mViewProjection  = new Matrix4();
    private Matrix4 mView            = new Matrix4();
    private float   mCameraRadius    = 5.0f;
    private float   mCameraLongitude = 0.0f;
    private float   mCameraLatitude  = 0.0f;
    private Vector3 mCameraPosition  = new Vector3(0, 0, mCameraRadius);
    private Vector3 mCameraUp        = new Vector3(0, 1, 0);
    private Vector2 mCameraViewSize  = new Vector2(800, 800);
    private Vector2 mZPlanes         = new Vector2(0.01f, 100.0f);
    private float   mCameraFOV       = 10.0f;
    
    /* Lighting information */
    private Vector3 mLightPos = new Vector3(0, 5, 0);
    private Vector3 mLightIntensity = new Vector3(5, 5, 5);
    
    /* Particle System information*/
    private ParticleSystem mParticleSystem = new ParticleSystem(MAX_PARTICLES);
    
    /* Mouse information */
    private boolean mousePressed = false;

    @Override
    public void build() {
        // First create the programs: one for the particles, one for the wireframes.
        program = new GLProgram(false);
        program.quickCreateResource("particles", "cs4621/Particles/shaders/particles.vert", "cs4621/Particles/shaders/particles.frag", null);
        
        linesProgram = new GLProgram(false);
        linesProgram.quickCreateResource("lines", "cs4621/Particles/shaders/lines.vert", "cs4621/Particles/shaders/lines.frag", null);
        
        // Initialize camera matrix.
        updateCamera();
        
        // Initialize shader interfaces.
        particlesSI = new ShaderInterface(new ArrayBind[]{
                                          new ArrayBind(Semantic.Position, GLType.Float, 3, 0),
                                          new ArrayBind(Semantic.Color, GLType.Float, 3, 3 * 4    /* 3 floats x 4 bytes offset */),
                                          new ArrayBind(Semantic.TexCoord, GLType.Float, 2, 6 * 4 /* 6 floats x 4 bytes offset */)
        });
        particlesSI.build(program.semanticLinks);
        
        linesSI = new ShaderInterface(new ArrayBind[]{new ArrayBind(Semantic.Position, GLType.Float, 3, 0)});
        linesSI.build(linesProgram.semanticLinks);
        
        // Initialize the vertex buffers
        rasterVerts = new GLBuffer(BufferTarget.ArrayBuffer, BufferUsageHint.StaticDraw, true);
        rasterVerts.setAsVertex(8 * 4); // 1 vertex element = (3 floats for position + 2 for uv + 3 for color) * 4 bytes
        vBuf = NativeMem.createFloatBuffer(8 * 4); // 4 vertices * (3 floats for position + 2 for uv + 3 for color) elements 
        
        wireframeVerts = new GLBuffer(BufferTarget.ArrayBuffer, BufferUsageHint.StaticDraw, true);
        wireframeVerts.setAsVertex(4 * 3); // 1 vertex element = 3 floats for position * 4 bytes
        vBufWireframe = NativeMem.createFloatBuffer(3 * 4); // 4 vertices * 3 floats for position
        
        velocityVerts = new GLBuffer(BufferTarget.ArrayBuffer, BufferUsageHint.StaticDraw, true);
        velocityVerts.setAsVertex(4 * 3); // 1 vertex element = 3 floats for position * 4 bytes
        vBufVelocities = NativeMem.createFloatBuffer(3 * 2); // 2 vertices * 3 floats for position
        
        // Initialize index buffers.
        ibTris       = new GLBuffer(BufferTarget.ElementArrayBuffer, BufferUsageHint.StaticDraw, true);
        ibWireframe  = new GLBuffer(BufferTarget.ElementArrayBuffer, BufferUsageHint.StaticDraw, true);
        ibVelocities = new GLBuffer(BufferTarget.ElementArrayBuffer, BufferUsageHint.StaticDraw, true);
        
        ibTris.setAsIndexInt();
        IntBuffer iBuf = NativeMem.createIntBuffer(6);
        iBuf.put(new int[]{0, 1, 2, 0, 2, 3});
        iBuf.rewind();
        ibTris.setDataInitial(iBuf);
        
        ibWireframe.setAsIndexInt();
        iBuf = NativeMem.createIntBuffer(8);
        iBuf.put(new int[]{0, 1, 1, 2, 2, 3, 3, 0});
        iBuf.rewind();
        ibWireframe.setDataInitial(iBuf);
        
        ibVelocities.setAsIndexInt();
        iBuf = NativeMem.createIntBuffer(2);
        iBuf.put(new int[]{0, 1});
        iBuf.rewind();
        ibVelocities.setDataInitial(iBuf);
        
        // Initialize all of the controls.
        initKeybindings();
        initMouseControls();
        
    }
    
    /**
     * Initialize mouse controls
     *  Scroll         : zoom
     *  Click and drag : rotate camera about origin.
     */
    private void initMouseControls() {
        // Zoom in and out.
        MouseEventDispatcher.OnMouseScroll.add(new ACEventFunc<MouseWheelEventArgs>() {
            @Override
            public void receive(Object sender, MouseWheelEventArgs args) {
                mCameraRadius = Math.max(mCameraRadius + (float) args.ScrollChange * 0.01f, 0.0f);
                updateCamera();
            }
        });
        
        MouseEventDispatcher.OnMousePress.add(new ACEventFunc<MouseButtonEventArgs>() {
           @Override
           public void receive(Object sender, MouseButtonEventArgs args) {
               if(!mousePressed && args.button == MouseButton.Left) {
                   mousePressed = true;
               }
               else if(args.button == MouseButton.Middle) {
                   mCameraRadius    = 5.0f;
                   mCameraLongitude = 0.0f;
                   mCameraLatitude  = 0.0f;
                   mCameraUp        = new Vector3(0, 1, 0);
                   updateCamera();
               }
           }
        });
        
        MouseEventDispatcher.OnMouseRelease.add(new ACEventFunc<MouseButtonEventArgs>() {
            @Override
            public void receive(Object sender, MouseButtonEventArgs args) {
                if(args.button == MouseButton.Left) {
                    mousePressed = false;
                }
            }
         });
        
        MouseEventDispatcher.OnMouseMotion.add(new ACEventFunc<MouseMoveEventArgs>() {
            @Override
            public void receive(Object sender, MouseMoveEventArgs args) {
                if(mousePressed) {
                    /* Calculate dragged delta. */
                    float deltaX = -((float) args.dx * 0.5f);
                    float deltaY = -((float) args.dy * 0.5f);
                    
                    /* Update longitude, wrapping as necessary. */
                    mCameraLongitude += deltaX;
                    
                    if (mCameraLongitude > 360.0f)
                    {
                        mCameraLongitude -= 360.0f;
                    }
                    else if (mCameraLongitude < 0.0f)
                    {
                        mCameraLongitude += 360.0f;
                    }
                    
                    /* Update latitude, clamping as necessary. */
                    if (Math.abs(mCameraLatitude + deltaY) <= 89.0f)
                    {
                        mCameraLatitude += deltaY;
                    }
                    else
                    {
                        mCameraLatitude = 89.0f * Math.signum(mCameraLatitude);
                    }
                
                    updateCamera();
                }
            }
        });
    }
    
    /**
     * Initialize all the key bindings.
     */
    private void initKeybindings() {
        System.out.println("==========================================\n"
                         + "           Keyboard controls: \n"
                         + "==========================================\n"
                         + " <Space> : Play/pause particle system.\n"
                         + "    w    : Increase wind speed.\n"
                         + "    q    : Decrease wind speed.\n"
                         + "    d    : Increase drag.\n"
                         + "    s    : Decrease drag.\n"
                         + "    g    : Increase gravity.\n"
                         + "    f    : Decrease gravity.\n"
                         + "    r    : Reset particle system.\n"
                         + "    v    : Display lines for velocity.\n"
                         + "  <Tab>  : Toggle wireframes.\n"
                         + "=========================================");
        
        KeyboardEventDispatcher.OnKeyPressed.add(new ACEventFunc<KeyboardKeyEventArgs>() {
            @Override
            public void receive(Object sender, KeyboardKeyEventArgs args) {
                if(args.key == Keyboard.KEY_SPACE) {
                    mParticleSystem.mPaused = !mParticleSystem.mPaused;
                    if(mParticleSystem.mPaused)
                        System.out.println("Simulation paused.");
                    else
                        System.out.println("Starting...");
                }
                else if(args.key == Keyboard.KEY_W) {
                    mParticleSystem.wind += 1.0f;
                    System.out.println("Wind = " + mParticleSystem.wind);
                }
                else if(args.key == Keyboard.KEY_Q) {
                    mParticleSystem.wind -= 1.0f;
                    System.out.println("Wind = " + mParticleSystem.wind);
                }
                else if(args.key == Keyboard.KEY_D) {
                    mParticleSystem.drag += 1.0f;
                    System.out.println("Drag = " + mParticleSystem.drag);
                }
                else if(args.key == Keyboard.KEY_S) {
                    mParticleSystem.drag -= 1.0f;
                    System.out.println("Drag = " + mParticleSystem.drag);
                }
                else if(args.key == Keyboard.KEY_G) {
                    mParticleSystem.gravity += 1.0f;
                    System.out.println("Gravity = " + mParticleSystem.gravity);
                }
                else if(args.key == Keyboard.KEY_F) {
                    mParticleSystem.gravity -= 1.0f;
                    System.out.println("Gravity = " + mParticleSystem.gravity);
                }
                else if(args.key == Keyboard.KEY_TAB) {
                    showWireFrames = !showWireFrames;
                    if(showWireFrames)
                        System.out.println("Displaying wireframes");
                    else
                        System.out.println("No longer displaying wireframes");
                }
                else if(args.key == Keyboard.KEY_R) {
                    System.out.println("Reset");
                    mParticleSystem.reset();
                }
                else if(args.key == Keyboard.KEY_V) {
                    showVelocities = !showVelocities;
                    if(showVelocities)
                        System.out.println("Displaying velocities");
                    else
                        System.out.println("No longer displaying velocities");
                }
            }
        });
    }
    
    @Override
    public void destroy(GameTime gameTime) {
        program.dispose();
        linesProgram.dispose();
        rasterVerts.dispose();
        ibTris.dispose();
        ibWireframe.dispose();
    }
    
    @Override
    public void onEntry(GameTime gameTime) {
        RasterizerState.CULL_NONE.set();
        GL11.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        GL11.glClearDepth(1.0);
        GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
    }
    
    @Override
    public void onExit(GameTime gameTime) {
        // Empty
    }  

    /**
     * Updates the camera position, camera up vector and the view projection matrix 
     * based on the camera's latitude, longitude, and radius from the lookAt point.
     */
    public void updateCamera() {
        // Update the eye position.
        mCameraPosition.set(0.0f, 0.0f, mCameraRadius);
        Matrix4 rotation = Matrix4.createRotationY(mCameraLongitude * (float) Math.PI / 180.0f);
        rotation.mulBefore(Matrix4.createRotationX(-mCameraLatitude  * (float) Math.PI / 180.0f));
        mCameraPosition = rotation.mulPos(mCameraPosition);
        
        // Recalculate the up vector
        mCameraUp.set(0, 1, 0);
        mCameraUp = rotation.mulDir(mCameraUp).normalize();
        Vector3 viewDir = mCameraPosition.clone().mul(-1).normalize();
        Vector3 w       = viewDir.clone().cross(mCameraUp).normalize();
        mCameraUp       = w.cross(viewDir).normalize();
        
        // Update the view projection matrix
        mView              = Matrix4.createLookAt(mCameraPosition, new Vector3(0, 0, 0) /* lookAt */, mCameraUp);
        Matrix4 projection = Matrix4.createPerspectiveFOV((float) (mCameraFOV * Math.PI / 180.0) /* FOV */, 
                                                          mCameraViewSize.x / mCameraViewSize.y  /* Aspect ratio */, 
                                                          mZPlanes.x, mZPlanes.y /* Clipping planes */);
        
        mViewProjection.set(mView).mulAfter(projection);
        
        // If a camera rotation has occurred, then we need to billboard again.
        mParticleSystem.billboard(mView);
    }
    
    @Override
    public void update(GameTime gameTime) {
        if(mParticleSystem.mPaused) return;
        
        mParticleSystem.animate((float) gameTime.elapsed);
    }
    
    @Override
    public void draw(GameTime gameTime) {
        // Benchmarking
        if (gameTime.elapsed > 0.0f) {
            mFps = mFps*(1-FPS_ALPHA) + (1/(float)gameTime.elapsed)*FPS_ALPHA;
            mFps /= 2;
            //System.out.printf("%.2f Frames per Second\n", mFps);
        }
        
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        
        // Enable blending
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glAlphaFunc(GL11.GL_GREATER, 0.6f);
        
        for(Particle p : mParticleSystem.mSpawnedParticles) {
            vBuf.rewind(); vBufWireframe.rewind();
            
            // Get the positions of the quad for this particle.
            Vector3 particlePosition = p.getParticlePosition();
            MeshData md = p.getMeshData();
            Colord color = p.getColor();
            float scale = p.getScale();
            
            for(int i = 0; i < md.vertexCount; ++i) {
                Vector3 quadPosition = new Vector3((float) p.getPosition(i).x, 
                                                   (float) p.getPosition(i).y, 
                                                   (float) p.getPosition(i).z);
                mParticleSystem.getBillboardTransform().mulPos(quadPosition);
                quadPosition.mul(scale).add(particlePosition);
                
                vBuf.put((float) quadPosition.x);
                vBuf.put((float) quadPosition.y);
                vBuf.put((float) quadPosition.z);
                vBuf.put((float) color.x);
                vBuf.put((float) color.y);
                vBuf.put((float) color.z);
                vBuf.put((float) p.getUV(i).x);
                vBuf.put((float) p.getUV(i).y);
                
                vBufWireframe.put((float) quadPosition.x);
                vBufWireframe.put((float) quadPosition.y);
                vBufWireframe.put((float) quadPosition.z);
                
                
            }
            vBuf.rewind();
            vBufWireframe.rewind();
            vBufVelocities.rewind();
            
            rasterVerts.setDataInitial(vBuf);
            wireframeVerts.setDataInitial(vBufWireframe);
            velocityVerts.setDataInitial(vBufVelocities);
            
            program.use();
            {
                // Pass over view projection matrix.
                GLUniform.setST(program.getUniform("mModelViewProjection"), mViewProjection, false);
                
                // Bind the texture.
                mParticleSystem.particleTexture.use(TextureUnit.Texture0, program.getUniform("particleTexture"));
                
                // Bind the attributes.
                rasterVerts.useAsAttrib(particlesSI);
                ibTris.bind();
                GL11.glDrawElements(PrimitiveType.Triangles, 6, GLType.UnsignedInt, 0);
                ibTris.unbind();
                
            }
            GLProgram.unuse();
            
            if(showWireFrames) {
                linesProgram.use();
                {
                    // Pass over view projection matrix.
                    GLUniform.setST(linesProgram.getUniform("mModelViewProjection"), mViewProjection, false);

                    wireframeVerts.useAsAttrib(linesSI);
                    
                    ibWireframe.bind();
                    GL11.glDrawElements(PrimitiveType.Lines, 8, GLType.UnsignedInt, 0);
                    ibWireframe.unbind();
                }
                GLProgram.unuse();
            }
            
            if(showVelocities) {
                // TODO#PPA3 SOLUTION START:
                // Populate the vertex buffer to display velocities for each particle.
                // 1.) Disable depth testing so that you can see the lines no matter what. 
                //     Don't forget to re-enable this at the end!
                // 2.) Put the particle position and the particle position + (velocity scaled
                //     by some constant amount) into the float buffer for velocities (vBufVelocities).
                //     Don't forget to rewind at the appropriate locations!
                // 3.) Set the appropriate GLBuffer (velocityVerts) using vBufVelocities
                // 4.) Use the program (linesProgram and bind the attributes, uniforms and the  
                //     appropriate index buffer (ibVelocities).
                // 5.) Bind the GLBuffer to the appropriate shader interface (linesSI) and draw
                //     the velocity lines.
                
                // SOLUTION END
            }
        }
    }
    
   
    @Override
    public int getNext() {
        // Don't modify this method
        return 0;
    }
    @Override
    protected void setNext(int next) {
        // Don't modify this method
    }

    @Override
    public int getPrevious() {
        // Don't modify this method
        return 0;
    }
    @Override
    protected void setPrevious(int previous) {
        // Don't modify this method
    }
}
