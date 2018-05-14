#version 330 core
#extension GL_NV_shadow_samplers_cube : enable

out vec4 fragColor;

uniform sampler2D tex;

in vec2 v_coords;

void main(void)
{
	float depthValue = texture(tex, v_coords).r;
	fragColor = vec4(vec3(depthValue), 1.0);
}