//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package sertyo.events.module.impl.render;

import net.minecraft.client.network.play.NetworkPlayerInfo;
import net.minecraft.util.text.TextFormatting;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import net.minecraft.scoreboard.ScorePlayerTeam;
import sertyo.events.Main;
import sertyo.events.manager.dragging.Draggable;
import sertyo.events.manager.staff.Staff;
import sertyo.events.utility.Utility;
import sertyo.events.utility.render.ColorUtility;
import sertyo.events.utility.render.RenderUtil;
import sertyo.events.utility.render.RenderUtility;
import sertyo.events.utility.render.animation.AnimationMath;
import sertyo.events.utility.render.fonts.Fonts;

public class StaffList implements Utility {
   private static List<Staff> allStaff = new ArrayList();
   public static float realOffset = 0.0F;

   public StaffList() {
   }

   public static void render() {
      Main.getInstance().getScaleMath().pushScale();
      List<Staff> sortedStaff = (List)allStaff.stream().sorted(Comparator.comparing((staffx) -> {
         return Fonts.mntssb15.getStringWidth(staffx.getText());
      }, Comparator.reverseOrder())).collect(Collectors.toList());
      Hud hud = (Hud) Main.getInstance().getModuleManager().getModule(Hud.class);
      Draggable staffListDraggable = hud.staffListDraggable;
      int width = 105;
      int offset = -1;
      if (!sortedStaff.isEmpty()) {
         width = (Integer)sortedStaff.stream().max(Comparator.comparingInt((staffx) -> {
            return Fonts.mntssb15.getStringWidth(staffx.getText());
         })).map((staffx) -> {
            return Fonts.mntssb15.getStringWidth(staffx.getText());
         }).get() + 11;
         if (width < 105) {
            width = 105;
         }

         offset = 0;

         for(Iterator var5 = sortedStaff.iterator(); var5.hasNext(); offset += 11) {
            Staff staff = (Staff)var5.next();
         }
      }

      realOffset = AnimationMath.fast(realOffset, (float)offset, 15.0F);
      staffListDraggable.setWidth((float)width);
      staffListDraggable.setHeight((float)(19 + offset));
      int bgColor = Color.decode("#1B1C2C").getRGB();
      int elementsColor = Color.decode("#1E1F30").getRGB();
      int strokeColor = Color.decode("#26273B").getRGB();
      RenderUtility.drawGlow((float)(staffListDraggable.getX() - 1), (float)(staffListDraggable.getY() - 1), (float)(width + 2), 21.0F + realOffset, 10, new Color(bgColor).getRGB());
      RenderUtility.drawRoundedRect((float)(staffListDraggable.getX() - 1), (float)(staffListDraggable.getY() - 1), (float)(width + 2), 21.0F + realOffset, 11.0F, strokeColor);
      RenderUtility.drawRoundedRect((float)staffListDraggable.getX(), (float)staffListDraggable.getY(), (float)width, 19.0F + realOffset, 10.0F, elementsColor);
      RenderUtility.drawGradientGlow((float)(staffListDraggable.getX() + width - 2 - Fonts.icons21.getStringWidth("o")), (float)staffListDraggable.getY() + 6.5F, (float)(Fonts.icons21.getStringWidth("p") - 3), (float)(Fonts.icons21.getFontHeight() - 1), 8, ColorUtility.applyOpacity2(Main.getInstance().getThemeManager().getCurrentStyleTheme().getColors()[0], 0.4F), ColorUtility.applyOpacity2(Main.getInstance().getThemeManager().getCurrentStyleTheme().getColors()[0], 0.4F), ColorUtility.applyOpacity2(Main.getInstance().getThemeManager().getCurrentStyleTheme().getColors()[1], 0.4F), ColorUtility.applyOpacity2(Main.getInstance().getThemeManager().getCurrentStyleTheme().getColors()[1], 0.4F));
      int finalWidth = width;
      RenderUtility.applyGradientMask((float)(staffListDraggable.getX() + width - 5 - Fonts.icons21.getStringWidth("p")), (float)staffListDraggable.getY() + 6.5F, (float)Fonts.icons21.getStringWidth("o"), (float)Fonts.icons21.getFontHeight(), 0.5F, Main.getInstance().getThemeManager().getCurrentStyleTheme().getColors()[0], Main.getInstance().getThemeManager().getCurrentStyleTheme().getColors()[1], Main.getInstance().getThemeManager().getCurrentStyleTheme().getColors()[0], Main.getInstance().getThemeManager().getCurrentStyleTheme().getColors()[1], () -> {
         Fonts.icons21.drawString("p", (float)(staffListDraggable.getX() + finalWidth - 5 - Fonts.icons21.getStringWidth("p")), (float)staffListDraggable.getY() + 6.5F, -1);
      });
      Fonts.mntssb16.drawString("Staff", (float)(staffListDraggable.getX() + 5), (float)staffListDraggable.getY() + 6.5F, -1);
      if (!sortedStaff.isEmpty()) {
         RenderUtility.Render2D.drawRect((float)staffListDraggable.getX(), (float)(staffListDraggable.getY() + 16), (float)width, 1.0F, strokeColor);
      }

      RenderUtil.SmartScissor.push();
      RenderUtil.SmartScissor.setFromComponentCoordinates((double)staffListDraggable.getX(), (double)staffListDraggable.getY(), (double)width, (double)(19.0F + realOffset));
      offset = 0;

      for(Iterator var9 = sortedStaff.iterator(); var9.hasNext(); offset += 11) {
         Staff staff = (Staff)var9.next();
         if (Fonts.mntssb15.getStringWidth(staff.getText()) > 98 && staff.getStaffPrefix().length() > 2) {
            Fonts.mntssb15.drawString(staff.getText(), (float)(staffListDraggable.getX() + 5), (float)(staffListDraggable.getY() + 22 + offset), -1);
            RenderUtility.drawShadow((float)(staffListDraggable.getX() + 90), (float)(staffListDraggable.getY() + 17 + offset), (float)(Fonts.mntssb15.getStringWidth(staff.getText()) - 10), 13.0F, 5, new Color(bgColor).getRGB(), new Color(elementsColor).getRGB());
         } else {
            Fonts.mntssb15.drawString(staff.getText(), (float)(staffListDraggable.getX() + 5), (float)(staffListDraggable.getY() + 22 + offset), -1);
         }

         if (staff.getStaffPrefix().equals("Spec")) {
            Fonts.icons16.drawString(TextFormatting.RED + "q", (float)(staffListDraggable.getX() + width - 5 - Fonts.icons21.getStringWidth("q")), (float)staffListDraggable.getY() + 22.5F + (float)offset, Color.white.getRGB());
         } else {
            Fonts.mntssb15.drawString(staff.getStaffPrefix(), (float)(staffListDraggable.getX() + width - 5 - Fonts.mntssb15.getStringWidth(staff.getStaffPrefix())), (float)(staffListDraggable.getY() + 22 + offset), -1);
         }
      }

      RenderUtil.SmartScissor.unset();
      RenderUtil.SmartScissor.pop();
      Main.getInstance().getScaleMath().popScale();
   }

   public static void updateList() {
      allStaff = getOnlineStaff();
      allStaff.addAll(getVanishedStaff());
   }

   public static boolean isStaff(String prefix) {
      return prefix.contains("helper") || prefix.contains("moder") || prefix.contains("admin") || prefix.contains("owner") || prefix.contains("curator") || prefix.contains("хелпер") || prefix.contains("модер") || prefix.contains("админ") || prefix.contains("стажёр") || prefix.contains("стажер") || prefix.contains("сотрудник") || prefix.contains("помощник") || prefix.contains("youtube") || prefix.equals("yt");
   }

   private static ArrayList<Staff> getOnlineStaff() {
      if (mc.player == null) {
         return new ArrayList();
      } else {
         ArrayList<Staff> list = new ArrayList();
         Iterator var1 = mc.player.connection.getPlayerInfoMap().iterator();

         while(true) {
            NetworkPlayerInfo networkPlayerInfo;
            ScorePlayerTeam scorePlayerTeam;
            do {
               do {
                  if (!var1.hasNext()) {
                     return list;
                  }

                  networkPlayerInfo = (NetworkPlayerInfo)var1.next();
                  scorePlayerTeam = networkPlayerInfo.getPlayerTeam();
               } while(scorePlayerTeam == null);
            } while(!isStaff(TextFormatting.getTextWithoutFormattingCodes(scorePlayerTeam.getPrefix().getString()).toLowerCase()) && !Main.getInstance().getStaffManager().getStaff().contains(networkPlayerInfo.getGameProfile().getName()));

            list.add(new Staff(networkPlayerInfo.getGameProfile().getName(), scorePlayerTeam.getPrefix().getString(), false));
         }
      }
   }

   public static List<Staff> getVanishedStaff() {
      if (mc.world == null) {
         return new ArrayList();
      } else {
         List<Staff> list = new ArrayList();
         Iterator var1 = mc.world.getScoreboard().getTeams().iterator();

         while(true) {
            ScorePlayerTeam scorePlayerTeam;
            String username;
            do {
               do {
                  do {
                     do {
                        if (!var1.hasNext()) {
                           return list;
                        }

                        scorePlayerTeam = (ScorePlayerTeam)var1.next();
                     } while(TextFormatting.getTextWithoutFormattingCodes(scorePlayerTeam.getPrefix().getString()).length() == 0);

                     username = Arrays.toString(scorePlayerTeam.getMembershipCollection().toArray()).replace("[", "").replace("]", "");
                  } while(TextFormatting.getTextWithoutFormattingCodes(username).isEmpty());
               } while(((List)getOnlineStaff().stream().map(Staff::getName).collect(Collectors.toList())).contains(username));
            } while(!isStaff(TextFormatting.getTextWithoutFormattingCodes(scorePlayerTeam.getPrefix().getString()).toLowerCase()) && !Main.getInstance().getStaffManager().getStaff().contains(username));

            list.add(new Staff(username, scorePlayerTeam.getPrefix().getString(), true));
         }
      }
   }
}
