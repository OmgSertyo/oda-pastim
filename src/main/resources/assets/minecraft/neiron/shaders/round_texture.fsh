#version 120

uniform sampler2D texture;
uniform float width;
uniform float height;
uniform float radius;
uniform float alpha;

float dstfn(vec2 p, vec2 b, float r) {
    return length(max(abs(p) - b, 0.)) - r;
}


void main() {
    vec2 tex = gl_TexCoord[0].st;
    vec4 smpl = texture2D(texture, tex);
    vec2 size = vec2(width, height);
    vec2 pixel = gl_TexCoord[0].st * size;
    vec2 centre = .5 * size;
    float sa = smoothstep(0., 1, dstfn(centre - pixel, centre - radius - 1, radius));
    vec4 c = mix(vec4(smpl.rgb, 1), vec4(smpl.rgb, 0), sa);
    gl_FragColor = vec4(smpl.rgb, smpl.a * c.a * alpha);
}