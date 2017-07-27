#version 130

in vec4 Color;
out vec4 FragColor;

void main()
{
    if(Color.w == 0) discard;
    FragColor = Color;
}