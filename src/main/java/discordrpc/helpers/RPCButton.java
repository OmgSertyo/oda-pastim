package discordrpc.helpers;

import java.io.Serializable;
import javax.annotation.Nonnull;

public class RPCButton implements Serializable {
   private final String label;
   private final String url;

   protected RPCButton(String label, String url) {
      this.label = label;
      this.url = url;
   }

   public static RPCButton create(@Nonnull String label, @Nonnull String url) {
      if (label != null && !label.isEmpty() && url != null && !url.isEmpty()) {
         label = label.substring(0, Math.min(label.length(), 31));
         return new RPCButton(label, url);
      } else {
         throw new IllegalArgumentException("RPC Buttons require both a label and url");
      }
   }

   public String getLabel() {
      return this.label;
   }

   public String getUrl() {
      return this.url;
   }
}
