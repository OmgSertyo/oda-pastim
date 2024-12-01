package sertyo.events.module.impl.render;


import com.darkmagician6.eventapi.EventTarget;
import com.google.common.collect.Lists;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import jhlabs.vecmath.Vector4f;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.network.play.NetworkPlayerInfo;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Potion;
import net.minecraft.util.StringUtils;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.DimensionType;
import org.lwjgl.opengl.GL11;
import sertyo.events.Main;
import sertyo.events.event.input.EventInputKey;
import sertyo.events.event.player.EventUpdate;
import sertyo.events.event.render.EventRender2D;
import sertyo.events.manager.dragging.DragManager;
import sertyo.events.manager.dragging.Draggable;
import sertyo.events.module.Category;
import sertyo.events.module.Module;
import sertyo.events.module.ModuleAnnotation;
import sertyo.events.module.impl.combat.KillAura;
import sertyo.events.module.impl.util.Optimization;
import sertyo.events.module.setting.impl.KeySetting;
import sertyo.events.module.setting.impl.MultiBooleanSetting;
import sertyo.events.utility.Utility;
import sertyo.events.utility.font.styled.StyledFont;
import sertyo.events.utility.math.MathUtility;
import sertyo.events.utility.misc.ChatUtility;
import sertyo.events.utility.render.*;
import sertyo.events.utility.render.animation.Animation;
import sertyo.events.utility.render.animation.AnimationMath;
import sertyo.events.utility.render.animation.Direction;
import sertyo.events.utility.render.animation.impl.DecelerateAnimation;
import sertyo.events.utility.render.fonts.Fonts;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

import static sertyo.events.utility.render.RenderUtil.Render2D.*;
import static sertyo.events.utility.render.RenderUtil.scaleEnd;


@ModuleAnnotation(
        name = "Hud",
        category = Category.RENDER
)
public class Hud extends Module {
    public final Draggable keybindsDraggable = DragManager.create(this, "Keybinds", 200, 200);
    public final Draggable ft = DragManager.create(this, "FtEvents", 600, 400);

        public static final MultiBooleanSetting elements = new MultiBooleanSetting("Elements", Arrays.asList("Watermark", "Server Info", "Coords", "Inventory", "Potions", "Armor", "Target HUD", "Keybinds", "Staff List", "Spotify"));
        private final Draggable invViewDraggable = DragManager.create(this, "Inventory View", 10, 100);
        private final Draggable potionsDraggable = DragManager.create(this, "Potions", 10, 300);
        private final Draggable targetHudDraggable = DragManager.create(this, "Target HUD", 300, 10);
        public final Draggable staffListDraggable = DragManager.create(this, "Staff List", 200, 100);
        public final Draggable spotify = DragManager.create(this, "Spotify", 200, 200);
        public float realOffset = 0.0F;
        public float offset = 0;
        private final Animation animation;
        private float hp;
        public int b_color = new Color(0, 0, 0, 128).getRGB();
    public Hud() {
            this.animation = new DecelerateAnimation(175, 1.0F, Direction.BACKWARDS);
            this.potionsDraggable.setWidth(105.0F);
            this.invViewDraggable.setWidth(170.0F);
            this.invViewDraggable.setHeight(64.0F);
            this.targetHudDraggable.setWidth(170 / 1.5f);
            this.targetHudDraggable.setHeight(36.5f);
        }

    private LivingEntity currentTarget = null;



        @EventTarget
        public void onRender2D(EventRender2D event) {
            if (!Utility.mc.gameSettings.showDebugInfo) {
                int scaledWidth = Main.getInstance().getScaleMath().calc(event.getResolution().getScaledWidth());
                int scaledHeight = Main.getInstance().getScaleMath().calc(event.getResolution().getScaledHeight());
                Color color = Main.getInstance().getThemeManager().getCurrentStyleTheme().getColors()[0];
                Color color2 = Main.getInstance().getThemeManager().getCurrentStyleTheme().getColors()[1];
                int bgColor = Color.decode("#1B1C2C").getRGB();
                int elementsColor = Color.decode("#1E1F30").getRGB();
                int strokeColor = Color.decode("#26273B").getRGB();
                Main.getInstance().getScaleMath().pushScale();
                String serverIP;
                int i;
                int index;
                int serverOffset;
                if (elements.get(0)) {
                    String string = Main.cheatProfile.getRoleName();
                    float f = 5.0f;
                    float f2 = 5.0f;
                    String string2 = !mc.isSingleplayer() ? mc.getCurrentServerData().serverIP.toLowerCase() : "localhost";
                    float f3 = sertyo.events.utility.font.Fonts.icons1[16].getWidth("O");
                    float f4 = f3 + Math.max(80.0f, sertyo.events.utility.font.Fonts.msMedium[15].getWidth("   " + Main.cheatProfile.getName() + " | " + string + " | " + string2) + 5.0f);
                    RenderUtil.Render2D.drawGradientRound(5.0f, 5.0f, f4, 9.0f, 1.0f, new Color(31, 31, 31).getRGB(), new Color(31, 31, 31).getRGB(), new Color(31, 31, 31).getRGB(), new Color(31, 31, 31).getRGB());
                    RenderUtil.Render2D.drawShadow(5.0f, 5.0f, f4, 9.0f, 10, new Color(31, 31, 31).getRGB());
                    RenderUtil.Render2D.drawRoundedCorner(5.0f, 5.0f, f4, 9.0f, 1.5f, this.b_color);
                    RenderUtil.Render2D.drawRoundedRect(f + sertyo.events.utility.font.Fonts.gilroyBold[17].getWidth("    ") + 2.0f, f2, 1.0f, 9.0f, 0.0f, new Color(56, 54, 54, 255).getRGB());
                    sertyo.events.utility.font.Fonts.msMedium[15].drawString(new MatrixStack(), "   " + Main.cheatProfile.getName() + " | " + string + " | " + string2, 14.0, 8.0, -1);
                    sertyo.events.utility.font.Fonts.msMedium[16].drawString(new MatrixStack(), "n", 7.0, 8.5, -1);
                }

                String coordsText;
                if (elements.get(1)) {
                    serverIP = !Utility.mc.isSingleplayer() ? Utility.mc.getCurrentServerData().serverIP.toLowerCase() : "localhost";
                    NetworkPlayerInfo playerInfo = Utility.mc.getConnection().getPlayerInfo(Utility.mc.player.getGameProfile().getId());
                    int var10000 = playerInfo != null && !Utility.mc.isSingleplayer() ? playerInfo.getResponseTime() : 0;
                    coordsText = "" + var10000 + " ms";
                    serverOffset = 12 + Fonts.mntssb16.getStringWidth(serverIP);
                    int pingOffset = 12 + Fonts.mntssb16.getStringWidth(coordsText);
                    RenderUtility.drawGlow((float)(scaledWidth - pingOffset - serverOffset - 14), 31.0F, (float)(pingOffset - 2), 15.0F, 10, Color.BLACK.getRGB());
                    RenderUtility.drawRoundedRect((float)(scaledWidth - pingOffset - serverOffset - 15), 30.0F, (float)pingOffset, 17.0F, 10.0F, bgColor);
                    StencilUtility.initStencilToWrite();
                    RenderUtility.drawRoundedRect((float)(scaledWidth - pingOffset - serverOffset - 15), 30.0F, (float)pingOffset, 17.0F, 10.0F, bgColor);
                    StencilUtility.readStencilBuffer(1);
                    RenderUtility.drawGradientGlow((float)(scaledWidth - serverOffset - 24), 39.0F, (float)Fonts.icons21.getStringWidth("l"), (float)Fonts.icons21.getFontHeight(), 8, ColorUtility.applyOpacity2(color, 0.4F), ColorUtility.applyOpacity2(color, 0.4F), ColorUtility.applyOpacity2(color2, 0.4F), ColorUtility.applyOpacity2(color2, 0.4F));
                    int finalServerOffset2 = serverOffset;
                    RenderUtility.applyGradientMask((float)(scaledWidth - serverOffset - 27), 39.0F, (float)Fonts.icons21.getStringWidth("l"), (float)Fonts.icons21.getFontHeight(), 0.5F, color, color2, color, color2, () -> {
                        Fonts.icons21.drawString("l", (float)(scaledWidth - finalServerOffset2 - 27), 39.0F, -1);
                    });
                    StencilUtility.uninitStencilBuffer();
                    Fonts.mntssb16.drawCenteredString(coordsText, (float)(scaledWidth - pingOffset - serverOffset - 15) + (float)pingOffset / 2.0F, 36.0F, -1);
                    RenderUtility.drawGlow((float)(scaledWidth - serverOffset - 9), 31.0F, (float)(serverOffset - 2), 15.0F, 10, Color.BLACK.getRGB());
                    RenderUtility.drawRoundedRect((float)(scaledWidth - serverOffset - 10), 30.0F, (float)serverOffset, 17.0F, 10.0F, bgColor);
                    StencilUtility.initStencilToWrite();
                    RenderUtility.drawRoundedRect((float)(scaledWidth - serverOffset - 10), 30.0F, (float)serverOffset, 17.0F, 10.0F, bgColor);
                    StencilUtility.readStencilBuffer(1);
                    RenderUtility.drawGradientGlow((float)(scaledWidth - 19), 39.0F, (float)Fonts.icons21.getStringWidth("m"), (float)Fonts.icons21.getFontHeight(), 8, ColorUtility.applyOpacity2(color, 0.4F), ColorUtility.applyOpacity2(color, 0.4F), ColorUtility.applyOpacity2(color2, 0.4F), ColorUtility.applyOpacity2(color2, 0.4F));
                    RenderUtility.applyGradientMask((float)(scaledWidth - 22), 39.0F, (float)Fonts.icons21.getStringWidth("m"), (float)Fonts.icons21.getFontHeight(), 0.5F, color, color2, color, color2, () -> {
                        Fonts.icons21.drawString("m", (float)(scaledWidth - 22), 39.0F, -1);
                    });
                    StencilUtility.uninitStencilBuffer();
                    Fonts.mntssb16.drawCenteredString(serverIP, (float)(scaledWidth - serverOffset - 10) + (float)serverOffset / 2.0F, 36.0F, -1);
                }

                int offset;
                if (elements.get(2)) {
                    offset = 0;
                    if (Utility.mc.currentScreen instanceof ChatScreen) {
                        offset = -13;
                    }

                    Object[] var10001 = new Object[]{Math.hypot(Utility.mc.player.getPosX() - Utility.mc.player.prevPosX, Utility.mc.player.getPosZ() - Utility.mc.player.prevPosZ) * (double) 1 * 20.0};
                    String bpsText = "BPS: " + String.format("%.2f", var10001);
                    coordsText = this.getCoordsText();
                    RenderUtility.drawGlow(2.0F, (float)(scaledHeight - 38 + offset), (float)(Fonts.mntssb16.getStringWidth(bpsText) + Fonts.icons21.getStringWidth("v") + 11), 17.0F, 9, new Color(strokeColor).getRGB());
                    RenderUtility.drawRoundedRect(2.0F, (float)(scaledHeight - 38 + offset), (float)(Fonts.mntssb16.getStringWidth(bpsText) + Fonts.icons21.getStringWidth("v") + 11), 17.0F, 9.0F, strokeColor);
                    RenderUtility.drawRoundedRect(3.0F, (float)(scaledHeight - 37 + offset), (float)(Fonts.mntssb16.getStringWidth(bpsText) + Fonts.icons21.getStringWidth("v") + 9), 15.0F, 8.0F, bgColor);
                    Fonts.icons16.drawString("v", 6.0F, (float)scaledHeight - 30.5F + (float)offset, -1);
                    Fonts.mntssb16.drawString(bpsText, (float)(9 + Fonts.icons21.getStringWidth("v")), (float)scaledHeight - 31.5F + (float)offset, -1);
                    RenderUtility.drawGlow(2.0F, (float)(scaledHeight - 19 + offset), (float)(Fonts.mntssb16.getStringWidth(coordsText) + Fonts.icons21.getStringWidth("w") + 1), 17.0F, 9, new Color(strokeColor).getRGB());
                    RenderUtility.drawRoundedRect(2.0F, (float)(scaledHeight - 19 + offset), (float)(Fonts.mntssb16.getStringWidth(coordsText) + Fonts.icons21.getStringWidth("w") + 11), 17.0F, 9.0F, strokeColor);
                    RenderUtility.drawRoundedRect(3.0F, (float)(scaledHeight - 18 + offset), (float)(Fonts.mntssb16.getStringWidth(coordsText) + Fonts.icons21.getStringWidth("w") + 9), 15.0F, 8.0F, bgColor);
                    Fonts.icons16.drawString("w", 6.0F, (float)scaledHeight - 11.5F + (float)offset, -1);
                    Fonts.mntssb16.drawString(coordsText, (float)(9 + Fonts.icons21.getStringWidth("w")), (float)scaledHeight - 12.5F + (float)offset, -1);
                }


                if (elements.get(3)) {
                    RenderUtility.drawGlow((float)(this.invViewDraggable.getX() - 1), (float)(this.invViewDraggable.getY() - 1), 172.0F, 77.0F, 10, new Color(bgColor).getRGB());
                    RenderUtility.drawRoundedRect((float)(this.invViewDraggable.getX() - 1), (float)(this.invViewDraggable.getY() - 1), 172.0F, 77.0F, 11.0F, strokeColor);
                    RenderUtility.drawRoundedRect((float)this.invViewDraggable.getX(), (float)this.invViewDraggable.getY(), 170.0F, 75.0F, 10.0F, elementsColor);
                    RenderUtility.drawGradientGlow((float)(this.invViewDraggable.getX() + 170 - 5 - Fonts.icons21.getStringWidth("u")), (float)(this.invViewDraggable.getY() + 6), (float)Fonts.icons21.getStringWidth("u"), (float)Fonts.icons21.getFontHeight(), 8, ColorUtility.applyOpacity2(color, 0.4F), ColorUtility.applyOpacity2(color, 0.4F), ColorUtility.applyOpacity2(color2, 0.4F), ColorUtility.applyOpacity2(color2, 0.4F));
                    RenderUtility.applyGradientMask((float)(this.invViewDraggable.getX() + 170 - 5 - Fonts.icons21.getStringWidth("u")), (float)(this.invViewDraggable.getY() + 6), (float)Fonts.icons21.getStringWidth("u"), (float)Fonts.icons21.getFontHeight(), 0.5F, color, color2, color, color2, () -> {
                        Fonts.icons21.drawString("u", (float)(this.invViewDraggable.getX() + 170 - 5 - Fonts.icons21.getStringWidth("u")), (float)(this.invViewDraggable.getY() + 6), -1);
                    });
                    Fonts.mntssb16.drawString("Inventory", (float)(this.invViewDraggable.getX() + 5), (float)this.invViewDraggable.getY() + 5.5F, -1);
                    RenderUtility.Render2D.drawRect((float)this.invViewDraggable.getX(), (float)(this.invViewDraggable.getY() + 16), 170.0F, 1.0F, strokeColor);

                    for(offset = 9; offset < 36; ++offset) {
                        ItemStack itemStack = Utility.mc.player.inventory.getStackInSlot(offset);
                        if (!itemStack.isEmpty()) {
                            index = offset - 9;
                            RenderUtility.drawItemStack(itemStack, this.invViewDraggable.getX() + 5 + index % 9 * 18, this.invViewDraggable.getY() + 19 + index / 9 * 18);
                        }
                    }
                }

                if (elements.get(4)) {
                    offset = -3;
                    Iterator var19;
                    EffectInstance potionEffect;
                    if (!Utility.mc.player.getActivePotionEffects().isEmpty()) {
                        offset = 0;

                        for(var19 = Utility.mc.player.getActivePotionEffects().iterator(); var19.hasNext(); offset += 10) {
                            potionEffect = (EffectInstance)var19.next();
                        }
                    }

                    this.realOffset = AnimationMath.fast(this.realOffset, (float)offset, 15.0F);
                    this.potionsDraggable.setHeight((float)(21 + offset));
                    RenderUtility.drawGlow((float)(this.potionsDraggable.getX() - 1), (float)(this.potionsDraggable.getY() - 1), 107.0F, 23.0F + this.realOffset, 10, new Color(bgColor).getRGB());
                    RenderUtility.drawRoundedRect((float)(this.potionsDraggable.getX() - 1), (float)(this.potionsDraggable.getY() - 1), 107.0F, 23.0F + this.realOffset, 11.0F, strokeColor);
                    RenderUtility.drawRoundedRect((float)this.potionsDraggable.getX(), (float)this.potionsDraggable.getY(), 105.0F, 21.0F + this.realOffset, 10.0F, elementsColor);
                    RenderUtility.drawGradientGlow((float)(this.potionsDraggable.getX() + 105 - 3 - Fonts.icons21.getStringWidth("n")), (float)this.potionsDraggable.getY() + 6.5F, (float)(Fonts.icons21.getStringWidth("n") - 3), (float)(Fonts.icons21.getFontHeight() - 1), 8, ColorUtility.applyOpacity2(color, 0.4F), ColorUtility.applyOpacity2(color, 0.4F), ColorUtility.applyOpacity2(color2, 0.4F), ColorUtility.applyOpacity2(color2, 0.4F));
                    RenderUtility.applyGradientMask((float)(this.potionsDraggable.getX() + 105 - 5 - Fonts.icons21.getStringWidth("n")), (float)this.potionsDraggable.getY() + 6.5F, (float)Fonts.icons21.getStringWidth("n"), (float)Fonts.icons21.getFontHeight(), 0.5F, color, color2, color, color2, () -> {
                        Fonts.icons21.drawString("n", (float)(this.potionsDraggable.getX() + 105 - 5 - Fonts.icons21.getStringWidth("n")), (float)this.potionsDraggable.getY() + 6.5F, -1);
                    });
                    Fonts.mntssb16.drawString("Potions", (float)(this.potionsDraggable.getX() + 5), (float)this.potionsDraggable.getY() + 6.5F, -1);
                    if (!Utility.mc.player.getActivePotionEffects().isEmpty()) {
                        RenderUtility.Render2D.drawRect((float)this.potionsDraggable.getX(), (float)(this.potionsDraggable.getY() + 16), 105.0F, 1.0F, strokeColor);
                    }

                    RenderUtil.SmartScissor.push();
                    RenderUtil.SmartScissor.setFromComponentCoordinates((double)this.potionsDraggable.getX(), (double)this.potionsDraggable.getY(), 105.0, (double)(21.0F + this.realOffset));
                    offset = 0;

                    for(var19 = Utility.mc.player.getActivePotionEffects().iterator(); var19.hasNext(); offset += 10) {
                        potionEffect = (EffectInstance)var19.next();
                        String var25 = I18n.format(potionEffect.getEffectName(), new Object[0]);
                        String potionName = var25 + " " + getPotionPower(potionEffect);
                        String duration = getDuration(potionEffect);
                        Fonts.mntssb15.drawString(potionName, (float)(this.potionsDraggable.getX() + 5), (float)(this.potionsDraggable.getY() + 22 + offset), -1);
                        Fonts.mntssb15.drawString(duration, (float)(this.potionsDraggable.getX() + 105 - Fonts.mntssb15.getStringWidth(duration) - 5), (float)(this.potionsDraggable.getY() + 22 + offset), getPotionDurationColor(potionEffect));
                    }

                    RenderUtil.SmartScissor.unset();
                    RenderUtil.SmartScissor.pop();
                }


                if (elements.get(6)) {
                    if (KillAura.target != null) {
                        this.currentTarget = KillAura.target;
                    } else if (Utility.mc.currentScreen instanceof ChatScreen) {
                        this.currentTarget = Utility.mc.player;
                    }

                    if (this.currentTarget != null) {
                        if (KillAura.target == null && !(Utility.mc.currentScreen instanceof ChatScreen)) {
                            this.animation.setDirection(Direction.BACKWARDS);
                        } else {
                            this.animation.setDirection(Direction.FORWARDS);
                        }

                        RenderUtility.Render2D.scaleStart((float)(this.targetHudDraggable.getX() + 56), (float)(this.targetHudDraggable.getY() + 19), this.animation.getOutput());
                        RenderUtility.drawGlow((float)(this.targetHudDraggable.getX() - 1), (float)(this.targetHudDraggable.getY() - 1), 126.0F, 40.0F, 10, new Color(bgColor).getRGB());
                        RenderUtility.drawRoundedRect((float)(this.targetHudDraggable.getX() - 1), (float)(this.targetHudDraggable.getY() - 1), 126.0F, 40.0F, 11.0F, strokeColor);
                        RenderUtility.drawRoundedRect((float)this.targetHudDraggable.getX(), (float)this.targetHudDraggable.getY(), 124.0F, 38.0F, 10.0F, elementsColor);
                        if (this.currentTarget instanceof PlayerEntity) {
                            StencilUtility.initStencilToWrite();
                            RenderUtility.drawRoundedRect((float)(this.targetHudDraggable.getX() + 4), (float)(this.targetHudDraggable.getY() + 5), 28.0F, 28.0F, 15.0F, -1);
                            StencilUtility.readStencilBuffer(1);
                            float hurtPercent = getHurtPercent(this.currentTarget);
                            GlStateManager.pushMatrix();
                            GlStateManager.color((int) 1.0F, (int) (1.0F - hurtPercent), (int) (1.0F - hurtPercent), (int) 1.0F);
                            Utility.mc.getTextureManager().bindTexture(((AbstractClientPlayerEntity)this.currentTarget).getLocationSkin());
                            GlStateManager.enableTexture();
                            AbstractGui.drawScaledCustomSizeModalRect(this.targetHudDraggable.getX() + 4, this.targetHudDraggable.getY() + 5, 8.0F, 8.0F, 8, 8, 28, 28, 64.0F, 64.0F);
                            GlStateManager.popMatrix();
                            StencilUtility.uninitStencilBuffer();
                        } else {
                            Fonts.tenacityBold28.drawCenteredString("?", (float)(this.targetHudDraggable.getX() + 19), (float)(this.targetHudDraggable.getY() + 19) - (float)Fonts.tenacityBold28.getFontHeight() / 2.0F, -1);
                        }

                        StencilUtility.initStencilToWrite();
                        RenderUtility.drawRoundedRect((float)this.targetHudDraggable.getX(), (float)this.targetHudDraggable.getY(), 132.0F, 38.0F, 10.0F, elementsColor);
                        StencilUtility.readStencilBuffer(1);
                        Fonts.mntssb16.drawSubstring(TextFormatting.getTextWithoutFormattingCodes(this.currentTarget.getName().getString()), (float)(this.targetHudDraggable.getX() + 38), (float)(this.targetHudDraggable.getY() + 8), -1, 64.0F);
                        StencilUtility.uninitStencilBuffer();
                        this.hp = MathUtility.clamp(MathUtility.lerp(this.hp, this.currentTarget.getHealth() / this.currentTarget.getMaxHealth(), (float)(12.0 * AnimationMath.deltaTime())), 0.0F, 1.0F);
                        Fonts.mntssb14.drawString("HP: " + Math.round(this.hp * 20.0F) + " - Dst: " + MathUtility.round((double) Utility.mc.player.getDistance(this.currentTarget), 1), (float)(this.targetHudDraggable.getX() + 38), (float)(this.targetHudDraggable.getY() + 20), -1);
                        RenderUtility.drawRoundedRect((float)(this.targetHudDraggable.getX() + 38), (float)(this.targetHudDraggable.getY() + 26), 80.0F, 5.0F, 4.0F, Color.decode("#1E1F30").brighter().getRGB());
                        RenderUtility.drawRoundedGradientRect((float)(this.targetHudDraggable.getX() + 38), (float)(this.targetHudDraggable.getY() + 26), 80.0F * this.hp, 5.0F, 4.0F, 1.0F, color.getRGB(), color.getRGB(), color2.getRGB(), color2.getRGB());
                        RenderUtility.scaleEnd();
                    }
                }



                if (elements.get(7)) {
                    Keybinds.render();
                }
                if (elements.get(8)) {
                    StaffList.render();
                }

                Main.getInstance().getScaleMath().popScale();
                if (elements.get(5)) {
                    List<ItemStack> armor = Lists.reverse(Utility.mc.player.inventory.armorInventory);

                    for(i = 0; i < 4; ++i) {
                        RenderUtility.drawItemStack((ItemStack)armor.get(i), event.getResolution().getScaledWidth() / 2 + 13 + i * 18, event.getResolution().getScaledHeight() - this.getArmorOffset());
                    }
                }

            }
        }
    public void onEnable() {
        super.onEnable();
        StaffList.updateList();
    }

    @EventTarget
    public void onUpdate(EventUpdate eventUpdate) {
        if (Utility.mc.player.ticksExisted % 10 == 0 && elements.get(8)) {
            StaffList.updateList();
        }
    }
    public static String getPotionPower(EffectInstance potionEffect) {
        if (potionEffect.getAmplifier() == 1) {
            return "II";
        } else if (potionEffect.getAmplifier() == 2) {
            return "III";
        } else if (potionEffect.getAmplifier() == 3) {
            return "IV";
        } else if (potionEffect.getAmplifier() == 4) {
            return "V";
        } else if (potionEffect.getAmplifier() == 5) {
            return "VI";
        } else if (potionEffect.getAmplifier() == 6) {
            return "VII";
        } else if (potionEffect.getAmplifier() == 7) {
            return "VIII";
        } else if (potionEffect.getAmplifier() == 8) {
            return "IX";
        } else {
            return potionEffect.getAmplifier() == 9 ? "X" : "";
        }
    }

    public static String getDuration(EffectInstance potionEffect) {
        return potionEffect.getIsPotionDurationMax() ? "**:**" : StringUtils.ticksToElapsedTime(potionEffect.getDuration());
    }

    private static int getPotionDurationColor(EffectInstance potionEffect) {
        int potionColor = -1;
        if (potionEffect.getDuration() < 200) {
            potionColor = (new Color(255, 103, 32)).getRGB();
        } else if (potionEffect.getDuration() < 400) {
            potionColor = (new Color(231, 143, 32)).getRGB();
        } else if (potionEffect.getDuration() > 400) {
            potionColor = (new Color(205, 205, 205)).getRGB();
        }

        return potionColor;
    }
    private int getArmorOffset() {
        int offset = 56;
        if (Utility.mc.player.isCreative()) {
            offset -= 15;
        }

        if (!Utility.mc.player.isCreative() && Utility.mc.player.isInWater()) {
            offset += 10;
        }

        if (Utility.mc.player.getRidingEntity() != null && Utility.mc.player.getRidingEntity() instanceof LivingEntity) {
            LivingEntity entity = (LivingEntity) Utility.mc.player.getRidingEntity();
            offset = (int)((double)(offset - 10) + Math.ceil((double)((entity.getMaxHealth() - 1.0F) / 20.0F)) * 10.0) + (Utility.mc.player.isCreative() ? 15 : 0);
        }

        return offset;
    }
    private String getCoordsText() {
        if (Utility.mc.player == null) {
            return "";
        } else {
            StringBuilder coordsBuilder = (new StringBuilder()).append("XYZ: ");
            if (Utility.mc.player.world.getDimensionKey().equals(DimensionType.THE_END)) {
                coordsBuilder.append(TextFormatting.DARK_PURPLE).append(Utility.mc.player.getPosition().getX()).append(", ").append(Utility.mc.player.getPosition().getY()).append(", ").append(Utility.mc.player.getPosition().getZ()).append(TextFormatting.RESET);
            } else if (Utility.mc.player.getEntityWorld().getDimensionKey().equals(DimensionType.THE_NETHER)) {
                coordsBuilder.append(TextFormatting.RED).append(Utility.mc.player.getPosition().getX()).append(", ").append(Utility.mc.player.getPosition().getY()).append(", ").append(Utility.mc.player.getPosition().getZ()).append(TextFormatting.GREEN).append(" (").append(Utility.mc.player.getPosition().getX() * 8).append(", ").append(Utility.mc.player.getPosition().getY()).append(", ").append(Utility.mc.player.getPosition().getZ() * 8).append(")").append(TextFormatting.RESET);
            } else {
                coordsBuilder.append(TextFormatting.GREEN).append(Utility.mc.player.getPosition().getX()).append(", ").append(Utility.mc.player.getPosition().getY()).append(", ").append(Utility.mc.player.getPosition().getZ()).append(TextFormatting.RED).append(" (").append(Utility.mc.player.getPosition().getX() / 8).append(", ").append(Utility.mc.player.getPosition().getY()).append(", ").append(Utility.mc.player.getPosition().getZ() / 8).append(")").append(TextFormatting.RESET);
            }

            return coordsBuilder.toString();
        }
    }

    public static void drawStyledRect(float posX, float posY, float width, float height, float radius) {
          GaussianBlur.startBlur();
          GaussianBlur.endBlur(20, 2);
          RenderUtil.Render2D.drawRoundedRect(posX, posY, width, height, radius, ColorUtil.rgba(105, 105, 105, 200));
          RenderUtil.Render2D.drawRoundedRect(posX, posY, width, height, radius, ColorUtil.rgba(25, 25, 25, 170));

    }
    private void drawHead(MatrixStack matrix, final Entity entity, final double x, final double y, final int size) {
        if (entity instanceof AbstractClientPlayerEntity player) {
            RenderSystem.enableBlend();
            RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            RenderSystem.alphaFunc(GL11.GL_GREATER, 0);
            RenderSystem.enableTexture();
            mc.getTextureManager().bindTexture(player.getLocationSkin());
            float hurtPercent = (((AbstractClientPlayerEntity) entity).hurtTime - (((AbstractClientPlayerEntity) entity).hurtTime != 0 ? mc.timer.renderPartialTicks : 0.0f)) / 10.0f;
            RenderSystem.color4f(1, 1 - hurtPercent, 1 - hurtPercent, 1);
            AbstractGui.blit(matrix, (int) x, (int) y, size, size, 4F, 4F, (int) 4F, (int) 4F, (int) 32F, (int) 32F);
            scaleStart((float) (x + size / 2F), (float) (y + size / 2F), 1.1F);
            AbstractGui.blit(matrix, (int) x, (int) y, size, size, 20, 4, 4, 4, 32, 32);
            scaleEnd();
            RenderSystem.disableBlend();
        } else {
            //  int color = ColorUtil.getColor(20, 128);
            //    drawRoundedRect(matrix, (float) x, (float) y, (float) (x + size), (float) (y + size), 2F, 1, color, color, color, color, false, false, true, true);
            sertyo.events.utility.font.Fonts.msBold[size].drawCenteredString(matrix, "?", x + (size / 2F), y + 3 + (size / 2F) - (sertyo.events.utility.font.Fonts.msBold[size].getFontHeight() / 2F), -1);
        }
    }
}
