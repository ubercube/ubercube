#version 330 core
#extension GL_NV_shadow_samplers_cube : enable
#define FOG_COLOR vec4(221.0 / 255.0, 232.0 / 255.0, 255.0 / 255.0, 1.0)

out vec4 fragColor;

in vec4 v_color;

void main(void)
{
	fragColor = v_color;
}
