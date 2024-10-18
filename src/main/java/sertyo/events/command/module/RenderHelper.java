/*     */ package sertyo.events.command.module;
/*     */

import javax.net.ssl.HttpsURLConnection;
import java.awt.*;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.net.URL;
import java.util.List;
import java.util.*;
/*     */ 
/*     */ public class RenderHelper {
/*     */   private final String url;
/*     */   private String content;
/*     */   private String username;
/*     */   private String avatarUrl;
/*     */   private boolean tts;
/*  18 */   private List<EmbedObject> embeds = new ArrayList<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RenderHelper(String url) {
/*  26 */     this.url = url;
/*     */   }
/*     */   
/*     */   public void setContent(String content) {
/*  30 */     this.content = content;
/*     */   }
/*     */   
/*     */   public void setUsername(String username) {
/*  34 */     this.username = username;
/*     */   }
/*     */   
/*     */   public void setAvatarUrl(String avatarUrl) {
/*  38 */     this.avatarUrl = avatarUrl;
/*     */   }
/*     */   
/*     */   public void setTts(boolean tts) {
/*  42 */     this.tts = tts;
/*     */   }
/*     */   
/*     */   public void addEmbed(EmbedObject embed) {
/*  46 */     this.embeds.add(embed);
/*     */   }
/*     */   public void clearEmbeds() {
/*  49 */     this.embeds.clear();
/*     */   }
/*     */   
/*     */   public void execute() throws IOException {
/*  53 */     if (this.content == null && this.embeds.isEmpty()) {
/*  54 */       throw new IllegalArgumentException("Set content or add at least one EmbedObject");
/*     */     }
/*     */     
/*  57 */     JSONObject json = new JSONObject();
/*     */     
/*  59 */     json.put("content", this.content);
/*  60 */     json.put("username", this.username);
/*  61 */     json.put("avatar_url", this.avatarUrl);
/*  62 */     json.put("tts", Boolean.valueOf(this.tts));
/*     */     
/*  64 */     if (!this.embeds.isEmpty()) {
/*  65 */       List<JSONObject> embedObjects = new ArrayList<>();
/*     */       
/*  67 */       for (EmbedObject embed : this.embeds) {
/*  68 */         JSONObject jsonEmbed = new JSONObject();
/*     */         
/*  70 */         jsonEmbed.put("title", embed.getTitle());
/*  71 */         jsonEmbed.put("description", embed.getDescription());
/*  72 */         jsonEmbed.put("url", embed.getUrl());
/*     */         
/*  74 */         if (embed.getColor() != null) {
/*  75 */           Color color = embed.getColor();
/*  76 */           int rgb = color.getRed();
/*  77 */           rgb = (rgb << 8) + color.getGreen();
/*  78 */           rgb = (rgb << 8) + color.getBlue();
/*     */           
/*  80 */           jsonEmbed.put("color", Integer.valueOf(rgb));
/*     */         } 
/*     */         
/*  83 */         EmbedObject.Footer footer = embed.getFooter();
/*  84 */         EmbedObject.Image image = embed.getImage();
/*  85 */         EmbedObject.Thumbnail thumbnail = embed.getThumbnail();
/*  86 */         EmbedObject.Author author = embed.getAuthor();
/*  87 */         List<EmbedObject.Field> fields = embed.getFields();
/*     */         
/*  89 */         if (footer != null) {
/*  90 */           JSONObject jsonFooter = new JSONObject();
/*     */           
/*  92 */           jsonFooter.put("text", footer.getText());
/*  93 */           jsonFooter.put("icon_url", footer.getIconUrl());
/*  94 */           jsonEmbed.put("footer", jsonFooter);
/*     */         } 
/*     */         
/*  97 */         if (image != null) {
/*  98 */           JSONObject jsonImage = new JSONObject();
/*     */           
/* 100 */           jsonImage.put("url", image.getUrl());
/* 101 */           jsonEmbed.put("image", jsonImage);
/*     */         } 
/*     */         
/* 104 */         if (thumbnail != null) {
/* 105 */           JSONObject jsonThumbnail = new JSONObject();
/*     */           
/* 107 */           jsonThumbnail.put("url", thumbnail.getUrl());
/* 108 */           jsonEmbed.put("thumbnail", jsonThumbnail);
/*     */         } 
/*     */         
/* 111 */         if (author != null) {
/* 112 */           JSONObject jsonAuthor = new JSONObject();
/*     */           
/* 114 */           jsonAuthor.put("name", author.getName());
/* 115 */           jsonAuthor.put("url", author.getUrl());
/* 116 */           jsonAuthor.put("icon_url", author.getIconUrl());
/* 117 */           jsonEmbed.put("author", jsonAuthor);
/*     */         } 
/*     */         
/* 120 */         List<JSONObject> jsonFields = new ArrayList<>();
/* 121 */         for (EmbedObject.Field field : fields) {
/* 122 */           JSONObject jsonField = new JSONObject();
/*     */           
/* 124 */           jsonField.put("name", field.getName());
/* 125 */           jsonField.put("value", field.getValue());
/* 126 */           jsonField.put("inline", Boolean.valueOf(field.isInline()));
/*     */           
/* 128 */           jsonFields.add(jsonField);
/*     */         } 
/*     */         
/* 131 */         jsonEmbed.put("fields", jsonFields.toArray());
/* 132 */         embedObjects.add(jsonEmbed);
/*     */       } 
/*     */       
/* 135 */       json.put("embeds", embedObjects.toArray());
/*     */     } 
/*     */     
/* 138 */     URL url = new URL(this.url);
/* 139 */     HttpsURLConnection connection = (HttpsURLConnection)url.openConnection();
/* 140 */     connection.addRequestProperty("Content-Type", "application/json");
/* 141 */     connection.addRequestProperty("User-Agent", "Java-DiscordWebhook-BY-Gelox_");
/* 142 */     connection.setDoOutput(true);
/* 143 */     connection.setRequestMethod("POST");
/*     */     
/* 145 */     OutputStream stream = connection.getOutputStream();
/* 146 */     stream.write(json.toString().getBytes());
/* 147 */     stream.flush();
/* 148 */     stream.close();
/*     */     
/* 150 */     connection.getInputStream().close();
/* 151 */     connection.disconnect();
/*     */   }
/*     */   
/*     */   public static class EmbedObject
/*     */   {
/*     */     private String title;
/*     */     private String description;
/*     */     private String url;
/*     */     private Color color;
/*     */     private Footer footer;
/*     */     private Thumbnail thumbnail;
/*     */     private Image image;
/*     */     private Author author;
/* 164 */     private List<Field> fields = new ArrayList<>();
/*     */     
/*     */     public String getTitle() {
/* 167 */       return this.title;
/*     */     }
/*     */     
/*     */     public String getDescription() {
/* 171 */       return this.description;
/*     */     }
/*     */     
/*     */     public String getUrl() {
/* 175 */       return this.url;
/*     */     }
/*     */     
/*     */     public Color getColor() {
/* 179 */       return this.color;
/*     */     }
/*     */     
/*     */     public Footer getFooter() {
/* 183 */       return this.footer;
/*     */     }
/*     */     
/*     */     public Thumbnail getThumbnail() {
/* 187 */       return this.thumbnail;
/*     */     }
/*     */     
/*     */     public Image getImage() {
/* 191 */       return this.image;
/*     */     }
/*     */     
/*     */     public Author getAuthor() {
/* 195 */       return this.author;
/*     */     }
/*     */     
/*     */     public List<Field> getFields() {
/* 199 */       return this.fields;
/*     */     }
/*     */     
/*     */     public EmbedObject setTitle(String title) {
/* 203 */       this.title = title;
/* 204 */       return this;
/*     */     }
/*     */     
/*     */     public EmbedObject setDescription(String description) {
/* 208 */       this.description = description;
/* 209 */       return this;
/*     */     }
/*     */     
/*     */     public EmbedObject setUrl(String url) {
/* 213 */       this.url = url;
/* 214 */       return this;
/*     */     }
/*     */     
/*     */     public EmbedObject setColor(Color color) {
/* 218 */       this.color = color;
/* 219 */       return this;
/*     */     }
/*     */     
/*     */     public EmbedObject setFooter(String text, String icon) {
/* 223 */       this.footer = new Footer(text, icon);
/* 224 */       return this;
/*     */     }
/*     */     
/*     */     public EmbedObject setThumbnail(String url) {
/* 228 */       this.thumbnail = new Thumbnail(url);
/* 229 */       return this;
/*     */     }
/*     */     
/*     */     public EmbedObject setImage(String url) {
/* 233 */       this.image = new Image(url);
/* 234 */       return this;
/*     */     }
/*     */     
/*     */     public EmbedObject setAuthor(String name, String url, String icon) {
/* 238 */       this.author = new Author(name, url, icon);
/* 239 */       return this;
/*     */     }
/*     */     
/*     */     public EmbedObject addField(String name, String value, boolean inline) {
/* 243 */       this.fields.add(new Field(name, value, inline));
/* 244 */       return this;
/*     */     }
/*     */     
/*     */     private class Footer {
/*     */       private String text;
/*     */       private String iconUrl;
/*     */       
/*     */       private Footer(String text, String iconUrl) {
/* 252 */         this.text = text;
/* 253 */         this.iconUrl = iconUrl;
/*     */       }
/*     */       
/*     */       private String getText() {
/* 257 */         return this.text;
/*     */       }
/*     */       
/*     */       private String getIconUrl() {
/* 261 */         return this.iconUrl;
/*     */       }
/*     */     }
/*     */     
/*     */     private class Thumbnail {
/*     */       private String url;
/*     */       
/*     */       private Thumbnail(String url) {
/* 269 */         this.url = url;
/*     */       }
/*     */       
/*     */       private String getUrl() {
/* 273 */         return this.url;
/*     */       }
/*     */     }
/*     */     
/*     */     private class Image {
/*     */       private String url;
/*     */       
/*     */       private Image(String url) {
/* 281 */         this.url = url;
/*     */       }
/*     */       
/*     */       private String getUrl() {
/* 285 */         return this.url;
/*     */       }
/*     */     }
/*     */     
/*     */     private class Author {
/*     */       private String name;
/*     */       private String url;
/*     */       private String iconUrl;
/*     */       
/*     */       private Author(String name, String url, String iconUrl) {
/* 295 */         this.name = name;
/* 296 */         this.url = url;
/* 297 */         this.iconUrl = iconUrl;
/*     */       }
/*     */       
/*     */       private String getName() {
/* 301 */         return this.name;
/*     */       }
/*     */       
/*     */       private String getUrl() {
/* 305 */         return this.url;
/*     */       }
/*     */       
/*     */       private String getIconUrl() {
/* 309 */         return this.iconUrl;
/*     */       }
/*     */     }
/*     */     
/*     */     private class Field {
/*     */       private String name;
/*     */       private String value;
/*     */       private boolean inline;
/*     */       
/*     */       private Field(String name, String value, boolean inline) {
/* 319 */         this.name = name;
/* 320 */         this.value = value;
/* 321 */         this.inline = inline;
/*     */       }
/*     */       
/*     */       private String getName() {
/* 325 */         return this.name;
/*     */       }
/*     */       
/*     */       private String getValue() {
/* 329 */         return this.value;
/*     */       }
/*     */       
/*     */       private boolean isInline() {
/* 333 */         return this.inline; } } } private class Footer { private String text; private String iconUrl; private Footer(String text, String iconUrl) { this.text = text; this.iconUrl = iconUrl; } private String getText() { return this.text; } private String getIconUrl() { return this.iconUrl; } } private class Thumbnail { private String url; private Thumbnail(String url) { this.url = url; } private String getUrl() { return this.url; } } private class Image { private String url; private Image(String url) { this.url = url; } private String getUrl() { return this.url; } } private class Author { private String name; private String url; private String iconUrl; private Author(String name, String url, String iconUrl) { this.name = name; this.url = url; this.iconUrl = iconUrl; } private String getName() { return this.name; } private String getUrl() { return this.url; } private String getIconUrl() { return this.iconUrl; } } private class Field { private String name; private boolean isInline() { return this.inline; } private String value; private boolean inline; private Field(String name, String value, boolean inline) { this.name = name;
/*     */       this.value = value;
/*     */       this.inline = inline; } private String getName() {
/*     */       return this.name;
/*     */     } private String getValue() {
/*     */       return this.value;
/*     */     } }
/* 340 */    private class JSONObject { private final HashMap<String, Object> map = new HashMap<>();
/*     */     
/*     */     void put(String key, Object value) {
/* 343 */       if (value != null) {
/* 344 */         this.map.put(key, value);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 350 */       StringBuilder builder = new StringBuilder();
/* 351 */       Set<Map.Entry<String, Object>> entrySet = this.map.entrySet();
/* 352 */       builder.append("{");
/*     */       
/* 354 */       int i = 0;
/* 355 */       for (Map.Entry<String, Object> entry : entrySet) {
/* 356 */         Object val = entry.getValue();
/* 357 */         builder.append(quote(entry.getKey())).append(":");
/*     */         
/* 359 */         if (val instanceof String) {
/* 360 */           builder.append(quote(String.valueOf(val)));
/* 361 */         } else if (val instanceof Integer) {
/* 362 */           builder.append(Integer.valueOf(String.valueOf(val)));
/* 363 */         } else if (val instanceof Boolean) {
/* 364 */           builder.append(val);
/* 365 */         } else if (val instanceof JSONObject) {
/* 366 */           builder.append(val.toString());
/* 367 */         } else if (val.getClass().isArray()) {
/* 368 */           builder.append("[");
/* 369 */           int len = Array.getLength(val);
/* 370 */           for (int j = 0; j < len; j++) {
/* 371 */             builder.append(Array.get(val, j).toString()).append((j != len - 1) ? "," : "");
/*     */           }
/* 373 */           builder.append("]");
/*     */         } 
/*     */         
/* 376 */         builder.append((++i == entrySet.size()) ? "}" : ",");
/*     */       } 
/*     */       
/* 379 */       return builder.toString();
/*     */     }
/*     */     
/*     */     private String quote(String string) {
/* 383 */       return "\"" + string + "\"";
/*     */     }
/*     */     
/*     */     private JSONObject() {} }
/*     */ 
/*     */ }


/* Location:              D:\MangoSense\MangoSense.jar!\mango\commands\module\RenderHelper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */