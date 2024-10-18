package sertyo.events.module.impl.util.autobuyhw.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.awt.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import sertyo.events.utility.render.RenderUtil;

public class CustomButton extends Button {
  private Item item;

  private final String additionalName;

  public CustomButton(int x, int y, int width, int height, String text, Item item, String additionalName, Button.IPressable onPress) {
    super(x, y, width, height, new StringTextComponent(text), onPress);
    this.item = item;
    this.additionalName = additionalName;
  }

  @Override
  public void renderButton(MatrixStack m, int mouseX, int mouseY, float partialTicks) {
    if (this.item == Items.PLAYER_HEAD) {
      Minecraft.getInstance().getItemRenderer().renderItemIntoGUI(add("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZGM5MzY1NjQyYzZlZGRjZmVkZjViNWUxNGUyYmM3MTI1N2Q5ZTRhMzM2M2QxMjNjNmYzM2M1NWNhZmJmNmQifX19", "Custom Head"), this.x + 2, this.y + 2);
    } else if (this.item == Items.PLAYER_HEAD) {
      Minecraft.getInstance().getItemRenderer().renderItemIntoGUI(add("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZmFmZjJlYjQ5OGU1YzZhMDQ0ODRmMGM5Zjc4NWI0NDg0NzlhYjIxM2RmOTVlYzkxMTc2YTMwOGExMmFkZDcwIn19fQ==", "Custom Head 2"), this.x + 2, this.y + 2);
    } else if (this.item == Items.PLAYER_HEAD) {
      Minecraft.getInstance().getItemRenderer().renderItemIntoGUI(add("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzc0MDBlYTE5ZGJkODRmNzVjMzlhZDY4MjNhYzRlZjc4NmYzOWY0OGZjNmY4NDYwMjM2NmFjMjliODM3NDIyIn19fQ==", "Custom Head 3"), this.x + 2, this.y + 2);
    } else if (this.item == Items.PLAYER_HEAD) {
      Minecraft.getInstance().getItemRenderer().renderItemIntoGUI(add("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZWE2MmI5ZGU2YTI2Yjg2ODY5Y2EyMmVhNDBmMWJkZTgwYTA0MzBhNTQ1NDdiZWNjZThmZGE4NzA3Nzc3MjU4ZiJ9fX0=", "Custom Head 4"), this.x + 2, this.y + 2);
    } else if (this.item == Items.PLAYER_HEAD) {
      Minecraft.getInstance().getItemRenderer().renderItemIntoGUI(add("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjA5NWE3ZmQ5MGRhYTFiYmU3MDY5MDg5NzQwZTA1ZDBiZmM2NjI5NmVlM2M0MGVlNzFhNGUwYTY2MTZiMmJiYyJ9fX0=", "Custom Head 5"), this.x + 2, this.y + 2);
    } else {
      Minecraft.getInstance().getItemRenderer().renderItemIntoGUI(Items.BARRIER.getDefaultInstance(), -40000, this.y + 2);
      Minecraft.getInstance().getItemRenderer().renderItemIntoGUI(this.item.getDefaultInstance(), this.x + 2, this.y + 2);
    }
    RenderUtil.Render2D.drawRoundedRect((float) this.x, (float) (this.y + 20), 20.0F, 20.0F, 1.0F, new Color(20, 20, 20).getRGB());
  }

  @Override
  public boolean mouseClicked(double mouseX, double mouseY, int button) {
    return AutoBuyUI.hovered(mouseX, mouseY, this.x, this.y + 20, this.width, this.height);
  }

  public String getAdditionalName() {
    return this.additionalName;
  }

  public static ItemStack add(String texture, String name) {
    try {
      ItemStack itemStack = new ItemStack(Items.PLAYER_HEAD);
      itemStack.setTag(JsonToNBT.getTagFromJson(String.format("{SkullOwner:{Id:[I;-1949909288,1299464445,-1707774066,-249984712],Properties:{textures:[{Value:\"%s\"}]},Name:\"%s\"}}", texture, name)));
      itemStack.setDisplayName(new StringTextComponent(name));
      return itemStack;
    } catch (CommandSyntaxException e) {
      throw new RuntimeException(e);
    }
  }
}
