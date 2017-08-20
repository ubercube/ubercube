#version 330 core
#extension GL_NV_shadow_samplers_cube : enable
#define FOG_COLOR vec4(221.0 / 255.0, 232.0 / 255.0, 255.0 / 255.0, 1.0)

out vec4 fragColor;

uniform vec3 cameraPosition;
uniform float fogDistance;
uniform vec4 in_color;

in vec4 v_color;
in vec3 worldPosition;

void main(void)
{
	float dist = distance(cameraPosition, worldPosition) / fogDistance * 2 - 0.8;
	if (dist > 1)
		dist = 1;
	if (dist < 0)
		dist = 0;

	fragColor = mix(v_color, FOG_COLOR, dist);
}
