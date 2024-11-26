package sertyo.events.utility.render;

import org.lwjgl.opengl.GL11;
import sertyo.events.utility.Utility;
import sertyo.events.utility.misc.FileUtil;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.lwjgl.opengl.ARBShaderObjects.*;
import static org.lwjgl.opengl.GL20.*;

public class ShaderUtils implements Utility {

    public static ShaderUtils KAWASE_UP, KAWASE_DOWN, KAWASE_BLOOM_UP, KAWASE_BLOOM_DOWN;
    private final int programID;

    public ShaderUtils(String fragmentShaderLoc) {
        programID = glCreateProgramObjectARB();

        int fragmentShaderID = switch (fragmentShaderLoc) {
            case "round" -> createShader(new ByteArrayInputStream(round.getBytes()), GL_FRAGMENT_SHADER);
            case "gradientRound" -> createShader(new ByteArrayInputStream(gradientRound.getBytes()), GL_FRAGMENT_SHADER);
            case "vectorRound" -> createShader(new ByteArrayInputStream(vectorRound.getBytes()), GL_FRAGMENT_SHADER);
            case "glowRound" -> createShader(new ByteArrayInputStream(glowRound.getBytes()), GL_FRAGMENT_SHADER);
            case "gradientGlowRound" -> createShader(new ByteArrayInputStream(gradientGlowRound.getBytes()), GL_FRAGMENT_SHADER);
            case "textureRound" -> createShader(new ByteArrayInputStream(textureRound.getBytes()), GL_FRAGMENT_SHADER);
            case "faceRound" -> createShader(new ByteArrayInputStream(faceRound.getBytes()), GL_FRAGMENT_SHADER);
            case "textMask" -> createShader(new ByteArrayInputStream(textMask.getBytes()), GL_FRAGMENT_SHADER);
            case "gradient" -> createShader(new ByteArrayInputStream(gradient.getBytes()), GL_FRAGMENT_SHADER);
            case "outline" -> createShader(new ByteArrayInputStream(outline.getBytes()), GL_FRAGMENT_SHADER);
            case "gaussian" -> createShader(new ByteArrayInputStream(gaussian.getBytes()), GL_FRAGMENT_SHADER);
            case "kawaseUp" -> createShader(new ByteArrayInputStream(kawaseUp.getBytes()), GL_FRAGMENT_SHADER);
            case "kawaseDown" -> createShader(new ByteArrayInputStream(kawaseDown.getBytes()), GL_FRAGMENT_SHADER);
            case "kawaseBloomUp" -> createShader(new ByteArrayInputStream(kawaseUpBloom.getBytes()), GL_FRAGMENT_SHADER);
            case "kawaseBloomDown" -> createShader(new ByteArrayInputStream(kawaseDownBloom.getBytes()), GL_FRAGMENT_SHADER);
            default -> throw new IllegalStateException("Unexpected value: " + fragmentShaderLoc);
        };
        glAttachObjectARB(programID, fragmentShaderID);
        glAttachObjectARB(programID, createShader(new ByteArrayInputStream(vertex.getBytes()), GL_VERTEX_SHADER));
        glLinkProgramARB(programID);
    }

    public static void init() {
        KAWASE_UP = new ShaderUtils("kawaseUp");
        KAWASE_DOWN = new ShaderUtils("kawaseDown");
        KAWASE_BLOOM_UP = new ShaderUtils("kawaseBloomUp");
        KAWASE_BLOOM_DOWN = new ShaderUtils("kawaseBloomDown");
    }

    public int getUniform(String name) {
        return glGetUniformLocationARB(programID, name);
    }

    public void attach() {
        glUseProgramObjectARB(programID);
    }

    public void detach() {
        glUseProgram(0);
    }

    public void setUniform(String name, float... args) {
        int loc = glGetUniformLocationARB(programID, name);
        switch (args.length) {
            case 1: {
                glUniform1fARB(loc, args[0]);
                break;
            }
            case 2: {
                glUniform2fARB(loc, args[0], args[1]);
                break;
            }
            case 3: {
                glUniform3fARB(loc, args[0], args[1], args[2]);
                break;
            }
            case 4: {
                glUniform4fARB(loc, args[0], args[1], args[2], args[3]);
                break;
            }
        }
    }

    public void setUniform(String name, int... args) {
        int loc = glGetUniformLocationARB(programID, name);
        switch (args.length) {
            case 1: {
                glUniform1iARB(loc, args[0]);
                break;
            }
            case 2: {
                glUniform2iARB(loc, args[0], args[1]);
            }
            case 3: {
                glUniform3iARB(loc, args[0], args[1], args[2]);
                break;
            }
            case 4: {
                glUniform4iARB(loc, args[0], args[1], args[2], args[3]);
            }
        }
    }

    public void setUniformf(String var1, float... args) {
        int var3 = glGetUniformLocationARB(this.programID, var1);
        switch (args.length) {
            case 1: {
                glUniform1fARB(var3, args[0]);
                break;
            }
            case 2: {
                glUniform2fARB(var3, args[0], args[1]);
                break;
            }
            case 3: {
                glUniform3fARB(var3, args[0], args[1], args[2]);
                break;
            }
            case 4: {
                glUniform4fARB(var3, args[0], args[1], args[2], args[3]);
                break;
            }
        }
    }

    public static void drawQuads() {
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glTexCoord2f(0, 1);
        GL11.glVertex2f(0, 0);
        GL11.glTexCoord2f(0, 0);
        GL11.glVertex2f(0, Math.max(sr.getScaledHeight(), 1));
        GL11.glTexCoord2f(1, 0);
        GL11.glVertex2f(Math.max(sr.getScaledWidth(), 1), Math.max(sr.getScaledHeight(), 1));
        GL11.glTexCoord2f(1, 1);
        GL11.glVertex2f(Math.max(sr.getScaledWidth(), 1), 0);
        GL11.glEnd();
    }

    private int createShader(InputStream inputStream, int shaderType) {
        int shader = glCreateShaderObjectARB(shaderType);
        glShaderSourceARB(shader, FileUtil.readInputStream(inputStream));
        glCompileShaderARB(shader);
        if (glGetShaderi(shader, 35713) == 0) {
            throw new IllegalStateException(String.format("Shader (%s) failed to compile!", shaderType));
        }
        return shader;
    }

    String vertex = """
            #version 120
                        
            void main() {
                gl_TexCoord[0] = gl_MultiTexCoord0;
                gl_Position = gl_ModelViewProjectionMatrix * gl_Vertex;
            }
            """;

    String round = """
            #version 120

            uniform vec2 size;
            uniform vec4 color;
            uniform float radius;

            float calcLength(vec2 p, vec2 b, float r) {
                return length(max(abs(p) - b , 0)) - r;
            }

            void main() {
                vec2 st = gl_TexCoord[0].st;
                vec2 halfSize = size * 0.5;
                float distance = calcLength(halfSize - (st * size), halfSize - radius - 1, radius);
                float smoothedAlpha = (1 - smoothstep(0, 2, distance)) * color.a;
                gl_FragColor = vec4(color.rgb, smoothedAlpha);
            }
            """;

    String gradientRound = """
            #version 120

            uniform vec2 size;
            uniform vec4 color1, color2, color3, color4;
            uniform float radius;

            float calcLength(vec2 p, vec2 b, float r) {
                return length(max(abs(p) - b , 0)) - r;
            }

            vec3 createGradient(vec2 coords, vec3 color1, vec3 color2, vec3 color3, vec3 color4){
                vec3 color = mix(mix(color1.rgb, color2.rgb, coords.y), mix(color3.rgb, color4.rgb, coords.y), coords.x);
                return color;
            }

            void main() {
                vec2 st = gl_TexCoord[0].st;
                vec2 halfSize = size * 0.5;
                float distance = calcLength(halfSize - (st * size), halfSize - radius - 1, radius);
                float smoothedAlpha = (1 - smoothstep(0, 2, distance)) * color1.a;
                gl_FragColor = vec4(createGradient(st, color1.rgb, color2.rgb, color3.rgb, color4.rgb), smoothedAlpha);
            }
            """;

    String vectorRound = """
            #version 120
                        
            uniform vec2 size;
            uniform vec4 color;
            uniform vec4 radius;

            float calcLength(vec2 p, vec2 b, vec4 r) {
                r.xy = (p.x > 0) ? r.xy : r.zw;
                r.x = (p.y > 0) ? r.x : r.y;
                vec2 coords = abs(p) - b + r.x;
                return min(max(coords.x, coords.y), 0) + length(max(coords, vec2(0))) - r.x;
            }

            void main() {
                vec2 st = gl_TexCoord[0].st;
                vec2 halfSize = size * 0.5;
                float distance = calcLength(halfSize - (st * size), halfSize - 1, radius);
                float smoothedAlpha = (1 - smoothstep(0, 1, distance)) * color.a;
                gl_FragColor = vec4(color.rgb, smoothedAlpha);
            }
            """;

    String glowRound = """
            #version 120
                        
            uniform vec2 size;
            uniform vec4 color;
            uniform float radius, glowRadius;
            
            float calcLength(vec2 p, vec2 b, float r) {
                return length(max(abs(p) - b , 0)) - r;
            }

            void main() {
                vec2 st = gl_TexCoord[0].st;
                vec2 halfSize = size * 0.5;
                float distance = calcLength(halfSize - (st * size), halfSize - radius - glowRadius, radius);
                float smoothedAlpha = (1 - smoothstep(-glowRadius, glowRadius, distance)) * color.a;
                gl_FragColor = vec4(color.rgb, smoothedAlpha);
            }
            """;

    String gradientGlowRound = """
            #version 120
                        
            uniform vec2 size;
            uniform vec4 color1, color2, color3, color4;
            uniform float radius, glowRadius;
            
            float calcLength(vec2 p, vec2 b, float r) {
                return length(max(abs(p) - b , 0)) - r;
            }
            
            vec3 createGradient(vec2 coords, vec3 color1, vec3 color2, vec3 color3, vec3 color4){
                vec3 color = mix(mix(color1.rgb, color2.rgb, coords.y), mix(color3.rgb, color4.rgb, coords.y), coords.x);
                return color;
            }

            void main() {
                vec2 st = gl_TexCoord[0].st;
                vec2 halfSize = size * 0.5;
                float distance = calcLength(halfSize - (st * size), halfSize - radius - glowRadius, radius);
                float smoothedAlpha = (1 - smoothstep(-glowRadius, glowRadius, distance)) * color1.a;
                gl_FragColor = vec4(createGradient(st, color1.rgb, color2.rgb, color3.rgb, color4.rgb), smoothedAlpha);
            }
            """;

    String textureRound = """
            #version 120

            uniform vec2 size;
            uniform sampler2D textureIn;
            uniform float radius, alpha;

            float calcLength(vec2 centerPos, vec2 size, float radius) {
                return length(max(abs(centerPos) - size, 0)) - radius;
            }

            void main() {
                vec2 st = gl_TexCoord[0].st;
                float distance = calcLength((size * 0.5) - (st * size), (size * 0.5) - radius - 1, radius);
                float smoothedAlpha = (1 - smoothstep(0, 2, distance)) * alpha;
                gl_FragColor = vec4(texture2D(textureIn, st).rgb, smoothedAlpha);
            }
            """;

    String faceRound = """
            #version 120
                        
            uniform vec2 location, size;
            uniform sampler2D texture;
            uniform float radius, alpha;
            uniform float u, v, w, h;
                        
            float calcLength(vec2 p, vec2 b, float r) {
                return length(max(abs(p) - b, 0)) - r;
            }
                        
            void main() {
                vec2 halfSize = size * 0.5;
                vec2 st = gl_TexCoord[0].st;
                st.x = u + st.x * w;
                st.y = v + st.y * h;
                float distance = calcLength(halfSize - (gl_TexCoord[0].st * size), halfSize - radius - 1, radius);
                float smoothedAlpha = (1 - smoothstep(0, 2, distance)) * alpha;
                vec4 color = texture2D(texture, st);
                gl_FragColor = vec4(color.rgb, smoothedAlpha);
            }
            """;

    String textMask = """
            #version 120

            uniform sampler2D font;
            uniform vec4 inColor;
            uniform float width;
            uniform float maxWidth;

            void main() {
                float f = clamp(smoothstep(0.5, 1, 1 - (gl_FragCoord.x - maxWidth) / width), 0, 1);
                vec2 pos = gl_TexCoord[0].xy;
                vec4 color = texture2D(font, pos);
                
                if (color.a > 0) color.a = color.a * f;
                
                gl_FragColor = color * inColor;
            }
            """;

    String gradient = """
            #version 120
                                         
            uniform vec2 location, size;
            uniform sampler2D texture;
            uniform vec4 color1, color2, color3, color4;
            
            vec3 createGradient(vec2 coords, vec4 color1, vec4 color2, vec4 color3, vec4 color4){
                 vec3 color = mix(mix(color1.rgb, color2.rgb, coords.y), mix(color3.rgb, color4.rgb, coords.y), coords.x);
                 return color;
            }
            
            void main() {
                 vec2 coords = (gl_FragCoord.xy - location) / size;
                 float textureAlpha = texture2D(texture, gl_TexCoord[0].st).a;
                 gl_FragColor = vec4(createGradient(coords, color1, color2, color3, color4).rgb, textureAlpha);
            }
            """;

    String outline = """
           #version 120
                                     
           uniform vec4 color;
           uniform sampler2D textureIn, textureToCheck;
           uniform vec2 texelSize, direction;
           uniform float size;
           
           #define offset direction * texelSize
           
           void main() {
               if (direction.y == 1) {
                   if (texture2D(textureToCheck, gl_TexCoord[0].st).a != 0) discard;
               }
               vec4 innerAlpha = texture2D(textureIn, gl_TexCoord[0].st);
               innerAlpha *= innerAlpha.a;
               for (float r = 1; r <= size; r ++) {
                   vec4 colorCurrent1 = texture2D(textureIn, gl_TexCoord[0].st + offset * r);
                   vec4 colorCurrent2 = texture2D(textureIn, gl_TexCoord[0].st - offset * r);
                   colorCurrent1.rgb *= colorCurrent1.a;
                   colorCurrent2.rgb *= colorCurrent2.a;
                   innerAlpha += (colorCurrent1 + colorCurrent2) * r;
               }
               gl_FragColor = vec4(innerAlpha.rgb / innerAlpha.a, mix(innerAlpha.a, 1 - exp(-innerAlpha.a), step(0, direction.y)));
            }
            """;

    String gaussian = """
            #version 120
            
            uniform sampler2D textureIn;
            uniform vec2 texelSize, direction;
            uniform float radius, weights[256];
            
            #define offset texelSize * direction
            
            void main() {
                vec3 color = texture2D(textureIn, gl_TexCoord[0].st).rgb * weights[0];
                float totalWeight = weights[0];
                for (float f = 1; f <= radius; f++) {
                    color += texture2D(textureIn, gl_TexCoord[0].st + f * offset).rgb * (weights[int(abs(f))]);
                    color += texture2D(textureIn, gl_TexCoord[0].st - f * offset).rgb * (weights[int(abs(f))]);
                    totalWeight += (weights[int(abs(f))]) * 2;
                }
                gl_FragColor = vec4(color / totalWeight, 1);
            }
            """;

    String kawaseUp = """
            #version 120

            uniform sampler2D inTexture, textureToCheck;
            uniform vec2 halfPixel, offset, resolution;
            uniform int check;

            void main() {
                vec2 uv = vec2(gl_FragCoord.xy / resolution);
                vec4 sum = texture2D(inTexture, uv + vec2(-halfPixel.x * 2, 0) * offset);
                sum += texture2D(inTexture, uv + vec2(-halfPixel.x, halfPixel.y) * offset) * 2;
                sum += texture2D(inTexture, uv + vec2(0, halfPixel.y * 2) * offset);
                sum += texture2D(inTexture, uv + vec2(halfPixel.x, halfPixel.y) * offset) * 2;
                sum += texture2D(inTexture, uv + vec2(halfPixel.x * 2, 0) * offset);
                sum += texture2D(inTexture, uv + vec2(halfPixel.x, -halfPixel.y) * offset) * 2;
                sum += texture2D(inTexture, uv + vec2(0, -halfPixel.y * 2) * offset);
                sum += texture2D(inTexture, uv + vec2(-halfPixel.x, -halfPixel.y) * offset) * 2;
                gl_FragColor = vec4(sum.rgb / 12, mix(1, texture2D(textureToCheck, gl_TexCoord[0].st).a, check));
            }
            """;

    String kawaseDown = """
            #version 120

            uniform sampler2D inTexture;
            uniform vec2 offset, halfPixel, resolution;

            void main() {
                vec2 uv = vec2(gl_FragCoord.xy / resolution);
                vec4 sum = texture2D(inTexture, gl_TexCoord[0].st) * 4;
                sum += texture2D(inTexture, uv - halfPixel.xy * offset);
                sum += texture2D(inTexture, uv + halfPixel.xy * offset);
                sum += texture2D(inTexture, uv + vec2(halfPixel.x, -halfPixel.y) * offset);
                sum += texture2D(inTexture, uv - vec2(halfPixel.x, -halfPixel.y) * offset);
                gl_FragColor = vec4(sum.rgb * 0.125, 1);
            }
            """;

    String kawaseUpBloom = """
            #version 120

            uniform sampler2D inTexture, textureToCheck;
            uniform vec2 halfPixel, offset, resolution;
            uniform int check;

            void main() {
                vec2 uv = vec2(gl_FragCoord.xy / resolution);
                vec4 sum = texture2D(inTexture, uv + vec2(-halfPixel.x * 2, 0) * offset);
                sum.rgb *= sum.a;
                vec4 smpl1 =  texture2D(inTexture, uv + vec2(-halfPixel.x, halfPixel.y) * offset);
                smpl1.rgb *= smpl1.a;
                sum += smpl1 * 2;
                vec4 smp2 = texture2D(inTexture, uv + vec2(0, halfPixel.y * 2) * offset);
                smp2.rgb *= smp2.a;
                sum += smp2;
                vec4 smp3 = texture2D(inTexture, uv + vec2(halfPixel.x, halfPixel.y) * offset);
                smp3.rgb *= smp3.a;
                sum += smp3 * 2;
                vec4 smp4 = texture2D(inTexture, uv + vec2(halfPixel.x * 2, 0) * offset);
                smp4.rgb *= smp4.a;
                sum += smp4;
                vec4 smp5 = texture2D(inTexture, uv + vec2(halfPixel.x, -halfPixel.y) * offset);
                smp5.rgb *= smp5.a;
                sum += smp5 * 2;
                vec4 smp6 = texture2D(inTexture, uv + vec2(0, -halfPixel.y * 2) * offset);
                smp6.rgb *= smp6.a;
                sum += smp6;
                vec4 smp7 = texture2D(inTexture, uv + vec2(-halfPixel.x, -halfPixel.y) * offset);
                smp7.rgb *= smp7.a;
                sum += smp7 * 2;
                vec4 result = sum / 12;
                gl_FragColor = vec4(result.rgb / result.a, mix(result.a, result.a * (1 - texture2D(textureToCheck, gl_TexCoord[0].st).a), check));
            }
            """;

    String kawaseDownBloom = """
            #version 120

            uniform sampler2D inTexture;
            uniform vec2 offset, halfPixel, resolution;

            void main() {
                vec2 uv = vec2(gl_FragCoord.xy / resolution);
                vec4 sum = texture2D(inTexture, gl_TexCoord[0].st);
                sum.rgb *= sum.a;
                sum *= 4;
                vec4 smp1 = texture2D(inTexture, uv - halfPixel.xy * offset);
                smp1.rgb *= smp1.a;
                sum += smp1;
                vec4 smp2 = texture2D(inTexture, uv + halfPixel.xy * offset);
                smp2.rgb *= smp2.a;
                sum += smp2;
                vec4 smp3 = texture2D(inTexture, uv + vec2(halfPixel.x, -halfPixel.y) * offset);
                smp3.rgb *= smp3.a;
                sum += smp3;
                vec4 smp4 = texture2D(inTexture, uv - vec2(halfPixel.x, -halfPixel.y) * offset);
                smp4.rgb *= smp4.a;
                sum += smp4;
                vec4 result = sum / 8;
                gl_FragColor = vec4(result.rgb / result.a, result.a);
            }
            """;
}