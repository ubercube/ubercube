#version 330 core
#extension GL_NV_shadow_samplers_cube : enable

out vec4 fragColor;

uniform sampler2D tex;

in vec2 v_coords;

void main(void)
{
	fragColor = texture(tex, v_coords) * vec4(1, 1, 1, 1);
}
