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

    if(texColor.x < 0.1f && texColor.y < 0.1f && texColor.z < 0.1f && texColor.w > 0) texColor = vec4(OutlineColor.x, OutlineColor.y, OutlineColor.z, OutlineColor.w*texColor.w);
    else if(texColor.w > 0) texColor = vec4(TextColor.x, TextColor.y, TextColor.z, TextColor.w*texColor.w);

    FragColor = texColor;
}