package sertyo.events.utility.ab;

import com.jagrosh.discordipc.entities.Callback;

public class PrintStack {
    private static Runnable callback = null;
    private static Class clazz = null;

    public static void setCallback(Class clazz, Runnable callback) {
        if ((PrintStack.clazz != clazz || clazz == AutoBuyButton.class) && PrintStack.callback != null) {
            PrintStack.callback.run();
        }

        PrintStack.callback = callback;
        PrintStack.clazz = clazz;
    }
}