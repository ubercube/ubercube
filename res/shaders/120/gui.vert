#version 120
#extension GL_NV_shadow_samplers_cube : enable

attribute vec3 in_position;
attribute vec2 in_coords;
attribute vec4 in_colors;

uniform mat4 projectionMatrix;
uniform mat4 modelViewMatrix;

varying vec2 v_coords;
varying vec4 v_colors;

void main(void)
{
	v_coords = in_coords;
	v_colors = in_colors;
    vec4 modelViewTransform = modelViewMatrix *  vec4(in_position, 1.0);
	gl_Position = projectionMatrix * modelViewTransform;
}
