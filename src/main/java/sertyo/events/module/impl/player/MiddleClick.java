package sertyo.events.module.impl.player;

import com.darkmagician6.eventapi.EventTarget;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Items;
import net.minecraft.network.play.client.CHeldItemChangePacket;
import net.minecraft.network.play.client.CPlayerTryUseItemPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.text.TextFormatting;
import sertyo.events.Main;
import sertyo.events.event.input.EventMouse;
import sertyo.events.module.Category;
import sertyo.events.module.Module;
import sertyo.events.module.ModuleAnnotation;
import sertyo.events.module.setting.impl.BooleanSetting;
import sertyo.events.utility.misc.InventoryUtil;
import sertyo.events.utility.Utility;
import sertyo.events.utility.misc.ChatUtility;


@ModuleAnnotation(
   name = "MiddleClick",
   category = Category.PLAYER
)
public class MiddleClick extends Module {
   public BooleanSetting pearl = new BooleanSetting("Pearl", true);
   public BooleanSetting friend = new BooleanSetting("Friend", false);

   @EventTarget
   public void onMouse(EventMouse event) {
      if (this.pearl.get() && event.getButton() == 2 && !Utility.mc.player.getCooldownTracker().hasCooldown(Items.ENDER_PEARL)) {
         int hotbarPearls = InventoryUtil.getItemSlot(Items.ENDER_PEARL, true);
         if (hotbarPearls != -1) {
            Utility.mc.player.connection.sendPacket(new CHeldItemChangePacket(hotbarPearls));
            Utility.mc.player.connection.sendPacket(new CPlayerTryUseItemPacket(Hand.MAIN_HAND));
            Utility.mc.player.connection.sendPacket(new CHeldItemChangePacket(Utility.mc.player.inventory.currentItem));
         } else {
            int invPearls = InventoryUtil.getItemSlot(Items.ENDER_PEARL, false);
            if (invPearls != -1) {
               Utility.mc.playerController.pickItem(invPearls);
               Utility.mc.player.connection.sendPacket(new CPlayerTryUseItemPacket(Hand.MAIN_HAND));
               Utility.mc.player.connection.sendPacket(new CHeldItemChangePacket(Utility.mc.player.inventory.currentItem));
               Utility.mc.playerController.pickItem(invPearls);
            }
         }
      }
      //ENTITYPLAYER == PLAYERENTITY
      if (this.friend.get() && event.getButton() == 2 && Utility.mc.pointedEntity instanceof LivingEntity) {
         String name = Utility.mc.pointedEntity.getName().getString();
         if (Main.getInstance().getFriendManager().isFriend(name)) {
            Main.getInstance().getFriendManager().removeFriend(name);
            ChatUtility.addChatMessage(TextFormatting.RED + name + TextFormatting.GRAY + " успешно удален из списка друзей." + TextFormatting.RESET);
         } else {
            Main.getInstance().getFriendManager().addFriend(name);
            ChatUtility.addChatMessage(TextFormatting.RED + name + TextFormatting.GRAY + " успешно добавлен в друзья!" + TextFormatting.RESET);
         }
      }

   }
}
