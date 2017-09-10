#version 330 core
#extension GL_NV_shadow_samplers_cube : enable

layout (location = 0) in vec3 in_position;
layout (location = 1) in vec4 in_color;
layout (location = 2) in vec3 in_normal;

uniform mat4 projectionMatrix;
uniform mat4 modelViewMatrix;
uniform mat4 viewMatrix;

out vec4 v_color;
out vec3 v_normal;
out vec3 worldPosition;

void main(void)
{
	v_color = in_color;
	v_normal = (viewMatrix * vec4(in_normal, 0.0)).xyz;
    vec4 modelViewTransform = modelViewMatrix * vec4(in_position, 1.0);
	worldPosition = (modelViewTransform).xyz;
	gl_Position = projectionMatrix * modelViewTransform;
}
