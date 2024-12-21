package sertyo.events.module;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface ModuleAnnotation {
   String name();
   String desc() default "1";
   Category category();
}
