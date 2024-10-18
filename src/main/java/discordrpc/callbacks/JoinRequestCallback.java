package discordrpc.callbacks;

import com.sun.jna.Callback;
import discordrpc.DiscordUser;

public interface JoinRequestCallback extends Callback {
   void apply(DiscordUser var1);
}
