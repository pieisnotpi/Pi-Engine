#version 150

in vec2 TexCoord;

out vec4 FragColor;

uniform sampler2D sampler;

void main()
{
    vec4 t = texture(sampler, TexCoord);
    if(t.w == 0) discard;
    FragColor = t;
}