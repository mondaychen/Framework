package cs4621.Particles;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.LinkedList;

import cs4620.mesh.MeshData;
import cs4620.ray1.camera.Camera;
import egl.GLTexture;
import egl.NativeMem;
import egl.GL.PixelInternalFormat;
import egl.GL.TextureTarget;
import egl.math.*;

/**
 * 
 * ParticleSystem.java
 * 
 * The ParticleSystem class manages a collection of Particle objects with similar appearance 
 * and behavior.
 * 
 * Refactored and added to CS 4621 repository. Originally written by Asher Dunn (ad488).
 * 
 * @author Eric Gao (emg222)
 * @date 2015-11-01
 */
public class ParticleSystem {
    /* Adjustable parameters. */
    public float gravity = 9.8f;
    public float drag    = 1.0f;
    public float wind    = 1.0f;
    
    /* Array of dead/waiting particles which can be spawned during `animate()`. */
    private LinkedList<Particle> mUnspawnedParticles = new LinkedList<Particle>();
    public LinkedList<Particle> mSpawnedParticles = new LinkedList<Particle>();
    private float mTimeSinceLastSpawn;
    
    private int totalNumParticles;
    
    /* Texture used for all particles */
    public GLTexture particleTexture;
    
    /* Particle system state */
    public boolean mPaused = true;
    
    /* billboardTransform refers to the rotation you must apply to the quads that represent 
     * the particles so that they face the camera. This is updated through the 
     * billboard() method. An identity matrix means the camera is at the position (0, 0, z). */
    private Matrix4 billboardTransform = new Matrix4();
    
    /**
     * Creates a particle system with a certain maximum number of particles.
     * @param maxParticles The maximum number of particles which can exist at a single time. 
     *        Particles are created and destroyed in `animate()` depending on the behavior 
     *        of this particular system.
     */
    public ParticleSystem(int maxParticles) {
        totalNumParticles = maxParticles;
        
        // Load in the texture that will be used by all particles
        particleTexture = new GLTexture(TextureTarget.Texture2D, true);
        particleTexture.internalFormat = PixelInternalFormat.Rgba;
        try {
            particleTexture.setImage2D("data/textures/Blur.png", false);
        } catch (Exception e) {
            System.out.println("Could not load particle texture.\r\n" + e.getMessage());
        }
        
        // Create a single quad which will be shared by all particles in this system.
        FloatBuffer vertices  = NativeMem.createFloatBuffer(12);
        vertices.put(new float[]{-1.0f, -1.0f, 0.0f,
                                  1.0f, -1.0f, 0.0f,
                                  1.0f,  1.0f, 0.0f,
                                 -1.0f,  1.0f, 0.0f});
        
        FloatBuffer texcoords = NativeMem.createFloatBuffer(8);
        texcoords.put(new float[]{0.0f, 0.0f,  
                                  1.0f, 0.0f, 
                                  1.0f, 1.0f, 
                                  0.0f, 1.0f});
        
        IntBuffer quads = NativeMem.createIntBuffer(6);
        quads.put(new int[]{0, 1, 2, 0, 2, 3});
        
        // Create a random cloud of particles.
        for (int i = 0; i < maxParticles; ++i) {
            MeshData data = new MeshData();
            data.indexCount = 6;
            data.vertexCount = 4;
            data.positions = vertices;
            data.indices = quads;
            data.uvs = texcoords;
            
            Particle particle = new Particle(data);
            
            // TODO:PPA3 Feel free to play with the color!
            particle.setColor(Math.random(), 
                              0.5 + 0.5 * Math.random(),
                              0.5 + 0.5 * Math.random());

            
            particle.setScale(0.1f);
            mUnspawnedParticles.add(particle);
        }
    }

    /**
     * Create, destroy, and move particles.
     */
    public void animate(float dt) {
        // TODO#PPA3 SOLUTION START
        // Animate the particle system:
        // 1.) If the particle system is paused, return immediately.
        // 2.) Update the time since last spawn, and if a sufficient amount of time has
        //     elapsed since the last particle has spawned, spawn another if you can.
        //     This spawned particle should have some random initial velocity upward in the +y 
        //     direction and its position should be (0, -0.5, 0).
        // 3.) Remove the particle from the linked list of unspawned particles and put it
        //     onto the linked list of spawned particles.
        // 4.) For each spawned particle:
        //          - Accumulate forces: gravity should move the particle in -y direction
        //                               wind should move the particle in the +x direction
        //                               particle should be slowed down by the drag force.
        //          - Animate each particle according to these new forces.
        //          - Check if the particle is too old. If it is, remove it from the 
        //            linked list of spawned particles and append it to the linked list of
        //            unspawned particles.
        
        
        //ENDSOLUTION
    }
    
    /**
     * Points all particles in this system towards the camera.
     */
    public void billboard(Matrix4 view) {
        // TODO#PPA3 SOLUTION START
        // Set the billboardTransform so that if you multiply the particle's quad by this matrix
        // the particle is always facing the camera.
        // 1.) Obtain the inverse of the rotation of the camera.
        // 2.) Set billboardTransform.
        
        // SOLUTION END
    }
    
    public void reset() {
        int numSpawned = mSpawnedParticles.size();
        for(int i = 0; i < numSpawned; ++i) {
            mUnspawnedParticles.add(mSpawnedParticles.removeFirst());
        }
        
        // reset parameters
        gravity = 9.8f; drag = 1.0f; wind = 1.0f;
    }
    
    public Matrix4 getBillboardTransform() {
        return billboardTransform;
    }
}