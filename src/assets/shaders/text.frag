#version 130

uniform sampler2D sampler;
uniform int size = 1;
uniform float threshold = 0.5f;

in vec2 TexCoord;
in vec4 TextColor;
in vec4 OutlineColor;

out vec4 FragColor;

void main()
{
    vec4 texColor = texture(sampler, TexCoord);
    vec4 outlineSearch;
    ivec2 tsize = textureSize(sampler, 0);
    ivec2 point = ivec2(TexCoord.x*tsize.x, TexCoord.y*tsize.y);

    if(texColor.w == 0)
    {
        for(int x = -size; x <= size; x++)
        {
            for(int y = -size; y <= size; y++)
            {
				vec2 p = vec2((point.x + x)/tsize.x, (point.y + y)/tsize.y);
                outlineSearch = texture(sampler, TexCoord + p);
                //outlineSearch = texelFetch(sampler, ivec2(point.x + x, point.y + y), 0);

                if(outlineSearch.w != 0)
                {
                    FragColor = OutlineColor;
                    return;
                }
            }
        }
        discard;
    }

    //if(texColor.w != 0)
    FragColor = vec4(TextColor.x, TextColor.y, TextColor.z, texColor.w*TextColor.w);
    //FragColor = vec4(TextColor.x, TextColor.y, TextColor.z, 0.5);
}