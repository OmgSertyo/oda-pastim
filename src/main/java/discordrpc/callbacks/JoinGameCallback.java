package discordrpc.callbacks;

import com.sun.jna.Callback;

public interface JoinGameCallback extends Callback {
   void apply(String var1);
}