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

    if(texColor.x < 0.1 && texColor.y < 0.1 && texColor.z < 0.1 && texColor.w > 0) texColor *= OutlineColor;
    else if(texColor.w > 0) texColor *= TextColor;

    FragColor = texColor;
}