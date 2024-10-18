package sertyo.events.module.impl.util;

import com.darkmagician6.eventapi.EventTarget;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.vector.Vector4f;
import sertyo.events.event.render.EventRender2D;
import sertyo.events.module.Category;
import sertyo.events.module.Module;
import sertyo.events.module.ModuleAnnotation;
import sertyo.events.module.setting.impl.ModeSetting;
import sertyo.events.utility.font.Fonts;

@ModuleAnnotation(
        name = "VulcanESP",
        category = Category.RENDER
)
public class VulcanESP extends Module {
    private final HashMap<Entity, Vector4f> positions = new HashMap<>();
    private final ModeSetting modeSetting = new ModeSetting("Mode", "All", new String[]{"Diamonds", "Player_Heads", "Totems", "Netherite", "TRIPWIRE_HOOK", "All"});

    @EventTarget
    public void onRender(EventRender2D event) {
        if (mc.world == null) return;

        for (Entity entity : mc.world.getAllEntities()) {
            if (entity instanceof ItemEntity) {
                ItemEntity itemEntity = (ItemEntity) entity;
                ItemStack stack = itemEntity.getItem();
                MatrixStack matrixStack = new MatrixStack();
                if (isSpecificItem(stack)) {
                    String displayName = stack.getDisplayName() + "";
                    updateItemTimer(itemEntity);
                    renderItemName(matrixStack, itemEntity, displayName);
                }
            }
        }

    }





    private final Map<ItemEntity, Long> itemTimers = new HashMap<>();
    private final long delay = 1000L;

    private boolean isSpecificItem(ItemStack stack) {
        return stack.getItem() == net.minecraft.item.Items.TOTEM_OF_UNDYING ||
                stack.getItem() == net.minecraft.item.Items.PLAYER_HEAD ||
                stack.getItem() == net.minecraft.item.Items.SPLASH_POTION ||
                stack.getItem() == net.minecraft.item.Items.TRIPWIRE_HOOK;
    }
    private void updateItemTimer(ItemEntity itemEntity) {
        long currentTime = System.currentTimeMillis();
        if (!itemTimers.containsKey(itemEntity)) {
            itemTimers.put(itemEntity, currentTime);
        }
    }
    private void renderItemName(MatrixStack matrixStack, ItemEntity itemEntity, String name) {
        matrixStack.push();
        double distance = mc.player.getDistance(itemEntity);
        float scale = 0.025F * (1.0F + (float) distance / 50.0F);
        applyCommonTransformations(matrixStack, itemEntity, scale);
        renderText(matrixStack, name, 0, 10, itemEntity.getItem().getCount());
        renderDistance(matrixStack, distance);
        renderAdditionalText(matrixStack, itemEntity);

        matrixStack.pop();
    }
    private void applyCommonTransformations(MatrixStack matrixStack, ItemEntity itemEntity, float scale) {
        matrixStack.translate(
                itemEntity.getPosX() - mc.getRenderViewEntity().getPosition().getX(),
                itemEntity.getPosY() + 1.5D - mc.getRenderViewEntity().getPosition().getY(),
                itemEntity.getPosZ() - mc.getRenderViewEntity().getPosition().getZ()
        );
    //    matrixStack.rotate(mc.getRenderViewEntity().getPosition());
        matrixStack.scale(-scale, -scale, scale);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableDepthTest();
    }
    private void renderText(MatrixStack matrixStack, String textComponent, int x, int y, int itemCount) {
        Fonts.msBold[18].drawString(textComponent, x - Fonts.msBold[18].getWidth(textComponent) / 2.0F, y, 0xFFFFFF);
        String itemCountText = "x" + itemCount;
        Fonts.msBold[18].drawString(itemCountText, x + Fonts.msBold[18].getWidth(textComponent) / 2.0F + 5, y, 0xFFFFFF);

        RenderSystem.disableBlend();
    }
    private void renderDistance(MatrixStack matrixStack, double distance) {
        String distanceText = String.format("%.1f m", distance);
        int textWidth = (int) Fonts.msBold[18].getWidth(distanceText);
        int x = -textWidth / 2;
        int y = 20;
        Fonts.msBold[18].drawString(distanceText, x, y, 0xFFFFFF);
    }
    private void renderAdditionalText(MatrixStack matrixStack, ItemEntity itemEntity) {
        long currentTime = System.currentTimeMillis();
        long itemTime = itemTimers.getOrDefault(itemEntity, currentTime);
        long elapsed = currentTime - itemTime;
        boolean isAvailable = elapsed >= delay;
        int color = isAvailable ? 0x00FF00 : 0xFF0000;
        String message = isAvailable ? "+" : "-";
        Fonts.msBold[18].drawString(matrixStack, message, -Fonts.msBold[18].getWidth(message) / 2.0F, 30, color);
    }
}