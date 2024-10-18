/*     */
package sertyo.events.module.impl.util.autobuyhw;
/*     */ import com.darkmagician6.eventapi.EventTarget;
import com.google.gson.JsonArray;
/*     */ import com.google.gson.JsonElement;
/*     */ import com.google.gson.JsonIOException;
/*     */ import com.google.gson.JsonObject;
/*     */ import com.google.gson.JsonParseException;
/*     */ import com.google.gson.JsonSyntaxException;
/*     */ import com.google.gson.internal.Streams;
/*     */ import com.google.gson.stream.JsonReader;
/*     */ import com.google.gson.stream.JsonToken;
/*     */ import com.google.gson.stream.MalformedJsonException;

/*     */ import java.io.IOException;
/*     */ import java.io.Reader;
/*     */ import java.io.StringReader;
/*     */ import java.util.LinkedList;
import java.util.Queue;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.screen.Screen;
/*     */ import net.minecraft.inventory.container.ChestContainer;
/*     */ import net.minecraft.inventory.container.Slot;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.nbt.CompoundNBT;
/*     */ import net.minecraft.nbt.ListNBT;
import sertyo.events.event.player.EventUpdate;
import sertyo.events.module.Category;
import sertyo.events.module.ModuleAnnotation;
import sertyo.events.module.impl.util.A;
import sertyo.events.module.impl.util.autobuyhw.gui.ItemPrice;

@ModuleAnnotation(name = "AutoSetup", category = Category.UTIL)
/*     */ public class AutoSetup extends sertyo.events.module.Module {
/*  29 */   private final Minecraft mc = Minecraft.getInstance();
/*  30 */   private final Queue<String> searchQueue = new LinkedList<>();
/*     */   private boolean commandSent = false;
/*  32 */   int buynumber = 0;
/*  33 */   A ti = new A();
/*  34 */   A tis = new A();
/*     */ 
/*     */   
/*     */   public AutoSetup() {
/*  39 */     this.searchQueue.add("шлем этернити");
/*  40 */     this.searchQueue.add("ботинки eternity");
/*  41 */     this.searchQueue.add("универсальный ключ");
/*  42 */     this.searchQueue.add("броневая элитра");
/*  43 */     this.searchQueue.add("рассадник");
/*  44 */     this.searchQueue.add("опыт 100");
/*  45 */     this.searchQueue.add("опыт 50");
/*  46 */     this.searchQueue.add("талисман сатиры");
/*     */
/*  48 */     this.searchQueue.add("нерушимые элитры");
/*  49 */     this.searchQueue.add("справедливость");
/*  50 */     this.searchQueue.add("шлем солнца");
/*  51 */     this.searchQueue.add("стан");
/*  52 */     this.searchQueue.add("боевой фрагмент");
/*  53 */     this.searchQueue.add("взрывчатое вещество");
/*  54 */     this.searchQueue.add("нагрудник eternity");
/*  55 */     this.searchQueue.add("штаны eternity");
/*     */
/*  57 */     this.searchQueue.add("меч eternity");
/*  58 */     this.searchQueue.add("кирка eternity");
/*  59 */     this.searchQueue.add("шлем infinity");
/*  60 */     this.searchQueue.add("нагрудник infinity");
/*  61 */     this.searchQueue.add("штаны infinity");
/*  62 */     this.searchQueue.add("ботинки infinity");
/*  63 */     this.searchQueue.add("талисман eternity");
/*  64 */     this.searchQueue.add("талисман stinger");
/*     */
/*  66 */     this.searchQueue.add("сфера цербера");
/*  67 */     this.searchQueue.add("сфера флеша");
/*     */   }

/*     */   
/*     */   private void sendAuctionSearchCommand(String item) {
/*  72 */     if (this.mc.player != null) {
/*  73 */       this.mc.player.sendChatMessage("/ah search " + item);
/*  74 */       this.commandSent = true;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   @EventTarget
/*     */   public void onUpdate(EventUpdate event) {
    if (mc.player != null && this.mc.currentScreen != null &&
            this.mc.currentScreen.getTitle().getString().contains("Аукцион")) {
/*  83 */       String currentItem = this.searchQueue.peek();
/*     */       
/*  85 */       if (currentItem != null && currentItem.contains("меч eternity")) {
/*     */         
/*  87 */         ItemPrice.setPrice("swordeternityt", getminprice());
/*  88 */         ItemPrice.savePrices(); if (this.tis.hasTimeElapsed(800L, true)) mc.currentScreen = null;
/*     */       
/*     */       } 
/*     */       
/*  92 */       if (currentItem != null && currentItem.contains("кирка eternity")) {
/*     */         
/*  94 */         ItemPrice.setPrice("kirkaeternity", getminprice());
/*  95 */         ItemPrice.savePrices(); if (this.tis.hasTimeElapsed(800L, true)) mc.currentScreen = null;
/*     */       
/*     */       } 
/*  98 */       if (currentItem != null && currentItem.contains("шлем infinity")) {
/*     */         
/* 100 */         ItemPrice.setPrice("shleminf", getminprice());
/* 101 */         ItemPrice.savePrices(); if (this.tis.hasTimeElapsed(800L, true)) mc.currentScreen = null;
/*     */       
/*     */       } 
/* 104 */       if (currentItem != null && currentItem.contains("нагрудник infinity")) {
/*     */         
/* 106 */         ItemPrice.setPrice("grudakinf", getminprice());
/* 107 */         ItemPrice.savePrices(); if (this.tis.hasTimeElapsed(800L, true)) mc.currentScreen = null;
/*     */       
/*     */       } 
/*     */       
/* 111 */       if (currentItem != null && currentItem.contains("штаны infinity")) {
/*     */         
/* 113 */         ItemPrice.setPrice("legginsinf", getminprice());
/* 114 */         ItemPrice.savePrices(); if (this.tis.hasTimeElapsed(800L, true)) mc.currentScreen = null;
/*     */       
/*     */       } 
/* 117 */       if (currentItem != null && currentItem.contains("ботинки infinity")) {
/*     */         
/* 119 */         ItemPrice.setPrice("bootinf", getminprice());
/* 120 */         ItemPrice.savePrices(); if (this.tis.hasTimeElapsed(800L, true)) mc.currentScreen = null;
/*     */       
/*     */       } 
/* 123 */       if (currentItem != null && currentItem.contains("талисман stinger")) {
/*     */         
/* 125 */         ItemPrice.setPrice("talicstring", getminprice());
/* 126 */         ItemPrice.savePrices(); if (this.tis.hasTimeElapsed(800L, true)) mc.currentScreen = null;
/*     */       
/*     */       } 
/* 129 */       if (currentItem != null && currentItem.contains("талисман eternity")) {
/*     */         
/* 131 */         ItemPrice.setPrice("talicetern", getminprice());
/* 132 */         ItemPrice.savePrices(); if (this.tis.hasTimeElapsed(800L, true)) mc.currentScreen = null;
/*     */       
/*     */       } 
/*     */ 
/*     */       
/* 137 */       if (currentItem != null && currentItem.contains("шлем этернити")) {
/*     */         
/* 139 */         ItemPrice.setPrice("shlemetenity", getminprice());
/* 140 */         ItemPrice.savePrices(); if (this.tis.hasTimeElapsed(800L, true)) mc.currentScreen = null;
/*     */       
/*     */       } 
/* 143 */       if (currentItem != null && currentItem.contains("ботинки eternity")) {
/*     */         
/* 145 */         ItemPrice.setPrice("booteternity", getminprice());
/* 146 */         ItemPrice.savePrices(); if (this.tis.hasTimeElapsed(800L, true)) mc.currentScreen = null; 
/*     */       }
/* 149 */       if (currentItem != null && currentItem.contains("универсальный ключ")) {
/*     */         
/* 151 */         ItemPrice.setPrice("yniversalka", getminprice());
/* 152 */         ItemPrice.savePrices(); if (this.tis.hasTimeElapsed(800L, true)) mc.currentScreen = null; 
/*     */       } 
/* 154 */       if (currentItem != null && currentItem.contains("броневая элитра")) {
/*     */         
/* 156 */         ItemPrice.setPrice("armorelytra", getminprice());
/* 157 */         ItemPrice.savePrices(); if (this.tis.hasTimeElapsed(800L, true)) mc.currentScreen = null; 
/*     */       } 
/* 159 */       if (currentItem != null && currentItem.contains("рассадник")) {
/*     */         
/* 161 */         ItemPrice.setPrice("spawner", getminprice());
/* 162 */         ItemPrice.savePrices(); if (this.tis.hasTimeElapsed(800L, true)) mc.currentScreen = null; 
/*     */       } 
/* 164 */       if (currentItem != null && currentItem.contains("опыт 100")) {
/*     */         
/* 166 */         ItemPrice.setPrice("opit100", getminprice());
/* 167 */         ItemPrice.savePrices(); if (this.tis.hasTimeElapsed(800L, true)) mc.currentScreen = null; 
/*     */       } 
/* 169 */       if (currentItem != null && currentItem.contains("опыт 50")) {
/*     */         
/* 171 */         ItemPrice.setPrice("opit50", getminprice());
/* 172 */         ItemPrice.savePrices(); if (this.tis.hasTimeElapsed(800L, true)) mc.currentScreen = null; 
/*     */       } 
/* 174 */       if (currentItem != null && currentItem.contains("талисман сатиры")) {
/*     */         
/* 176 */         ItemPrice.setPrice("talicSatiri", getminprice());
/* 177 */         ItemPrice.savePrices(); if (this.tis.hasTimeElapsed(800L, true)) mc.currentScreen = null; 
/*     */       } 
/* 179 */       if (currentItem != null && currentItem.contains("нерушимые элитры")) {
/*     */         
/* 181 */         ItemPrice.setPrice("negaelt", getminprice());
/* 182 */         ItemPrice.savePrices(); if (this.tis.hasTimeElapsed(800L, true)) mc.currentScreen = null; 
/*     */       } 
/* 184 */       if (currentItem != null && currentItem.contains("справедливость")) {
/*     */         
/* 186 */         ItemPrice.setPrice("mekenik", getminprice());
/* 187 */         ItemPrice.savePrices(); if (this.tis.hasTimeElapsed(800L, true)) mc.currentScreen = null; 
/*     */       } 
/* 189 */       if (currentItem != null && currentItem.contains("шлем солнца")) {
/*     */         
/* 191 */         ItemPrice.setPrice("sunhelmet", getminprice());
/* 192 */         ItemPrice.savePrices(); if (this.tis.hasTimeElapsed(800L, true)) mc.currentScreen = null; 
/*     */       } 
/* 194 */       if (currentItem != null && currentItem.contains("стан")) {
/*     */         
/* 196 */         ItemPrice.setPrice("stan", getminprice());
/* 197 */         ItemPrice.savePrices(); if (this.tis.hasTimeElapsed(800L, true)) mc.currentScreen = null; 
/*     */       } 
/* 199 */       if (currentItem != null && currentItem.contains("боевой фрагмент")) {
/*     */         
/* 201 */         ItemPrice.setPrice("battle", getminprice());
/* 202 */         ItemPrice.savePrices(); if (this.tis.hasTimeElapsed(800L, true)) mc.currentScreen = null; 
/*     */       } 
/* 204 */       if (currentItem != null && currentItem.contains("взрывчатое вещество")) {
/*     */         
/* 206 */         ItemPrice.setPrice("clay", getminprice());
/* 207 */         ItemPrice.savePrices(); if (this.tis.hasTimeElapsed(800L, true)) mc.currentScreen = null; 
/*     */       } 
/* 209 */       if (currentItem != null && currentItem.contains("нагрудник eternity")) {
/*     */         
/* 211 */         ItemPrice.setPrice("chesteternity", getminprice());
/* 212 */         ItemPrice.savePrices(); if (this.tis.hasTimeElapsed(800L, true)) mc.currentScreen = null; 
/*     */       } 
/* 214 */       if (currentItem != null && currentItem.contains("штаны eternity")) {
/*     */         
/* 216 */         ItemPrice.setPrice("legginseternirty", getminprice());
/* 217 */         ItemPrice.savePrices(); if (this.tis.hasTimeElapsed(800L, true)) mc.currentScreen = null;
/*     */       
/*     */       } 
/*     */     } 
/* 221 */     if (this.mc.currentScreen == null && this.mc.player != null) {
/* 222 */       if (!this.commandSent && !this.searchQueue.isEmpty()) {
/* 223 */         sendAuctionSearchCommand(this.searchQueue.peek());
/*     */       }
/* 225 */       if (this.ti.hasTimeElapsed(300L, true)) {
/* 226 */         searchNextItem();
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void searchNextItem() {
/* 236 */     if (!this.searchQueue.isEmpty()) {
/* 237 */       this.searchQueue.poll();
/* 238 */       this.commandSent = false;
/*     */     } 
/*     */   }
/*     */ 
/*     */
/*     */   public double getminprice() {
    /* 244 */     if (this.mc.currentScreen instanceof net.minecraft.client.gui.screen.inventory.ChestScreen) {
        /* 245 */       Screen e = this.mc.currentScreen;
        /* 246 */       if (e.getTitle().getString().contains("Аукцион")) {
            /* 247 */         if (this.mc.player.openContainer instanceof ChestContainer) {
                /* 248 */           ChestContainer container = (ChestContainer) this.mc.player.openContainer;
                /* 249 */           Slot cheapestSlot = null;
                /* 250 */           double lowestUnitPrice = Double.MAX_VALUE;
                /*     */
                /* 252 */           for (Slot slot : container.inventorySlots) {
                    /* 253 */             if (slot.slotIndex > 44) {
                        /*     */               continue;
                        /*     */             }
                    /*     */
                    /* 256 */             ItemStack stack = slot.getStack();
                    /* 257 */             String nbtData = getNBT(stack);
                    /*     */
                    /* 259 */             if (nbtData != null) {
                        /* 260 */               int totalPrice = getPrice(stack);
                        /* 261 */               int itemCount = stack.getCount();
                        /*     */
                        /* 263 */               if (totalPrice != -1 && itemCount > 0) {
                            /* 264 */                 double unitPrice = totalPrice / itemCount;
                            /*     */
                            /* 266 */                 if (unitPrice < lowestUnitPrice) {
                                /* 267 */                   lowestUnitPrice = unitPrice;
                                /*     */                 }
                            /*     */               }
                        /*     */             }
                    /*     */           }
                /*     */
                /* 273 */           if (lowestUnitPrice < Double.MAX_VALUE) {
                    /* 274 */             double discountedPrice = lowestUnitPrice * 0.7D;
                    /* 275 */             return discountedPrice;
                    /*     */           }
                /*     */         }
            /*     */       }
        /*     */     }
    /* 279 */     return -1.0D;
    /*     */   }/*     */   public static String getNBT(ItemStack stack) {
/* 283 */     CompoundNBT tag = stack.getTag();
/* 284 */     return (tag != null) ? tag.toString() : null;
/*     */   }
/*     */   
/*     */   public static JsonElement parseString(String json) throws JsonSyntaxException {
/* 288 */     return parseReader(new StringReader(json));
/*     */   }
/*     */   
/*     */   public static JsonElement parseReader(Reader reader) throws JsonIOException, JsonSyntaxException {
/*     */     try {
/* 293 */       JsonReader jsonReader = new JsonReader(reader);
/* 294 */       JsonElement element = parseReader(jsonReader);
/* 295 */       if (!element.isJsonNull() && jsonReader.peek() != JsonToken.END_DOCUMENT) {
/* 296 */         throw new JsonSyntaxException("Did not consume the entire document.");
/*     */       }
/* 298 */       return element;
/*     */     }
/* 300 */     catch (MalformedJsonException var3) {
/* 301 */       MalformedJsonException e = var3;
/* 302 */       throw new JsonSyntaxException(e);
/* 303 */     } catch (IOException var4) {
/* 304 */       IOException e = var4;
/* 305 */       throw new JsonIOException(e);
/* 306 */     } catch (NumberFormatException var5) {
/* 307 */       NumberFormatException e = var5;
/* 308 */       throw new JsonSyntaxException(e);
/*     */     } 
/*     */   }
/*     */   public static JsonElement parseReader(JsonReader reader) throws JsonIOException, JsonSyntaxException {
/*     */     JsonElement var11;
/* 313 */     boolean lenient = reader.isLenient();
/* 314 */     reader.setLenient(true);
/*     */ 
/*     */     
/*     */     try {
/* 318 */       var11 = Streams.parse(reader);
/* 319 */     } catch (StackOverflowError var7) {
/* 320 */       StackOverflowError e = var7;
/* 321 */       throw new JsonParseException("Failed parsing JSON source: " + reader + " to Json", e);
/* 322 */     } catch (OutOfMemoryError var8) {
/* 323 */       OutOfMemoryError e = var8;
/* 324 */       throw new JsonParseException("Failed parsing JSON source: " + reader + " to Json", e);
/*     */     } finally {
/* 326 */       reader.setLenient(lenient);
/*     */     } 
/*     */     
/* 329 */     return var11;
/*     */   }
/*     */
/*     */   private int getPrice(ItemStack stack) {
    /* 333 */     CompoundNBT tag = stack.getTag();
    /*     */
    /* 335 */     if (tag != null && tag.contains("display", 10)) {
        /* 336 */       CompoundNBT display = tag.getCompound("display");
        /*     */
        /* 338 */       if (display.contains("Lore", 9)) {
            /* 339 */         ListNBT lore = display.getList("Lore", 8);
            /*     */
            /* 341 */         for (int j = 0; j < lore.size(); j++) {
                /* 342 */           String loreString = lore.getString(j);
                /* 343 */           JsonObject object = parseString(loreString).getAsJsonObject();
                /*     */
                /* 345 */           if (object.has("extra")) {
                    /* 346 */             JsonArray extraArray = object.getAsJsonArray("extra");
                    /*     */
                    /* 348 */             for (int k = 0; k < extraArray.size(); k++) {
                        /* 349 */               JsonObject extraObject = extraArray.get(k).getAsJsonObject();
                        /* 350 */               String text = extraObject.get("text").getAsString().trim();
                        /*     */
                        /* 352 */               if (text.contains("Цeнa")) {
                            /*     */                 if (k + 2 < extraArray.size()) {
                                /* 355 */                   JsonObject priceObject = extraArray.get(k + 2).getAsJsonObject();
                                /* 356 */                   String priceText = priceObject.get("text").getAsString().trim().replaceAll("[^0-9]", "");
                                /*     */
                                /*     */                   try {
                                    /* 359 */                     return Integer.parseInt(priceText);
                                    /* 360 */                   } catch (NumberFormatException e) {
                                    /*     */
                                    /* 362 */                     return -1;
                                    /*     */                   }
                                /*     */                 }
                            /*     */               }
                        /*     */             }
                    /*     */           }
                /*     */         }
            /*     */       }
        /*     */     }
    /*     */
    /* 372 */     return -1;
    /*     */   }/*     */
/*     */   
/*     */   public void onEnable() {
/* 377 */     this.ti.reset();
                System.out.println(ItemPrice.getPrice("sunhelmet"));
/* 379 */     super.onEnable();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void onDisable() {
/* 385 */     this.searchQueue.clear();
/* 386 */     this.searchQueue.add("шлем этернити");
/* 387 */     this.searchQueue.add("ботинки eternity");
/* 388 */     this.searchQueue.add("универсальный ключ");
/* 389 */     this.searchQueue.add("броневая элитра");
/* 390 */     this.searchQueue.add("рассадник");
/* 391 */     this.searchQueue.add("опыт 100");
/* 392 */     this.searchQueue.add("опыт 50");
/* 393 */     this.searchQueue.add("талисман сатиры");
/*     */     
/* 395 */     this.searchQueue.add("нерушимые элитры");
/* 396 */     this.searchQueue.add("справедливость");
/* 397 */     this.searchQueue.add("шлем солнца");
/* 398 */     this.searchQueue.add("стан");
/* 399 */     this.searchQueue.add("боевой фрагмент");
/* 400 */     this.searchQueue.add("взрывчатое вещество");
/* 401 */     this.searchQueue.add("нагрудник eternity");
/* 402 */     this.searchQueue.add("штаны eternity");
/* 404 */     super.onDisable();
/*     */   }
/*     */ }


/* Location:              /Users/v/Downloads/PrivatBuy.jar!/examplemod/Module/imlp/AutoSetup.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */