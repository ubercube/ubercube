#version 330 core
#extension GL_NV_shadow_samplers_cube : enable

layout (location = 0) in vec3 in_position;
layout (location = 1) in vec3 in_normal;

uniform mat4 projectionMatrix;
uniform mat4 modelViewMatrix;

out vec3 v_normal;
out vec3 worldPosition;

void main(void)
{
	v_normal = in_normal;
    vec4 modelViewTransform = modelViewMatrix * vec4(in_position, 1.0);
	worldPosition = (modelViewTransform).xyz;
	gl_Position = projectionMatrix * modelViewTransform;
}
