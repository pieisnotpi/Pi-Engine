#version 130

struct LightInfo
{
    vec4 Position;
    vec3 Intensity;
};

uniform LightInfo lights[4];

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
    vec3 d = vec3(lights[index].Position) - Position;
    vec3 i = lights[index].Intensity;

    float ds = sqrt(d.x*d.x + d.y*d.y + d.z*d.z)/10;
    if(ds < 0.75) ds = 0.75;

    vec3 n = normalize(Normal);
    vec3 s = normalize(d);
    vec3 h = normalize(normalize(-Position) + s);

    return i*(m.Ka + m.Kd*max(dot(s, Normal), 0.0) + m.Ks*pow(max(dot(h, n), 0.0), m.Shininess))/ds;
}

void main()
{
    vec3 Color = vec3(0);

    for(int i = 0; i < 4; i++) Color += ads(i);

    vec4 t = texture(sampler, TexCoord);
    if(t.w == 0) discard;
    FragColor = t*vec4(Color, 1);
}
