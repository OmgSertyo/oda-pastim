//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package sertyo.events.manager.staff;

import net.minecraft.client.network.play.NetworkPlayerInfo;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.GameType;
import sertyo.events.utility.Utility;


public class Staff implements Utility  {
   private String name;
   private String prefix;
   private boolean isVanished;

   public String getStaffPrefix() {
      PlayerEntity player = mc.world.getPlayerEntityByName(this.name);
      if (player != null) {
         return TextFormatting.YELLOW + "Near" + TextFormatting.RESET;
      } else {
         NetworkPlayerInfo networkPlayerInfo = mc.player.connection.getPlayerInfo(this.name);
         if (this.isVanished) {
            return "Spec";
         } else {
            return networkPlayerInfo != null && networkPlayerInfo.getGameType().equals(GameType.SPECTATOR) ? TextFormatting.GOLD + "GM3" + TextFormatting.RESET : "";
         }
      }
   }

   public String getText() {
      return this.name;
   }

   public String getName() {
      return this.name;
   }

   public String getPrefix() {
      return this.prefix;
   }

   public boolean isVanished() {
      return this.isVanished;
   }

   public Staff(String name, String prefix, boolean isVanished) {
      this.name = name;
      this.prefix = prefix;
      this.isVanished = isVanished;
   }
}
