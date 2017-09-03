#version 330 core
#extension GL_NV_shadow_samplers_cube : enable

layout (location = 0) in vec3 in_position;
layout (location = 1) in vec4 in_color;
layout (location = 2) in vec4 in_transform0;
layout (location = 3) in vec4 in_transform1;
layout (location = 4) in vec4 in_transform2;
layout (location = 5) in vec4 in_transform3;

uniform mat4 projectionMatrix;
uniform mat4 modelViewMatrix;

out vec4 v_color;
out vec3 worldPosition;

void main(void)
{
	v_color = in_color;
	mat4 transform = mat4(in_transform0, in_transform1, in_transform2, in_transform3);
    vec4 modelViewTransform = transform * vec4(in_position, 1.0);
	worldPosition = (modelViewTransform).xyz;
	gl_Position = projectionMatrix * modelViewTransform;
}
