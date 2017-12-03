#version 330 core
#extension GL_NV_shadow_samplers_cube : enable
#define FOG_COLOR vec4(221.0 / 255.0, 232.0 / 255.0, 255.0 / 255.0, 1.0)
#define SHADOW_CASCADE_COUNT 3

out vec4 fragColor;
uniform sampler2D shadowMap[SHADOW_CASCADE_COUNT];
uniform vec3 cameraPosition;
uniform float fogDistance;
uniform vec4 in_color;
uniform float shadowCascadeDistances[SHADOW_CASCADE_COUNT + 1];

in vec3 v_color;
in vec3 v_normal;
in vec3 worldPosition;
in vec4 lightPositions[SHADOW_CASCADE_COUNT];
in float shadowDist;
in float zDist;
in flat int renderShadows;

int getShadowCascadeID(float[SHADOW_CASCADE_COUNT + 1] cascadDistances, float dist)
{
	for (int i = 0; i < SHADOW_CASCADE_COUNT; i++)
	{
		if (dist < cascadDistances[i + 1])
			return i;
	}
	return SHADOW_CASCADE_COUNT - 1;
}

float calcShadowFactor(vec4[SHADOW_CASCADE_COUNT] lightPos, float[SHADOW_CASCADE_COUNT + 1] cascadDistances, float dist)
{
	int shadowMapID = getShadowCascadeID(cascadDistances, dist);
	vec4 lightPosition = lightPos[shadowMapID];

    vec3 projCoords = lightPosition.xyz / lightPosition.w;
    projCoords = projCoords * 0.5 + 0.5;

    float closestDepth = texture(shadowMap[shadowMapID], projCoords.xy).r;
    float currentDepth = projCoords.z;

	float bias = 0.00001 * (10.0 * (shadowMapID + 1));

    if (currentDepth - bias > closestDepth)
    	return 1.0 - 0.5;
    return 1.0;
}

void main(void)
{
	float fragDist = distance(cameraPosition.xz, worldPosition.xz);
	float dist = fragDist / fogDistance * 2 - 0.8;
	if (dist > 1)
		dist = 1;
	if (dist < 0)
		dist = 0;

	vec4 color = vec4(v_color, 1.0);
	if (in_color != vec4(-1, -1, -1, -1))
		color = in_color;

	vec4 shadow = vec4(1, 1, 1, 1);
	if (renderShadows == 1)
	{
		float shadowFactor = calcShadowFactor(lightPositions, shadowCascadeDistances, zDist);
		shadow = vec4(shadowFactor, shadowFactor, shadowFactor, 1.0);
	}
	vec4 finalColor = color * 1.2f * shadow;
	fragColor = mix(finalColor, FOG_COLOR, dist);
}
