#version 130

struct LightInfo
{
    vec4 Position;
    vec3 Intensity;
};

uniform LightInfo lights[16];

struct Material
{
    vec3 Kd;
    vec3 Ka;
    vec3 Ks;
    float Shininess;
};

uniform Material m;
uniform sampler2D sampler;

in vec3 Position;
in vec3 Normal;
in vec2 TexCoord;

out vec4 FragColor;

vec3 ads(int index)
{
    LightInfo l = lights[index];

    vec3 s = normalize(vec3(l.Position) - Position);
    vec3 v = normalize(vec3(-Position));
    vec3 h = normalize(v + s);

    return l.Intensity*(m.Ka + m.Kd*max(dot(s, Normal), 0.0 ) + m.Ks*pow(max(dot(h, Normal), 0.0), m.Shininess));
}

void main()
{
    vec3 Color = vec3(0);

    for(int i = 0; i < 16; i++) Color += ads(i);

    vec4 t = texture(sampler, TexCoord);
    if(t.w == 0) discard;
    FragColor = t*vec4(Color, 1);
}
