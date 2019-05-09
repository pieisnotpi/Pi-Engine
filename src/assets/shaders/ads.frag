#version 130

struct LightInfo
{
    vec4 Position;
    vec3 Intensity;
};

uniform LightInfo lights[16];

struct Material
{
    vec4 Kd;
    vec4 Ka;
    vec4 Ks;
    float Shininess;
};

uniform Material m;
uniform sampler2D sampler;

in vec4 Position;
in vec4 Normal;
in vec2 TexCoord;

out vec4 FragColor;

vec4 ads(int index)
{
    LightInfo l = lights[index];

    vec4 s = normalize(l.Position - Position);
    vec4 v = normalize(-Position);
    vec4 h = normalize(v + s);

    return vec4(l.Intensity, 1.0)*(m.Ka + m.Kd*max(dot(s, Normal), 0.0 ) + m.Ks*pow(max(dot(h, Normal), 0.0), m.Shininess));
}

void main()
{
    vec4 color = vec4(0);

    for(int i = 0; i < 16; i++) color += ads(i);

    vec4 t = texture(sampler, TexCoord);
    if(t.w == 0) discard;
    FragColor = t*color;
}
