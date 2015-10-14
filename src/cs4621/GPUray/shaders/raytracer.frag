#version 330

// Input from vertex shader (location on our camera plane)
in vec2 vUV;

// Constants for defining uniform array sizes
// In GLSL 330, uniforms arrays must have constant length
const int MAX_VERTS = 128;
const int MAX_TRIS = 256+64+8;
const int MAX_COLORS = 16;

// Axis-Aligned Bounding Box
const vec3 aabb_max = vec3(1000, 1000, 1000);
const vec3 aabb_min = vec3(-1000, -1000, -1000);

// The output color, of form (red,green,blue,alpha)
// The job of your fragment shader is to set this correctly.
out vec4 vFragColor;

// Uniforms
// TODO#PPA1 Solution Start
// Add any uniforms you need to pass in from java
uniform ivec3 triangles[MAX_TRIS];
uniform vec3 vertices[MAX_VERTS];
uniform vec3 normals[MAX_VERTS];
uniform vec3 colors[MAX_COLORS];
uniform vec3 light;
uniform vec3 diffuseColor;
uniform vec3 cameraOrigin;
uniform mat4 mVP;
uniform int debug_state;

// Solution End


// helper functoins for intersectCube
float rayIntersectPlane(vec3 origin, vec3 ray, vec3 planePoint, vec3 norm) {
  // t = (PlanePoint - origin) dot normal / ray dot normal
  return dot(planePoint - origin, normal) / dot(ray, normal);
}

bool isInCube(float t, vec3 cube_min, vec3 cube_max) {
  return t > cube_min.x && t > cube_min.y && t > cube_min.z &&
           t < cube_max.x && t < cube_max.y && t < cube_max.z;
}

// Function to return the intersection of a ray with a box.
// Returns a vec2 in which the x value contains the t value at the near intersection
// and the y value contains the t value at the far intersection.
vec2 intersectCube(vec3 origin, vec3 ray, vec3 cube_min, vec3 cube_max) {
  // TODO#PPA1 Solution Start
  vec2 result = vec2(-1, -1);

  // Implement axis-aligned box intersection here
  float nums[6] = {
    rayIntersectPlane(origin, ray, cube_min, vec3(0, 0, cube_max.z - cube_min.z)),
    rayIntersectPlane(origin, ray, cube_min, vec3(0, cube_max.y - cube_min.y), 0),
    rayIntersectPlane(origin, ray, cube_min, vec3(cube_max.x - cube_min.x), 0, 0),
    rayIntersectPlane(origin, ray, cube_max, vec3(0, 0, cube_max.z - cube_min.z)),
    rayIntersectPlane(origin, ray, cube_max, vec3(0, cube_max.y - cube_min.y), 0),
    rayIntersectPlane(origin, ray, cube_max, vec3(cube_max.x - cube_min.x), 0, 0),
  };

  for (int i = 0; i < 6; i++) {
    if (nums[i] > 0 && isInCube(nums[i], cube_min, cube_max)) {
      if (result.x > 0) {
        result.y = nums[i];
      } else {
        result.x = nums[i];
      }
      if (result.x > 0 && result.y > 0) {
        break;
      }
    }
  }

  if (result.x > result.y) {
    return result.yx;
  }
  return result;

  // Solution End
}


// Gets the direction given a 2D position p,
// and a Camera with basis U, V, W and projection distance d.
vec3 get_direction(vec2 p, vec3 U, vec3 V, vec3 W, float d) {
  // TODO#PPA1 Solution Start

  // Return the direction towards a point p on UV plane a distance
  // d away from the camera along W.
  return U * p.x + V * p.y + U * d;

  // Solution End
}

// Generates the eye ray for the given camera and a 2D position.
void setup_camera(vec2 uv, inout vec3 eyeRayOrigin, inout vec3 eyeRayDir, 
                  inout vec3 camU, inout vec3 camV,  inout vec3 camW,                  
                  inout float camd) {

  // TODO#PPA1 Solution Start

  // 1) Set the eyeRayOrigin to the view point of your camera
  // 2) Set camU to the first column of invMVP
  //    Set camV to the second,
  //    Set camW to the third.
  //    Set camd to the projection distance
  // 3) Set eyeRayDir the direction of the eyeRay
  eyeRayOrigin = cameraOrigin;
  eyeRayDir = vec3(uv.x, uv.y, 1) - cameraOrigin;
  camU = mVP[0];
  camV = mVP[1];
  camW = mVP[2];
  camd = length(eyeRayDir); //??

  // Solution End
}


// Ray triangle intesection routine. The normal is returned in the given
// normal reference argument.
vec4 intersectTriangle(vec3 origin, vec3 dir, int index, inout vec3 normal ) {
  // TODO#PPA1 Solution Start

  // 1) Get the triangle indices
  // 2) Use indices to acquire the three vertices of the triangle
  // 3) Use these indices to also acquire normals if provided. Otherwise, the normal can be
  //    calculated by the vertex poisitions.
  // 4) Find t, beta, and gamma using Cramer's Rule
  //    Note that you will have to define a-f yourself this time
  // 5) If beta, gamma, or (implicitly) alpha are out of bounds
  //    then return vec4(-1,0,0,0) to mark a miss
  // 6) If there is a hit, set the "inout" normal variable
  // 7) Return a vec4 containing (t, beta, gamma, i) where i is the
  //    index of the color for the given triangle

  vec3 v0 = vertices[triangles[index][0]];
  vec3 v1 = vertices[triangles[index][1]];
  vec3 v2 = vertices[triangles[index][2]];

  float a, b, c, d, e, f, g, h, i, j, k, l;

  a = v0.x-v1.x;
  b = v0.y-v1.y;
  c = v0.z-v1.z;

  d = v0.x-v2.x;
  e = v0.y-v2.y;
  f = v0.z-v2.z;

  g = dir.x;
  h = dir.y;
  i = dir.z;
  j = v0.x - dir.x;
  k = v0.y - dir.y;
  l = v0.z - dir.z;

  float M = a * (e * i - h * f) + b * (g * f - d * i) + c * (d * h - e * g);
  float beta = ( j * (e * i - h * f) + k * (g * f - d * i) + l * (d * h - e * g) ) / M;
  float gamma = ( i * (a * k - j * b) + h * (j * c - a * l) + g * (b * l - k * c) )/ M;
  float t = - ( f * (a * k - j * b) + e * (j * c - a * l) + d * (b * l - k * c) )/ M;

  if (gamma < 0 || gamma > 1) {
    return vec4(-1,0,0,0);
  }

  if (beta < 0 || beta > 1 - gamma) {
    return vec4(-1,0,0,0);
  }

  // how to get normals????
  if (hasNormals) {
    vec3 n0 = normals[triangles[index][0]];
    vec3 n1 = normals[triangles[index][1]];
    vec3 n2 = normals[triangles[index][2]];
    normal = (1 - gamma - beta) * n0 + beta * n1 + gamma * n2;
  } else {
    vec3 e0 = v1 - v0;
    vec3 e1 = v2 - v0;
    normal = cross(e0, e1);
  }


  return vec4(t, beta, gamma, i);

  // Solution End
}

// Function that tests if a ray intersects any object in the scene.
// If so, it returns 0.5; otherwise 1. This value is used to scale
// the output color, simulating shadow.
float compute_shadow(vec3 origin, vec3 dir ) {
	// TODO#PPA1 Solution Start

  //    Iterate through all triangles
  //    if the ray intersects a triangle, return 0.5
  //    Otherwise return 1.0
  vec3 normal = vec3(0,0,0);
  for (int i = 0; i < triangles.length(); i++) {
    if (intersectTriangle(origin, dir, i, normal) != vec4(-1,0,0,0)) {
      return 0.5;
    }
  }

  return 1.0;

  // Solution End
}

// Function to compute lambertian shading
vec3 shade_lambertian(vec3 normal, vec3 light_dir, vec3 mesh_color) {
  // TODO#PPA1 Solution Start
  float lightDotNormal = dot(light_dir, normal);
  if (lightDotNormal < 0) {
    lightDotNormal = 0;
  }
  // Return the RGB vector obtained from the lambertian shading model

  return mesh_color * lightDotNormal;
  // ??? intensity and distance?
  // Solution End
}

void main() {

  //set the maximum t value
  float t = 100000000;

  // TODO#PPA1 Solution Start

  // Set the initial fragment colour to the scene's background colour
  // You will have to make a new uniform for this
  vFragColor = vec4(0,0,0,1);

  // Solution End

  // Setup the camera
  vec3 origin;
  vec3 dir;
  vec3 camU;
  vec3 camV;
  vec3 camW;
  float camd;
  setup_camera(vUV, origin, dir, camU, camV, camW, camd);

  // check if the ray intersects the scene bounding box
  vec2 tNearFar = intersectCube(origin, dir, aabb_min, aabb_max);

  if (tNearFar.x<tNearFar.y) {
    t = tNearFar.y+1; //offset the near intersection to remove the depth artifacts

    // TODO#PPA1 Solution Start

    // 1) Trace ray through the whole triangle list and see if we have a intersection
    // 2) If there is a valid intersection:
    //    a) Get intersection point
    //    b) Get direction from the intersection point to the light
    //    c) Check for shadows
    //    d) Set vFragColor according to the current debug state
    //       Debug state 0: color = lambertian + shadows
    //                      remember to include the mesh color, the diffuse modifier,
    //                      and the shadowing in your final color
    //       Debug state 1: color = normal directions
    //       Debug state 2: color = intersection location
    //       Debug state 3: color = white if not shadowed, else 50% grey


    // Solution End
  }
  
}
