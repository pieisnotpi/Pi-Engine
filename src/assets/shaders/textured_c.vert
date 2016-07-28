#version 150

in vec3 VertexPosition;
in vec4 VertexColor;
in vec2 VertexTexCoords;

out vec4 Color;
out vec2 TexCoord;

uniform mat4 cameras[16];
uniform int mID;
void main()
{
    Color = VertexColor;
    TexCoord = VertexTexCoords;

    gl_Position = cameras[mID]*vec4(VertexPosition, 1.0);
}
