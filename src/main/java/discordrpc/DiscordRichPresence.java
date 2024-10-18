package discordrpc;

import com.sun.jna.Structure;
import discordrpc.helpers.RPCButton;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class DiscordRichPresence extends Structure {
   public String state;
   public String details;
   public long startTimestamp;
   public long endTimestamp;
   public String largeImageKey;
   public String largeImageText;
   public String smallImageKey;
   public String smallImageText;
   public String partyId;
   public int partySize;
   public int partyMax;
   public String partyPrivacy;
   public String matchSecret;
   public String joinSecret;
   public String spectateSecret;
   public String button_label_1;
   public String button_url_1;
   public String button_label_2;
   public String button_url_2;
   public int instance;

   protected List<String> getFieldOrder() {
      return Arrays.asList("state", "details", "startTimestamp", "endTimestamp", "largeImageKey", "largeImageText", "smallImageKey", "smallImageText", "partyId", "partySize", "partyMax", "partyPrivacy", "matchSecret", "joinSecret", "spectateSecret", "button_label_1", "button_url_1", "button_label_2", "button_url_2", "instance");
   }

   public DiscordRichPresence() {
      this.setStringEncoding("UTF-8");
   }

   public static class Builder {
      private final DiscordRichPresence rpc = new DiscordRichPresence();

      public Builder setState(String state) {
         if (state != null && !state.isEmpty()) {
            this.rpc.state = state.substring(0, Math.min(state.length(), 128));
         }

         return this;
      }

      public Builder setDetails(String details) {
         if (details != null && !details.isEmpty()) {
            this.rpc.details = details.substring(0, Math.min(details.length(), 128));
         }

         return this;
      }

      public Builder setStartTimestamp(long timestamp) {
         this.rpc.startTimestamp = timestamp;
         return this;
      }

      public Builder setStartTimestamp(OffsetDateTime timestamp) {
         this.rpc.startTimestamp = timestamp.toEpochSecond();
         return this;
      }

      public Builder setEndTimestamp(long timestamp) {
         this.rpc.endTimestamp = timestamp;
         return this;
      }

      public Builder setEndTimestamp(OffsetDateTime timestamp) {
         this.rpc.endTimestamp = timestamp.toEpochSecond();
         return this;
      }

      public Builder setLargeImage(String key) {
         return this.setLargeImage(key, "");
      }

      public Builder setLargeImage(String key, String text) {
         if (text != null && !text.isEmpty() && key != null) {
            throw new IllegalArgumentException("Image key cannot be null when assigning a hover text");
         } else {
            this.rpc.largeImageKey = key;
            this.rpc.largeImageText = text;
            return this;
         }
      }

      public Builder setSmallImage(String key) {
         return this.setSmallImage(key, "");
      }

      public Builder setSmallImage(String key, String text) {
         this.rpc.smallImageKey = key;
         this.rpc.smallImageText = text;
         return this;
      }

      public Builder setParty(String party, int size, int max) {
         if ((this.rpc.button_label_1 == null || !this.rpc.button_label_1.isEmpty()) && (this.rpc.button_label_2 == null || !this.rpc.button_label_2.isEmpty())) {
            this.rpc.partyId = party;
            this.rpc.partySize = size;
            this.rpc.partyMax = max;
            return this;
         } else {
            return this;
         }
      }

      public Builder setSecrets(String match, String join, String spectate) {
         if ((this.rpc.button_label_1 == null || !this.rpc.button_label_1.isEmpty()) && (this.rpc.button_label_2 == null || !this.rpc.button_label_2.isEmpty())) {
            this.rpc.matchSecret = match;
            this.rpc.joinSecret = join;
            this.rpc.spectateSecret = spectate;
            return this;
         } else {
            return this;
         }
      }

      public Builder setSecrets(String join, String spectate) {
         if ((this.rpc.button_label_1 == null || !this.rpc.button_label_1.isEmpty()) && (this.rpc.button_label_2 == null || !this.rpc.button_label_2.isEmpty())) {
            this.rpc.joinSecret = join;
            this.rpc.spectateSecret = spectate;
            return this;
         } else {
            return this;
         }
      }

      public Builder setInstance(boolean i) {
         if ((this.rpc.button_label_1 == null || !this.rpc.button_label_1.isEmpty()) && (this.rpc.button_label_2 == null || !this.rpc.button_label_2.isEmpty())) {
            this.rpc.instance = i ? 1 : 0;
            return this;
         } else {
            return this;
         }
      }

      public Builder setButtons(RPCButton button) {
         return this.setButtons(Collections.singletonList(button));
      }

      public Builder setButtons(RPCButton button1, RPCButton button2) {
         return this.setButtons(Arrays.asList(button1, button2));
      }

      public Builder setButtons(List<RPCButton> rpcButtons) {
         if (rpcButtons != null && !rpcButtons.isEmpty()) {
            int length = Math.min(rpcButtons.size(), 2);
            this.rpc.button_label_1 = ((RPCButton)rpcButtons.get(0)).getLabel();
            this.rpc.button_url_1 = ((RPCButton)rpcButtons.get(0)).getUrl();
            if (length == 2) {
               this.rpc.button_label_2 = ((RPCButton)rpcButtons.get(1)).getLabel();
               this.rpc.button_url_2 = ((RPCButton)rpcButtons.get(1)).getUrl();
            }
         }

         return this;
      }

      public DiscordRichPresence build() {
         return this.rpc;
      }
   }
}
