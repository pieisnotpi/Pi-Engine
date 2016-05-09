#version 450

layout (location = 0) in vec3 VertexPosition;
layout (location = 1) in vec3 VertexNormal;

out vec3 Intensity;

uniform vec4 LightPosition;
uniform vec3 Kd;
uniform vec3 Ld;

uniform mat3 NM;
uniform mat4 MVM;
uniform mat4 PM;
uniform mat4 MVP;

void main()
{
    vec3 normal = normalize(NM * VertexNormal);
    vec4 eye = MVM * vec4(VertexPosition, 1);

    vec3 s = normalize(vec3(LightPosition - eye));

    LightIntensity = Ld * Kd * max(dot(s, normal), 0);

    gl_Position = MVP * vec4(VertexPosition, 1);
}