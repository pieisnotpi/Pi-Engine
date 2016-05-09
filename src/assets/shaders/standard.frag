#version 450

in vec4 Color;
in vec2 TexCoord;

out vec4 FragColor;

uniform sampler2D sampler;

void main()
{
    vec4 frag = Color;
    if(tex != -1) frag = texture(sampler, TexCoord);

    FragColor = frag;
}