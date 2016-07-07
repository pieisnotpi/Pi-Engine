#version 450

in vec3 VertexPosition;
in vec2 VertexTexCoords;
in vec3 VertexNormal;

out vec2 texCoords;
out vec3 intensity;

uniform vec3 Kd;
uniform vec3 Ld;
uniform vec4 LightPosition;

uniform mat3 NormalMatrix;
uniform mat4 ModelViewMatrix;
uniform mat4 ProjectionMatrix;
uniform mat4 MVP;

void main()
{
    vec3 normal = normalize(NormalMatrix * VertexNormal);
    vec4 pos = vec4(VertexPosition, 1), eye = ModelViewMatrix*pos;

    vec3 s = normalize(vec3(LightPosition - eye));

    intensity = Ld*Kd*max(dot(s, normal), 0);
    texCoords = VertexTexCoords;

    gl_Position = MVP*pos;
}