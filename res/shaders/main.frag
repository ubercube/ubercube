#version 330 core
#extension GL_NV_shadow_samplers_cube : enable
#define FOG_COLOR vec4(221.0 / 255.0, 232.0 / 255.0, 255.0 / 255.0, 1.0)

out vec4 fragColor;

uniform vec3 cameraPosition;
uniform float fogDistance;
uniform vec4 in_color;

in vec3 v_position;
in float v_light;
in vec3 worldPosition;

void main(void)
{
	float dist = length(v_position + vec3(1, 1, 1)) * 0.2 + 1;
	fragColor = vec4(in_color.rgb * v_light / dist, 1.0) * in_color.a;
}
