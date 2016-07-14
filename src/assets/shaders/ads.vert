#version 130

uniform mat4 ModelViewMatrix;
uniform mat3 NormalMatrix;
uniform mat4 MVP;

in vec3 VertexPosition;
in vec3 VertexNormal;
in vec2 VertexTexCoord;

out vec3 Position;
out vec3 Normal;
out vec2 TexCoord;

void main()
{
    Position = vec3(ModelViewMatrix*vec4(VertexPosition, 1));
    Normal = normalize(NormalMatrix*VertexNormal);
    TexCoord = VertexTexCoord;

    gl_Position = MVP*vec4(VertexPosition, 1);
}

