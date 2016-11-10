#version 130

uniform sampler2D sampler;

in vec2 TexCoord;
in vec4 TextColor;
in vec4 OutlineColor;

out vec4 FragColor;

void main()
{
    vec4 texColor = texture(sampler, TexCoord);

    if(texColor.w == 0) discard;

    if(texColor.x < 0.05 && texColor.y < 0.05 && texColor.z < 0.05) texColor = vec4(OutlineColor.x, OutlineColor.y, OutlineColor.z, texColor.w*OutlineColor.w);
    else texColor = vec4(TextColor.x, TextColor.y, TextColor.z, texColor.w*TextColor.w);

    FragColor = texColor;
}