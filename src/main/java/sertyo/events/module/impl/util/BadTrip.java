package sertyo.events.module.impl.util;


import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.vector.Vector3f;
import sertyo.events.event.misc.EntityRenderMatrixEvent;
import sertyo.events.event.player.EventUpdate;
import sertyo.events.module.Category;
import sertyo.events.module.Module;
import sertyo.events.module.ModuleAnnotation;
import sertyo.events.module.setting.impl.NumberSetting;
import sertyo.events.utility.math.MathUtility;
import sertyo.events.utility.misc.SoundUtil;

@ModuleAnnotation(name = "BadTrip", category = Category.UTIL)
public class BadTrip extends sertyo.events.module.Module {

    private final NumberSetting wobbleMusicVolume = new NumberSetting("Громкость", .3F, .05F, 1.F, .025F);
    private final String roflMusic = "kraken";
    private final SoundUtil.AudioClipPlayController wobbleMusicTuner = SoundUtil.AudioClipPlayController.build(SoundUtil.AudioClip.build(roflMusic + ".wav", true), () -> isEnabled(), true);
    @EventTarget
    private void onrender(EntityRenderMatrixEvent event) {
        if (isEnabled()) {
            if (event.getEntity() instanceof PlayerEntity) {
                float wobble = (event.getEntity().getEntityId() * 100 % 400) / 400F;
                wobble = (wobble > .5 ? 1 - wobble : wobble) * 2F;
                wobble = MathUtility.clamp01(wobble);
                event.getMatrix().scale(wobble * 2F + 1, 1 - .5f * wobble, wobble * 2F + 1);
            }
        }
    }
    @EventTarget
    public void onupdate(EventUpdate event)
    {
        updateMusic();
    }

    @Override
    public void onEnable() {
        updateMusic();
        super.onEnable();
    }

    private void updateMusic() {
        wobbleMusicTuner.updatePlayingStatus();
        if (wobbleMusicTuner.isSucessPlaying())
            wobbleMusicTuner.getAudioClip().setVolume(wobbleMusicVolume.get());
    }

    @Override
    public void onDisable() {

        wobbleMusicTuner.updatePlayingStatus();
        super.onEnable();
    }
}
