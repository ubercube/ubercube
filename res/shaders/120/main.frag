#version 120
#extension GL_NV_shadow_samplers_cube : enable
#define FOG_COLOR vec4(221.0 / 255.0, 232.0 / 255.0, 255.0 / 255.0, 1.0)

uniform vec3 cameraPosition;
uniform float fogDistance;
uniform vec4 in_color;

varying vec3 v_position;
varying float v_light;
varying vec3 worldPosition;

void main(void)
{
	float dist = length(v_position + vec3(1, 1, 1)) * 0.2 + 1;
	gl_FragColor = vec4(in_color.rgb * v_light / dist, 1.0) * in_color.a;
}
