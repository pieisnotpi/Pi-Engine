#version 450

in vec2 texCoords;
in vec3 intensity;

out vec4 FragColor;

uniform sampler2D sampler;

void main()
{
    FragColor = texture(sampler, texCoords)*vec4(LightIntensity, 1);
}