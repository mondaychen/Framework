package cs4620.common.texture;

import java.awt.image.BufferedImage;
import java.io.InputStream;

import javax.imageio.ImageIO;

import egl.GL;
import egl.NativeMem;
import egl.GL.TextureTarget;
import egl.GL.TextureUnit;
import egl.GL.TextureMagFilter;
import egl.GL.TextureMinFilter;
import ext.java.IOUtils;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL11.glTexParameteri;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL20.glUniform1i;

import java.nio.ByteBuffer;
//import java.nio.ByteBuffer;






//import org.lwjgl.BufferUtils;
import egl.GL.PixelInternalFormat;
import egl.GL.PixelType;
import egl.GL.TextureParameterName;
import egl.math.Color;


public class TexCubeMap {
	private int uintTexCube;
	boolean created;
	
	public TexCubeMap() {
		created = false;
	}
	
	public int getUint() {
		return uintTexCube;
	}
	
	public void createCubeMap(String dir) throws Exception {
		
	}
	
	public boolean use(int textureUnit, int unCubeMap) {
		if(unCubeMap != GL.BadUniformLocation && created) {
			glActiveTexture(textureUnit);
			glBindTexture(TextureTarget.TextureCubeMap, uintTexCube);
			glUniform1i(unCubeMap, textureUnit - TextureUnit.Texture0);
			return true;
		}
		return false;
	}
	

	private Boolean loadCubeMapSide(int uintTexture, int sideTarget, String name) throws Exception{
		
    	
    	return true;
	}
	
}