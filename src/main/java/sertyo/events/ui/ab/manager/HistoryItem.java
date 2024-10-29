package sertyo.events.ui.ab.manager;

import net.minecraft.item.ItemStack;

public class HistoryItem {
    public int price;
    public ItemStack stack;
    public boolean purchased = true;

    public HistoryItem(int price, ItemStack stack) {
        this.price = price;
        this.stack = stack;
    }
}