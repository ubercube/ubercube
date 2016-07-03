#version 120
#extension GL_NV_shadow_samplers_cube : enable

uniform sampler2D tex;
uniform vec4 in_color;
varying vec2 v_coords;
varying vec4 v_colors;
uniform int useTexture;
uniform int useVColor;

void main(void)
{
	vec4 textureColor = texture2D(tex, v_coords);
	vec4 color = in_color;
	if (useVColor == 1)
		color *= v_colors;
	if (useTexture == 1)
		color *= textureColor;
	gl_FragColor = color;
}
