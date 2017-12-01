#version 330 core
#extension GL_NV_shadow_samplers_cube : enable

layout (location = 0) in vec3 in_position;
layout (location = 1) in vec3 in_color;
layout (location = 2) in vec3 in_normal;

uniform mat4 projectionMatrix;
uniform mat4 modelViewMatrix;
uniform mat4 lightMatrix;
uniform vec3 cameraPosition;

out vec3 v_color;
out vec3 v_normal;
out vec3 worldPosition;
out vec4 lightPosition;
out float shadowDist;

void main(void)
{
	v_color = in_color;
	v_normal = in_normal;
    vec4 modelViewTransform = modelViewMatrix * vec4(in_position, 1.0);
	worldPosition = (modelViewTransform).xyz;
	lightPosition = lightMatrix * modelViewTransform;

	float dist = distance(cameraPosition, worldPosition);
	dist = dist - (45.0 - 5.0);
	dist = dist / 45.0;
	shadowDist = clamp(1.0 - dist, 0.0, 1.0);
	gl_Position = projectionMatrix * modelViewTransform;
}
