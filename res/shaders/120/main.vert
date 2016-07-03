#version 120
#extension GL_NV_shadow_samplers_cube : enable

attribute vec3 in_position;
attribute float in_light;

uniform mat4 projectionMatrix;
uniform mat4 modelViewMatrix;

varying float v_light;
varying vec3 v_position;
varying vec3 worldPosition;

void main(void)
{
	v_position = in_position;
	v_light = in_light;
    vec4 modelViewTransform = modelViewMatrix *  vec4(in_position, 1.0);
	worldPosition = (modelViewTransform).xyz;
	gl_Position = projectionMatrix * modelViewTransform;
}
