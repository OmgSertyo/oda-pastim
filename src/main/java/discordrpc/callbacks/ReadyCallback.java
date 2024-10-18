package discordrpc.callbacks;

import com.sun.jna.Callback;
import discordrpc.DiscordUser;

public interface ReadyCallback extends Callback {
   void apply(DiscordUser var1);
}
