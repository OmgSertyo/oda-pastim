package org.luaj.vm2.customs.events;

import org.luaj.vm2.customs.EventHook;
import sertyo.events.event.render.EventRender2D;
import sertyo.events.event.render.EventRender3D;

public class EventRenderHook extends EventHook {

    private EventRender2D render2d;
    private EventRender3D render3d;

    public EventRenderHook(EventRender2D event) {
        super(event);
        this.render2d = event;
    }

    public EventRenderHook(EventRender3D event) {
        super(event);
        this.render3d = event;
    }

    public float getPartialTicks2D() {
        return render2d.getPartialTicks();
    }

    public float getPartialTicks3D() {
        return render3d.getPartialTicks();
    }

    @Override
    public String getName() {
        return "render_event";
    }
}
