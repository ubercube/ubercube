#version 330 core
#extension GL_NV_shadow_samplers_cube : enable

out vec4 fragColor;

uniform samplerCube map;
uniform vec3 cameraPosition;
uniform vec4 in_color;

in vec3 v_normal;
in vec3 worldPosition;

void main(void)
{
	vec3 eyeDirection = normalize(cameraPosition - worldPosition);
	vec3 reflectDirection = reflect(-eyeDirection, v_normal);
	vec4 reflectionColor = textureCube(map, reflectDirection);
	float dotFactor = dot(eyeDirection, v_normal) * 0.5 + 0.5;
	fragColor = vec4(reflectionColor.xyz * dotFactor, 1.0);
}
