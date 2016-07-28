#version 150

in vec3 VertexPosition;
in vec2 VertexTexCoords;
in vec4 VertexTextColor;
in vec4 VertexOutlineColor;

out vec2 TexCoord;
out vec4 TextColor;
out vec4 OutlineColor;

uniform mat4 transform;
uniform mat4 cameras[16];
uniform int mID;

void main()
{
    TexCoord = VertexTexCoords;
    TextColor = VertexTextColor;
    OutlineColor = VertexOutlineColor;
    gl_Position = cameras[mID]*transform*vec4(VertexPosition, 1.0);
}
