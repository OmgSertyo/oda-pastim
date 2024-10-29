package sertyo.events.ui.ab;

import com.darkmagician6.eventapi.EventManager;
import com.darkmagician6.eventapi.EventTarget;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import net.minecraft.client.Minecraft;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class ActivationLogic {
    final Minecraft mc;
    @Getter
    @Setter
    public State currentState;

    public ActivationLogic() {
        this.currentState = State.INACTIVE;
        this.mc = Minecraft.getInstance();
        EventManager.register(this);
    }

    @EventTarget
    private void onUpdate(sertyo.events.event.player.EventUpdate e) {
        switch (currentState) {
            case ACTIVE:
                processActive();
                break;
            case INACTIVE:
                break;
        }
    }

    private void processActive() {
        currentState = State.INACTIVE;
            //AutoBuy.enabled = true;

    }

    public void toggleState() {
        currentState = (currentState == State.ACTIVE) ? State.INACTIVE : State.ACTIVE;
        AutoBuy.enabled = false;
    }

    public boolean isActive() {
        return currentState == State.ACTIVE;
    }

    public enum State {
        ACTIVE,
        INACTIVE
    }
}
