#version 130

uniform mat4 camera;
uniform mat4 transform;

in vec3 VertexPosition;
in vec2 VertexTexCoord;
in vec4 VertexTextColor;
in vec4 VertexOutlineColor;

out vec2 TexCoord;
out vec4 TextColor;
out vec4 OutlineColor;

void main()
{
    TexCoord = VertexTexCoord;
    TextColor = VertexTextColor;
    OutlineColor = VertexOutlineColor;
    gl_Position = camera*transform*vec4(VertexPosition, 1);
}
