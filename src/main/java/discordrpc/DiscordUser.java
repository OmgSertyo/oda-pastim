package discordrpc;

import com.sun.jna.Structure;
import java.util.Arrays;
import java.util.List;

public class DiscordUser extends Structure {
   public String userId;
   public String username;
   /** @deprecated */
   @Deprecated
   public String discriminator;
   public String avatar;

   protected List<String> getFieldOrder() {
      return Arrays.asList("userId", "username", "discriminator", "avatar");
   }
}
