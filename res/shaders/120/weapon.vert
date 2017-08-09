#version 120
#extension GL_NV_shadow_samplers_cube : enable

attribute vec3 in_position;
attribute vec4 in_color;
attribute vec3 in_normal;

uniform mat4 projectionMatrix;
uniform mat4 modelViewMatrix;

varying vec4 v_color;
varying vec3 v_normal;
varying vec3 worldPosition;

void main(void)
{
	v_color = in_color;
	v_normal = in_normal;
    vec4 modelViewTransform = modelViewMatrix * vec4(in_position, 1.0);
	worldPosition = (modelViewTransform).xyz;
	gl_Position = projectionMatrix * modelViewTransform;
}
