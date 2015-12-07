#version 120

// Note: We multiply a vector with a matrix from the left side (M * v)!
// mProj * mView * mWorld * pos

// RenderCamera Input
uniform mat4 mViewProjection;

// RenderObject Input
uniform mat4 mWorld;
uniform vec3 worldCam;

uniform mat3 mWorldIT;


// RenderMesh Input
attribute vec4 vPosition; // Sem (POSITION 0)

varying vec4 worldPos;
varying vec3 sunPositon;




//
//void updateSunPostion() {
//    float key = time / 180 / 10;
//    vec3 sunPositon = SUN_RADIUS * vec3(sin(key), cos(key), 0);
//}

void main() {
  // TODO A4
    
  //updateSunPostion();
  vec4 worldPos = mWorld * vPosition;
  
  //Set the ray from the camera to intersect point;
    
  gl_Position = mViewProjection * worldPos;
  
}

