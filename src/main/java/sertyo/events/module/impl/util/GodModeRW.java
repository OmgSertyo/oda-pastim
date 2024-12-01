package sertyo.events.module.impl.util;

import com.darkmagician6.eventapi.EventTarget;

import net.minecraft.client.Minecraft;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.util.text.TextFormatting;
import sertyo.events.event.input.EventInputKey;
import sertyo.events.event.player.EventUpdate;
import sertyo.events.module.Category;
import sertyo.events.module.Module;
import sertyo.events.module.ModuleAnnotation;
import sertyo.events.module.setting.impl.KeySetting;
import sertyo.events.utility.misc.ChatUtility;
import sertyo.events.utility.misc.TimerHelper;

@ModuleAnnotation(name = "KtLeave", category = Category.UTIL)
public class GodModeRW extends Module {

    private final Minecraft mc = Minecraft.getInstance();
    private final TimerHelper warpDelay = new TimerHelper();
    private boolean menuClosed = false;
    private final KeySetting leavekey = new KeySetting("Кнопка лива", -1);
    @Override
    public void onEnable() {
        resetState();
        sendWarpCommand();
        warpDelay.reset();
    }

    @Override
    public void onDisable() {
        resetState();
        super.onDisable();
    }

    private void sendWarpCommand() {
        mc.player.sendChatMessage("/darena");
        menuClosed = false;
    }

    private void clickSlot() {
        if (mc.player.openContainer != null && mc.player.openContainer.getSlot(24) != null) {
            mc.playerController.windowClick(mc.player.openContainer.windowId, 24, 0, ClickType.QUICK_MOVE, mc.player);
        }
    }

    private void forceCloseMenu() {
        mc.displayGuiScreen(null);
        menuClosed = true;

        ChatUtility.addChatMessage(TextFormatting.GREEN + "KtLeave Enabled!");
    }

    private void resetState() {
        menuClosed = false;
        warpDelay.reset();
    }

    private boolean leave = false;

    @EventTarget
    private void onUpdate(EventUpdate event) {
        if (!menuClosed && warpDelay.hasReached(90)) {
            forceCloseMenu();
        }



        if (leave) {
                clickSlot();
                leave = false;
            }

    }
    @EventTarget
    public void onKey(EventInputKey event) {
        if (event.getKey() == leavekey.get()) {
            leave = true;
        }
    }
}