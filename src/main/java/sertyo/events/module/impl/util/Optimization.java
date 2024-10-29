package sertyo.events.module.impl.util;

import sertyo.events.module.Category;
import sertyo.events.module.Module;
import sertyo.events.module.ModuleAnnotation;
import sertyo.events.module.setting.impl.MultiBooleanSetting;

import java.util.Arrays;
@ModuleAnnotation(name = "Optimization", category = Category.UTIL)
public class Optimization extends Module {
    public static final MultiBooleanSetting elements = new MultiBooleanSetting("Elements", Arrays.asList("Blur"));

}
