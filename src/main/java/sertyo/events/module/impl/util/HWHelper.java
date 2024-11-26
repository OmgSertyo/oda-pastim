package sertyo.events.module.impl.util;

import com.darkmagician6.eventapi.EventTarget;
import com.google.common.eventbus.Subscribe;
import net.minecraft.item.AirItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.play.client.CHeldItemChangePacket;
import net.minecraft.network.play.client.CPlayerTryUseItemPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.text.TextFormatting;
import sertyo.events.event.input.EventInputKey;
import sertyo.events.event.packet.EventPacket;
import sertyo.events.event.player.EventUpdate;
import sertyo.events.module.Category;
import sertyo.events.module.Module;
import sertyo.events.module.ModuleAnnotation;
import sertyo.events.module.impl.render.InvUtil;
import sertyo.events.module.setting.impl.BooleanSetting;
import sertyo.events.module.setting.impl.KeySetting;
import sertyo.events.module.setting.impl.MultiBooleanSetting;
import sertyo.events.utility.misc.ChatUtility;
import sertyo.events.utility.misc.InventoryUtil;

import java.util.Arrays;


@ModuleAnnotation(name = "HWHelper", category = Category.UTIL)
public class HWHelper extends Module {


    private final MultiBooleanSetting mode = new MultiBooleanSetting("Тип", Arrays.asList(
            "Использовать по бинду", "Закрывать меню"));


    private final KeySetting disorientationKey = new KeySetting("Кнопка трапки", -1, () -> mode.get(1));
    private final KeySetting trapKey = new KeySetting("Кнопка стана", -1,
            () -> mode.get(1));

    InvUtil.Hand handUtil = new InvUtil.Hand();
    long delay;
    boolean disorientationThrow, trapThrow, snejokThrow, yavkaThrow, bojauraThrow, plastThrow;



    @EventTarget
    private void onKey(EventInputKey e) {
        if (e.getKey() == disorientationKey.get()) {
            disorientationThrow = true;
        }
        if (e.getKey() == trapKey.get()) {
            trapThrow = true;
        }
    }

    @EventTarget
    private void onUpdate(EventUpdate e) {
        if (disorientationThrow) {
            this.handUtil.handleItemChange(System.currentTimeMillis() - this.delay > 200L);
            int hbSlot = getItemForName("Взрывная трапка", true);
            int invSlot = getItemForName("Взрывная трапка", false);

            if (invSlot == -1 && hbSlot == -1) {
                ChatUtility.addChatMessage("Взрывная трапка не найдена!");
                disorientationThrow = false;
                return;
            }

            if (!mc.player.getCooldownTracker().hasCooldown(Items.ENDER_EYE)) {
                ChatUtility.addChatMessage("Заюзал взрывную трапку!");
                int slot = findAndTrowItem(hbSlot, invSlot);
                if (slot > 8) {
                    mc.playerController.pickItem(slot);
                }
            }
            disorientationThrow = false;
        }
        if (plastThrow) {
            this.handUtil.handleItemChange(System.currentTimeMillis() - this.delay > 200L);
            int hbSlot = getItemForName("Стан", true);
            int invSlot = getItemForName("Стан", false);

            if (invSlot == -1 && hbSlot == -1) {
                ChatUtility.addChatMessage("Стан не найден!");
                plastThrow = false;
                return;
            }

            if (!mc.player.getCooldownTracker().hasCooldown(Items.DRIED_KELP)) {
                ChatUtility.addChatMessage("Заюзал стан!");
                int slot = findAndTrowItem(hbSlot, invSlot);
                if (slot > 8) {
                    mc.playerController.pickItem(slot);
                }
            }
            plastThrow = false;
        }
    }

    @EventTarget
    private void onPacket(EventPacket e) {
        this.handUtil.onEventPacket(e);
    }

    private int findAndTrowItem(int hbSlot, int invSlot) {
        if (hbSlot != -1) {
            this.handUtil.setOriginalSlot(mc.player.inventory.currentItem);
            mc.player.connection.sendPacket(new CHeldItemChangePacket(hbSlot));
            mc.player.connection.sendPacket(new CPlayerTryUseItemPacket(Hand.MAIN_HAND));
            mc.player.swingArm(Hand.MAIN_HAND);
            this.delay = System.currentTimeMillis();
            return hbSlot;
        }
        if (invSlot != -1) {
            handUtil.setOriginalSlot(mc.player.inventory.currentItem);
            mc.playerController.pickItem(invSlot);
            mc.player.connection.sendPacket(new CPlayerTryUseItemPacket(Hand.MAIN_HAND));
            mc.player.swingArm(Hand.MAIN_HAND);
            this.delay = System.currentTimeMillis();
            return invSlot;
        }
        return -1;
    }

    @Override
    public void onDisable() {
        disorientationThrow = false;
        trapThrow = false;
        snejokThrow = false;
        bojauraThrow = false;
        yavkaThrow = false;
        plastThrow = false;
        delay = 0;
        super.onDisable();
    }

    private int getItemForName(String name, boolean inHotBar) {
        int firstSlot = inHotBar ? 0 : 9;
        int lastSlot = inHotBar ? 9 : 36;
        for (int i = firstSlot; i < lastSlot; i++) {
            ItemStack itemStack = mc.player.inventory.getStackInSlot(i);

            if (itemStack.getItem() instanceof AirItem) {
                continue;
            }

            String displayName = TextFormatting.getTextWithoutFormattingCodes(itemStack.getDisplayName().getString());
            if (displayName != null && displayName.toLowerCase().contains(name)) {
                return i;
            }
        }
        return -1;
    }
}