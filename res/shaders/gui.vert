#version 330 core
#extension GL_NV_shadow_samplers_cube : enable

layout (location = 0) in vec3 in_position;
layout (location = 1) in vec2 in_coords;

uniform mat4 projectionMatrix;
uniform mat4 modelViewMatrix;

out vec2 v_coords;

void main(void)
{
	v_coords = in_coords;
    vec4 modelViewTransform = modelViewMatrix *  vec4(in_position, 1.0);
	gl_Position = projectionMatrix * modelViewTransform;
}
