#version 120

uniform vec2 location, rectSize;
uniform vec4 color, outlineColor1,outlineColor2,outlineColor3,outlineColor4;
uniform float radius, outlineThickness;
#define NOISE .5/255.0

float roundedSDF(vec2 centerPos, vec2 size, float radius) {
    return length(max(abs(centerPos) - size + radius, 0.0)) - radius;
}

vec3 createGradient(vec2 coords, vec3 color1, vec3 color2, vec3 color3, vec3 color4)
{
    vec3 color = mix(mix(color1.rgb, color2.rgb, coords.y), mix(color3.rgb, color4.rgb, coords.y), coords.x);
    //Dithering the color
    // from https://shader-tutorial.dev/advanced/color-banding-dithering/
    color += mix(NOISE, -NOISE, fract(sin(dot(coords.xy, vec2(12.9898, 78.233))) * 43758.5453));
    return color;
}

void main() {
    float distance = roundedSDF(gl_FragCoord.xy - location - (rectSize * .5), (rectSize * .5) + (outlineThickness * 0.5) - 1.0, radius);

    float blendAmount = smoothstep(0., 2., abs(distance) - (outlineThickness * 0.5));
    vec4 outlineColor = vec4(createGradient(gl_TexCoord[0].st, outlineColor1.rgb, outlineColor2.rgb, outlineColor3.rgb, outlineColor4.rgb), outlineColor1.a);
    vec4 insideColor = (distance < 0.) ? color : vec4(outlineColor.rgb,  0.0);
    gl_FragColor = mix(outlineColor, insideColor, blendAmount);
}