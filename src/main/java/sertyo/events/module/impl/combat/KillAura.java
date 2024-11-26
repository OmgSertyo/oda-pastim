package sertyo.events.module.impl.combat;
import com.darkmagician6.eventapi.EventTarget;
import lombok.Getter;
import net.minecraft.client.settings.BooleanOption;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.item.ArmorStandEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.play.client.CEntityActionPacket;
import net.minecraft.network.play.client.CHeldItemChangePacket;
import net.minecraft.potion.Effects;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector2f;
import net.minecraft.util.math.vector.Vector3d;
import sertyo.events.Main;
import sertyo.events.event.player.EventMotion;
import sertyo.events.event.player.EventUpdate;
import sertyo.events.event.render.EventRender2D;
import sertyo.events.module.Category;
import sertyo.events.module.Module;
import sertyo.events.module.ModuleAnnotation;
import sertyo.events.module.impl.util.A;
import sertyo.events.module.setting.impl.BooleanSetting;
import sertyo.events.module.setting.impl.ModeSetting;
import sertyo.events.module.setting.impl.MultiBooleanSetting;
import sertyo.events.module.setting.impl.NumberSetting;
import sertyo.events.utility.math.AuraUtil;
import sertyo.events.utility.math.GCDUtil;
import sertyo.events.utility.math.RayTraceUtil;
import sertyo.events.utility.misc.InventoryUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import static java.lang.Math.*;

@SuppressWarnings("all")
@ModuleAnnotation(name = "Aura", category = Category.COMBAT)
public class KillAura extends Module {
    @Getter
    public static LivingEntity target = null;

    private long lerpStartTime = 0;
    private float rotationSpeed = 0.7f;//0.7f
    A timer = new A();

    public Vector2f rotate = new Vector2f(0, 0);

    private final ModeSetting rotationMode = new ModeSetting("Мод ротации", "Обычная", "Обычная", "Снапы", "Funtime");

    private final ModeSetting sortMode = new ModeSetting("Сортировать",
            "По всему",
            "По всему", "По здоровью", "По дистанции"
    );

    private final MultiBooleanSetting targets = new MultiBooleanSetting("Цели", Arrays.asList(
            "Игроки",
            "Друзья",
            "Голые",
            "Мобы"
    ));

    private final NumberSetting distance = new NumberSetting("Дистанция аттаки", 3.0f, 2.0f, 5.0f, 0.05f);
    private final NumberSetting rotateDistance = new NumberSetting("Дистанция ротации", 1.5f, 0.0f, 3.0f, 0.05f);

    public final MultiBooleanSetting settings = new MultiBooleanSetting("Настройки", Arrays.asList(
            "Только критами",
            "Коррекция движения",
            "Отжимать щит",
           "Ломать щит",
            "Таргет ЕСП",
            "Останавливать спринт"
    ));


    private final BooleanSetting onlySpaceCritical = new BooleanSetting("Только с пробелом", false);

    private final BooleanSetting lerpRot = new BooleanSetting("Плавная наводка", true);

    private final BooleanSetting silent = new BooleanSetting("Сайлент коррекция", true);

    private final BooleanSetting cooldown = new BooleanSetting("Кулдаун аттаки", true);


    float ticksUntilNextAttack;
    public boolean hasRotated;
    private long cpsLimit = 0;


    public float al = distance.get();
    public float alr = rotateDistance.get();

    /*@Override
    public void onEvent(final Event event) {


        if (event instanceof EventInteractEntity entity) {
            if (target != null)
                entity.setCancel(true);
        }
        if (event instanceof EventInput eventInput) {
            if (settings.get(1) && silent.get()) {
                MoveUtil.fixMovement(eventInput, Managment.FUNCTION_MANAGER.autoPotionFunction.isActivePotion ? Minecraft.getInstance().player.rotationYaw : rotate.x);
            }
        }
    }*/

    @EventTarget
    public void onUpdate(EventUpdate motionEvent) {
        if (!(target != null && isValidTarget(target))) {
            target = findTarget();
        }
        if (target == null) {
            cpsLimit = System.currentTimeMillis();
            rotate = new Vector2f(mc.player.rotationYaw, mc.player.rotationPitch);
            return;
        }

        attackAndRotateOnEntity(target);
    }

        @EventTarget
        public void onMotion(EventMotion motionEvent) {
            handleMotionEvent(motionEvent);
        }

    private void disableSprint() {
        mc.player.setSprinting(false);
        mc.gameSettings.keyBindSprint.setPressed(false);
    }

    private void enableSprint() {
        mc.player.setSprinting(true);
        mc.gameSettings.keyBindSprint.setPressed(true);
    }

    private double prevCircleStep, circleStep;



    public Vector2f clientRot = null;

    private void handleMotionEvent(EventMotion motionEvent) {
        if (target == null)
            return;

        motionEvent.setYaw(rotate.x);
        motionEvent.setPitch(rotate.y);
        mc.player.rotationYawHead = rotate.x;
        mc.player.renderYawOffset = rotate.x;
        mc.player.rotationPitchHead = rotate.y;

    }

    private void attackAndRotateOnEntity(LivingEntity target) {

        boolean sprint = mc.player.isSprinting();

        if (sprint && settings.get(5)) {
            disableSprint();
        }

        hasRotated = false;

        switch (rotationMode.getIndexs()) {

            case 2 -> {
                if (shouldAttack(target)) {
                    if (!mc.player.isElytraFlying()) {
                        ticksUntilNextAttack = 0.3f;
                        attackTarget();
                        attackTarget();
                    }
                    if (mc.player.isElytraFlying()) {
                        if ((getDistance(target) - distance.get() - 0.3) <= 0.0 && distance.get() >= 3.4) {
                            ticksUntilNextAttack = 0.4f;
                            attackTarget();
                            attackTarget();
                        }
                    }
                }

                if (ticksUntilNextAttack > 0) {
                    setRotation(target, false);
                    ticksUntilNextAttack--;
                } else {
                    rotate.x = mc.player.rotationYaw;
                    rotate.y = mc.player.rotationPitch;
                }
            }

            case 0 -> {
                hasRotated = false;
                if (shouldAttack(target) && RayTraceUtil.getMouseOver(target, rotate.x, rotate.y, distance.get()) == target
                        ) {
                    attackTarget();
                }
                if (!hasRotated)
                    setRotation(target, false);
            }

            case 1 -> {
                if (shouldAttack(target)) {
                    attackTarget();
                    ticksUntilNextAttack = 2.1f;
                }

                if (ticksUntilNextAttack > 0) {
                    setRotation(target, false);
                    ticksUntilNextAttack--;
                } else {
                    rotate.x = mc.player.rotationYaw;
                    rotate.y = mc.player.rotationPitch;
                }
            }
        }
        if (settings.get(5)) {
            enableSprint();
        }
    }

    private void attackTarget() {
            if (mc.player.isHandActive() && onlySpaceCritical.get()) return;

            if (settings.get(2) && mc.player.isActiveItemStackBlocking()) {
                mc.playerController.onStoppedUsingItem(mc.player);
            }


            long lastAttackTime = 0;
            int attackCycleStep = 0;
            int[] cpsIntervals = {500, 520, 494, 498};
            int[] cpsCycles = {3, 2, 2, 4};


            long currentTime = System.currentTimeMillis();

            if (currentTime >= lastAttackTime + cpsIntervals[attackCycleStep]) {

                lastAttackTime = currentTime;


                cpsLimit = System.currentTimeMillis() + cpsIntervals[attackCycleStep];
                mc.playerController.attackEntity(mc.player, target);
                mc.player.swingArm(Hand.MAIN_HAND);


                if (--cpsCycles[attackCycleStep] <= 0) {

                    attackCycleStep = (attackCycleStep + 1) % cpsIntervals.length; // Цикличность

                    cpsCycles[attackCycleStep] = attackCycleStep == 0 ? 3 : (attackCycleStep == 1 ? 2 : (attackCycleStep == 2 ? 2 : 4));
                }

                if (target instanceof PlayerEntity playerEntity && settings.get(3)) {
                    breakShieldAndSwapSlot();

                }
            }


    }

    private void breakShieldAndSwapSlot() {
        LivingEntity targetEntity = target;
        if (targetEntity instanceof PlayerEntity player) {
            if (target.isActiveItemStackBlocking(2)
                    && !player.isSpectator()
                    && !player.isCreative()
                    && (target.getHeldItemOffhand().getItem() == Items.SHIELD
                    || target.getHeldItemMainhand().getItem() == Items.SHIELD)) {
                int slot = breakShield(player);
                if (slot > 8) {
                    mc.playerController.pickItem(slot);
                }
            }
        }
    }


    public int breakShield(LivingEntity target) {
        int hotBarSlot = InventoryUtil.getAxe(true);
        if (hotBarSlot != -1) {
            mc.player.connection.sendPacket(new CHeldItemChangePacket(hotBarSlot));
            mc.playerController.attackEntity(mc.player, target);
            mc.player.swingArm(Hand.MAIN_HAND);
            mc.player.connection.sendPacket(new CHeldItemChangePacket(mc.player.inventory.currentItem));
            return hotBarSlot;
        }
        int inventorySLot = InventoryUtil.getAxe(false);
        if (inventorySLot != -1) {
            mc.playerController.pickItem(inventorySLot);
            mc.playerController.attackEntity(mc.player, target);
            mc.player.swingArm(Hand.MAIN_HAND);
            return inventorySLot;
        }
        return -1;
    }

    private boolean shouldAttack(LivingEntity targetEntity) {
        return canAttack() && targetEntity != null && (cpsLimit <= System.currentTimeMillis());
    }

    private void setRotation(final LivingEntity base, final boolean attack) {
        this.hasRotated = true;

        Vector3d vec3d = AuraUtil.getVector(base);

        double diffX = vec3d.x + target.getMotion().x;
        double diffY = vec3d.y;
        double diffZ = vec3d.z + target.getMotion().z;


        if ((target.getPosY() - mc.player.getPosY() > 0.0 && (target.getPosY() - mc.player.getPosY() < 1.1 && !target.isSwimming() && !target.isElytraFlying()))) {
            diffY = vec3d.y - (target.getPosY() - mc.player.getPosY());
        } else {
            if ((mc.player.getPosY() - target.getPosY()) > 0.0 && (mc.player.getPosY() - target.getPosY() <= 0.25 && !target.isSwimming() && !target.isElytraFlying() && !target.isSneaking())) {
                diffY = vec3d.y + (mc.player.getPosY() - target.getPosY());
            } else {
                if ((mc.player.getPosY() - target.getPosY()) > 0.0 && (mc.player.getPosY() - target.getPosY() <= 0.25 && !target.isSwimming() && !target.isElytraFlying() && target.isSneaking())) {
                    diffY = vec3d.y;
                }
            }
            if ((target.getPosY() - mc.player.getPosY() >= 1.1 && !target.isSwimming() && !target.isElytraFlying())) {
                diffY = vec3d.y - 1.1;
            }

            if ((mc.player.getPosY() - target.getPosY() > 0.25 && !target.isSwimming() && !target.isElytraFlying() && !target.isSneaking())) {
                diffY = vec3d.y + 0.25;
            } else {
                if ((mc.player.getPosY() - target.getPosY() > 0.25 && !target.isSwimming() && !target.isElytraFlying() && target.isSneaking())) {
                    diffY = vec3d.y;
                }
            }
        }


        if (rotationMode.is("Обычная") && lerpRot.get()) {
            float[] rotations = new float[]{
                    (float) Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0F,
                    (float) (-Math.toDegrees(Math.atan2(diffY, Math.hypot(diffX, diffZ))))
            };

            float deltaYaw = MathHelper.wrapDegrees(calculateDelta(rotations[0], this.rotate.x));
            float deltaPitch = calculateDelta(rotations[1], this.rotate.y);

            float limitedYaw = min(max(abs(deltaYaw), 1.0F), 180.0F);
            float limitedPitch = (float) min(max(abs(deltaPitch), 1.0F), 15.0F);

            float finalYaw = 0;
            float finalPitch = 0;

            finalYaw = this.rotate.x + (deltaYaw > 0.0f ? limitedYaw : -limitedYaw) + ThreadLocalRandom.current().nextFloat(-1.0f, 1.0f);

            finalPitch = MathHelper.clamp(this.rotate.y + (deltaPitch > 0.0f ? limitedPitch : -limitedPitch) + ThreadLocalRandom.current().nextFloat(-0.7f, 0.7f), -89.0f, 89.0f);

            float gcd = GCDUtil.getGCDValue() / 1.2f;
            float resolveX = (float) target.getMotion().x + (float) mc.player.getMotion().x;
            float resolveZ = (float) target.getMotion().z + (float) mc.player.getMotion().z;
            float resolveY = (float) target.getMotion().y + (float) mc.player.getMotion().y;

            float resolve = (resolveX + resolveZ + resolveY);

            resolveX = abs(resolveX);
            resolveZ = abs(resolveZ);
            resolveY = abs(resolveY);

            if (resolve >= 1.2) {
                gcd = GCDUtil.getGCDValue() / resolve;
            }

            float speed = rotationSpeed * (deltaYaw > 0.0f ? limitedYaw : -limitedYaw);

            finalYaw = (float) ((double) finalYaw - (double) (finalYaw - this.rotate.x) % gcd);
            finalPitch = (float) ((double) finalPitch - (double) (finalPitch - rotate.y) % gcd);
            finalYaw = this.rotate.x + speed;

            float progress = min((System.currentTimeMillis() - lerpStartTime) / 1000.0f, 1.0f);
            float lerpedYaw = AnimationMath.lerp(this.rotate.x, finalYaw, progress);
            float lerpedPitch = AnimationMath.lerp(this.rotate.y, finalPitch, progress);

            this.rotate.x = lerpedYaw;
            this.rotate.y = lerpedPitch;

            if (progress >= 1.0f) {
                lerpStartTime = 0;
            }
        } else {
            float[] rotations = new float[]{
                    (float) Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0F,
                    (float) (-Math.toDegrees(Math.atan2(diffY, Math.hypot(diffX, diffZ))))
            };

            float deltaYaw = MathHelper.wrapDegrees(calculateDelta(rotations[0], this.rotate.x));
            float deltaPitch = calculateDelta(rotations[1], this.rotate.y);

            float limitedYaw = min(max(abs(deltaYaw), 1.0F), 180.0F);
            float limitedPitch = (float) min(max(abs(deltaPitch), 1.0F), 15.0F);

            float finalYaw = 0;
            float finalPitch = 0;

            finalYaw = this.rotate.x + (deltaYaw > 0.0f ? limitedYaw : -limitedYaw) + ThreadLocalRandom.current().nextFloat(-1.0f, 1.0f);

            finalPitch = MathHelper.clamp(this.rotate.y + (deltaPitch > 0.0f ? limitedPitch : -limitedPitch) + ThreadLocalRandom.current().nextFloat(-1.0f, 1.0f), -89.0f, 89.0f);

            float gcd = GCDUtil.getGCDValue() / 1.2f;
            float resolveX = (float) target.getMotion().x + (float) mc.player.getMotion().x;
            float resolveZ = (float) target.getMotion().z + (float) mc.player.getMotion().z;
            float resolveY = (float) target.getMotion().y + (float) mc.player.getMotion().y;

            float resolve = (resolveX + resolveZ + resolveY);

            resolveX = abs(resolveX);
            resolveZ = abs(resolveZ);
            resolveY = abs(resolveY);

            if (resolve >= 1.2) {
                gcd = GCDUtil.getGCDValue() / resolve;
            }

            finalYaw = (float) ((double) finalYaw - (double) (finalYaw - this.rotate.x) % gcd);
            finalPitch = (float) ((double) finalPitch - (double) (finalPitch - rotate.y) % gcd);

            this.rotate.x = finalYaw;
            this.rotate.y = finalPitch;

        }
    }

    public static float calculateDelta(float a, float b) {
        return a - b;
    }

    public class AnimationMath {
        public static float lerp(float start, float end, float progress) {
            return start + progress * (end - start);
        }
    }

    public boolean canAttack() {
        final boolean onSpace = onlySpaceCritical.get()
                && mc.player.isOnGround()
                && !mc.gameSettings.keyBindJump.isKeyDown();

        final boolean reasonForAttack = mc.player.isPotionActive(Effects.BLINDNESS)
                || mc.player.isOnLadder()
                || mc.player.isInWater() && mc.player.areEyesInFluid(FluidTags.WATER)
                || mc.player.isRidingHorse()
                || mc.player.abilities.isFlying || mc.player.isElytraFlying();

        if (getDistance(target) >= distance.get()
                || mc.player.getCooledAttackStrength(1.5F) < 0.92F) {
            return false;
        }
      //ДОДЕЛАТЬ ФРИК АМЕРУ  if (Managment.FUNCTION_MANAGER.freeCam.player != null) return true;

        if (!reasonForAttack && settings.get(0)) {
            return onSpace || !mc.player.isOnGround() && mc.player.fallDistance > 0.0F;
        }
        return true;
    }

    private LivingEntity findTarget() {
        List<LivingEntity> targets = new ArrayList<>();

        for (Entity entity : mc.world.getAllEntities()) {
            if (entity instanceof LivingEntity && isValidTarget((LivingEntity) entity)) {
                targets.add((LivingEntity) entity);
            }
        }

        if (targets.isEmpty()) {
            return null;
        }

        if (targets.size() > 1) {
            switch (sortMode.get()) {
                case "По всему" -> {
                    targets.sort(Comparator.comparingDouble(target -> {
                        if (target instanceof PlayerEntity player) {
                            return -this.getEntityArmor(player);
                        }
                        if (target instanceof LivingEntity livingEntity) {
                            return -livingEntity.getTotalArmorValue();
                        }
                        return 0.0;
                    }).thenComparing((o, o1) -> {
                        double health = getEntityHealth((LivingEntity) o);
                        double health1 = getEntityHealth((LivingEntity) o1);
                        return Double.compare(health, health1);
                    }).thenComparing((object, object2) -> {
                        double d2 = getDistance((LivingEntity) object);
                        double d3 = getDistance((LivingEntity) object2);
                        return Double.compare(d2, d3);
                    }));
                }
                case "По дистанции" -> {
                    targets.sort(Comparator.comparingDouble(KillAura::getDistance).thenComparingDouble(this::getEntityHealth));
                }
                case "По здоровью" -> {
                    targets.sort(Comparator.comparingDouble(this::getEntityHealth).thenComparingDouble(mc.player::getDistance));
                }
            }
        } else {
            cpsLimit = System.currentTimeMillis();
        }
        return targets.get(0);
    }

    private boolean isValidTarget(final LivingEntity base) {
        if (base.getShouldBeDead() || !base.isAlive() || base == mc.player) return false;

        if (base instanceof PlayerEntity) {
            String playerName = base.getName().getString();
            if (Main.getInstance().getFriendManager().isFriend(playerName) && !targets.get(1) || base.getTotalArmorValue() == 0 && (!targets.get(0) || !targets.get(2)))
                return false;
        }

        if ((base instanceof MobEntity || base instanceof AnimalEntity) && !targets.get(3)) return false;

        if (base instanceof ArmorStandEntity || base instanceof PlayerEntity && ((PlayerEntity) base).isBot)
            return false;


        return getDistance(base) <= distance.get()
                + (rotationMode.is("Обычная") ? rotateDistance.get() : 0.0f);
    }

    private static double getDistance(LivingEntity entity) {
        return AuraUtil.getVector(entity).length();
    }

    public double getEntityArmor(PlayerEntity target) {

        double totalArmor = 0.0;

        for (ItemStack armorStack : target.inventory.armorInventory) {
            if (armorStack != null && armorStack.getItem() instanceof ArmorItem) {
                totalArmor += getProtectionLvl(armorStack);
            }
        }
        return totalArmor;
    }
    private double getProtectionLvl(ItemStack stack) {
        ArmorItem armor = (ArmorItem) stack.getItem();
        double damageReduce = armor.getDamageReduceAmount();
        if (stack.isEnchanted()) {
            damageReduce += (double) EnchantmentHelper.getEnchantmentLevel(Enchantments.PROTECTION, stack) * 0.25;
        }
        return damageReduce;
    }
    public double getEntityHealth(Entity ent) {
        if (ent instanceof PlayerEntity player) {
            double armorValue = getEntityArmor(player) / 20.0;
            return (player.getHealth() + player.getAbsorptionAmount()) * armorValue;
        } else if (ent instanceof LivingEntity livingEntity) {
            return livingEntity.getHealth() + livingEntity.getAbsorptionAmount();
        }
        return 0.0;
    }
    @Override
    public void onDisable() {
        this.rotate = new Vector2f(mc.player.rotationYaw, mc.player.rotationPitch);
        target = null;
        cpsLimit = System.currentTimeMillis();
        super.onDisable();
    }
}