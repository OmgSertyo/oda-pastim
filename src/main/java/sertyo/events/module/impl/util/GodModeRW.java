package sertyo.events.module.impl.util;
import com.darkmagician6.eventapi.EventTarget;
import me.sertyo.j2c.J2c;
import net.minecraft.client.Minecraft;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.util.text.TextFormatting;
import sertyo.events.event.input.EventInputKey;
import sertyo.events.module.Category;
import sertyo.events.module.Module;
import sertyo.events.module.ModuleAnnotation;
import sertyo.events.module.setting.impl.KeySetting;
import sertyo.events.utility.misc.ChatUtility;
@J2c
@ModuleAnnotation(name = "KtLeave", category = Category.COMBAT)
public class GodModeRW extends Module {
    private final A stopWatch = new A();
    private final A warpDelay = new A();
    private boolean menuClosed = false;
    private Thread updateThread;
    private final KeySetting key = new KeySetting("Кнопка лива", -1);
    @Override
    public void onEnable() {
        resetState();
        sendWarpCommand();
        warpDelay.reset();

        updateThread = new Thread(this::run);
        updateThread.start();


        super.onEnable();
    }
    @Override
    public void onDisable() {
        resetState();
        if (updateThread != null && updateThread.isAlive()) {
            updateThread.interrupt();
        }
        super.onDisable();
    }
    private void sendWarpCommand() {
        mc.player.sendChatMessage("/darena");
        mc.mouseHelper.grabMouse();
        menuClosed = false;
    }
    private void clickSlot(int slotIndex) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player.openContainer != null) {
            mc.player.openContainer.getSlot(slotIndex);
            mc.playerController.windowClick(mc.player.openContainer.windowId, slotIndex, 0, ClickType.QUICK_MOVE, mc.player);


        } else {

        }
    }
    private void forceCloseMenu() {
        Minecraft mc = Minecraft.getInstance();
        mc.displayGuiScreen(null);
        mc.mouseHelper.ungrabMouse();
        menuClosed = true;

        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }


        ChatUtility.addChatMessage(TextFormatting.WHITE + "KtLeave активирован не ходите, не открывайте контейнеры(инвентарь сундук, мистик)!");
    }
    private void resetState() {

        menuClosed = false;
        stopWatch.reset();
        warpDelay.reset();

    }
    private boolean leave = false;
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                Thread.sleep(50);

                if (!menuClosed && warpDelay.hasTimeElapsed(1000L)) {
                    forceCloseMenu();
                }

                if (leave) {
                    clickSlot(24);
                    leave = false;
                }

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
    @EventTarget
    public void onkey(EventInputKey e) {
        if (e.getKey() == key.get()) {
            leave = true;
        }
    }

}