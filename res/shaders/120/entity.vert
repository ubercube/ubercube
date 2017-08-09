#version 120
#extension GL_NV_shadow_samplers_cube : enable

attribute vec3 in_position;
attribute vec4 in_color;
attribute vec4 in_transform0;
attribute vec4 in_transform1;
attribute vec4 in_transform2;
attribute vec4 in_transform3;

uniform mat4 projectionMatrix;
uniform mat4 modelViewMatrix;

varying vec4 v_color;
varying vec3 worldPosition;

void main(void)
{
	v_color = in_color;
	mat4 transform = mat4(in_transform0, in_transform1, in_transform2, in_transform3);
    vec4 modelViewTransform = transform * vec4(in_position, 1.0);
	worldPosition = (modelViewTransform).xyz;
	gl_Position = projectionMatrix * modelViewTransform;
}
