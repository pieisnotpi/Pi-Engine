#version 130

struct LightInfo
{
    vec4 Position;
    vec3 Intensity;
};

uniform LightInfo lights[4];
uniform vec3 Kd;
uniform vec3 Ka;
uniform vec3 Ks;
uniform float Shininess;
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

    return i*(Ka + Kd*max(dot(s, Normal), 0.0) + Ks*pow(max(dot(h, n), 0.0), Shininess))/ds;
}

void main()
{
    vec3 Color = vec3(0);

    for(int i = 0; i < 4; i++) Color += ads(i);

    vec4 t = texture(sampler, TexCoord);
    if(t.w == 0) discard;
    FragColor = t*vec4(Color, 1);
}
