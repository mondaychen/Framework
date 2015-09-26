package cs4621.GPUray;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import blister.GameScreen;
import blister.GameTime;
import blister.input.KeyboardEventDispatcher;
import blister.input.KeyboardKeyEventArgs;
import cs4620.mesh.MeshData;
import cs4620.ray1.shader.Shader;
import cs4620.ray1.Light;
import cs4620.ray1.Parser;
import cs4620.ray1.Scene;
import cs4620.ray1.shader.Lambertian;
import cs4620.ray1.shader.Phong;
import cs4620.ray1.surface.Mesh;
import cs4620.ray1.RayTracer.ScenePath;
import cs4620.ray1.surface.Surface;
import egl.GL.BufferUsageHint;
import egl.GLBuffer;
import egl.GLProgram;
import egl.GLUniform;
import egl.NativeMem;
import egl.RasterizerState;
import egl.math.Colord;
import egl.math.Matrix3;
import egl.math.Matrix4;
import egl.math.Vector3;
import egl.math.Vector3d;
import egl.math.Vector4;
import ext.csharp.ACEventFunc;

public final class RayTracerScreen extends GameScreen{
    // Constants for defining arrays in GLSL
    private final int MAX_VERTS = 128;
    private final int MAX_TRIS = 256 + 64 + 8;
    private final int MAX_COLORS = 16;
    private final int NUM_DEBUG_STATES = 4;
    private final Vector3 DEFAULT_COLOR = new Vector3(0.5f, 1, 0.3f);
    
    // Vertex shader inputs
    private GLBuffer rasterVerts;
    private int vaoId = 0;

    // Shader state/uniforms with state 
    // These are just default values
    // You will probably want to add to these
    private GLProgram program;
    private Matrix4 mVP = new Matrix4();
    private Vector3 mEyePos = new Vector3( -2, 0, -10);
    // ...
    
    // Mesh state 
    private IntBuffer ibTris = NativeMem.createIntBuffer(4 * MAX_TRIS); // 4th component is index into fbColors
    private FloatBuffer fbVerts = NativeMem.createFloatBuffer(3 * MAX_VERTS);
    private FloatBuffer fbColors = NativeMem.createFloatBuffer(3 * MAX_COLORS);
    private int numTris = 0;
    private int dbgState = 0;
    
    @Override
    public void build() {
    	// TODO#PPA1 Solution Start
    	// The build method is called once, when your RayTracerScreen is first created.
    	// 1) Create the GLProgram by compiling and linking the vertex and fragment shaders
    	// 2) Load the scene from XML
    	// 3) Set VP matrix for the first time
    	// 4) Set up any data/buffers necessary to transfer to the shaders
    	// 5) Create a KeyBoardEventDispatcher to handle state changes via the ZERO key.
    	
    	program = new GLProgram(false);
        // TODO#PPA1: load shaders from src and create program (using available framework methods)

        // TODO#PPA1: Call setupScene() with a ScenePath to an xml scene
    	// (see scenes/ray1; bunny-shadow-scene.xml is a good place to start)

        // Create a new Vertex Array Object in memory and bind it
        vaoId = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(vaoId);

        rasterVerts = GLBuffer.createAsVertex(new float[] {-1, 3, -1, -1, 3, -1 }, 2, BufferUsageHint.StaticDraw);
        rasterVerts.bind();

        // Put the VBO in the attributes list at index 0
        GL20.glEnableVertexAttribArray(program.getAttribute("vVertex"));
        GL20.glVertexAttribPointer(program.getAttribute("vVertex"), 2, GL11.GL_FLOAT, false, 0, 0);

        // Unbind the VBO
        rasterVerts.unbind();
         
        // VAO Deselect (bind to 0)
        GL30.glBindVertexArray(0); 

        // TODO#PPA1: Initialize keyboard interaction using KeyboardEventDispatcher
 
        // Solution end
    }
    @Override
    public void destroy(GameTime gameTime) {
    	// TODO#PPA1 Solution Start
    	// Dispose of any resources you allocated in build
    	// Specifically you will want to call the dispose methods of your 
    	// GLProgram and GLBuffer(s) if you set them up in build()

        GL30.glBindVertexArray(0);
        GL30.glDeleteVertexArrays(vaoId);
    	
    	// Solution End
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

    public void setupScene(ScenePath p) {
    	// TODO#PPA1 Solution Start
    	// 1) Parse the scene using the ray1 parser
    	// 2) Load the first light in the XML file
    	// 3) Load the meshes in the scene using addMesh()
    	// 4) Load the camera position from the scene
    	//    Note that your camera should look directly at the origin.
    	//    The Matrix4 methods CreatePerspectiveMatrix and CreateLookatMatrix 
    	//    might be helpful here
    	// 5) Send mesh data to the shaders using glUniform* calls. Don't forget to
    	//    rewind your buffers before sending them, and remember that the program
    	//    must be in use before setting these uniforms. The program.getUniform() and
    	//    program.getUniformArray() methods will be useful here.
    	// 6) Don't forget to unuse your program when finished.


        // Solution End
    }
    
    public void addMesh(Mesh m, int meshNum) {
    	// Utility method for adding a mesh to your OpenGL instance

    	MeshData md = m.getMeshData();
    	if (md == null) return;
    	System.out.println(md.toString());

    	// Provide colors to color buffer
    	Shader s = m.getShader();
    	if (s instanceof Lambertian) {
    		// TODO#PPA1 Get the diffuse color values from the shader s and put them in the color buffer
    	} else if (s instanceof Phong) {
    		// TODO#PPA1 Get the diffuse color values from the shader s and put them in the color buffer
    	} else {
    		fbColors.put(DEFAULT_COLOR.x);fbColors.put(DEFAULT_COLOR.y);fbColors.put(DEFAULT_COLOR.z);
    	}

    	if (fbVerts.remaining() >= md.vertexCount && ibTris.remaining() >= md.indexCount/3*4) {
    		// Provide positions to position buffer
    		int last = fbVerts.position() / 3;
    		md.positions.rewind();
    		fbVerts.put(md.positions);

    		// Provide indices to index buffer
    		// Every 4th index should be meshNum; we will use this for color selection
    		for (int i = 0; i < md.indexCount; i++){
    			ibTris.put(md.indices.get(i)+last);
    			if (i % 3 == 2) {
    				ibTris.put(meshNum);
    			}
    		}

    		// TODO#PPA1: update the numTris variable appropriately

    	} else {
    		System.out.println("Not enough space to hold this mesh");
    	}

    	// Solution end
    }
             
    @Override
    public void update(GameTime gameTime) {
        // TODO#PPA1 Solution Start
    	// 1) Using the keyboard class detect when the spacebar is held down
    	// 2) If the spacebar is down, create a rotation matrix based on the GameTime
    	// 3) Use the rotation matrix to alter your camera uniform parameters.
    	// 4) Repeat this for the left shift key and your point light emitter's uniforms. 
        
    	
        // Solution End
    }
    
    @Override
    public void draw(GameTime gameTime) {
    	// TODO#PPA1 Solutuion Start
        // 1) Update the average FPS using gameTime.Elapsed
    	// 2) Use the raytracer GLProgram
    	// 3) Set all uniforms that may have changed since the last frame
    	// 4) After the scene is drawn, unuse the raytracer GLProgram
    	
    	
    	// Performance benchmark 
        // TODO#PPA1: Print time taken per frame to the console
    	
    		
    	// Clear the screen and use your program
    	GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
        
        // TODO#PPA1: Use program and set Uniforms
        

        // Call to bind to the VAO
        GL30.glBindVertexArray(vaoId);

        rasterVerts.useAsAttrib(program.getAttribute("vVertex"));
        // Draw the scene
        GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, 3);

        // Deselect the vertex array
        GL30.glBindVertexArray(0);
        
        // TODO#PPA1: Unuse program

        // Solution end
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
