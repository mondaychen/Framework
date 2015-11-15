package cs4621.Particles;

import egl.math.*;
import cs4620.mesh.MeshData;
import cs4620.ray1.camera.Camera;
import cs4620.ray1.surface.Mesh;

/**
 * Particle.java
 * 
 * Particle objects are owned by a ParticleSystem and handle animating a single particle.
 * 
 * Refactored and added to CS 4621 repository. Originally written by Asher Dunn (ad488).
 * 
 * @author Eric Gao (emg222)
 * @date 2015-11-01
 */
public class Particle extends Mesh {
    /* Particle state. */
    private Vector3 mForcesThisFrame = new Vector3();
    private Vector3 mVelocity = new Vector3();
    private Vector3 mPosition = new Vector3();
    private float mMass = 0.0f;
    private float mAge = 0.0f;
    
    /* Rendering information */
    private Colord mColor = new Colord(); // color of the particle
    private float mScale = 1.0f;          // size of the particle
    
    /**
     * Default constructor. You must call `spawn()` for the particle to be ready to use.
     */
    public Particle() {
        /* nothing */
    }
    
    public Particle(MeshData data) {
        super(data);
    }

    /**
     * Spawns or respawns this particle with the passed attributes.
     * 
     * @param mass The mass of the new particle.
     * @param initialPosition The initial position of the particle.
     * @param initialVelocity The initial velocity of the particle.
     * 
     * @return Returns self to facilitate patterns like `myParticleSystem.addChild(myParticle.spawn(...))`.
     */
    public Particle spawn(float mass, Vector3 initialPosition, Vector3 initialVelocity) {
        mMass = mass;
        mPosition.set(initialPosition);
        mVelocity.set(initialVelocity);
        mAge = 0.0f;
        
        return this;
    }
    
    /**
     * Returns the time since this particle spawned, in seconds.
     */
    public float getAge() {
        return mAge;
    }
    
    /**
     * Returns the current velocity of this particle.
     */
    public Vector3 getVelocity() {
        return mVelocity;
    }
    
    /**
     * Adds a force to this particle for the current frame.
     */
    public void accumForce(Vector3 force) {
        mForcesThisFrame.add(force);
    }
    
    /**
     * Resets accumuated forces for the next animation frame. 
     */
    public void resetForces() {
        mForcesThisFrame.set(0.0f, 0.0f, 0.0f);
    }
    
    public void setColor(Colord c) {
        mColor.set(c);
    }
    
    public void setColor(double r, double g, double b) {
        mColor.set(r, g, b);
    }
    
    public Colord getColor() {
        return mColor;
    }
    
    public void setScale(float s) {
        mScale = s;
    }
    
    public float getScale() {
        return mScale;
    }
    
    public void setParticlePosition(Vector3 position) {
        mPosition = position;
    }
    
    public Vector3 getParticlePosition() {
        return mPosition;
    }

    /**
     * Updates velocity and position by integrating applied forces for the current frame, and 
     * then resets the applied force accumulator. Apply forces with the `accumForce()` function.
     * @param dt Time step since the last frame, in seconds.
     */
    public void animate(float dt) {
        // TODO#PPA3 SOLUTION START
        // Update the particle's position given the forces acting on the particle this frame.
        // 1.) Obtain the acceleration.
        // 2.) Update the velocity based on the acceleration.
        // 3.) Update the position based on the velocity.
        // 4.) Update the particle's age.

        // SOLUTION END
    }
}