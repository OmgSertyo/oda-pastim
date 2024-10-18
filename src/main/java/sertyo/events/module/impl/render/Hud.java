package sertyo.events.module.impl.render;


import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextFormatting;
import sertyo.events.Main;
import sertyo.events.event.render.EventRender2D;
import sertyo.events.manager.dragging.DragManager;
import sertyo.events.manager.dragging.Draggable;
import sertyo.events.module.Category;
import sertyo.events.module.Module;
import sertyo.events.module.ModuleAnnotation;
import sertyo.events.module.setting.impl.MultiBooleanSetting;
import sertyo.events.utility.Utility;
import sertyo.events.utility.render.ColorUtil;
import sertyo.events.utility.render.RenderUtil;
import sertyo.events.utility.render.animation.Animation;
import sertyo.events.utility.render.animation.Direction;
import sertyo.events.utility.render.animation.impl.DecelerateAnimation;
import sertyo.events.utility.font.Fonts;

import java.awt.*;
import java.util.Arrays;


@ModuleAnnotation(
        name = "Hud",
        category = Category.RENDER
)
public class Hud extends Module {

        public static final MultiBooleanSetting elements = new MultiBooleanSetting("Elements", Arrays.asList("Watermark", "Server Info", "Coords", "Inventory", "Potions", "Armor", "Target HUD", "Keybinds", "Staff List"));
        private final Draggable invViewDraggable = DragManager.create(this, "Inventory View", 10, 100);
        private final Draggable potionsDraggable = DragManager.create(this, "Potions", 10, 300);
        private final Draggable targetHudDraggable = DragManager.create(this, "Target HUD", 300, 10);
        public final Draggable keybindsDraggable = DragManager.create(this, "Keybinds", 200, 200);
        public final Draggable staffListDraggable = DragManager.create(this, "Staff List", 200, 100);
        public float realOffset = 0.0F;
        private final Animation animation;
        private float hp;

        public Hud() {
            this.animation = new DecelerateAnimation(175, 1.0F, Direction.BACKWARDS);
            this.potionsDraggable.setWidth(105.0F);
            this.invViewDraggable.setWidth(170.0F);
            this.invViewDraggable.setHeight(64.0F);
            this.targetHudDraggable.setWidth(137.0F);
            this.targetHudDraggable.setHeight(45.0F);
        }

        public void onEnable() {
            super.onEnable();
        }



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
                    final float width = (float) Fonts.msBold[15].getWidth("neironmyst | " + Main.getInstance().getUsername() + "fps: " + net.minecraft.client.Minecraft.debugFPS) + 3;
            //        RenderUtil.Render2D.drawRoundedGradientRect(5.5F, 5.5F, width + 12, 16, 10, 1, ColorUtil.getColor(0), ColorUtil.getColor(90), ColorUtil.getColor(180), ColorUtil.getColor(270));
                        RenderUtil.Render2D.drawShadow(6, 6, width + 13, 15, 8,
                                new Color(ColorUtil.setAlpha(ColorUtil.getColor(0), 175)).getRGB(),
                                new Color(ColorUtil.setAlpha(ColorUtil.getColor(90), 175)).getRGB(),
                                new Color(ColorUtil.setAlpha(ColorUtil.getColor(180), 175)).getRGB(),
                                new Color(ColorUtil.setAlpha(ColorUtil.getColor(270), 175)).getRGB());

                    RenderUtil.Render2D.drawRoundedRect(6, 6, width + 11, 15, 10, new Color(28, 28, 28, 200).getRGB());
                    Fonts.msBold[15].drawString(TextFormatting.DARK_GRAY + " | " + TextFormatting.RESET + TextFormatting.GRAY + Main.getInstance().getUsername() + TextFormatting.RESET + TextFormatting.DARK_GRAY + " | " + TextFormatting.GRAY + "fps: " + Minecraft.debugFPS, 56, 11.5F, -1);
                    Fonts.msBold[15].drawString(TextFormatting.GRAY + "neironmyst", 10, 11.5F, -1);
                }

                String coordsText;
                if (elements.get(1)) {
                }

                int offset;
                if (elements.get(2)) {
                }

                if (elements.get(3)) {
                }

                if (elements.get(4)) {
                }

                if (elements.get(6)) {
                }

                if (elements.get(7)) {
                 //   Keybinds.render();
                }

                if (elements.get(8)) {
                 //   StaffList.render();
                }

                Main.getInstance().getScaleMath().popScale();
                if (elements.get(5)) {
                }

            }
        }
}
