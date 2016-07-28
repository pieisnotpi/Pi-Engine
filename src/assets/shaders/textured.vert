#version 150

in vec3 VertexPosition;
in vec2 VertexTexCoords;

out vec2 TexCoord;

uniform mat4 transform;
uniform mat4 cameras[16];
uniform int mID;

void main()
{
    TexCoord = VertexTexCoords;
    gl_Position = cameras[mID]*transform*vec4(VertexPosition, 1.0);
}
