#version 120

// Lighting Information
const int MAX_LIGHTS = 16;
uniform vec3 lightPosition[MAX_LIGHTS];

// RenderCamera Input
uniform mat4 mViewProjection;

// RenderObject Input
uniform mat4 mWorld;
uniform mat3 mWorldIT;

// RenderMesh Input
attribute vec4 vPosition; // Sem (POSITION 0)
attribute vec3 vNormal; // Sem (NORMAL 0)

varying vec3 fN; // Normal at the vertex in world-space coordinates
varying vec4 worldPos; // Vertex position in world-space coordinates

const float PI = 3.1415926535;
// The height of the flower in object coordinates
const float height = 3.0;


void main() {
  float L_x = sqrt(lightPosition[0].x * lightPosition[0].x +
                   lightPosition[0].z * lightPosition[0].z);
  float L_y = lightPosition[0].y;

  if (L_x < 0.00001) {
    // If the light is too close to directly above the flower, the math
    // for the bending of the flower becomes unstable, so just render
    // the unbent flower

    // TODO#PPA2 SOLUTION START

    // Calculate Point In World Space
      worldPos = mWorld * vPosition;
      
    // Calculate Projected Point
      gl_Position = mViewProjection * worldPos;
      
    // Calculate normal
      fN = normalize((mWorldIT * vNormal).xyz);
      
      //gl_Position = vec4(0,0,0,0);
    // SOLUTION END

  } else {
    // These matrices map between the frame of thflower mesh's vertices and a frame in which
    // the light lies on the z>0 part of the x-y plane
    mat4 frameToObj = mat4(lightPosition[0].x / L_x, 0, lightPosition[0].z / L_x, 0,
                           0,                        1,  0,                        0,
                           -lightPosition[0].z / L_x, 0,  lightPosition[0].x / L_x, 0,
                           0,                        0,  0,                        1);

    // Find inverse of frameToObj
    mat4 objToFrame = transpose(frameToObj);

    // TODO#PPA2 SOLUTION START

    // Calculate the angle theta from the diagram in pa2a.pdf
    // Calculate the value "R" and its inverse according to the formula
    // for the bending of the flower
    // find vertex/normal in axis-aligned frame
    // find transformed vertex/normal in local frame
    // map transformed vertex/normal back to object frame and to eye/screen space
//      
//    
    float theta = atan(L_y, L_x);
    float phi = PI / 2 - theta;
    float r = height / phi;
    float index = vPosition.y;
    float arpha = index * phi / height ;
    
 
    mat4 Vrotation = mat4(cos(-arpha * 0.6), sin(-arpha * 0.6),  0, 0,
                          -sin(-arpha  * 0.6), cos(-arpha * 0.6), 0, 0,
                           0,           0,            1, 0,
                           0,           0,            0, 1);
      
      
    mat4 Nrotation = mat4(cos(-arpha), sin(-arpha ),  0, 0,
                          -sin(-arpha), cos(-arpha ), 0, 0,
                          0,           0,            1, 0,
                          0,           0,            0, 1);

      worldPos = mWorld * frameToObj * Vrotation * objToFrame * vPosition;
      gl_Position = mViewProjection * worldPos;
      
      
     
      fN = normalize(mWorldIT * (frameToObj * Nrotation * objToFrame  * vec4(vNormal, 0)).xyz);

      
      
      
      

    // SOLUTION END
  }
}
