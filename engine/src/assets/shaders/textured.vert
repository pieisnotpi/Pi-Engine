#version 130

uniform mat4 transform;
uniform mat4 camera;

in vec3 VertexPosition;
in vec2 VertexTexCoord;

out vec2 TexCoord;

void main()
{
    TexCoord = VertexTexCoord;
    gl_Position = camera*transform*vec4(VertexPosition, 1.0f);
}
