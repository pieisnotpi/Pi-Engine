#version 130

uniform sampler2D sampler;
uniform int size;
uniform bool smoothOutline = true;

in vec2 TexCoord;
in vec4 TextColor;
in vec4 OutlineColor;

out vec4 FragColor;

float getAvg()
{
	float total = 0;
	ivec2 tsize = textureSize(sampler, 0);
	ivec2 point = ivec2(TexCoord.x*tsize.x, TexCoord.y*tsize.y);
	vec4 search;

	for(int x = -size; x <= size; x++)
	{
		for(int y = -size; y <= size; y++)
		{
			float nx = point.x + x, ny = point.y + y;

			if(smoothOutline) search = texture(sampler, vec2(nx/tsize.x, ny/tsize.y), 0);
			else search = texelFetch(sampler, ivec2(x + point.x, y + point.y), 0);
			
			total += search.w;
		}
	}

	total /= (size*size);

	return total;
}

void main()
{
    vec4 texColor = texture(sampler, TexCoord);
	
	if(size == 0)
	{
		if(texColor.w == 0) discard;
		FragColor = vec4(TextColor.xyz, texColor.w*TextColor.w);
		return;
	}

    if(texColor.w == 0)
    {
		float total = getAvg();
		
		if(total != 0)
		{
			FragColor = vec4(OutlineColor.xyz, OutlineColor.w*total);
			return;
		}
		
        discard;
    }
 
	if(smoothOutline)
	{
		float tMult = texColor.w, oMult = 1 - texColor.w;
		FragColor = vec4(tMult*TextColor.x + oMult*OutlineColor.x, tMult*TextColor.y + oMult*OutlineColor.y, tMult*TextColor.z + oMult*OutlineColor.z, 1);
	}
	else FragColor = vec4(TextColor.xyz, texColor.w*TextColor.w);
}