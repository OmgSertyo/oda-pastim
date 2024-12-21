package sertyo.events.ui.autobuy.impl;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import net.minecraft.item.Item;
import sertyo.events.ui.autobuy.impl.fields.CustomField;

@RequiredArgsConstructor
@Data
@FieldDefaults(makeFinal = true)
public class TestItem {
    public final String name;
    public final Item item;
    public final boolean donItem;
    public CustomField maxPrice;
    public long maxPriceValue;


    public TestItem(String name, Item item, boolean donItem, long maxPriceValue) {
        this.name = name;
        this.item = item;
        this.donItem = donItem;
        this.maxPriceValue = maxPriceValue;
        this.maxPrice = new CustomField("Введите цену товара", "да да прям здесь", String.valueOf(maxPriceValue));

    }
}
