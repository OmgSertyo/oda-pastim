package sertyo.events.module.impl.util;

import com.darkmagician6.eventapi.EventTarget;
import com.google.common.eventbus.Subscribe;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import net.minecraft.client.gui.screen.inventory.ChestScreen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.ChestContainer;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.play.client.CClickWindowPacket;
import org.apache.commons.lang3.time.StopWatch;
import sertyo.events.event.player.EventUpdate;
import sertyo.events.module.Category;
import sertyo.events.module.Module;
import sertyo.events.module.ModuleAnnotation;
import sertyo.events.module.setting.impl.BooleanSetting;
import sertyo.events.module.setting.impl.ModeSetting;
import sertyo.events.module.setting.impl.NumberSetting;
import sertyo.events.utility.misc.PlayerUtil;
import sertyo.events.utility.misc.TimerHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import static sertyo.events.utility.Utility.mc;

@ModuleAnnotation(name = "ChestStealer", category = Category.PLAYER)
public class ChestStealer extends Module {
    private NumberSetting delay = new NumberSetting("Delay", 30, 10, 150, 1);
    private BooleanSetting autoLeave = new BooleanSetting("Ливать есть сфера или тал", true);

    private final A timer = new A();
    private final A timerUtil = new A();

    boolean hasMessageSent;

    @EventTarget
    public void onUpdate(EventUpdate event) {
        if (hasEnchantedTotemOrPlayerHead()) {
            if (!PlayerUtil.isPvp()) {
                if (!hasMessageSent) {
                    mc.player.sendChatMessage("/darena");
                    hasMessageSent = true;
                }
                if (mc.player.openContainer instanceof ChestContainer containere) {
                    if (timerUtil.hasTimeElapsed(250L)) {
                        for (int i = 0; i < containere.inventorySlots.size(); i++) {
                            ItemStack itemStack = containere.getSlot(i).getStack();
                            if (!itemStack.isEmpty() && itemStack.getTranslationKey().endsWith("pufferfish")) {
                                mc.player.connection.sendPacket(new CClickWindowPacket(containere.windowId, i, 0, ClickType.QUICK_MOVE, itemStack, (short) 0
                                ));
                                toggle();
                                hasMessageSent = false;
                            }
                        }
                        timerUtil.reset();
                    }
                }
            }
        } else {
            toggle();
        }
        if (mc.player.openContainer instanceof ChestContainer) {
            ChestContainer container = (ChestContainer) mc.player.openContainer;

            for (int index = 0; index < container.inventorySlots.size(); ++index) {
                if (container.getLowerChestInventory().getStackInSlot(index).getItem() != Item.getItemById(0)
                        && timer.hasTimeElapsed(35L)) {


                        mc.playerController.windowClick(container.windowId, index, 0, ClickType.QUICK_MOVE, mc.player);
                        timer.reset();
                        continue;

                    }
                }
            }
    }



    private boolean hasEnchantedTotemOrPlayerHead() {
        // Проходим по всем слотам инвентаря игрока
        for (int i = 0; i < mc.player.inventory.mainInventory.size(); i++) {
            ItemStack stack = mc.player.inventory.mainInventory.get(i);

            // Проверяем, является ли предмет зачарованным тотемом
            if (stack.hasDisplayName()) {
                String displayName = stack.getDisplayName().getString().toLowerCase(Locale.ROOT);

                // Проверяем, содержит ли название "талисман" или "сфера"
                if (displayName.contains("талисман") || displayName.contains("сфера")) {
                    return true; // Найден предмет с нужным названием
                }
            }
        }
        return false; // Ничего не найдено
    }

    private List<ItemStack> getEmptyOrUnnecessaryStacks(ChestScreen container) {
        List<ItemStack> emptyOrUnnecessaryStacks = new ArrayList<>();
        for (int i = 0; i < container.getContainer().inventorySlots.size(); ++i) {
            if (container.getContainer().getLowerChestInventory().getStackInSlot(i).isEmpty())
                emptyOrUnnecessaryStacks.add(container.getContainer().getLowerChestInventory().getStackInSlot(i));
        }

        return emptyOrUnnecessaryStacks;
    }
}