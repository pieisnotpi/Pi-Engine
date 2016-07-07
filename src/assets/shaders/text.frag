#version 150

in vec2 TexCoord;
in vec4 TextColor;
in vec4 OutlineColor;

out vec4 FragColor;

uniform sampler2D sampler;

void main()
{
    vec4 texColor = texture(sampler, TexCoord);

    if(texColor.x == 1 && texColor.y == 1 && texColor.z == 1 && texColor.w > 0)
    {
        float change = (1 - texColor.w)/6;
        texColor = vec4(TextColor.x - change, TextColor.y - change, TextColor.z - change, TextColor.w);
    }

    //if(texColor.x == 1 && texColor.y == 1 && texColor.z == 1 && texColor.w > 0) texColor = vec4(TextColor.x, TextColor.y, TextColor.z, TextColor.w - (1 - texColor.w));
    else if(texColor.x == 0 && texColor.y == 0 && texColor.z == 0 && texColor.w > 0)
    {
        float change = (1 - texColor.w)/6;
        texColor = vec4(OutlineColor.x + change, OutlineColor.y + change, OutlineColor.z + change, OutlineColor.w);
    }

    if(texColor.w == 0) discard;

    FragColor = texColor;
}