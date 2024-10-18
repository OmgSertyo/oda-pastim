package sertyo.events.utility;

import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;

public interface Utility {
   Minecraft mc = Minecraft.getInstance();
   Tessellator tessellator = Tessellator.getInstance();
   MainWindow sr = mc.getMainWindow();
   BufferBuilder buffer = tessellator.getBuffer();

}
