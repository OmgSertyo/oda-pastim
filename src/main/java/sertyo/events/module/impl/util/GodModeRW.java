package sertyo.events.module.impl.util;

import com.darkmagician6.eventapi.EventTarget;
import com.google.common.eventbus.Subscribe;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ClientBossInfo;
import net.minecraft.client.gui.overlay.BossOverlayGui;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.util.text.TextFormatting;
import sertyo.events.event.player.EventUpdate;
import sertyo.events.module.Category;
import sertyo.events.module.Module;
import sertyo.events.module.ModuleAnnotation;
import sertyo.events.utility.misc.ChatUtility;
import sertyo.events.utility.misc.TimerHelper;

@ModuleAnnotation(name = "GodModeRW", category = Category.UTIL)
public class GodModeRW extends Module {

    private final Minecraft mc = Minecraft.getInstance();
    private final TimerHelper stopWatch = new TimerHelper();
    private final TimerHelper warpDelay = new TimerHelper();
    private boolean clickingSlot13 = false;
    private boolean slot21Clicked = false;
    private boolean menuClosed = false;

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
        mc.player.sendChatMessage("/warp");
        menuClosed = false;
    }

    private void clickSlot(int slotIndex) {
        if (mc.player.openContainer != null && mc.player.openContainer.getSlot(slotIndex) != null) {
            mc.playerController.windowClick(mc.player.openContainer.windowId, slotIndex, 0, ClickType.QUICK_MOVE, mc.player);
        }
    }

    private void forceCloseMenu() {
        mc.displayGuiScreen(null);
        menuClosed = true;

        ChatUtility.addChatMessage(TextFormatting.GREEN + "GodMode Enabled!");
    }

    private void startClickingSlot13() {
        clickingSlot13 = true;
    }

    private void stopClickingSlot13() {
        clickingSlot13 = false;
    }

    private void resetState() {
        clickingSlot13 = false;
        slot21Clicked = false;
        menuClosed = false;
        stopWatch.reset();
        warpDelay.reset();
    }

    private boolean isPvpBossBarActive() {
        BossOverlayGui bossOverlayGui = mc.ingameGUI.getBossOverlay();
        for (ClientBossInfo bossInfo : bossOverlayGui.getActiveBossBars()) {
            String bossName = bossInfo.getName().getString();
            if (bossName.contains("Режим ПВП") || bossName.contains("PVP")) {
                return true;
            }
        }
        return false;
    }

    @EventTarget
    private void onUpdate(EventUpdate event) {
        if (!menuClosed && warpDelay.hasReached(90)) {
            forceCloseMenu();
        }

        if (warpDelay.hasReached(50) && !slot21Clicked && mc.player.openContainer != null) {
            clickSlot(21);
            slot21Clicked = true;
        }

        if (isPvpBossBarActive()) {
            if (!clickingSlot13) {
                startClickingSlot13();
            }
        } else {
            if (clickingSlot13) {
                stopClickingSlot13();
            }
        }

        if (clickingSlot13 && stopWatch.hasReached(50)) {
            clickSlot(13);
            stopWatch.reset();
        }
    }
}