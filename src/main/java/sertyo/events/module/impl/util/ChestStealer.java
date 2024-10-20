package sertyo.events.module.impl.util;

import com.darkmagician6.eventapi.EventTarget;
import com.google.common.eventbus.Subscribe;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import net.minecraft.client.gui.screen.inventory.ChestScreen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.ChestContainer;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.play.client.CClickWindowPacket;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import obf.sertyo.nativeobf.Native;
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

import java.util.*;

import static sertyo.events.utility.Utility.mc;
@Native
@ModuleAnnotation(name = "AutoMyst", category = Category.PLAYER)
public class ChestStealer extends Module {
    public long delay;


    @EventTarget
    private void onTickEvent(EventUpdate event) {
        if (mc.player.openContainer == null) return;

        if (mc.currentScreen != null && (mc.currentScreen.getTitle().equals(new TranslationTextComponent("container.enderchest")) || mc.currentScreen.getTitle().getString().contains("Аукцион") || mc.currentScreen.getTitle().getString().contains("Аукционы") || mc.currentScreen.getTitle().getString().contains("Покупка предмета"))) return;

        Container screen = mc.player.openContainer;

        if (screen instanceof ChestContainer containerScreen) {
            IInventory inventory = containerScreen.getLowerChestInventory();

            if (inventory.isEmpty()) {
                mc.player.closeScreen();
                return;
            }

            move(inventory);
        }
    }

    private void move(IInventory inventory) {
        List<Integer> slots = getInv(inventory);

        for (Integer slot : slots) {
            if (inventory.getStackInSlot(slot).getItem().equals(Items.TOTEM_OF_UNDYING) && totemCount() >= 4) continue;

            if (System.currentTimeMillis() >= delay) {
                mc.playerController.windowClick(mc.player.openContainer.windowId, slot, 0, ClickType.QUICK_MOVE, mc.player);
                delay = System.currentTimeMillis() + 100;
            }
        }
    }

    public List<Integer> getInv(IInventory inventory) {
        List<Integer> list = new ArrayList<>();

        List<Item> priority = new ArrayList<>();
        priority.add(Items.TRIPWIRE_HOOK);
        priority.add(Items.BLAZE_SPAWN_EGG);
        priority.add(Items.WITCH_SPAWN_EGG);
        priority.add(Items.CREEPER_SPAWN_EGG);
        priority.add(Items.PRISMARINE_CRYSTALS);
        priority.add(Items.TOTEM_OF_UNDYING);
        priority.add(Items.PLAYER_HEAD);
        priority.add(Items.PRISMARINE_SHARD);
        priority.add(Items.NETHER_STAR);

        for (Item item : priority) {
            list.addAll(getSlots(inventory, item));
        }

        return list;
    }

    private List<Integer> getSlots(IInventory in, Item item) {
        List<Integer> slots = new ArrayList<>();

        for (int i = 0; i < in.getSizeInventory(); i++) {
            ItemStack stack = in.getStackInSlot(i);
            if (stack.getItem().equals(item)) {
                slots.add(i);
            }
        }

        slots.sort(Comparator.comparing(i -> -getId(in.getStackInSlot(i))));

        return slots;
    }

    private long getId(ItemStack stack) {
        long id = 0L;

        for (char aByte : stack.getItem().getTranslationKey().toCharArray()) {
            id++;
        }

        for (ITextComponent text : stack.getTooltip(mc.player, ITooltipFlag.TooltipFlags.NORMAL)) {
            for (char aByte : text.getString().toCharArray()) {
                id++;
            }
        }

        return id;
    }

    private int totemCount() {
        int i = 0;
        for (int i1 = 0; i1 < mc.player.inventory.getSizeInventory(); i1++) {
            if (mc.player.inventory.getStackInSlot(i1).getItem().equals(Items.TOTEM_OF_UNDYING)) i++;
        }
        return i;
    }
}