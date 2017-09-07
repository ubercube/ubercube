#version 330 core
#extension GL_NV_shadow_samplers_cube : enable

out vec4 fragColor;
in vec4 v_color;

void main(void)
{
	fragColor = v_color;
}
