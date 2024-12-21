package sertyo.events.utility.render;

import com.mojang.blaze3d.systems.IRenderCall;
import sertyo.events.Main;
import sertyo.events.module.impl.render.PostProcessing;
import sertyo.events.utility.Utility;


import java.util.LinkedList;
import java.util.Queue;

public interface IWrapper extends Utility {

    Queue<IRenderCall> blurQueue = new LinkedList<>();
    Queue<IRenderCall> bloomQueue = new LinkedList<>();

    static void executeQueue(boolean blur, boolean bloom) {
        if (!blur) {
            blurQueue.clear();
        } else if (Main.getInstance().getModuleManager().getModule(PostProcessing.class).isEnabled() && PostProcessing.blur.get()) {

        }
        if (!bloom) {
            bloomQueue.clear();
        } else if (Main.getInstance().getModuleManager().getModule(PostProcessing.class).isEnabled() && PostProcessing.glow.get()) {

        }
    }
}