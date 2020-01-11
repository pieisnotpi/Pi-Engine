#version 130

uniform mat4 ModelMatrix;
uniform mat4 ViewMatrix;
uniform mat3 NormalMatrix;
uniform mat4 MVP;

in vec3 VertexPosition;
in vec3 VertexNormal;
in vec2 VertexTexCoord;

out vec4 Position;
out vec4 Normal;
out vec2 TexCoord;

void main()
{
    Position = ModelMatrix*ViewMatrix*vec4(VertexPosition, 1);
    Normal = vec4(normalize(NormalMatrix*VertexNormal), 1.0);
    TexCoord = VertexTexCoord;

    gl_Position = MVP*ModelMatrix*vec4(VertexPosition, 1);
}

