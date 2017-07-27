#version 130

uniform mat4 transform;
uniform mat4 camera;

in vec3 VertexPosition;
in vec4 VertexColor;

out vec4 Color;

void main()
{
    Color = VertexColor;
    gl_Position = camera*transform*vec4(VertexPosition, 1.0);
}
