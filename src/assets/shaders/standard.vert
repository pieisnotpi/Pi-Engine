#version 450

layout (location = 0) in vec3 VertexPosition;
layout (location = 1) in vec4 VertexColor;
layout (location = 2) in vec2 VertexTexCoords;

out vec4 Color;
out vec2 TexCoord;

uniform mat4 camera;

void main()
{
    Color = VertexColor;
    TexCoord = VertexTexCoords;

    gl_Position = camera*vec4(VertexPosition, 1.0);
}
