package sertyo.events.module.impl.util;

/*     */ import com.darkmagician6.eventapi.EventTarget;
import com.google.gson.JsonArray;
/*     */ import com.google.gson.JsonElement;
/*     */ import com.google.gson.JsonIOException;
/*     */ import com.google.gson.JsonObject;
/*     */ import com.google.gson.JsonParseException;
/*     */ import com.google.gson.JsonSyntaxException;
/*     */ import com.google.gson.internal.Streams;
/*     */ import com.google.gson.stream.JsonReader;
/*     */ import com.google.gson.stream.MalformedJsonException;
/*     */ import java.io.IOException;
/*     */ import java.io.Reader;
/*     */ import java.io.StringReader;
/*     */ import java.util.Random;
/*     */ import net.minecraft.client.gui.screen.inventory.ChestScreen;
import net.minecraft.entity.player.PlayerEntity;
/*     */ import net.minecraft.inventory.container.ChestContainer;
/*     */ import net.minecraft.inventory.container.ClickType;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.item.Items;
/*     */ import net.minecraft.nbt.CompoundNBT;
/*     */ import net.minecraft.nbt.ListNBT;
import sertyo.events.event.player.EventUpdate;
import sertyo.events.module.Category;
import sertyo.events.module.Module;
import sertyo.events.module.ModuleAnnotation;
import sertyo.events.module.impl.util.autobuyhw.BuyHelp;
import sertyo.events.module.impl.util.autobuyhw.gui.ItemPrice;

/*     */@ModuleAnnotation(name = "AutoBuy", category = Category.UTIL)

/*     */ public class AutoBuy extends Module {
/*     */   boolean kypit = true;
/*     */   boolean bombom;
/*     */   String balance;
/*     */   Random ra;
/*  30 */   int count = 0; A jump; A updatedefault;
/*     */   A buy;
/*     */   
/*     */   public AutoBuy() {
/*     */
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  44 */     this.bombom = false;
/*  45 */     this.balance = "0";
/*     */ 
/*     */     
/*  48 */     this.ra = new Random();
/*     */ 
/*     */     
/*  51 */     this.jump = new A();
/*  52 */     this.updatedefault = new A();
/*  53 */     this.buy = new A(); }
    public static void update() {
            mc.playerController.windowClick(mc.player.container.windowId, 48, 0, ClickType.CLONE, mc.player);

    }
            @EventTarget
/*     */       public void onTick(EventUpdate e) {

                /*     */
                ChestContainer container = (ChestContainer) mc.player.openContainer;
                if (container == null) {

                    mc.player.sendChatMessage("/ah");
                }
/*  64 */     if (mc.currentScreen instanceof ChestScreen) {
            /*  66 */       if (this.kypit) {
                        if (this.updatedefault.hasTimeElapsed(1000, true))
/*  68 */           update();
                if (container != null && !mc.currentScreen.getTitle().getString().contains("Покупка предмета") &&
                        mc.currentScreen.getTitle().getString().contains("Аукцион")) {
                    for (int index = 0; index < container.inventorySlots.size(); index++) {
                        ItemStack stack = container.inventorySlots.get(index).getStack();
/*  74 */             if (BuyHelp.ShlemEternity(stack)) {
/*  75 */               String nbt = getNBT(index);
/*  76 */               int price = getPrice(stack);
/*  77 */               if (price <= ItemPrice.getPrice("shlemetenity") && !moderator(stack.getTag().toString())) {
/*  78 */                 this.updatedefault.reset();
                            System.out.println(container.windowId);
/*  79 */                 mc.playerController.windowClick(container.windowId, index, 0, ClickType.PICKUP, (PlayerEntity)mc.player);
/*     */               }
/*     */             }
/*     */
/*  83 */             if (BuyHelp.booteternity(stack)) {
/*  84 */               String nbt = getNBT(index);
/*  85 */               int price = getPrice(stack);
/*  86 */               if (price <= ItemPrice.getPrice("booteternity") && !moderator(stack.getTag().toString())) {
/*  87 */                 this.updatedefault.reset();
/*  88 */                 mc.playerController.windowClick(container.windowId, index, 0, ClickType.PICKUP, (PlayerEntity)mc.player);
/*     */               }
/*     */             }
/*     */
/*  92 */             if (stack.getTag() != null &&
/*  93 */               stack.getItem() == Items.TRIPWIRE_HOOK &&
/*  94 */               stack.getTag().toString().contains("minecraft:luck_of_the_sea")) {
/*  95 */               String nbt = getNBT(index);
/*  96 */               int price = getPrice(stack);
/*  97 */               if (price <= ItemPrice.getPrice("yniversalka") && !moderator(stack.getTag().toString())) {
/*  98 */                 this.updatedefault.reset();
/*  99 */                 mc.playerController.windowClick(container.windowId, index, 0, ClickType.PICKUP, (PlayerEntity)mc.player);
/*     */               }
/*     */             }
/*     */
/*     */
/*     */
/* 105 */             if (stack.getTag() != null &&
/* 106 */               stack.getItem() == Items.POTION &&
/* 107 */               stack.getTag().toString().contains("JUSTICE")) {
/* 108 */               String nbt = getNBT(index);
/* 109 */               int price = getPrice(stack);
/* 110 */               if (price <= ItemPrice.getPrice("mekenik") && !moderator(stack.getTag().toString())) {
/* 111 */                 this.updatedefault.reset();
/* 112 */                 mc.playerController.windowClick(container.windowId, index, 0, ClickType.PICKUP, (PlayerEntity)mc.player);
/*     */               }
/*     */             }
/*     */
/*     */
/*     */
/* 118 */             if (stack.getTag() != null &&
/* 119 */               stack.getItem() == Items.GOLDEN_HELMET &&
/* 120 */               stack.getTag().toString().contains("Шлем Солнца")) {
/* 121 */               String nbt = getNBT(index);
/* 122 */               int price = getPrice(stack);
/* 123 */               if (price <= ItemPrice.getPrice("sunhelmet") && !moderator(stack.getTag().toString())) {
/* 124 */                 this.updatedefault.reset();
                                System.out.println(container.windowId);

/* 125 */                 mc.playerController.windowClick(container.windowId, index, 0, ClickType.PICKUP, (PlayerEntity)mc.player);
/*     */               }
/*     */             }
/*     */
/*     */
/*     */
/* 131 */             if (stack.getTag() != null &&
/* 132 */               stack.getItem() == Items.TOTEM_OF_UNDYING &&
/* 133 */               stack.getTag().toString().contains("Прыгучесть II") && stack.getTag().toString().contains("Урон IV")) {
/* 134 */               String nbt = getNBT(index);
/* 135 */               int price = getPrice(stack);
/* 136 */               if (price <= ItemPrice.getPrice("talicSatiri") && !moderator(stack.getTag().toString())) {
/* 137 */                 this.updatedefault.reset();
/*     */
/* 139 */                 mc.playerController.windowClick(container.windowId, index, 0, ClickType.PICKUP, (PlayerEntity)mc.player);
/*     */               }
/*     */             }
/*     */
/*     */
/*     */
/* 145 */             if (stack.getTag() != null &&
/* 146 */               stack.getItem() == Items.PLAYER_HEAD)
/*     */             {
/* 148 */               if (stack.getTag().toString().contains("IJUSTBADLOVE")) {
/* 149 */                 String nbt = getNBT(index);
/* 150 */                 int price = getPrice(stack);
/* 151 */                 if (price <= ItemPrice.getPrice("sferkaflesh") && !moderator(stack.getTag().toString())) {
/* 152 */                   this.updatedefault.reset();
/*     */
/* 154 */                   mc.playerController.windowClick(container.windowId, index, 0, ClickType.PICKUP, (PlayerEntity)mc.player);
/*     */                 }
/*     */               }
/*     */             }
/*     */
/* 159 */             if (stack.getTag() != null &&
/* 160 */               stack.getItem() == Items.PLAYER_HEAD &&
/* 161 */               stack.getTag().toString().contains("ARMORTALITY")) {
/* 162 */               String nbt = getNBT(index);
/* 163 */               int price = getPrice(stack);
/* 164 */               if (price <= ItemPrice.getPrice("sferkaarmor") && !moderator(stack.getTag().toString())) {
/* 165 */                 this.updatedefault.reset();
/*     */
/* 167 */                 mc.playerController.windowClick(container.windowId, index, 0, ClickType.PICKUP, (PlayerEntity)mc.player);
/*     */               }
/*     */             }
/*     */
/*     */
/* 172 */             if (stack.getTag() != null &&
/* 173 */               stack.getItem() == Items.PLAYER_HEAD &&
/* 174 */               stack.getTag().toString().contains("CERBER")) {
/* 175 */               String nbt = getNBT(index);
/* 176 */               int price = getPrice(stack);
/* 177 */               if (price <= ItemPrice.getPrice("sferkacerbe") && !moderator(stack.getTag().toString())) {
/* 178 */                 this.updatedefault.reset();
/*     */
/* 180 */                 mc.playerController.windowClick(container.windowId, index, 0, ClickType.PICKUP, (PlayerEntity)mc.player);
/*     */               }
/*     */             }
/*     */
/*     */
/* 185 */             if (stack.getTag() != null &&
/* 186 */               stack.getItem() == Items.NETHERITE_SWORD &&
/* 187 */               stack.getTag().toString().contains("Фармер II")) {
/* 188 */               String nbt = getNBT(index);
/* 189 */               int price = getPrice(stack);
/* 190 */               if (price <= ItemPrice.getPrice("farmert") && !moderator(stack.getTag().toString())) {
/* 191 */                 this.updatedefault.reset();
/*     */
/* 193 */                 mc.playerController.windowClick(container.windowId, index, 0, ClickType.PICKUP, (PlayerEntity)mc.player);
/*     */               }
/*     */             }
/*     */
/*     */
/* 198 */             if (stack.getTag() != null &&
/* 199 */               stack.getItem() == Items.NETHERITE_SWORD && (
/* 200 */               stack.getTag().toString().contains("Фармер III") || stack.getTag().toString().contains("Фармер IV") || stack.getTag().toString().contains("Фармер V") || stack.getTag().toString().contains("Фармер VI") || stack.getTag().toString().contains("Фармер VII") || stack.getTag().toString().contains("Фармер VIII") || stack.getTag().toString().contains("Фармер IX") || stack.getTag().toString().contains("Фармер X"))) {
/* 201 */               String nbt = getNBT(index);
/* 202 */               int price = getPrice(stack);
/* 203 */               if (price <= ItemPrice.getPrice("farmerg") && !moderator(stack.getTag().toString())) {
/* 204 */                 this.updatedefault.reset();
/*     */
/* 206 */                 mc.playerController.windowClick(container.windowId, index, 0, ClickType.PICKUP, (PlayerEntity)mc.player);
/*     */               }
/*     */             }
/*     */
/*     */
/*     */
/*     */
/* 213 */             if (stack.getTag() != null &&
/* 214 */               stack.getItem() == Items.ELYTRA &&
/* 215 */               stack.getTag().toString().contains("ArmorElytra")) {
/* 216 */               String nbt = getNBT(index);
/* 217 */               int price = getPrice(stack);
/* 218 */               if (price <= ItemPrice.getPrice("armorelytra") && !moderator(stack.getTag().toString())) {
/* 219 */                 this.updatedefault.reset();
/*     */
/* 221 */                 mc.playerController.windowClick(container.windowId, index, 0, ClickType.PICKUP, (PlayerEntity)mc.player);
/*     */               }
/*     */             }
/*     */
/*     */
/* 226 */             if (stack.getTag() != null &&
/* 227 */               stack.getItem() == Items.PLAYER_HEAD) {
/* 228 */               String nbt = getNBT(index);
/* 229 */               int price = getPrice(stack);
/* 230 */               if (stack.getTag().toString().contains("Скорость II") && stack.getTag().toString().contains("Броня II") && stack.getTag().toString().contains("Урон II") &&
/* 231 */                 price <= ItemPrice.getPrice("topsfera") && !moderator(stack.getTag().toString())) {
/* 232 */                 this.updatedefault.reset();
/*     */
/* 234 */                 mc.playerController.windowClick(container.windowId, index, 0, ClickType.PICKUP, (PlayerEntity)mc.player);
/*     */               }
/*     */             }
/*     */
/*     */
/* 239 */             if (stack.getTag() != null &&
/* 240 */               stack.getItem() == Items.PLAYER_HEAD) {
/* 241 */               String nbt = getNBT(index);
/* 242 */               int price = getPrice(stack);
/* 243 */               if (stack.getTag().toString().contains("Броня III") && stack.getTag().toString().contains("Урон II") &&
/* 244 */                 price <= ItemPrice.getPrice("mifkasfera") && !moderator(stack.getTag().toString())) {
/* 245 */                 this.updatedefault.reset();
/* 246 */                 mc.playerController.windowClick(container.windowId, index, 0, ClickType.PICKUP, (PlayerEntity)mc.player);
/*     */               }
/*     */             }
/*     */
/*     */
/*     */
/* 252 */             if (stack.getTag() != null &&
/* 253 */               stack.getItem() == Items.PLAYER_HEAD) {
/* 254 */               String nbt = getNBT(index);
/* 255 */               int price = getPrice(stack);
/* 256 */               if (stack.getTag().toString().contains("Броня III") && stack.getTag().toString().contains("Урон II") &&
/* 257 */                 price <= ItemPrice.getPrice("mifkasfera") && !moderator(stack.getTag().toString())) {
/* 258 */                 this.updatedefault.reset();
/* 259 */                 mc.playerController.windowClick(container.windowId, index, 0, ClickType.PICKUP, (PlayerEntity)mc.player);
/*     */               }
/*     */             }
/*     */
/*     */
/*     */
/* 265 */             if (stack.getTag() != null &&
/* 266 */               stack.getItem() == Items.TRIPWIRE_HOOK) {
/* 267 */               String nbt = getNBT(index);
/* 268 */               int price = getPrice(stack);
/* 269 */               if (stack.getTag().toString().contains("Секретная") &&
/* 270 */                 price <= ItemPrice.getPrice("otmichkaso") && !moderator(stack.getTag().toString())) {
/* 271 */                 this.updatedefault.reset();
/* 272 */                 mc.playerController.windowClick(container.windowId, index, 0, ClickType.PICKUP, (PlayerEntity)mc.player);
/*     */               }
/*     */             }
/*     */
/*     */
/*     */
/* 278 */             if (stack.getTag() != null &&
/* 279 */               stack.getItem() == Items.TRIPWIRE_HOOK) {
/* 280 */               String nbt = getNBT(index);
/* 281 */               int price = getPrice(stack);
/* 282 */               if (stack.getTag().toString().contains("Секретная") &&
/* 283 */                 price <= ItemPrice.getPrice("otmichkaso") && !moderator(stack.getTag().toString())) {
/* 284 */                 this.updatedefault.reset();
/* 285 */                 mc.playerController.windowClick(container.windowId, index, 0, ClickType.PICKUP, (PlayerEntity)mc.player);
/*     */               }
/*     */             }
/*     */
/*     */
/*     */
/* 291 */             if (stack.getTag() != null &&
/* 292 */               stack.getItem() == Items.TRIPWIRE_HOOK) {
/* 293 */               String nbt = getNBT(index);
/* 294 */               int price = getPrice(stack);
/* 295 */               if (stack.getTag().toString().contains("Уникальная") &&
/* 296 */                 price <= ItemPrice.getPrice("otmichkobo") && !moderator(stack.getTag().toString())) {
/* 297 */                 this.updatedefault.reset();
/* 298 */                 mc.playerController.windowClick(container.windowId, index, 0, ClickType.PICKUP, (PlayerEntity)mc.player);
/*     */               }
/*     */             }
/*     */
/*     */
/*     */
/* 304 */             if (stack.getTag() != null &&
/* 305 */               stack.getItem() == Items.TRIPWIRE_HOOK) {
/* 306 */               String nbt = getNBT(index);
/* 307 */               int price = getPrice(stack);
/* 308 */               if (stack.getTag().toString().contains("Редкая") &&
/* 309 */                 price <= ItemPrice.getPrice("freokatd") && !moderator(stack.getTag().toString())) {
/* 310 */                 this.updatedefault.reset();
/* 311 */                 mc.playerController.windowClick(container.windowId, index, 0, ClickType.PICKUP, (PlayerEntity)mc.player);
/*     */               }
/*     */             }
/*     */
/*     */
/*     */
/* 317 */             if (stack.getTag() != null &&
/* 318 */               stack.getItem() == Items.TRIPWIRE_HOOK) {
/* 319 */               String nbt = getNBT(index);
/* 320 */               int price = getPrice(stack);
/* 321 */               if (stack.getTag().toString().contains("Обычная") &&
/* 322 */                 price <= ItemPrice.getPrice("obicgjsa") && !moderator(stack.getTag().toString())) {
/* 323 */                 this.updatedefault.reset();
/* 324 */                 mc.playerController.windowClick(container.windowId, index, 0, ClickType.PICKUP, (PlayerEntity)mc.player);
/*     */               }
/*     */             }
/*     */
/*     */
/*     */

/*     */
/*     */
/* 382 */             if (stack.getTag() != null &&
/* 383 */               stack.getItem() == Items.PLAYER_HEAD) {
/*     */
/* 385 */               String nbt = getNBT(index);
/* 386 */               int price = getPrice(stack);
/* 387 */               if (!stack.getTag().toString().contains("Скорость") && !stack.getTag().toString().contains("Броня") && stack.getTag().toString().contains("Урон III") &&
/* 388 */                 price <= ItemPrice.getPrice("sferahui") && !moderator(stack.getTag().toString())) {
/* 389 */                 this.updatedefault.reset();
/* 390 */                 mc.playerController.windowClick(container.windowId, index, 0, ClickType.PICKUP, (PlayerEntity)mc.player);
/*     */               }
/*     */             }
/*     */
/*     */
/*     */
/* 396 */             if (stack.getTag() != null &&
/* 397 */               stack.getItem() == Items.PLAYER_HEAD) {
/* 398 */               String nbt = getNBT(index);
/* 399 */               int price = getPrice(stack);
/* 400 */               if (stack.getTag().toString().contains("Скорость III") && !stack.getTag().toString().contains("Броня") && !stack.getTag().toString().contains("Урон") &&
/* 401 */                 price <= ItemPrice.getPrice("sferahui") && !moderator(stack.getTag().toString())) {
/* 402 */                 this.updatedefault.reset();
/* 403 */                 mc.playerController.windowClick(container.windowId, index, 0, ClickType.PICKUP, (PlayerEntity)mc.player);
/*     */               }
/*     */             }
/*     */
/*     */
/*     */
/* 409 */             if (stack.getTag() != null &&
/* 410 */               stack.getItem() == Items.PLAYER_HEAD) {
/* 411 */               String nbt = getNBT(index);
/* 412 */               int price = getPrice(stack);
/* 413 */               if (!stack.getTag().toString().contains("Скорость") && stack.getTag().toString().contains("Броня II") && stack.getTag().toString().contains("Урон III") &&
/* 414 */                 price <= ItemPrice.getPrice("sferka") && !moderator(stack.getTag().toString())) {
/* 415 */                 this.updatedefault.reset();
/* 416 */                 mc.playerController.windowClick(container.windowId, index, 0, ClickType.PICKUP, (PlayerEntity)mc.player);
/*     */               }
/*     */             }
/*     */
/*     */
/*     */
/* 422 */             if (stack.getTag() != null &&
/* 423 */               stack.getItem() == Items.TOTEM_OF_UNDYING) {
/* 424 */               String nbt = getNBT(index);
/* 425 */               int price = getPrice(stack);
/* 426 */               if (stack.getTag().toString().contains("здоровье II") && stack.getTag().toString().contains("Скорость II") && stack.getTag().toString().contains("Броня I") && stack.getTag().toString().contains("Урон II") &&
/* 427 */                 price <= ItemPrice.getPrice("talick") && !moderator(stack.getTag().toString())) {
/* 428 */                 this.updatedefault.reset();
/* 429 */                 mc.playerController.windowClick(container.windowId, index, 0, ClickType.PICKUP, (PlayerEntity)mc.player);
/*     */               }
/*     */             }
/*     */
/*     */
/*     */
/* 435 */             if (stack.getTag() != null &&
/* 436 */               stack.getItem() == Items.PLAYER_HEAD) {
/* 437 */               String nbt = getNBT(index);
/* 438 */               int price = getPrice(stack);
/* 439 */               if (!stack.getTag().toString().contains("Скорость") && stack.getTag().toString().contains("Броня II") && stack.getTag().toString().contains("Урон II") &&
/* 440 */                 price <= ItemPrice.getPrice("sferadam") && !moderator(stack.getTag().toString())) {
/* 441 */                 this.updatedefault.reset();
/* 442 */                 mc.playerController.windowClick(container.windowId, index, 0, ClickType.PICKUP, (PlayerEntity)mc.player);
/*     */               }
/*     */             }
/*     */
/*     */
/*     */
/* 448 */             if (stack.getTag() != null &&
/* 449 */               stack.getItem() == Items.PLAYER_HEAD) {
/* 450 */               String nbt = getNBT(index);
/*     */
/* 452 */               int price = getPrice(stack);
/* 453 */               if (stack.getTag().toString().contains("Скорость II") && !stack.getTag().toString().contains("Броня") && stack.getTag().toString().contains("Урон II") &&
/* 454 */                 price <= ItemPrice.getPrice("sped") && !moderator(stack.getTag().toString())) {
/* 455 */                 this.updatedefault.reset();
/* 456 */                 mc.playerController.windowClick(container.windowId, index, 0, ClickType.PICKUP, (PlayerEntity)mc.player);
/*     */               }
/*     */             }
/*     */
/*     */
/*     */
/* 462 */             if (stack.getTag() != null &&
/* 463 */               stack.getItem() == Items.PLAYER_HEAD) {
/* 464 */               String nbt = getNBT(index);
/* 465 */               int price = getPrice(stack);
/* 466 */               if (stack.getTag().toString().contains("Скорость I") && stack.getTag().toString().contains("Броня II") && stack.getTag().toString().contains("Урон II") &&
/* 467 */                 price <= ItemPrice.getPrice("spedi") && !moderator(stack.getTag().toString())) {
/* 468 */                 this.updatedefault.reset();
/* 469 */                 mc.playerController.windowClick(container.windowId, index, 0, ClickType.PICKUP, (PlayerEntity)mc.player);
/*     */               }
/*     */             }
/*     */
/*     */
/*     */
/*     */
/* 476 */             if (stack.getTag() != null &&
/* 477 */               stack.getItem() == Items.SPAWNER) {
/* 478 */               String nbt = getNBT(index);
/* 479 */               int price = getPrice(stack);
/* 480 */               if (price <= ItemPrice.getPrice("spawner") && !moderator(stack.getTag().toString())) {
/* 481 */                 this.updatedefault.reset();
/* 482 */                 mc.playerController.windowClick(container.windowId, index, 0, ClickType.PICKUP, (PlayerEntity)mc.player);
/*     */               }
/*     */             }
/*     */
/*     */
/* 487 */             if (stack.getTag() != null &&
/* 488 */               stack.getItem() == Items.EXPERIENCE_BOTTLE) {
/* 489 */               String nbt = getNBT(index);
/* 490 */               int price = getPrice(stack);
/* 491 */               if (price <= ItemPrice.getPrice("opit50") && !moderator(stack.getTag().toString()) &&
/* 492 */                 stack.getTag().toString().contains("holy-exp-bottle-value:5345")) {
/* 493 */                 mc.playerController.windowClick(container.windowId, index, 0, ClickType.PICKUP, (PlayerEntity)mc.player);
/* 494 */                 this.updatedefault.reset();
/*     */               }
/*     */             }
/*     */
/*     */
/*     */
/* 500 */             if (stack.getTag() != null &&
/* 501 */               stack.getItem() == Items.EXPERIENCE_BOTTLE) {
/* 502 */               String nbt = getNBT(index);
/* 503 */               int price = getPrice(stack);
/* 504 */               if (price <= ItemPrice.getPrice("opit100") && !moderator(stack.getTag().toString()) &&
/* 505 */                 stack.getTag().toString().contains("holy-exp-bottle-value:30971")) {
/* 506 */                 this.updatedefault.reset(); mc.playerController.windowClick(container.windowId, index, 0, ClickType.PICKUP, (PlayerEntity)mc.player);
/*     */               }
/*     */             }
/*     */
/*     */
/*     */
/* 512 */             if (stack.getTag() != null &&
/* 513 */               stack.getItem() == Items.NETHER_STAR) {
/* 514 */               String nbt = getNBT(index);
/* 515 */               int price = getPrice(stack);
/* 516 */               if (price <= ItemPrice.getPrice("stan") && !moderator(stack.getTag().toString()) &&
/* 517 */                 stack.getTag().toString().contains("STUN_STAR")) {
/* 518 */                 this.updatedefault.reset();
/* 519 */                 mc.playerController.windowClick(container.windowId, index, 0, ClickType.PICKUP, (PlayerEntity)mc.player);
/*     */               }
/*     */             }
/*     */
/*     */
/*     */
/* 525 */             if (stack.getTag() != null &&
/* 526 */               stack.getItem() == Items.PRISMARINE_CRYSTALS) {
/* 527 */               String nbt = getNBT(index);
/* 528 */               int price = getPrice(stack);
/* 529 */               if (price <= ItemPrice.getPrice("battle") && !moderator(stack.getTag().toString()) &&
/* 530 */                 stack.getTag().toString().contains("BattleFragment")) {
/* 531 */                 this.updatedefault.reset(); mc.playerController.windowClick(container.windowId, index, 0, ClickType.PICKUP, (PlayerEntity)mc.player);
/*     */               }
/*     */             }
/*     */
/*     */
/*     */
/* 537 */             if (stack.getTag() != null &&
/* 538 */               stack.getItem() == Items.CLAY) {
/* 539 */               String nbt = getNBT(index);
/* 540 */               int price = getPrice(stack);
/* 541 */               if (price <= ItemPrice.getPrice("clay") && !moderator(stack.getTag().toString()) &&
/* 542 */                 stack.getTag().toString().contains("EXPLOSIVE_SUBSTANCE")) {
/* 543 */                 this.updatedefault.reset(); mc.playerController.windowClick(container.windowId, index, 0, ClickType.PICKUP, (PlayerEntity)mc.player);
/*     */               }
/*     */             }
/*     */
/*     */
/*     */
/* 549 */             if (BuyHelp.chestEternity(stack)) {
/* 550 */               String nbt = getNBT(index);
/* 551 */               int price = getPrice(stack);
/* 552 */               if (price <= ItemPrice.getPrice("chesteternity") && !moderator(stack.getTag().toString())) {
/*     */
/* 554 */                 this.updatedefault.reset(); mc.playerController.windowClick(container.windowId, index, 0, ClickType.PICKUP, (PlayerEntity)mc.player);
/*     */               }
/*     */             }
/*     */
/* 558 */             if (BuyHelp.legeternity(stack)) {
/* 559 */               int price = getPrice(stack);
/* 560 */               if (price <= ItemPrice.getPrice("legginseternirty") && !moderator(stack.getTag().toString()))
/*     */               {
/* 562 */                 mc.playerController.windowClick(container.windowId, index, 0, ClickType.PICKUP, (PlayerEntity)mc.player);
/*     */               }
/*     */             }
/*     */
/*     */
/*     */
/* 568 */             if (BuyHelp.isEternitySword(stack)) {
/* 569 */               String nbt = getNBT(index);
/* 570 */               int price = getPrice(stack);
/* 571 */               if (price <= ItemPrice.getPrice("swordeternityt") && !moderator(stack.getTag().toString())) {
/* 572 */                 this.updatedefault.reset();
/* 573 */                 mc.playerController.windowClick(container.windowId, index, 0, ClickType.PICKUP, (PlayerEntity)mc.player);
/*     */               }
/*     */             }
/*     */
/* 577 */             if (BuyHelp.isKirkaKrush(stack)) {
/* 578 */               String nbt = getNBT(index);
/* 579 */               int price = getPrice(stack);
/* 580 */               if (price <= ItemPrice.getPrice("kirkaeternity") && !moderator(stack.getTag().toString())) {
/*     */
/* 582 */                 this.updatedefault.reset(); mc.playerController.windowClick(container.windowId, index, 0, ClickType.PICKUP, (PlayerEntity)mc.player);
/*     */               }
/*     */             }
/*     */
/* 586 */             if (BuyHelp.isHelmetZ5(stack)) {
/* 587 */               String nbt = getNBT(index);
/* 588 */               int price = getPrice(stack);
/* 589 */               if (price <= ItemPrice.getPrice("z5shlem") && !moderator(stack.getTag().toString())) {
/* 590 */                 this.updatedefault.reset();
/*     */
/* 592 */                 mc.playerController.windowClick(container.windowId, index, 0, ClickType.PICKUP, (PlayerEntity)mc.player);
/*     */               }
/*     */             }
/* 595 */             if (BuyHelp.isChestZ5(stack)) {
/* 596 */               String nbt = getNBT(index);
/* 597 */               int price = getPrice(stack);
/* 598 */               if (price <= ItemPrice.getPrice("z5grudak") && !moderator(stack.getTag().toString()))
/*     */               {
/*     */
/* 601 */                 mc.playerController.windowClick(container.windowId, index, 0, ClickType.PICKUP, (PlayerEntity)mc.player);
/*     */               }
/*     */             }
/* 604 */             if (BuyHelp.isLegginsZ5(stack)) {
/* 605 */               String nbt = getNBT(index);
/* 606 */               int price = getPrice(stack);
/* 607 */               if (price <= ItemPrice.getPrice("z5leggins") && !moderator(stack.getTag().toString())) {
/* 608 */                 this.updatedefault.reset();
/*     */
/* 610 */                 mc.playerController.windowClick(container.windowId, index, 0, ClickType.PICKUP, (PlayerEntity)mc.player);
/*     */               }
/*     */             }
/* 613 */             if (BuyHelp.isBootsZ5(stack)) {
/* 614 */               String nbt = getNBT(index);
/* 615 */               int price = getPrice(stack);
/* 616 */               if (price <= ItemPrice.getPrice("z5boots") && !moderator(stack.getTag().toString())) {
/* 617 */                 this.updatedefault.reset();
/* 618 */                 mc.playerController.windowClick(container.windowId, index, 0, ClickType.PICKUP, (PlayerEntity)mc.player);
/*     */               }
/*     */             }
/*     */

/*     */
/* 640 */             if (BuyHelp.leginsinf(stack)) {
/* 641 */               String nbt = getNBT(index);
/* 642 */               int price = getPrice(stack);
/* 643 */               if (price <= ItemPrice.getPrice("legginsinf") && !moderator(stack.getTag().toString())) {
/* 644 */                 this.updatedefault.reset();
/*     */
/* 646 */                 mc.playerController.windowClick(container.windowId, index, 0, ClickType.PICKUP, (PlayerEntity)mc.player);
/*     */               }
/*     */             }
/* 649 */             if (BuyHelp.bootinfinty(stack)) {
/* 650 */               String nbt = getNBT(index);
/* 651 */               int price = getPrice(stack);
/* 652 */               if (price <= ItemPrice.getPrice("bootinf") && !moderator(stack.getTag().toString())) {
/* 653 */                 this.updatedefault.reset();
/*     */
/* 655 */                 mc.playerController.windowClick(container.windowId, index, 0, ClickType.PICKUP, (PlayerEntity)mc.player);
/*     */               }
/*     */             }
/* 658 */             if (BuyHelp.taliceternity(stack)) {
/* 659 */               String nbt = getNBT(index);
/* 660 */               int price = getPrice(stack);
/* 661 */               if (price <= ItemPrice.getPrice("talicetern") && !moderator(stack.getTag().toString())) {
/* 662 */                 this.updatedefault.reset();
/* 663 */                 mc.playerController.windowClick(container.windowId, index, 0, ClickType.PICKUP, (PlayerEntity)mc.player);
/*     */               }
/*     */             }
/*     */
/* 667 */             if (BuyHelp.talicStringer(stack)) {
/* 668 */               String nbt = getNBT(index);
/* 669 */               int price = getPrice(stack);
/* 670 */               if (price <= ItemPrice.getPrice("talicstring") && !moderator(stack.getTag().toString())) {
/*     */
/* 672 */                 this.updatedefault.reset();
/*     */
/* 674 */                 mc.playerController.windowClick(container.windowId, index, 0, ClickType.PICKUP, (PlayerEntity)mc.player);
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     } 
/*     */
        if (mc.currentScreen != null) {
            if (mc.currentScreen.getTitle().getString().contains("Покупка предмета")) {
                this.count++;
                this.kypit = false;
                assert mc.playerController != null;
                mc.playerController.windowClick(mc.player.openContainer.windowId, 0, 0, ClickType.PICKUP, mc.player);
                this.count = 0;
            } else {
                this.kypit = true;
            }
        }
/*     */   }
/*     */   
/*     */   public boolean moderator(String name) {
/* 696 */     if (name.contains("_reve_2045_") || name.contains("K1ona") || name.contains("halflite") || name.contains("_Sukuba_") || name.contains("Makson1gg") || name.contains("smokenigger") || name.contains("egorhack9") || name.contains("ProcessHacker") || name.contains("a11ways") || name.contains("minkyy1337") || name.contains("pr3k0l") || name.contains("Pupsik_BB") || name.contains("Soul_Stealer") || name.contains("uliana_pim") || name.contains("uFerz") || name.contains("nor1x") || name.contains("skyvolik") || name.contains("_Darkdeath_") || name.contains("HarukaKasugano_") || name.contains("petya_external") || name.contains("KillMe_plz") || name.contains("Ksenon4ik_") || name.contains("Tr1ggered") || name.contains("3uMa_MaN") || name.contains("maggasty") || name.contains("Botik_884") || name.contains("ninetydyygres") || name.contains("MisterKapybara") || name.contains("Dellarba") || name.contains("ChtoStop") || name.contains("zxcichert") || name.contains("The_Destr0yer") || name.contains("xDedForTopch1ll") || name.contains("thepieboyz") || name.contains("hqd_d") || name.contains("HapyC0t") || name.contains("1WantToHearth") || name.contains("BeMoRe_") || name.contains("m0untain") || name.contains("HackerCat777") || name.contains("_Bupsik_") || name.contains("Cmeetane") || name.contains("DumpCave") || name.contains("Asya_Masya") || name.contains("YarikPR02205") || name.contains("Darkk_Knight") || name.contains("_SenpaiCrown_") || name.contains("Fl1ckzzz") || name.contains("alanova") || name.contains("ApTa_He_UmBa") || name.contains("CAMAPA") || name.contains("Derty_1001") || name.contains("Deth_Angel") || name.contains("gromaforz") || name.contains("hikomori") || name.contains("XiSh3n") || name.contains("That0neBear") || name.contains("hellobaby") || name.contains("klemen") || name.contains("ToplecCHlKA")) {
/* 697 */       return true;
/*     */     }
/* 699 */     return false;
/*     */   }
/*     */   
/*     */   public static JsonElement parseString(String json) throws JsonSyntaxException {
/* 703 */     return parseReader(new StringReader(json));
/*     */   }
/*     */   
/*     */   public static JsonElement parseReader(Reader reader) throws JsonIOException, JsonSyntaxException {
    try {
        JsonReader jsonReader = new JsonReader(reader);
        JsonElement element = parseReader(jsonReader);

        // Убираем проверку jsonReader.peek() != JsonToken.END_DOCUMENT
        if (!element.isJsonNull()) {
            return element;
        } else {
            throw new JsonSyntaxException("Document is null or empty.");
        }
    } catch (NumberFormatException var5) {
        throw new JsonSyntaxException(var5);
    }
}
/*     */   public static JsonElement parseReader(JsonReader reader) throws JsonIOException, JsonSyntaxException {
/*     */     JsonElement var11;
/* 728 */     boolean lenient = reader.isLenient();
/* 729 */     reader.setLenient(true);
/*     */ 
/*     */     
/*     */     try {
/* 733 */       var11 = Streams.parse(reader);
/* 734 */     } catch (StackOverflowError var7) {
/* 735 */       StackOverflowError e = var7;
/* 736 */       throw new JsonParseException("Failed parsing JSON source: " + reader + " to Json", e);
/* 737 */     } catch (OutOfMemoryError var8) {
/* 738 */       OutOfMemoryError e = var8;
/* 739 */       throw new JsonParseException("Failed parsing JSON source: " + reader + " to Json", e);
/*     */     } finally {
/* 741 */       reader.setLenient(lenient);
/*     */     } 
/*     */     
/* 744 */     return var11;
/*     */   }
/*     */
private int getPrice(ItemStack stack) {
    CompoundNBT tag = stack.getTag();

    if (tag != null && tag.contains("display", 10)) {
        CompoundNBT display = tag.getCompound("display");

        if (display.contains("Lore", 9)) {
            ListNBT lore = display.getList("Lore", 8);

            for (int j = 0; j < lore.size(); j++) {
                String loreString = lore.getString(j);
                JsonObject object = parseString(loreString).getAsJsonObject();

                if (object.has("extra")) {
                    JsonArray extraArray = object.getAsJsonArray("extra");

                    for (int k = 0; k < extraArray.size(); k++) {
                        JsonObject extraObject = extraArray.get(k).getAsJsonObject();
                        String text = extraObject.get("text").getAsString().trim();

                        if (text.contains("Цeнa")) {
                            if (k + 2 < extraArray.size()) {
                                JsonObject priceObject = extraArray.get(k + 2).getAsJsonObject();
                                String priceText = priceObject.get("text").getAsString().trim().replaceAll("[^0-9]", "");

                                try {
                                    return Integer.parseInt(priceText);
                                } catch (NumberFormatException e) {
                                    return -1;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    return -1;
}

    public static String getNBT(int itemslot) {
        return mc.player.openContainer.getSlot(itemslot).getStack().getTag().toString();
    }
/*     */ }


/* Location:              /Users/v/Downloads/PrivatBuy.jar!/examplemod/Module/imlp/AutoBuy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */