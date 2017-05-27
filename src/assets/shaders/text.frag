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

    if(texColor.w < 0.5)
    {
		float total = 0;
	
        for(int x = -size; x <= size; x++)
        {
            for(int y = -size; y <= size; y++)
            {
				float nx = point.x + x, ny = point.y + y;
				vec2 p = vec2(nx/tsize.x, ny/tsize.y);
				
                outlineSearch = texture(sampler, p, 0);
                //outlineSearch = texelFetch(sampler, ivec2(point.x + x, point.y + y), 0);

                if(outlineSearch.w != 0)
                {
					total += outlineSearch.w;
					/*FragColor = OutlineColor;
                    return;*/
                }
            }
        }
		
		total /= (size*size);
		
		if(total != 0) 
		{
			FragColor = vec4(OutlineColor.x, OutlineColor.y, OutlineColor.z, OutlineColor.w*total);
			//FragColor = vec4(total, total, total, 1);
			return;
		}
		
        discard;
    }

    //if(texColor.w != 0)
    //FragColor = vec4(TextColor.x, TextColor.y, TextColor.z, texColor.w*TextColor.w);
	if(size == 0) FragColor = vec4(TextColor.xyz, texColor.w*TextColor.w);
	else FragColor = TextColor;
}