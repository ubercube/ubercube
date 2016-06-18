#version 330 core
#extension GL_NV_shadow_samplers_cube : enable

out vec4 fragColor;

uniform sampler2D tex;
uniform vec4 in_color;
in vec2 v_coords;
in vec4 v_colors;
uniform int useTexture;
uniform int useVColor;

void main(void)
{
	vec4 textureColor = texture(tex, v_coords);
	fragColor = textureColor * vec4(0, 0, 0, 1);
}
