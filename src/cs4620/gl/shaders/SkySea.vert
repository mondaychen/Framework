#version 120

// Note: We multiply a vector with a matrix from the left side (M * v)!
// mProj * mView * mWorld * pos

uniform float time;

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
varying vec3 sunDir;

const float SUN_RADIUS = 1000;
const float PI = 3.1415926;

void main() {
    
    //Modify the y-axis value to control the sun go up and down:
    //negative value_going up
    //postive value_going down
    float theta = time / 10 + PI / 2;
    sunPositon = SUN_RADIUS * vec3(0.0, sin(theta), 1);
   
    //sunPositon = vec3(0.0, -150.0, 1000.0);
    
    //Setting the sundirection;
    sunDir =normalize(vec3(0.0, 0.0, 1.0));


	worldPos = mWorld * vPosition;
    

 	gl_Position = mViewProjection * worldPos;
  
}

