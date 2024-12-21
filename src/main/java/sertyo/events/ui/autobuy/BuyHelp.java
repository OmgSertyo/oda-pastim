package sertyo.events.ui.autobuy;

import com.google.common.collect.Multimap;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.potion.PotionUtils;

import java.util.List;
import java.util.Map;

public class BuyHelp {

    public BuyHelp() {
    }




    public boolean KrushTotemItem(ItemStack itemStack) {
        Multimap<Attribute, AttributeModifier> attributes = itemStack.getAttributeModifiers(EquipmentSlotType.OFFHAND);
        boolean meetsMaxHealthRequirement = false, meetsAttackDamageRequirement = false, meetsArmorToughnessRequirement = false, meetsArmorRequirement = false;
        for (Map.Entry<Attribute, AttributeModifier> entry : attributes.entries()) {
            Attribute attribute = entry.getKey();
            AttributeModifier modifier = entry.getValue();
            if (attribute == Attributes.MAX_HEALTH && modifier.getAmount() == 4 && modifier.getOperation() == AttributeModifier.Operation.ADDITION)
                meetsMaxHealthRequirement = true;
            else if (attribute == Attributes.ATTACK_DAMAGE && modifier.getAmount() == 3 && modifier.getOperation() == AttributeModifier.Operation.ADDITION)
                meetsAttackDamageRequirement = true;
            else if (attribute == Attributes.ARMOR_TOUGHNESS && modifier.getAmount() == 2 && modifier.getOperation() == AttributeModifier.Operation.ADDITION)
                meetsArmorToughnessRequirement = true;
            else if (attribute == Attributes.ARMOR && modifier.getAmount() == 2 && modifier.getOperation() == AttributeModifier.Operation.ADDITION)
                meetsArmorRequirement = true;
        }
        return meetsMaxHealthRequirement && meetsAttackDamageRequirement && meetsArmorToughnessRequirement && meetsArmorRequirement;
    }
    public boolean IasoItem(ItemStack itemStack) {
        Multimap<Attribute, AttributeModifier> attributes = itemStack.getAttributeModifiers(EquipmentSlotType.OFFHAND);
        boolean meetsArmorRequirement = false, meetsArmorToughnessRequirement = false;
        for (Map.Entry<Attribute, AttributeModifier> entry : attributes.entries()) {
            Attribute attribute = entry.getKey();
            AttributeModifier modifier = entry.getValue();
            if (attribute == Attributes.ARMOR && modifier.getAmount() == 1 && modifier.getOperation() == AttributeModifier.Operation.ADDITION)
                meetsArmorRequirement = true;
            else if (attribute == Attributes.ARMOR_TOUGHNESS && modifier.getAmount() == 1 && modifier.getOperation() == AttributeModifier.Operation.ADDITION)
                meetsArmorToughnessRequirement = true;
        }
        return meetsArmorRequirement && meetsArmorToughnessRequirement;
    }
    private boolean isTridentKrysh(ItemStack itemStack) {

        Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(itemStack);

        boolean meetsRequirements = enchantments.containsKey(Enchantments.CHANNELING) &&
                enchantments.containsKey(Enchantments.FIRE_ASPECT) && enchantments.get(Enchantments.FIRE_ASPECT) >= 2 &&
                enchantments.containsKey(Enchantments.IMPALING) && enchantments.get(Enchantments.IMPALING) >= 5 &&
                enchantments.containsKey(Enchantments.LOYALTY) && enchantments.get(Enchantments.LOYALTY) >= 3 &&
                enchantments.containsKey(Enchantments.MENDING) &&
                enchantments.containsKey(Enchantments.SHARPNESS) && enchantments.get(Enchantments.SHARPNESS) >= 7 &&
                enchantments.containsKey(Enchantments.UNBREAKING) && enchantments.get(Enchantments.UNBREAKING) >= 5;

        return meetsRequirements;
    }
    public boolean isBootsKrysh(ItemStack itemStack) {

        if (itemStack.getItem() != Items.NETHERITE_BOOTS) {
            return false;
        }

        Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(itemStack);

        boolean hasBlastProtection = enchantments.containsKey(Enchantments.BLAST_PROTECTION) && enchantments.get(Enchantments.BLAST_PROTECTION) >= 5;
        boolean hasDepthStrider = enchantments.containsKey(Enchantments.DEPTH_STRIDER) && enchantments.get(Enchantments.DEPTH_STRIDER) >= 3;
        boolean hasFeatherFalling = enchantments.containsKey(Enchantments.FEATHER_FALLING) && enchantments.get(Enchantments.FEATHER_FALLING) >= 4;
        boolean hasFireProtection = enchantments.containsKey(Enchantments.FIRE_PROTECTION) && enchantments.get(Enchantments.FIRE_PROTECTION) >= 5;
        boolean hasMending = enchantments.containsKey(Enchantments.MENDING);
        boolean hasProjectileProtection = enchantments.containsKey(Enchantments.PROJECTILE_PROTECTION) && enchantments.get(Enchantments.PROJECTILE_PROTECTION) >= 5;
        boolean hasProtection = enchantments.containsKey(Enchantments.PROTECTION) && enchantments.get(Enchantments.PROTECTION) >= 5;
        boolean hasSoulSpeed = enchantments.containsKey(Enchantments.SOUL_SPEED) && enchantments.get(Enchantments.SOUL_SPEED) >= 3;
        boolean hasUnbreaking = enchantments.containsKey(Enchantments.UNBREAKING) && enchantments.get(Enchantments.UNBREAKING) >= 5;

        boolean hasThorns = enchantments.containsKey(Enchantments.THORNS) && enchantments.get(Enchantments.THORNS) >= 3;

        boolean meetsRequirements = hasBlastProtection && hasDepthStrider && hasFeatherFalling &&
                hasFireProtection && hasMending && hasProjectileProtection &&
                hasProtection && hasSoulSpeed && hasUnbreaking;

        return meetsRequirements;
    }
    public boolean isLeggingsKrysh(ItemStack itemStack) {
        if (itemStack.getItem() != Items.NETHERITE_LEGGINGS) {
            return false;
        }

        Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(itemStack);

        boolean hasBlastProtection = enchantments.containsKey(Enchantments.BLAST_PROTECTION) && enchantments.get(Enchantments.BLAST_PROTECTION) >= 5;
        boolean hasFireProtection = enchantments.containsKey(Enchantments.FIRE_PROTECTION) && enchantments.get(Enchantments.FIRE_PROTECTION) >= 5;
        boolean hasMending = enchantments.containsKey(Enchantments.MENDING);
        boolean hasProjectileProtection = enchantments.containsKey(Enchantments.PROJECTILE_PROTECTION) && enchantments.get(Enchantments.PROJECTILE_PROTECTION) >= 5;
        boolean hasProtection = enchantments.containsKey(Enchantments.PROTECTION) && enchantments.get(Enchantments.PROTECTION) >= 5;
        boolean hasUnbreaking = enchantments.containsKey(Enchantments.UNBREAKING) && enchantments.get(Enchantments.UNBREAKING) >= 5;

        boolean hasThorns = enchantments.containsKey(Enchantments.THORNS) && enchantments.get(Enchantments.THORNS) >= 3;

        boolean meetsRequirements = hasBlastProtection && hasFireProtection && hasMending &&
                hasProjectileProtection && hasProtection && hasUnbreaking;

        return meetsRequirements;
    }
    public boolean isChestplateKrysh(ItemStack itemStack) {
        if (itemStack.getItem() != Items.NETHERITE_CHESTPLATE) {
            return false;
        }
        Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(itemStack);

        boolean hasBlastProtection = enchantments.containsKey(Enchantments.BLAST_PROTECTION) && enchantments.get(Enchantments.BLAST_PROTECTION) >= 5;
        boolean hasFireProtection = enchantments.containsKey(Enchantments.FIRE_PROTECTION) && enchantments.get(Enchantments.FIRE_PROTECTION) >= 5;
        boolean hasMending = enchantments.containsKey(Enchantments.MENDING);
        boolean hasProjectileProtection = enchantments.containsKey(Enchantments.PROJECTILE_PROTECTION) && enchantments.get(Enchantments.PROJECTILE_PROTECTION) >= 5;
        boolean hasProtection = enchantments.containsKey(Enchantments.PROTECTION) && enchantments.get(Enchantments.PROTECTION) >= 5;
        boolean hasUnbreaking = enchantments.containsKey(Enchantments.UNBREAKING) && enchantments.get(Enchantments.UNBREAKING) >= 5;

        boolean hasThorns = enchantments.containsKey(Enchantments.THORNS) && enchantments.get(Enchantments.THORNS) >= 3;

        boolean meetsRequirements = hasBlastProtection && hasFireProtection && hasMending &&
                hasProjectileProtection && hasProtection && hasUnbreaking;

        return meetsRequirements;
    }
    public boolean isCrossbowKrysh(ItemStack itemStack) {

        Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(itemStack);

        boolean meetsRequirements = enchantments.containsKey(Enchantments.MENDING) &&
                enchantments.containsKey(Enchantments.MULTISHOT) &&
                enchantments.containsKey(Enchantments.PIERCING) && enchantments.get(Enchantments.PIERCING) >= 5 &&
                enchantments.containsKey(Enchantments.QUICK_CHARGE) && enchantments.get(Enchantments.QUICK_CHARGE) >= 3 &&
                enchantments.containsKey(Enchantments.UNBREAKING) && enchantments.get(Enchantments.UNBREAKING) >= 3;

        return meetsRequirements;
    }
    public boolean isHelmetKrysh(ItemStack itemStack) {
        if (itemStack.getItem() != Items.NETHERITE_HELMET) {
            return false;
        }

        Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(itemStack);

        boolean hasAquaAffinity = enchantments.containsKey(Enchantments.AQUA_AFFINITY);
        boolean hasBlastProtection = enchantments.containsKey(Enchantments.BLAST_PROTECTION) && enchantments.get(Enchantments.BLAST_PROTECTION) >= 5;
        boolean hasFireProtection = enchantments.containsKey(Enchantments.FIRE_PROTECTION) && enchantments.get(Enchantments.FIRE_PROTECTION) >= 5;
        boolean hasMending = enchantments.containsKey(Enchantments.MENDING);
        boolean hasProjectileProtection = enchantments.containsKey(Enchantments.PROJECTILE_PROTECTION) && enchantments.get(Enchantments.PROJECTILE_PROTECTION) >= 5;
        boolean hasProtection = enchantments.containsKey(Enchantments.PROTECTION) && enchantments.get(Enchantments.PROTECTION) >= 5;
        boolean hasRespiration = enchantments.containsKey(Enchantments.RESPIRATION) && enchantments.get(Enchantments.RESPIRATION) >= 3;
        boolean hasUnbreaking = enchantments.containsKey(Enchantments.UNBREAKING) && enchantments.get(Enchantments.UNBREAKING) >= 5;

        boolean hasThorns = enchantments.containsKey(Enchantments.THORNS) && enchantments.get(Enchantments.THORNS) >= 3;

        boolean meetsRequirements = hasAquaAffinity && hasBlastProtection && hasFireProtection && hasMending &&
                hasProjectileProtection && hasProtection && hasRespiration && hasUnbreaking;

        return meetsRequirements;
    }
    public boolean HaurbekName(ItemStack itemStack) {
        Multimap<Attribute, AttributeModifier> attributes = itemStack.getAttributeModifiers(EquipmentSlotType.OFFHAND);
        boolean meetsAttackDamageRequirement = false, meetsAttackSpeedRequirement = false;
        for (Map.Entry<Attribute, AttributeModifier> entry : attributes.entries()) {
            Attribute attribute = entry.getKey();
            AttributeModifier modifier = entry.getValue();
            if (attribute == Attributes.ATTACK_DAMAGE && modifier.getAmount() == 5 && modifier.getOperation() == AttributeModifier.Operation.ADDITION)
                meetsAttackDamageRequirement = true;
            else if (attribute == Attributes.ATTACK_SPEED && modifier.getAmount() == -0.15 && modifier.getOperation() == AttributeModifier.Operation.MULTIPLY_BASE)
                meetsAttackSpeedRequirement = true;
        }
        return meetsAttackDamageRequirement && meetsAttackSpeedRequirement;
    }

    public boolean ManesaName(ItemStack itemStack) {
        Multimap<Attribute, AttributeModifier> attributes = itemStack.getAttributeModifiers(EquipmentSlotType.OFFHAND);
        boolean meetsAttackDamageRequirement = false, meetsAttackSpeedRequirement = false;
        for (Map.Entry<Attribute, AttributeModifier> entry : attributes.entries()) {
            Attribute attribute = entry.getKey();
            AttributeModifier modifier = entry.getValue();
            if (attribute == Attributes.ATTACK_DAMAGE && modifier.getAmount() == -2 && modifier.getOperation() == AttributeModifier.Operation.ADDITION)
                meetsAttackDamageRequirement = true;
            else if (attribute == Attributes.ATTACK_SPEED && modifier.getAmount() == 0.30 && modifier.getOperation() == AttributeModifier.Operation.MULTIPLY_BASE)
                meetsAttackSpeedRequirement = true;
        }
        return meetsAttackDamageRequirement && meetsAttackSpeedRequirement;
    }

    public boolean DionisaName(ItemStack itemStack) {
        Multimap<Attribute, AttributeModifier> attributes = itemStack.getAttributeModifiers(EquipmentSlotType.OFFHAND);
        boolean meetsSpeedRequirement = false, meetsAttackDamageRequirement = false, meetsAttackSpeedRequirement = false;
        for (Map.Entry<Attribute, AttributeModifier> entry : attributes.entries()) {
            Attribute attribute = entry.getKey();
            AttributeModifier modifier = entry.getValue();
            if (attribute == Attributes.MOVEMENT_SPEED && modifier.getAmount() == 0.10 && modifier.getOperation() == AttributeModifier.Operation.MULTIPLY_BASE)
                meetsSpeedRequirement = true;
            else if (attribute == Attributes.ATTACK_DAMAGE && modifier.getAmount() == 2 && modifier.getOperation() == AttributeModifier.Operation.ADDITION)
                meetsAttackDamageRequirement = true;
            else if (attribute == Attributes.ATTACK_SPEED && modifier.getAmount() == 0.15 && modifier.getOperation() == AttributeModifier.Operation.MULTIPLY_BASE)
                meetsAttackSpeedRequirement = true;
        }
        return meetsSpeedRequirement  && meetsAttackDamageRequirement && meetsAttackSpeedRequirement;
    }

    public boolean KobraName(ItemStack itemStack) {
        Multimap<Attribute, AttributeModifier> attributes = itemStack.getAttributeModifiers(EquipmentSlotType.OFFHAND);
        boolean meetsMaxHealthRequirement = false, meetsAttackDamageRequirement = false, meetsAttackSpeedRequirement = false, meetsArmorRequirement = false, meetsArmorToughnessRequirement = false;
        for (Map.Entry<Attribute, AttributeModifier> entry : attributes.entries()) {
            Attribute attribute = entry.getKey();
            AttributeModifier modifier = entry.getValue();
            if (attribute == Attributes.MAX_HEALTH && modifier.getAmount() == -4 && modifier.getOperation() == AttributeModifier.Operation.ADDITION)
                meetsMaxHealthRequirement = true;
            else if (attribute == Attributes.ATTACK_DAMAGE && modifier.getAmount() == 5 && modifier.getOperation() == AttributeModifier.Operation.ADDITION)
                meetsAttackDamageRequirement = true;
        }
        return meetsMaxHealthRequirement && meetsAttackDamageRequirement;
    }

    public boolean KraitName(ItemStack itemStack) {
        Multimap<Attribute, AttributeModifier> attributes = itemStack.getAttributeModifiers(EquipmentSlotType.OFFHAND);
        boolean meetsAttackDamageRequirement = false, meetsAttackSpeedRequirement = false, meetsArmorRequirement = false, meetsArmorToughnessRequirement = false;
        for (Map.Entry<Attribute, AttributeModifier> entry : attributes.entries()) {
            Attribute attribute = entry.getKey();
            AttributeModifier modifier = entry.getValue();
            if (attribute == Attributes.ATTACK_DAMAGE && modifier.getAmount() == 3 && modifier.getOperation() == AttributeModifier.Operation.ADDITION)
                meetsAttackDamageRequirement = true;
            else if (attribute == Attributes.ATTACK_SPEED && modifier.getAmount() == -0.25 && modifier.getOperation() == AttributeModifier.Operation.MULTIPLY_BASE)
                meetsAttackSpeedRequirement = true;
            else if (attribute == Attributes.ARMOR && modifier.getAmount() == 2 && modifier.getOperation() == AttributeModifier.Operation.ADDITION)
                meetsArmorRequirement = true;
            else if (attribute == Attributes.ARMOR_TOUGHNESS && modifier.getAmount() == 2 && modifier.getOperation() == AttributeModifier.Operation.ADDITION)
                meetsArmorToughnessRequirement = true;
        }
        return meetsAttackDamageRequirement && meetsAttackSpeedRequirement && meetsArmorRequirement && meetsArmorToughnessRequirement;
    }

    public boolean FugaName(ItemStack itemStack) {
        Multimap<Attribute, AttributeModifier> attributes = itemStack.getAttributeModifiers(EquipmentSlotType.OFFHAND);
        boolean meetsMaxHealthRequirement = false, meetsAttackDamageRequirement = false, meetsArmorRequirement = false, meetsArmorToughnessRequirement = false;
        for (Map.Entry<Attribute, AttributeModifier> entry : attributes.entries()) {
            Attribute attribute = entry.getKey();
            AttributeModifier modifier = entry.getValue();
            if (attribute == Attributes.MAX_HEALTH && modifier.getAmount() == -2 && modifier.getOperation() == AttributeModifier.Operation.ADDITION)
                meetsMaxHealthRequirement = true;
            else if (attribute == Attributes.ATTACK_DAMAGE && modifier.getAmount() == 6 && modifier.getOperation() == AttributeModifier.Operation.ADDITION)
                meetsAttackDamageRequirement = true;
            else if (attribute == Attributes.ARMOR && modifier.getAmount() == -4 && modifier.getOperation() == AttributeModifier.Operation.ADDITION)
                meetsArmorRequirement = true;
            else if (attribute == Attributes.ARMOR_TOUGHNESS && modifier.getAmount() == -4 && modifier.getOperation() == AttributeModifier.Operation.ADDITION)
                meetsArmorToughnessRequirement = true;
        }
        return meetsMaxHealthRequirement && meetsAttackDamageRequirement && meetsArmorRequirement && meetsArmorToughnessRequirement;
    }

    public boolean GefestName(ItemStack itemStack) {
        Multimap<Attribute, AttributeModifier> attributes = itemStack.getAttributeModifiers(EquipmentSlotType.OFFHAND);
        boolean meetsSpeedRequirement = false, meetsMaxHealthRequirement = false, meetsAttackDamageRequirement = false, meetsAttackSpeedRequirement = false, meetsArmorRequirement = false, meetsArmorToughnessRequirement = false;
        for (Map.Entry<Attribute, AttributeModifier> entry : attributes.entries()) {
            Attribute attribute = entry.getKey();
            AttributeModifier modifier = entry.getValue();
            if (attribute == Attributes.MOVEMENT_SPEED && modifier.getAmount() == -0.50 && modifier.getOperation() == AttributeModifier.Operation.MULTIPLY_BASE)
                meetsSpeedRequirement = true;
            else if (attribute == Attributes.MAX_HEALTH && modifier.getAmount() == 2 && modifier.getOperation() == AttributeModifier.Operation.ADDITION)
                meetsMaxHealthRequirement = true;
            else if (attribute == Attributes.ATTACK_DAMAGE && modifier.getAmount() == 3 && modifier.getOperation() == AttributeModifier.Operation.ADDITION)
                meetsAttackDamageRequirement = true;
            else if (attribute == Attributes.ATTACK_SPEED && modifier.getAmount() == 0.20 && modifier.getOperation() == AttributeModifier.Operation.MULTIPLY_BASE)
                meetsAttackSpeedRequirement = true;
        }
        return meetsSpeedRequirement && meetsMaxHealthRequirement && meetsAttackDamageRequirement && meetsAttackSpeedRequirement;
    }

    public boolean LekarjaName(ItemStack itemStack) {
        Multimap<Attribute, AttributeModifier> attributes = itemStack.getAttributeModifiers(EquipmentSlotType.OFFHAND);
        boolean meetsMaxHealthRequirement = false, meetsArmorRequirement = false;
        for (Map.Entry<Attribute, AttributeModifier> entry : attributes.entries()) {
            Attribute attribute = entry.getKey();
            AttributeModifier modifier = entry.getValue();
            if (attribute == Attributes.MAX_HEALTH && modifier.getAmount() == 6 && modifier.getOperation() == AttributeModifier.Operation.ADDITION)
                meetsMaxHealthRequirement = true;
            else if (attribute == Attributes.ARMOR && modifier.getAmount() == -4 && modifier.getOperation() == AttributeModifier.Operation.ADDITION)
                meetsArmorRequirement = true;
        }
        return meetsMaxHealthRequirement && meetsArmorRequirement;
    }

    public boolean AfinaName(ItemStack itemStack) {
        Multimap<Attribute, AttributeModifier> attributes = itemStack.getAttributeModifiers(EquipmentSlotType.OFFHAND);
        boolean meetsSpeedRequirement = false, meetsMaxHealthRequirement = false, meetsAttackDamageRequirement = false, meetsAttackSpeedRequirement = false;
        for (Map.Entry<Attribute, AttributeModifier> entry : attributes.entries()) {
            Attribute attribute = entry.getKey();
            AttributeModifier modifier = entry.getValue();
            if (attribute == Attributes.MOVEMENT_SPEED && modifier.getAmount() == 0.15 && modifier.getOperation() == AttributeModifier.Operation.MULTIPLY_BASE)
                meetsSpeedRequirement = true;
            else if (attribute == Attributes.MAX_HEALTH && modifier.getAmount() == -2 && modifier.getOperation() == AttributeModifier.Operation.ADDITION)
                meetsMaxHealthRequirement = true;
            else if (attribute == Attributes.ATTACK_DAMAGE && modifier.getAmount() == 3 && modifier.getOperation() == AttributeModifier.Operation.ADDITION)
                meetsAttackDamageRequirement = true;
            else if (attribute == Attributes.ATTACK_SPEED && modifier.getAmount() == 0.15 && modifier.getOperation() == AttributeModifier.Operation.MULTIPLY_BASE)
                meetsAttackSpeedRequirement = true;
        }
        return meetsSpeedRequirement && meetsMaxHealthRequirement && meetsAttackDamageRequirement && meetsAttackSpeedRequirement;
    }
    public boolean KaratelName(ItemStack itemStack) {
        Multimap<Attribute, AttributeModifier> attributes = itemStack.getAttributeModifiers(EquipmentSlotType.OFFHAND);
        boolean meetsSpeedRequirement = false, meetsMaxHealthRequirement = false, meetsAttackDamageRequirement = false;
        for (Map.Entry<Attribute, AttributeModifier> entry : attributes.entries()) {
            Attribute attribute = entry.getKey();
            AttributeModifier modifier = entry.getValue();
            if (attribute == Attributes.MOVEMENT_SPEED && modifier.getAmount() == 0.10 && modifier.getOperation() == AttributeModifier.Operation.MULTIPLY_BASE)
                meetsSpeedRequirement = true;
            else if (attribute == Attributes.MAX_HEALTH && modifier.getAmount() == -4 && modifier.getOperation() == AttributeModifier.Operation.ADDITION)
                meetsMaxHealthRequirement = true;
            else if (attribute == Attributes.ATTACK_DAMAGE && modifier.getAmount() == 7 && modifier.getOperation() == AttributeModifier.Operation.ADDITION)
                meetsAttackDamageRequirement = true;
        }
        return meetsSpeedRequirement && meetsMaxHealthRequirement && meetsAttackDamageRequirement ;
    }

    public boolean SoranaName(ItemStack itemStack) {
        Multimap<Attribute, AttributeModifier> attributes = itemStack.getAttributeModifiers(EquipmentSlotType.OFFHAND);
        boolean meetsMaxHealthRequirement = false, meetsAttackDamageRequirement = false, meetsArmorRequirement = false;
        for (Map.Entry<Attribute, AttributeModifier> entry : attributes.entries()) {
            Attribute attribute = entry.getKey();
            AttributeModifier modifier = entry.getValue();
            if (attribute == Attributes.MAX_HEALTH && modifier.getAmount() == 2 && modifier.getOperation() == AttributeModifier.Operation.ADDITION)
                meetsMaxHealthRequirement = true;
            else if (attribute == Attributes.ATTACK_DAMAGE && modifier.getAmount() == 3 && modifier.getOperation() == AttributeModifier.Operation.ADDITION)
                meetsAttackDamageRequirement = true;
            else if (attribute == Attributes.ARMOR && modifier.getAmount() == -3 && modifier.getOperation() == AttributeModifier.Operation.ADDITION)
                meetsArmorRequirement = true;
        }
        return meetsMaxHealthRequirement && meetsAttackDamageRequirement && meetsArmorRequirement;
    }

    public boolean TeurgiaName(ItemStack itemStack) {
        Multimap<Attribute, AttributeModifier> attributes = itemStack.getAttributeModifiers(EquipmentSlotType.OFFHAND);
        boolean meetsMaxHealthRequirement = false, meetsAttackDamageRequirement = false, meetsAttackSpeedRequirement = false;
        for (Map.Entry<Attribute, AttributeModifier> entry : attributes.entries()) {
            Attribute attribute = entry.getKey();
            AttributeModifier modifier = entry.getValue();
            if (attribute == Attributes.MAX_HEALTH && modifier.getAmount() == 4 && modifier.getOperation() == AttributeModifier.Operation.ADDITION)
                meetsMaxHealthRequirement = true;
            else if (attribute == Attributes.ATTACK_DAMAGE && modifier.getAmount() == 4 && modifier.getOperation() == AttributeModifier.Operation.ADDITION)
                meetsAttackDamageRequirement = true;
            else if (attribute == Attributes.ATTACK_SPEED && modifier.getAmount() == -0.15 && modifier.getOperation() == AttributeModifier.Operation.MULTIPLY_BASE)
                meetsAttackSpeedRequirement = true;
        }
        return meetsMaxHealthRequirement && meetsAttackDamageRequirement && meetsAttackSpeedRequirement;
    }

    public boolean MagmaName(ItemStack itemStack) {
        Multimap<Attribute, AttributeModifier> attributes = itemStack.getAttributeModifiers(EquipmentSlotType.OFFHAND);
        boolean meetsSpeedRequirement = false, meetsArmorRequirement = false, meetsArmorToughnessRequirement = false;
        for (Map.Entry<Attribute, AttributeModifier> entry : attributes.entries()) {
            Attribute attribute = entry.getKey();
            AttributeModifier modifier = entry.getValue();
            if (attribute == Attributes.MOVEMENT_SPEED && modifier.getAmount() == -0.15 && modifier.getOperation() == AttributeModifier.Operation.MULTIPLY_BASE)
                meetsSpeedRequirement = true;
            else if (attribute == Attributes.ARMOR && modifier.getAmount() == 2 && modifier.getOperation() == AttributeModifier.Operation.ADDITION)
                meetsArmorRequirement = true;
            else if (attribute == Attributes.ARMOR_TOUGHNESS && modifier.getAmount() == 2 && modifier.getOperation() == AttributeModifier.Operation.ADDITION)
                meetsArmorToughnessRequirement = true;
        }
        return meetsSpeedRequirement && meetsArmorRequirement && meetsArmorToughnessRequirement;
    }


    public boolean AbantiName(ItemStack itemStack) {
        Multimap<Attribute, AttributeModifier> attributes = itemStack.getAttributeModifiers(EquipmentSlotType.OFFHAND);
        boolean meetsMaxHealthRequirement = false, meetsLuckRequirement = false;
        for (Map.Entry<Attribute, AttributeModifier> entry : attributes.entries()) {
            Attribute attribute = entry.getKey();
            AttributeModifier modifier = entry.getValue();
            if (attribute == Attributes.MAX_HEALTH && modifier.getAmount() == 2 && modifier.getOperation() == AttributeModifier.Operation.ADDITION)
                meetsMaxHealthRequirement = true;
            else if (attribute == Attributes.LUCK && modifier.getAmount() == 1 && modifier.getOperation() == AttributeModifier.Operation.ADDITION)
                meetsLuckRequirement = true;
        }
        return meetsMaxHealthRequirement && meetsLuckRequirement;
    }

    public boolean PanakeiaName(ItemStack itemStack) {
        Multimap<Attribute, AttributeModifier> attributes = itemStack.getAttributeModifiers(EquipmentSlotType.OFFHAND);
        boolean meetsMaxHealthRequirement = false, meetsAttackDamageRequirement = false;
        for (Map.Entry<Attribute, AttributeModifier> entry : attributes.entries()) {
            Attribute attribute = entry.getKey();
            AttributeModifier modifier = entry.getValue();
            if (attribute == Attributes.MAX_HEALTH && modifier.getAmount() == -2 && modifier.getOperation() == AttributeModifier.Operation.ADDITION)
                meetsMaxHealthRequirement = true;
            else if (attribute == Attributes.ATTACK_DAMAGE && modifier.getAmount() == 4 && modifier.getOperation() == AttributeModifier.Operation.ADDITION)
                meetsAttackDamageRequirement = true;
        }
        return meetsMaxHealthRequirement && meetsAttackDamageRequirement;
    }

    public boolean FilonaName(ItemStack itemStack) {
        Multimap<Attribute, AttributeModifier> attributes = itemStack.getAttributeModifiers(EquipmentSlotType.OFFHAND);
        boolean meetsMaxHealthRequirement = false, meetsAttackSpeedRequirement = false;
        for (Map.Entry<Attribute, AttributeModifier> entry : attributes.entries()) {
            Attribute attribute = entry.getKey();
            AttributeModifier modifier = entry.getValue();
            if (attribute == Attributes.MAX_HEALTH && modifier.getAmount() == 2 && modifier.getOperation() == AttributeModifier.Operation.ADDITION)
                meetsMaxHealthRequirement = true;
            else if (attribute == Attributes.ATTACK_SPEED && modifier.getAmount() == 0.20 && modifier.getOperation() == AttributeModifier.Operation.MULTIPLY_BASE)
                meetsAttackSpeedRequirement = true;
        }
        return meetsMaxHealthRequirement && meetsAttackSpeedRequirement;
    }

    public boolean SkifaName(ItemStack itemStack) {
        Multimap<Attribute, AttributeModifier> attributes = itemStack.getAttributeModifiers(EquipmentSlotType.OFFHAND);
        boolean meetsSpeedRequirement = false, meetsAttackSpeedRequirement = false, meetsArmorRequirement = false;
        for (Map.Entry<Attribute, AttributeModifier> entry : attributes.entries()) {
            Attribute attribute = entry.getKey();
            AttributeModifier modifier = entry.getValue();
            if (attribute == Attributes.MOVEMENT_SPEED && modifier.getAmount() == 0.10 && modifier.getOperation() == AttributeModifier.Operation.MULTIPLY_BASE)
                meetsSpeedRequirement = true;
            else if (attribute == Attributes.ATTACK_SPEED && modifier.getAmount() == 0.10 && modifier.getOperation() == AttributeModifier.Operation.MULTIPLY_BASE)
                meetsAttackSpeedRequirement = true;
            else if (attribute == Attributes.ARMOR && modifier.getAmount() == -2 && modifier.getOperation() == AttributeModifier.Operation.ADDITION)
                meetsArmorRequirement = true;
        }
        return meetsSpeedRequirement && meetsAttackSpeedRequirement && meetsArmorRequirement;
    }

    public boolean EpionaName(ItemStack itemStack) {
        Multimap<Attribute, AttributeModifier> attributes = itemStack.getAttributeModifiers(EquipmentSlotType.OFFHAND);
        boolean meetsAttackDamageRequirement = false, meetsArmorRequirement = false;
        for (Map.Entry<Attribute, AttributeModifier> entry : attributes.entries()) {
            Attribute attribute = entry.getKey();
            AttributeModifier modifier = entry.getValue();
            if (attribute == Attributes.ATTACK_DAMAGE && modifier.getAmount() == 3 && modifier.getOperation() == AttributeModifier.Operation.ADDITION)
                meetsAttackDamageRequirement = true;
            else if (attribute == Attributes.ARMOR && modifier.getAmount() == -2 && modifier.getOperation() == AttributeModifier.Operation.ADDITION)
                meetsArmorRequirement = true;
        }
        return meetsAttackDamageRequirement && meetsArmorRequirement;
    }

    public boolean OtrigaName(ItemStack itemStack) {
        List<EffectInstance> potionEffects = PotionUtils.getEffectsFromStack(itemStack);

        boolean hasBlindness = false;
        boolean hasGlowing = false;
        boolean hasHunger = false;
        boolean hasSlowness = false;
        boolean hasWither = false;

        for (EffectInstance effectInstance : potionEffects) {
            Effect effect = effectInstance.getPotion();
            if (effect == Effects.BLINDNESS && effectInstance.getDuration() >= 10 * 20) {
                hasBlindness = true;
            } else if (effect == Effects.GLOWING && effectInstance.getDuration() >= 3 * 60 * 20) {
                hasGlowing = true;
            } else if (effect == Effects.HUNGER && effectInstance.getAmplifier() == 10 && effectInstance.getDuration() >= 90 * 20) {
                hasHunger = true;
            } else if (effect == Effects.SLOWNESS && effectInstance.getAmplifier() == 2 && effectInstance.getDuration() >= 3 * 60 * 20) {
                hasSlowness = true;
            } else if (effect == Effects.WITHER && effectInstance.getAmplifier() == 4 && effectInstance.getDuration() >= 30 * 20) {
                hasWither = true;
            }
        }

        return hasBlindness && hasGlowing && hasHunger && hasSlowness && hasWither;
    }

    public boolean KillerName(ItemStack itemStack) {

        List<EffectInstance> potionEffects = PotionUtils.getEffectsFromStack(itemStack);
        boolean hasStrength = false;
        boolean hasResistance = false;

        for (EffectInstance effectInstance : potionEffects) {
            Effect effect = effectInstance.getPotion();

            if (effect == Effects.STRENGTH && effectInstance.getAmplifier() == 4 && effectInstance.getDuration() >= 3 * 60 * 20) {
                hasStrength = true;
            } else if (effect == Effects.RESISTANCE && effectInstance.getAmplifier() == 3 && effectInstance.getDuration() >= 3 * 60 * 20) {
                hasResistance = true;
            }
        }

        return hasStrength && hasResistance;
    }
    public boolean PobedilkaName(ItemStack itemStack) {

        List<EffectInstance> potionEffects = PotionUtils.getEffectsFromStack(itemStack);

        boolean hasHealthBoost = false;
        boolean hasInvisibility = false;
        boolean hasRegeneration = false;
        boolean hasResistance = false;

        for (EffectInstance effectInstance : potionEffects) {
            Effect effect = effectInstance.getPotion();
            int durationTicks = effectInstance.getDuration();
            int durationMinutes = durationTicks / 20 / 60;

            if (effect == Effects.HEALTH_BOOST && effectInstance.getAmplifier() == 1 && durationMinutes >= 3) {
                hasHealthBoost = true;
            } else if (effect == Effects.INVISIBILITY && durationMinutes >= 15) {
                hasInvisibility = true;
            } else if (effect == Effects.REGENERATION && effectInstance.getAmplifier() == 1 && durationMinutes >= 1) {
                hasRegeneration = true;
            } else if (effect == Effects.RESISTANCE && durationMinutes >= 1) {
                hasResistance = true;
            }
        }

        return hasHealthBoost && hasInvisibility && hasRegeneration && hasResistance;
    }
    public boolean MedikaName(ItemStack itemStack) {

        List<EffectInstance> potionEffects = PotionUtils.getEffectsFromStack(itemStack);

        boolean hasHealthBoost = false;
        boolean hasRegeneration = false;

        for (EffectInstance effectInstance : potionEffects) {
            Effect effect = effectInstance.getPotion();
            int durationTicks = effectInstance.getDuration();
            int durationSeconds = durationTicks / 20;

            if (effect == Effects.HEALTH_BOOST && effectInstance.getAmplifier() == 2 && durationSeconds == 45) {
                hasHealthBoost = true;
            } else if (effect == Effects.REGENERATION && effectInstance.getAmplifier() == 2 && durationSeconds == 45) {
                hasRegeneration = true;
            }
        }

        return hasHealthBoost && hasRegeneration;
    }
    public boolean SerkaName(ItemStack itemStack) {

        List<EffectInstance> potionEffects = PotionUtils.getEffectsFromStack(itemStack);

        boolean hasPoison = false;
        boolean hasSlowness = false;
        boolean hasWeakness = false;
        boolean hasWither = false;

        for (EffectInstance effectInstance : potionEffects) {
            Effect effect = effectInstance.getPotion();
            int durationTicks = effectInstance.getDuration();
            int durationSeconds = durationTicks / 20;

            if (effect == Effects.POISON && effectInstance.getAmplifier() == 1 && durationSeconds == 50) {
                hasPoison = true;
            } else if (effect == Effects.SLOWNESS && effectInstance.getAmplifier() == 3 && durationSeconds == 90) {
                hasSlowness = true;
            } else if (effect == Effects.WEAKNESS && effectInstance.getAmplifier() == 2 && durationSeconds == 90) {
                hasWeakness = true;
            } else if (effect == Effects.WITHER && effectInstance.getAmplifier() == 4 && durationSeconds == 30) {
                hasWither = true;
            }
        }

        return hasPoison && hasSlowness && hasWeakness && hasWither;
    }
    public boolean AgentName(ItemStack itemStack) {
        List<EffectInstance> potionEffects = PotionUtils.getEffectsFromStack(itemStack);

        boolean hasInvisibility = false;
        boolean hasHaste = false;
        boolean hasFireResistance = false;
        boolean hasSpeed = false;
        boolean hasStrength = false;

        for (EffectInstance effectInstance : potionEffects) {
            Effect effect = effectInstance.getPotion();
            int durationTicks = effectInstance.getDuration();
            int durationMinutes = durationTicks / 20 / 60;

            if (effect == Effects.INVISIBILITY && effectInstance.getAmplifier() >= 1 && durationMinutes >= 15) {
                hasInvisibility = true;
            } else if (effect == Effects.HASTE && durationMinutes >= 2) {
                hasHaste = true;
            } else if (effect == Effects.FIRE_RESISTANCE && durationMinutes >= 15) {
                hasFireResistance = true;
            } else if (effect == Effects.SPEED && effectInstance.getAmplifier() >= 1 && durationMinutes >= 15) {
                hasSpeed = true;
            } else if (effect == Effects.STRENGTH && effectInstance.getAmplifier() >= 2 && durationMinutes >= 5) {
                hasStrength = true;
            }
        }

        return hasInvisibility && hasHaste && hasFireResistance && hasSpeed && hasStrength;
    }
    public boolean isEgida(ItemStack itemStack) {
        Multimap<Attribute, AttributeModifier> attributes = itemStack.getAttributeModifiers(EquipmentSlotType.OFFHAND);
        boolean meetsArmorToughnessRequirement = false, meetsArmorRequirement = false;
        for (Map.Entry<Attribute, AttributeModifier> entry : attributes.entries()) {
            Attribute attribute = entry.getKey();
            AttributeModifier modifier = entry.getValue();
            if (attribute == Attributes.ARMOR_TOUGHNESS && modifier.getAmount() == 2 && modifier.getOperation() == AttributeModifier.Operation.ADDITION)
                meetsArmorToughnessRequirement = true;
            else if (attribute == Attributes.ARMOR && modifier.getAmount() == 2 && modifier.getOperation() == AttributeModifier.Operation.ADDITION)
                meetsArmorRequirement = true;
            else
                return false;
        }
        return meetsArmorToughnessRequirement && meetsArmorRequirement;
    }
    public boolean isSilka(ItemStack itemStack) {
        List<EffectInstance> potionEffects = PotionUtils.getEffectsFromStack(itemStack);

        for (EffectInstance effectInstance : potionEffects) {
            Effect effect = effectInstance.getPotion();
            int durationTicks = effectInstance.getDuration();
            int durationSeconds = durationTicks / 20;

            if (effect == Effects.STRENGTH && effectInstance.getAmplifier() >= 2 && durationSeconds >= 60) {
                return true;
            }
        }

        return false;
    }
    public boolean isSkorka(ItemStack itemStack) {
        List<EffectInstance> potionEffects = PotionUtils.getEffectsFromStack(itemStack);

        for (EffectInstance effectInstance : potionEffects) {
            Effect effect = effectInstance.getPotion();
            int durationTicks = effectInstance.getDuration();
            int durationSeconds = durationTicks / 20;

            if (effect == Effects.SPEED && effectInstance.getAmplifier() >= 2 && durationSeconds >= 60) {
                return true;
            }
        }

        return false;
    }
}
