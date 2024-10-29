package sertyo.events.event.misc;

import com.darkmagician6.eventapi.events.Event;
import com.mojang.blaze3d.matrix.MatrixStack;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.entity.Entity;

@Getter
@AllArgsConstructor
public final class EntityRenderMatrixEvent implements Event {
    private final MatrixStack matrix;
    private final Entity entity;
}
