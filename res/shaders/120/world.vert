#version 120
#extension GL_NV_shadow_samplers_cube : enable

attribute vec3 in_position;
attribute vec3 in_color;

uniform mat4 projectionMatrix;
uniform mat4 modelViewMatrix;

varying vec3 v_color;
varying vec3 worldPosition;

void main(void)
{
	v_color = in_color;
    vec4 modelViewTransform = modelViewMatrix * vec4(in_position, 1.0);
	worldPosition = (modelViewTransform).xyz;
	gl_Position = projectionMatrix * modelViewTransform;
}
