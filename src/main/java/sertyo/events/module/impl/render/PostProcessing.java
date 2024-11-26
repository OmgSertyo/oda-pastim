package sertyo.events.module.impl.render;


import sertyo.events.module.Category;
import sertyo.events.module.Module;
import sertyo.events.module.ModuleAnnotation;
import sertyo.events.module.setting.impl.BooleanSetting;

@ModuleAnnotation(name = "PostProcessing", category = sertyo.events.module.Category.RENDER)
public class PostProcessing extends Module {

    public static BooleanSetting glow = new BooleanSetting("Подсветка", true);
    public static BooleanSetting blur = new BooleanSetting("Блюр", true);



}