#version 150

in vec3 VertexPosition;
in vec2 VertexTexCoords;

out vec2 TexCoord;

uniform mat4 camera;

void main()
{
    TexCoord = VertexTexCoords;
    gl_Position = camera*vec4(VertexPosition, 1.0);
}
