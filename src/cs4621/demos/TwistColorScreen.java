package cs4621.demos;

import static org.lwjgl.opengl.GL11.GL_TRIANGLES;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import blister.GameScreen;
import blister.GameTime;
import egl.GL.BufferUsageHint;
import egl.GL.GLType;
import egl.GLBuffer;
import egl.GLProgram;
import egl.GLUniform;
import egl.RasterizerState;
import egl.math.Matrix4;
import egl.math.Vector3;


public class TwistColorScreen extends GameScreen {

	GLBuffer vb, ib;
	int indexCount;
	
	GLProgram program;
	
	@Override
	public int getNext() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected void setNext(int next) {
		// TODO Auto-generated method stub

	}

	@Override
	public int getPrevious() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected void setPrevious(int previous) {
		// TODO Auto-generated method stub

	}

	@Override
	public void build() {
		// TODO Auto-generated method stub

	}

	@Override
	public void destroy(GameTime gameTime) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onEntry(GameTime gameTime) {
		program = new GLProgram(false);
		program.quickCreateResource("GreenTriangle", "cs4621/demos/shaders/twisting_color.vert", "cs4621/demos/shaders/twisting_color.frag", null);
        
		// Set triangle vertex positions
//		float [] vertexPositions;
//		int [] quadIndices;
//		int count = 200;
//		float size = 1.0f / count; 
//		//TASK: create vertices and indices
//		    						
//		vb = GLBuffer.createAsVertex(vertexPositions, 2, BufferUsageHint.StaticDraw);
//		ib = GLBuffer.createAsIndex(quadIndices, BufferUsageHint.StaticDraw);
//		indexCount = quadIndices.length;
		
		RasterizerState.CULL_NONE.set();
		GL11.glClearDepth(1.0);
		GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
	}

	@Override
	public void onExit(GameTime gameTime) {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(GameTime gameTime) {
		// TODO Auto-generated method stub

	}

	@Override
	public void draw(GameTime gameTime) {
		GL11.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
		
		
		program.use();

           //legacy approach
		
		   int count = 200;
		    float size = 1.0f / count;
		    
		    //GL11.glBegin(GL11.GL_QUADS); 
		    GL11.glBegin(GL11.GL_POINTS);
		    
		    for(int i=0;i<count;i++)
		    	for(int j=0;j<count;j++)	    
		    	{
		    		float x = -0.5f + i * size;
		    		float y = -0.5f + j * size;

		    		GL11.glVertex2f(x,     y);
		    		
//		    		GL11.glVertex2f(x,     y);
//		    		GL11.glVertex2f(x+size, y);
//		    		GL11.glVertex2f(x+size, y+size);
//		    		GL11.glVertex2f(x,      y+size);
		    	}

		    GL11.glEnd();
		    

		
		GLUniform.set(program.getUniform("color"), new Vector3(1, 1, 1));
		GL20.glUniform1f(program.getUniform("twisting"), (float)gameTime.total);
		
		//Use mesh vertices and indices
		//vb.useAsAttrib(program.getAttribute("vPos"));
		//ib.bind();
		//GL11.glDrawElements(GL_TRIANGLES, indexCount, GLType.UnsignedInt, 0);
		//ib.unbind();
		
		GLProgram.unuse();
	}

}
