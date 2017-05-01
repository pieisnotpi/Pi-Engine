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
    //vec2 newTexCoord;
    ivec2 tsize = textureSize(sampler, 0);
    ivec2 point = ivec2(TexCoord.x*tsize.x, TexCoord.y*tsize.y);

    //if(texColor.w < 0.5f)
    if(texColor.w == 0)
    {
        /*if(texColor.w != 0)
        {
            FragColor = OutlineColor;
            return;
        }*/
        for(int x = -size; x <= size; x++)
        {
            for(int y = -size; y <= size; y++)
            {
                /*if(point.x + x > tsize.x || point.x + x < 0 || point.y + y > tsize.y || point.y + y < 0)
                {
                    FragColor = vec4(1, 0, 1, 1);
                    return;
                }*/

                //if(abs(x) == size || abs(y) == size)

                /*newTexCoord = vec2(TexCoord.x + x/tsize.x, TexCoord.y + y/tsize.y);
                outlineSearch = texture(sampler, newTexCoord);*/
                outlineSearch = texelFetch(sampler, ivec2(point.x + x, point.y + y), 0);

                if(outlineSearch.w != 0)
                {
                    //vec2 coords = vec2(x/tsize.x, y/tsize.y);
                    //vec4 t = texture2D(sampler, coords);
                    //FragColor = vec4(OutlineColor.x, OutlineColor.y, OutlineColor.z, outlineSearch.w*OutlineColor.w);
                    FragColor = OutlineColor;
                    /*if(texColor.w == 0)
                    {

                        //FragColor.w *= texColor.w;
                    }
                    else
                    {
                        *//*float temp = 0.5f;
                        vec2 converted = vec2(point.x/tsize.x, point.y/tsize.y);
                        if(abs(TexCoord.x - converted.x) > temp || abs(TexCoord.y - converted.y) > temp) continue;*//*

                        FragColor = OutlineColor;
                        *//*FragColor.x = (texColor.w*TextColor.x + (1 - texColor.w)*OutlineColor.x)/2;
                        FragColor.y = (texColor.w*TextColor.y + (1 - texColor.w)*OutlineColor.y)/2;
                        FragColor.z = (texColor.w*TextColor.z + (1 - texColor.w)*OutlineColor.z)/2;
                        FragColor.w = 1;*//*
                    }*/
                    return;
                }
            }
        }

        /*FragColor = vec4(1, 0, 1, 1);
        return;*/
        discard;
    }

    /*if(texColor.x < 0.05 && texColor.y < 0.05 && texColor.z < 0.05) texColor = vec4(OutlineColor.x, OutlineColor.y, OutlineColor.z, texColor.w*OutlineColor.w);
    else*/

    /*if(size == 0)*/ FragColor = vec4(TextColor.x, TextColor.y, TextColor.z, texColor.w*TextColor.w);
    //else FragColor = vec4(TextColor.x, TextColor.y, TextColor.z, TextColor.w);
}