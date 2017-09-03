#version 330 core
#extension GL_NV_shadow_samplers_cube : enable

out vec4 fragColor;

uniform sampler2D tex;
uniform vec4 in_color;
in vec2 v_coords;
uniform int useTexture;

void main(void)
{
	vec4 textureColor = texture(tex, v_coords);
	vec4 color = in_color;
	if (useTexture == 1)
		color *= textureColor;
	fragColor = color;
}
