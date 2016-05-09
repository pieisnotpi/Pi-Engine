#version 150

in vec3 VertexPosition;
in vec4 VertexColor;

out vec4 Color;

uniform mat4 camera;

void main()
{
    Color = VertexColor;
    gl_Position = camera*vec4(VertexPosition, 1.0);
}
