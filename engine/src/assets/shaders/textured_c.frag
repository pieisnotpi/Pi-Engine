#version 130

uniform sampler2D sampler;

in vec4 Color;
in vec2 TexCoord;

out vec4 FragColor;

void main()
{
    vec4 texVal = texture(sampler, TexCoord);

    if(texVal.w == 0) discard;

    if(Color.w != 0)
    {
        texVal.x = (Color.x*Color.w + texVal.x*texVal.w)/2;
        texVal.y = (Color.y*Color.w + texVal.y*texVal.w)/2;
        texVal.z = (Color.z*Color.w + texVal.z*texVal.w)/2;
    }

    FragColor = texVal;
}