#version 330 core
#extension GL_NV_shadow_samplers_cube : enable

layout (location = 0) in vec3 in_position;
layout (location = 1) in vec4 in_color;

uniform mat4 projectionMatrix;
uniform mat4 modelViewMatrix;

out vec4 v_color;

void main(void)
{
	v_color = in_color;
    vec4 modelViewTransform = modelViewMatrix *  vec4(in_position, 1.0);
	gl_Position = projectionMatrix * modelViewTransform;
}
