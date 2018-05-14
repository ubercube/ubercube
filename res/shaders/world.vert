#version 330 core
#extension GL_NV_shadow_samplers_cube : enable
#define SHADOW_CASCADE_COUNT 3

layout (location = 0) in vec3 in_position;
layout (location = 1) in vec3 in_color;
layout (location = 2) in vec3 in_normal;

uniform mat4 	projectionMatrix;
uniform mat4 	modelViewMatrix;
uniform mat4 	lightMatrix[SHADOW_CASCADE_COUNT];
uniform vec3 	cameraPosition;
uniform int		isShadows;

out vec3 v_color;
out vec3 v_normal;
out vec3 worldPosition;
out vec4 lightPositions[SHADOW_CASCADE_COUNT];
out float shadowDist;
out float zDist;
out float renderShadows;

void main(void)
{
	v_color = in_color;
	v_normal = in_normal;
    vec4 modelViewTransform = modelViewMatrix * vec4(in_position, 1.0);
	worldPosition = (modelViewTransform).xyz;
	renderShadows = isShadows == 1 ? 10 : 0;

	if (isShadows == 1)
		for (int i = 0; i < SHADOW_CASCADE_COUNT; i++)
			lightPositions[i] = lightMatrix[i] * modelViewTransform;

	gl_Position = projectionMatrix * modelViewTransform;
	zDist = gl_Position.z;

	float dist = zDist;
	dist = dist - (45.0 - 5.0);
	dist = dist / 45.0;
	shadowDist = clamp(1.0 - dist, 0.0, 1.0);
}
