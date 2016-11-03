#version 130

uniform sampler2D sampler;

in vec2 TexCoord;

out vec4 FragColor;

void main()
{
    vec4 t = texture(sampler, TexCoord);
    if(t.w == 0f) discard;
    FragColor = t;
}