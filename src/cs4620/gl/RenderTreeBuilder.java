package cs4620.gl;

import java.util.ArrayList;
import java.util.HashMap;

import cs4620.common.Cubemap;
import cs4620.common.Material;
import cs4620.common.Mesh;
import cs4620.common.Scene;
import cs4620.common.SceneCamera;
import cs4620.common.SceneLight;
import cs4620.common.SceneObject;
import cs4620.common.Texture;
import egl.math.Matrix3;
import egl.math.Matrix4;
import egl.math.Vector2;

/**
 * Class to hold functions that build the scene hierarchy for OpenGL rendering, starting from the common 
 * Scene hierarchy that is parsed from XML.  These rendering data structures correspond to the objects 
 * and resources in the scene but contain extra state and information used for rendering and interaction.
 * 
 * @author cristian
 */
public class RenderTreeBuilder {

	/**
	 * Build a RenderEnvironment that can be used to render the given scene, by constructing the corresponding
	 * rendering objects for the scene hierarchy and the meshes, textures, and materials it uses.
	 * 
	 * @param scene  the Scene environment to be rendered
	 * @param viewSize  the size of the viewport in pixels
	 * @return
	 */
	public static RenderEnvironment build(Scene scene, Vector2 viewSize) {
		RenderEnvironment env = new RenderEnvironment(viewSize);
		
		// Resources
		buildTextures(scene, env);
		buildMeshes(scene, env);
		buildCubemaps(scene, env);
		// Render-able State Is Created Here
		buildMaterials(scene, env);
		buildTree(scene, env);
		
		return env;
	}
	
	/**
	 * Build a tree of RenderObjects that mirrors the tree of SceneObjects in <scene>.
	 * 
	 * After the objects are created and linked up using the RenderObject.parent and RenderObject.children 
	 * fields, the frame-to-world transformations of all objects are computed.
	 *  
	 * @param scene
	 * @param env
	 */
	public static void buildTree(Scene scene, RenderEnvironment env) {
		// Clear Out Any Old Data
		env.cameras.clear();
		env.lights.clear();
		
		// Pass 1: Create The Render Object Mapping
		HashMap<String, RenderObject> dict = new HashMap<>();
		for(SceneObject so : scene.objects) {
			RenderObject ro;		
			if(so instanceof SceneCamera) {
				ro = new RenderCamera(so, env.viewportSize);
				env.cameras.add((RenderCamera)ro);
			}
			else if(so instanceof SceneLight) {
				ro = new RenderLight(so);
				env.lights.add((RenderLight)ro);
			}
			else {
				ro = new RenderObject(so);
			}
			dict.put(so.getID().name, ro);

		}

		// Pass 2: Create Parent-Children Bindings
		for(SceneObject so : scene.objects) {
			if(so.parent != null) {
				// Get The Child
				RenderObject o = dict.get(so.getID().name);
				if(o != null) {
					// Get The Parent
					RenderObject p = dict.get(so.parent);
					if(p != null) {
						// Bind Child And Parent
						p.children.add(o);
						o.parent = p;
					}
				}
			}
		}

		// Pass 3: Find A Root Node If It Exists
		env.root = dict.get("World");
		rippleTransformations(env);
		
		// Set Up Render State
		env.linkObjectResources();
	}
	
	/**
	 * Compute the frame-to-world transformations for all objects in the hierarchy.
	 * 
	 * The root node's frame-to-world transformation is simply its own transformation, and
	 * the frame-to-world transformation of every other object in the scene is the composition
	 * of its parent's frame-to-world transformation with the object's own transformation.
	 * 
	 * The results are stored in the RenderObject.mWorldTransform field of each object
	 * in the hierarchy.  At the same time, the inverse-transpose matrices, which are used
	 * in transforming normal vectors, are stored in the RenderObject.mWorldTransformIT.
	 * 
	 * Additionally, for all the cameras in the scene (RenderEnvironment.cameras) you must
	 * recalculate the camera's ViewPerspectiveProjection matrix.
	 * 
	 * @param env  The environment containing the hierarchy to be processed.
	 */
	public static void rippleTransformations(RenderEnvironment env) {
		// TODO#A3 SOLUTION START	
		// store the matrix4 and matrix3 in every node;
		env.root.mWorldTransform.set(env.root.sceneObject.transformation);
		env.root.mWorldTransformIT.set(env.root.sceneObject.transformation.getAxes().clone().invert().transpose());
		set(env.root, env);	
		//update camera;
		ArrayList<RenderCamera> cameras = env.cameras;
		for(RenderCamera cam : cameras) {
			cam.updateCameraMatrix(cam.viewportSize);
		}
	}
	// SOLUTION END
	
	//To get the mWorldtransform of every node;
	public static Matrix4 gettransform(RenderObject node, RenderEnvironment env) {
		return node.sceneObject.transformation.clone().mulBefore(node.parent.sceneObject.transformation);
	}
	
	//To get the mWorldTransformIT of every node;
	public static Matrix3 gettransformIT(RenderObject node) {
		  return node.sceneObject.transformation.getAxes().clone().invert().transpose();
		//to calculate the mWorldTransformIT for every node;
		
	}
	//To get the child node of each node;
	public static void set(RenderObject node, RenderEnvironment env) {
		if(node.children.size() == 0) return;
		for(RenderObject childnode : node.children) {
			set(childnode, env);
			childnode.mWorldTransform.set(gettransform(childnode,env));
			childnode.mWorldTransformIT.set(gettransformIT(childnode));
		}	
	}
	
	
	
	/**
	 * Make a RenderMaterial for each Material in <scene>.
	 * @param scene  the Scene to read from
	 * @param env  the RenderEnvironment to write to
	 */
	public static void buildMaterials(Scene scene, RenderEnvironment env) {
		for(Material m : scene.materials) {
			env.addMaterial(m);
		}
	}

	/**
	 * Make a RenderMesh for each Mesh in <scene>.
	 * @param scene  the Scene to read from
	 * @param env  the RenderEnvironment to write to
	 */
	public static void buildMeshes(Scene scene, RenderEnvironment env) {
		for(Mesh m : scene.meshes) {
			env.addMesh(m);
		}
	}
	
	public static void buildCubemaps(Scene scene, RenderEnvironment env) {
		for(Cubemap c : scene.cubemaps) {
			env.addCubemap(c);
		}
	}

	/**
	 * Make a GLTexture for each Texture in <scene>.
	 * @param scene  the Scene to read from
	 * @param env  the RenderEnvironment to write to
	 */
	public static void buildTextures(Scene scene, RenderEnvironment env) {
		for(Texture t : scene.textures){ 
			env.addTexture(t);
		}
	}
}