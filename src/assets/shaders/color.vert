#version 150

in vec3 VertexPosition;
in vec4 VertexColor;

out vec4 Color;

uniform mat4 transform;
uniform mat4 cameras[16];
uniform int mID;

void main()
{
    Color = VertexColor;
    gl_Position = cameras[mID]*transform*vec4(VertexPosition, 1.0);
}
