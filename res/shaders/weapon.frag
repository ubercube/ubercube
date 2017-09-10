#version 330 core
#extension GL_NV_shadow_samplers_cube : enable
#define FOG_COLOR vec4(221.0 / 255.0, 232.0 / 255.0, 255.0 / 255.0, 1.0)

out vec4 fragColor;

uniform samplerCube map;
uniform vec3 cameraPosition;
uniform float fogDistance;
uniform vec4 in_color;

in vec4 v_color;
in vec3 v_normal;
in vec3 worldPosition;

void main(void)
{
	float dist = distance(cameraPosition, worldPosition) / fogDistance * 2 - 0.8;
	if (dist > 1)
		dist = 1;
	if (dist < 0)
		dist = 0;

	vec3 eyeDirection = normalize(cameraPosition - worldPosition);
	vec3 reflectDirection = reflect(-eyeDirection, v_normal);
	vec4 reflectionColor = textureCube(map, reflectDirection);
	vec4 weaponColor = v_color;
	float lightDot = clamp(dot(v_normal, normalize(vec3(1, 1, 1))) + 0.8, 0.8, 1.0);
	vec4 finalColor = mix(weaponColor, reflectionColor, 0.03) * vec4(lightDot, lightDot, lightDot, 1.0);
	fragColor = mix(finalColor, FOG_COLOR, dist) * vec4(lightDot, lightDot, lightDot, 1.0);
}
