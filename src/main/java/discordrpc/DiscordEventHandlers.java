package discordrpc;

import com.sun.jna.Structure;
import discordrpc.callbacks.DisconnectedCallback;
import discordrpc.callbacks.ErroredCallback;
import discordrpc.callbacks.JoinGameCallback;
import discordrpc.callbacks.JoinRequestCallback;
import discordrpc.callbacks.ReadyCallback;
import discordrpc.callbacks.SpectateGameCallback;
import java.util.Arrays;
import java.util.List;

public class DiscordEventHandlers extends Structure {
   public ReadyCallback ready;
   public DisconnectedCallback disconnected;
   public ErroredCallback errored;
   public JoinGameCallback joinGame;
   public SpectateGameCallback spectateGame;
   public JoinRequestCallback joinRequest;

   protected List<String> getFieldOrder() {
      return Arrays.asList("ready", "disconnected", "errored", "joinGame", "spectateGame", "joinRequest");
   }

   public static class Builder {
      private final DiscordEventHandlers handlers = new DiscordEventHandlers();

      public Builder ready(ReadyCallback readyCallback) {
         this.handlers.ready = readyCallback;
         return this;
      }

      public Builder disconnected(DisconnectedCallback disconnectedCallback) {
         this.handlers.disconnected = disconnectedCallback;
         return this;
      }

      public Builder errored(ErroredCallback erroredCallback) {
         this.handlers.errored = erroredCallback;
         return this;
      }

      public Builder joinGame(JoinGameCallback joinGameCallback) {
         this.handlers.joinGame = joinGameCallback;
         return this;
      }

      public Builder spectateGame(SpectateGameCallback spectateGameCallback) {
         this.handlers.spectateGame = spectateGameCallback;
         return this;
      }

      public Builder joinRequest(JoinRequestCallback joinRequestCallback) {
         this.handlers.joinRequest = joinRequestCallback;
         return this;
      }

      public DiscordEventHandlers build() {
         return this.handlers;
      }
   }
}
