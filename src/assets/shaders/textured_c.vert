#version 150

in vec3 VertexPosition;
in vec4 VertexColor;
in vec2 VertexTexCoords;

out vec4 Color;
out vec2 TexCoord;

uniform mat4 camera;

void main()
{
    Color = VertexColor;
    TexCoord = VertexTexCoords;

    gl_Position = camera*vec4(VertexPosition, 1.0);
}
