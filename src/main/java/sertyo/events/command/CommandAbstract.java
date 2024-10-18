package sertyo.events.command;

import sertyo.events.utility.misc.ChatUtility;

public abstract class CommandAbstract {
   public String name = ((Command)this.getClass().getAnnotation(Command.class)).name();
   public String description = ((Command)this.getClass().getAnnotation(Command.class)).description();

   public abstract void error();

   public abstract void execute(String[] var1) throws Exception;

   public void sendMessage(String message) {
      ChatUtility.addChatMessage(message);
   }
}
