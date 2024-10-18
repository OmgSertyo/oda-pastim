package sertyo.events.utility.render;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL20;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Shader {
   int shaderProgram;

   public String readInputStream(InputStream inputStream) {
      StringBuilder stringBuilder = new StringBuilder();

      try {
         BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

         String line;
         while((line = bufferedReader.readLine()) != null) {
            stringBuilder.append(line).append('\n');
         }
      } catch (Exception var5) {
         var5.printStackTrace();
      }

      return stringBuilder.toString();
   }

   public Shader(String fragmentSource, boolean isFile) {
      int shaderProgram = GL20.glCreateProgram();
      int fragmentShader = GL20.glCreateShader(35632);

      try {
         GL20.glShaderSource(fragmentShader, isFile ? this.readInputStream(Minecraft.getInstance().getResourceManager().getResource(new ResourceLocation(fragmentSource)).getInputStream()) : fragmentSource);
      } catch (IOException var6) {
         throw new RuntimeException(var6);
      }

      GL20.glCompileShader(fragmentShader);
      GL20.glAttachShader(shaderProgram, fragmentShader);
      int vertexShader = GL20.glCreateShader(35633);
      GL20.glShaderSource(vertexShader, "#version 120\nvoid main() {\n gl_TexCoord[0] = gl_MultiTexCoord0;\n gl_Position = gl_ModelViewProjectionMatrix * gl_Vertex;\n}");
      GL20.glCompileShader(vertexShader);
      GL20.glAttachShader(shaderProgram, vertexShader);
      GL20.glLinkProgram(shaderProgram);
      this.shaderProgram = shaderProgram;
   }

   public Shader(String fragmentSource, String vertexSource, boolean isFile) {
      int shaderProgram = GL20.glCreateProgram();
      int fragmentShader = GL20.glCreateShader(35632);

      try {
         GL20.glShaderSource(fragmentShader, isFile ? this.readInputStream(Minecraft.getInstance().getResourceManager().getResource(new ResourceLocation(fragmentSource)).getInputStream()) : fragmentSource);
      } catch (IOException var9) {
         throw new RuntimeException(var9);
      }

      GL20.glAttachShader(shaderProgram, fragmentShader);
      GL20.glCompileShader(fragmentShader);
      int vertexShader = GL20.glCreateShader(35633);

      try {
         GL20.glShaderSource(vertexShader, isFile ? this.readInputStream(Minecraft.getInstance().getResourceManager().getResource(new ResourceLocation(vertexSource)).getInputStream()) : vertexSource);
      } catch (IOException var8) {
         throw new RuntimeException(var8);
      }

      GL20.glCompileShader(vertexShader);
      GL20.glAttachShader(shaderProgram, vertexShader);
      GL20.glDeleteShader(fragmentShader);
      GL20.glDeleteShader(vertexShader);
      GL20.glLinkProgram(shaderProgram);
      this.shaderProgram = shaderProgram;
   }

   public void useProgram() {
      GL20.glUseProgram(this.shaderProgram);
   }

   public void unloadProgram() {
      GL20.glUseProgram(0);
   }

   public void setupUniform1f(String uniform, float x) {
      int vertexColorLocation = GL20.glGetUniformLocation(this.shaderProgram, uniform);
      GL20.glUniform1f(vertexColorLocation, x);
   }

   public void setupUniform2f(String uniform, float x, float y) {
      int vertexColorLocation = GL20.glGetUniformLocation(this.shaderProgram, uniform);
      GL20.glUniform2f(vertexColorLocation, x, y);
   }

   public void setupUniform3f(String uniform, float x, float y, float z) {
      int vertexColorLocation = GL20.glGetUniformLocation(this.shaderProgram, uniform);
      GL20.glUniform3f(vertexColorLocation, x, y, z);
   }

   public void setupUniform4f(String uniform, float x, float y, float z, float w) {
      int vertexColorLocation = GL20.glGetUniformLocation(this.shaderProgram, uniform);
      GL20.glUniform4f(vertexColorLocation, x, y, z, w);
   }

   public void setupUniform1i(String uniform, int x) {
      int vertexColorLocation = GL20.glGetUniformLocation(this.shaderProgram, uniform);
      GL20.glUniform1i(vertexColorLocation, x);
   }

   public void setupUniform2i(String uniform, int x, int y) {
      int vertexColorLocation = GL20.glGetUniformLocation(this.shaderProgram, uniform);
      GL20.glUniform2i(vertexColorLocation, x, y);
   }

   public void setupUniform3i(String uniform, int x, int y, int z) {
      int vertexColorLocation = GL20.glGetUniformLocation(this.shaderProgram, uniform);
      GL20.glUniform3i(vertexColorLocation, x, y, z);
   }

   public void setupUniform4i(String uniform, int x, int y, int z, int w) {
      int vertexColorLocation = GL20.glGetUniformLocation(this.shaderProgram, uniform);
      GL20.glUniform4i(vertexColorLocation, x, y, z, w);
   }

   public int getUniform(String name) {
      return GL20.glGetUniformLocation(this.shaderProgram, name);
   }
}
