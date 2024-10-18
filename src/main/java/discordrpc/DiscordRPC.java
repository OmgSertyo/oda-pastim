package discordrpc;

import com.sun.jna.Library;
import com.sun.jna.Native;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface DiscordRPC extends Library {
   DiscordRPC INSTANCE = (DiscordRPC)Native.loadLibrary("discord-rpc", DiscordRPC.class);

   void Discord_Initialize(@Nonnull String var1, @Nullable DiscordEventHandlers var2, boolean var3, @Nullable String var4);

   void Discord_Shutdown();

   void Discord_RunCallbacks();

   void Discord_UpdateConnection();

   void Discord_UpdatePresence(@Nullable DiscordRichPresence var1);

   void Discord_ClearPresence();

   void Discord_Respond(@Nonnull String var1, int var2);

   void Discord_UpdateHandlers(@Nullable DiscordEventHandlers var1);

   void Discord_Register(String var1, String var2);

   void Discord_RegisterSteamGame(String var1, String var2);

   public static enum DiscordReply {
      NO(0),
      YES(1),
      IGNORE(2);

      public final int reply;

      private DiscordReply(int reply) {
         this.reply = reply;
      }
   }
}
