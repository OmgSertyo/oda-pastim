package sertyo.events.ui.autobuy.impl;

import lombok.Getter;
import lombok.experimental.UtilityClass;
import net.minecraft.item.Item;
import net.minecraft.item.Items;

import java.util.ArrayList;
import java.util.Optional;

@UtilityClass
public class ItemManager {
    public ArrayList<TestItem> selectedItemsList = new ArrayList<>();

    public ArrayList<TestItem> itemList = new ArrayList<>();

    public void register() {
        itemList.clear();
        registerDonateItem("Шлем крушителя", Items.NETHERITE_HELMET, 500);
        registerDonateItem("Нагрудник крушителя", Items.NETHERITE_CHESTPLATE, 500);
        registerDonateItem("Поножи крушителя", Items.NETHERITE_LEGGINGS, 500);
        registerDonateItem("Ботинки крушителя", Items.NETHERITE_BOOTS, 500);
        registerDonateItem("Арбалет крушителя", Items.CROSSBOW, 500);
        registerDonateItem("Шалкеровый ящик", Items.SHULKER_BOX, 500);
    }
    

    public void registerDonateItem(String name, Item item, int maxPrice) {
        itemList.add(new TestItem(name, item, true, maxPrice));
    }


    public int getPrice(TestItem item) {
        return getPrice(item.name);
    }

    public int getPrice(String name) {
        Optional<TestItem> founded = itemList.stream().filter(e -> e.name.toLowerCase().equals(name.toLowerCase())).findFirst();
        return founded.map(testItem -> {
            String a = testItem.maxPrice.field.set.text;
            if (!a.isEmpty())
                return Integer.parseInt(testItem.maxPrice.field.set.text);
            else
                return 500;
        }).orElse(-1);
    }


    public void registerDefaultItem(Item item) {
        itemList.add(new TestItem(item.getDefaultInstance().getDisplayName().getString(), item, false, 100)); // ����� ������������ ������� ���� �� ���������
    }
}
