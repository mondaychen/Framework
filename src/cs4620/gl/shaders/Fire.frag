#version 120

// You May Use The Following Functions As RenderMaterial Input
// vec4 getDiffuseColor(vec2 uv) // samples fire.png
// vec4 getNormalColor(vec2 uv)  // samples noise.png

uniform float time;

const vec3 texture_scales = vec3(1.0, 2.0, 3.0);
const vec3 scroll_speeds = vec3(1.0, 1.0, 1.0);

varying vec2 fUV;
varying vec3 fPos;

void main() {
  // TODO#PPA2 SOLUTION START

  vec2 noiseUVs[3] = vec2[](fUV, fUV, fUV);
  vec2 textureUV = vec2(0, 0);
  vec4 finalColor = vec4(0);
  for (int i = 0; i < 3; i++) {
    noiseUVs[i] *= texture_scales[i];
    noiseUVs[i].y += scroll_speeds[i] * time;
    // put it in [0, 1]
    noiseUVs[i].x -= int(noiseUVs[i].x);
    noiseUVs[i].y -= int(noiseUVs[i].y);

    textureUV += getNormalColor(noiseUVs[i]).xy;
    finalColor += getDiffuseColor(getNormalColor(noiseUVs[i]).xy);
  }
  textureUV /= 3;
  finalColor /= 3;

	gl_FragColor = getDiffuseColor(textureUV);
  gl_FragColor = finalColor;
  // SOLUTION END
}