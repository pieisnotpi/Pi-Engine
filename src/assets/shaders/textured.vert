#version 130

uniform mat4 transform;
uniform mat4 camera;

in vec3 VertexPosition;
in vec2 VertexTexCoords;

out vec2 TexCoord;

void main()
{
    TexCoord = VertexTexCoords;
    gl_Position = camera*transform*vec4(VertexPosition, 1.0f);
}
