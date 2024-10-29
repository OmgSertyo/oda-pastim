package sertyo.events.utility;

import net.minecraft.item.AxeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class InventoryUtility implements Utility {
   public static boolean doesHotbarHaveAxe() {
      for(int i = 0; i < 9; ++i) {
         mc.player.inventory.getStackInSlot(i);
         if (mc.player.inventory.getStackInSlot(i).getItem() instanceof AxeItem) {
            return true;
         }
      }

      return false;
   }

   public static int getAxe() {
      for(int i = 0; i < 9; ++i) {
         ItemStack itemStack = mc.player.inventory.getStackInSlot(i);
         if (itemStack.getItem() instanceof AxeItem) {
            return i;
         }
      }

      return 1;
   }

   public static int getItemSlot(Item item, boolean isHotbar) {
      for(int i = 0; i < (isHotbar ? 9 : 45); ++i) {
         if (mc.player.inventory.getStackInSlot(i).getItem() == item) {
            return i;
         }
      }

      return -1;
   }

   public static int getItemSlot(Item input) {
      if (input == mc.player.getHeldItemOffhand().getItem()) {
         return -2;
      } else {
         for(int i = 36; i >= 0; --i) {
            Item item = mc.player.inventory.getStackInSlot(i).getItem();
            if (item == input) {
               if (i < 9) {
                  i += 36;
               }

               return i;
            }
         }

         return -1;
      }
   }
}
