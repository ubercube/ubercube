#version 330 core
#extension GL_NV_shadow_samplers_cube : enable

out vec4 fragColor;

uniform sampler2D tex;
uniform int use_color;
uniform vec4 in_color;

in vec2 v_coords;

void main(void)
{
	if (use_color == 1)
		fragColor = texture(tex, v_coords) * in_color;
	else
		fragColor = texture(tex, v_coords) * vec4(1, 1, 1, 1);
}
