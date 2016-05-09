#version 150

in vec2 TexCoord;

out vec4 FragColor;

uniform sampler2D sampler;

void main()
{
    FragColor = texture(sampler, TexCoord);
}