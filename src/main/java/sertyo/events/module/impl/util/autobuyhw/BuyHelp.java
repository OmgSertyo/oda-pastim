package sertyo.events.module.impl.util.autobuyhw;

import com.google.common.collect.Multimap;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.text.ITextComponent;

public class BuyHelp {
    public static boolean booteternity(ItemStack itemStack) {
        if (itemStack.getItem() != Items.NETHERITE_BOOTS)
            return false;
        Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(itemStack);
        boolean hasBlastProtection = (enchantments.containsKey(Enchantments.BLAST_PROTECTION) && enchantments.get(Enchantments.BLAST_PROTECTION) >= 5);
        boolean hasDepthStrider = (enchantments.containsKey(Enchantments.DEPTH_STRIDER) && enchantments.get(Enchantments.DEPTH_STRIDER) >= 3);
        boolean hasFeatherFalling = (enchantments.containsKey(Enchantments.FEATHER_FALLING) && enchantments.get(Enchantments.FEATHER_FALLING) >= 4);
        boolean hasFireProtection = (enchantments.containsKey(Enchantments.FIRE_PROTECTION) && enchantments.get(Enchantments.FIRE_PROTECTION) >= 5);
        boolean hasMending = enchantments.containsKey(Enchantments.MENDING);
        boolean hasProjectileProtection = (enchantments.containsKey(Enchantments.PROJECTILE_PROTECTION) && enchantments.get(Enchantments.PROJECTILE_PROTECTION) >= 5);
        boolean hasProtection = (enchantments.containsKey(Enchantments.PROTECTION) && enchantments.get(Enchantments.PROTECTION) >= 5);
        boolean hasSoulSpeed = (enchantments.containsKey(Enchantments.SOUL_SPEED) && enchantments.get(Enchantments.SOUL_SPEED) >= 3);
        boolean hasUnbreaking = (enchantments.containsKey(Enchantments.UNBREAKING) && enchantments.get(Enchantments.UNBREAKING) >= 5);
        boolean hasThorns = (enchantments.containsKey(Enchantments.THORNS) && enchantments.get(Enchantments.THORNS) >= 3);
        
        boolean meetsRequirements = (hasBlastProtection && hasDepthStrider && hasFeatherFalling && hasFireProtection && hasMending && hasProjectileProtection && hasProtection && hasSoulSpeed && hasUnbreaking && hasThorns);
        return (meetsRequirements && hasHelmet(itemStack));
    }

    public static boolean taliceternity(ItemStack itemStack) {
        Multimap<Attribute, AttributeModifier> attributes = itemStack.getAttributeModifiers(EquipmentSlotType.OFFHAND);
        boolean meetsSpeedRequirement = false, meetsMaxHealthRequirement = false, meetsAttackDamageRequirement = false;

        for (Map.Entry<Attribute, AttributeModifier> entry : attributes.entries()) {
            Attribute attribute = entry.getKey();
            AttributeModifier modifier = entry.getValue();
            if (attribute == Attributes.MOVEMENT_SPEED && modifier.getAmount() == 0.2D && modifier.getOperation() == AttributeModifier.Operation.MULTIPLY_BASE) {
                meetsSpeedRequirement = true;
                continue;
            }
            if (attribute == Attributes.MAX_HEALTH && modifier.getAmount() == 2.0D && modifier.getOperation() == AttributeModifier.Operation.ADDITION) {
                meetsMaxHealthRequirement = true;
                continue;
            }
            if (attribute == Attributes.ATTACK_DAMAGE && modifier.getAmount() == 2.0D && modifier.getOperation() == AttributeModifier.Operation.ADDITION)
                meetsAttackDamageRequirement = true;
        }
        
        return (meetsSpeedRequirement && meetsMaxHealthRequirement && meetsAttackDamageRequirement);
    }

    public static boolean talicStringer(ItemStack itemStack) {
        Multimap<Attribute, AttributeModifier> attributes = itemStack.getAttributeModifiers(EquipmentSlotType.OFFHAND);
        boolean meetsSpeedRequirement = false, meetsMaxHealthRequirement = false, meetsAttackDamageRequirement = false;

        for (Map.Entry<Attribute, AttributeModifier> entry : attributes.entries()) {
            Attribute attribute = entry.getKey();
            AttributeModifier modifier = entry.getValue();
            if (attribute == Attributes.MOVEMENT_SPEED && modifier.getAmount() == 0.1D && modifier.getOperation() == AttributeModifier.Operation.MULTIPLY_BASE) {
                meetsSpeedRequirement = true;
                continue;
            }
            if (attribute == Attributes.MAX_HEALTH && modifier.getAmount() == 2.0D && modifier.getOperation() == AttributeModifier.Operation.ADDITION) {
                meetsMaxHealthRequirement = true;
                continue;
            }
            if (attribute == Attributes.ATTACK_DAMAGE && modifier.getAmount() == 2.0D && modifier.getOperation() == AttributeModifier.Operation.ADDITION)
                meetsAttackDamageRequirement = true;
        }
        
        return (meetsSpeedRequirement && meetsMaxHealthRequirement && meetsAttackDamageRequirement);
    }

    public static boolean bootinfinty(ItemStack itemStack) {
        if (itemStack.getItem() != Items.NETHERITE_BOOTS)
            return false;
        Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(itemStack);
        boolean hasBlastProtection = (enchantments.containsKey(Enchantments.BLAST_PROTECTION) && enchantments.get(Enchantments.BLAST_PROTECTION) >= 5);
        boolean hasDepthStrider = (enchantments.containsKey(Enchantments.DEPTH_STRIDER) && enchantments.get(Enchantments.DEPTH_STRIDER) >= 3);
        boolean hasFeatherFalling = (enchantments.containsKey(Enchantments.FEATHER_FALLING) && enchantments.get(Enchantments.FEATHER_FALLING) >= 4);
        boolean hasFireProtection = (enchantments.containsKey(Enchantments.FIRE_PROTECTION) && enchantments.get(Enchantments.FIRE_PROTECTION) >= 5);
        boolean hasMending = enchantments.containsKey(Enchantments.MENDING);
        boolean hasProjectileProtection = (enchantments.containsKey(Enchantments.PROJECTILE_PROTECTION) && enchantments.get(Enchantments.PROJECTILE_PROTECTION) >= 5);
        boolean hasProtection = (enchantments.containsKey(Enchantments.PROTECTION) && enchantments.get(Enchantments.PROTECTION) >= 5);
        boolean hasSoulSpeed = (enchantments.containsKey(Enchantments.SOUL_SPEED) && enchantments.get(Enchantments.SOUL_SPEED) >= 3);
        boolean hasUnbreaking = (enchantments.containsKey(Enchantments.UNBREAKING) && enchantments.get(Enchantments.UNBREAKING) >= 5);
        boolean hasThorns = (enchantments.containsKey(Enchantments.THORNS) && enchantments.get(Enchantments.THORNS) >= 3);
        
        boolean meetsRequirements = (hasBlastProtection && hasDepthStrider && hasFeatherFalling && hasFireProtection && hasMending && hasProjectileProtection && hasProtection && hasSoulSpeed && hasUnbreaking && !hasThorns);
        return (meetsRequirements && hasHelmet(itemStack));
    }

    public static boolean legeternity(ItemStack itemStack) {
        if (itemStack.getItem() != Items.NETHERITE_LEGGINGS)
            return false;
        Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(itemStack);
        boolean hasBlastProtection = (enchantments.containsKey(Enchantments.BLAST_PROTECTION) && enchantments.get(Enchantments.BLAST_PROTECTION) >= 5);
        boolean hasFireProtection = (enchantments.containsKey(Enchantments.FIRE_PROTECTION) && enchantments.get(Enchantments.FIRE_PROTECTION) >= 5);
        boolean hasMending = enchantments.containsKey(Enchantments.MENDING);
        boolean hasProjectileProtection = (enchantments.containsKey(Enchantments.PROJECTILE_PROTECTION) && enchantments.get(Enchantments.PROJECTILE_PROTECTION) >= 5);
        boolean hasProtection = (enchantments.containsKey(Enchantments.PROTECTION) && enchantments.get(Enchantments.PROTECTION) >= 5);
        boolean hasUnbreaking = (enchantments.containsKey(Enchantments.UNBREAKING) && enchantments.get(Enchantments.UNBREAKING) >= 5);
        boolean hasThorns = (enchantments.containsKey(Enchantments.THORNS) && enchantments.get(Enchantments.THORNS) >= 3);
        
        boolean meetsRequirements = (hasBlastProtection && hasFireProtection && hasMending && hasProjectileProtection && hasProtection && hasUnbreaking && !hasThorns);
        return (meetsRequirements && hasHelmet(itemStack));
    }

    public static boolean leginsinf(ItemStack itemStack) {
        if (itemStack.getItem() != Items.NETHERITE_LEGGINGS)
            return false;
        Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(itemStack);
        boolean hasBlastProtection = (enchantments.containsKey(Enchantments.BLAST_PROTECTION) && enchantments.get(Enchantments.BLAST_PROTECTION) >= 5);
        boolean hasFireProtection = (enchantments.containsKey(Enchantments.FIRE_PROTECTION) && enchantments.get(Enchantments.FIRE_PROTECTION) >= 5);
        boolean hasMending = enchantments.containsKey(Enchantments.MENDING);
        boolean hasProjectileProtection = (enchantments.containsKey(Enchantments.PROJECTILE_PROTECTION) && enchantments.get(Enchantments.PROJECTILE_PROTECTION) >= 5);
        boolean hasProtection = (enchantments.containsKey(Enchantments.PROTECTION) && enchantments.get(Enchantments.PROTECTION) >= 5);
        boolean hasUnbreaking = (enchantments.containsKey(Enchantments.UNBREAKING) && enchantments.get(Enchantments.UNBREAKING) >= 5);
        boolean hasThorns = (enchantments.containsKey(Enchantments.THORNS) && enchantments.get(Enchantments.THORNS) >= 3);
        
        boolean meetsRequirements = (hasBlastProtection && hasFireProtection && hasMending && hasProjectileProtection && hasProtection && hasUnbreaking && !hasThorns);
        return (meetsRequirements && hasHelmet(itemStack));
    }

    public static boolean chestEternity(ItemStack itemStack) {
        if (itemStack.getItem() != Items.NETHERITE_CHESTPLATE)
            return false;
        Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(itemStack);
        boolean hasBlastProtection = (enchantments.containsKey(Enchantments.BLAST_PROTECTION) && enchantments.get(Enchantments.BLAST_PROTECTION) >= 5);
        boolean hasFireProtection = (enchantments.containsKey(Enchantments.FIRE_PROTECTION) && enchantments.get(Enchantments.FIRE_PROTECTION) >= 5);
        boolean hasMending = enchantments.containsKey(Enchantments.MENDING);
        boolean hasProjectileProtection = (enchantments.containsKey(Enchantments.PROJECTILE_PROTECTION) && enchantments.get(Enchantments.PROJECTILE_PROTECTION) >= 5);
        boolean hasProtection = (enchantments.containsKey(Enchantments.PROTECTION) && enchantments.get(Enchantments.PROTECTION) >= 5);
        boolean hasRespiration = (enchantments.containsKey(Enchantments.RESPIRATION) && enchantments.get(Enchantments.RESPIRATION) >= 3);
        boolean hasUnbreaking = (enchantments.containsKey(Enchantments.UNBREAKING) && enchantments.get(Enchantments.UNBREAKING) >= 5);
        boolean hasThorns = (enchantments.containsKey(Enchantments.THORNS) && enchantments.get(Enchantments.THORNS) >= 3);
        
        boolean meetsRequirements = (hasBlastProtection && hasFireProtection && hasMending && hasProjectileProtection && hasProtection && hasRespiration && hasUnbreaking && hasThorns);
        return (meetsRequirements && hasHelmet(itemStack));
    }

    public static boolean isInfinityChest(ItemStack itemStack) {
        if (itemStack.getItem() != Items.NETHERITE_CHESTPLATE)
            return false;
        Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(itemStack);
        boolean hasBlastProtection = (enchantments.containsKey(Enchantments.BLAST_PROTECTION) && enchantments.get(Enchantments.BLAST_PROTECTION) >= 5);
        boolean hasFireProtection = (enchantments.containsKey(Enchantments.FIRE_PROTECTION) && enchantments.get(Enchantments.FIRE_PROTECTION) >= 5);
        boolean hasMending = enchantments.containsKey(Enchantments.MENDING);
        boolean hasProjectileProtection = (enchantments.containsKey(Enchantments.PROJECTILE_PROTECTION) && enchantments.get(Enchantments.PROJECTILE_PROTECTION) >= 5);
        boolean hasProtection = (enchantments.containsKey(Enchantments.PROTECTION) && enchantments.get(Enchantments.PROTECTION) >= 5);
        boolean hasRespiration = (enchantments.containsKey(Enchantments.RESPIRATION) && enchantments.get(Enchantments.RESPIRATION) >= 3);
        boolean hasUnbreaking = (enchantments.containsKey(Enchantments.UNBREAKING) && enchantments.get(Enchantments.UNBREAKING) >= 5);
        boolean hasThorns = (enchantments.containsKey(Enchantments.THORNS) && enchantments.get(Enchantments.THORNS) >= 3);
        
        boolean meetsRequirements = (hasBlastProtection && hasFireProtection && hasMending && hasProjectileProtection && hasProtection && hasRespiration && hasUnbreaking && !hasThorns);
        return (meetsRequirements && hasHelmet(itemStack));
    }

    public static boolean isEternitySword(ItemStack itemStack) {
        if (itemStack.getItem() != Items.NETHERITE_SWORD)
            return false;
        Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(itemStack);
        boolean hasBaneOfArthropods = (enchantments.containsKey(Enchantments.BANE_OF_ARTHROPODS) && enchantments.get(Enchantments.BANE_OF_ARTHROPODS) == 7);
        boolean hasFireAspect = (enchantments.containsKey(Enchantments.FIRE_ASPECT) && enchantments.get(Enchantments.FIRE_ASPECT) == 2);
        boolean hasLooting = (enchantments.containsKey(Enchantments.LOOTING) && enchantments.get(Enchantments.LOOTING) == 5);
        boolean hasMending = enchantments.containsKey(Enchantments.MENDING);
        boolean hasSharpness = (enchantments.containsKey(Enchantments.SHARPNESS) && enchantments.get(Enchantments.SHARPNESS) == 7);
        boolean hasSmite = (enchantments.containsKey(Enchantments.SMITE) && enchantments.get(Enchantments.SMITE) == 7);
        boolean hasSweepingEdge = (enchantments.containsKey(Enchantments.SWEEPING) && enchantments.get(Enchantments.SWEEPING) == 3);
        boolean hasUnbreaking = (enchantments.containsKey(Enchantments.UNBREAKING) && enchantments.get(Enchantments.UNBREAKING) == 5);
        
        boolean meetsRequirements = (hasBaneOfArthropods && hasFireAspect && hasMending && hasLooting && hasSharpness && hasSmite && hasSweepingEdge && hasUnbreaking);
        return (meetsRequirements && hasSword(itemStack));
    }

    private static final List<String> requiredTooltipsSword = Arrays.asList("II", "I", "II");

    public static boolean hasSword(ItemStack stack) {
        List<ITextComponent> tooltips = stack.getTooltip(Minecraft.getInstance().player, ITooltipFlag.TooltipFlags.NORMAL);
        for (ITextComponent text : tooltips) {
            for (String tooltip : requiredTooltipsSword) {
                if (text.getString().contains(tooltip))
                    return true;
            }
        }
        return false;
    }

    public static boolean isKirkaKrush(ItemStack itemStack) {
        if (itemStack.getItem() != Items.NETHERITE_PICKAXE)
            return false;
        Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(itemStack);
        boolean hasEfficiency = (enchantments.containsKey(Enchantments.EFFICIENCY) && enchantments.get(Enchantments.EFFICIENCY) == 10);
        boolean hasFortune = (enchantments.containsKey(Enchantments.FORTUNE) && enchantments.get(Enchantments.FORTUNE) == 5);
        boolean hasMending = enchantments.containsKey(Enchantments.MENDING);
        boolean hasUnbreaking = (enchantments.containsKey(Enchantments.UNBREAKING) && enchantments.get(Enchantments.UNBREAKING) == 5);
        
        boolean meetsRequirements = (hasEfficiency && hasFortune && hasMending && hasUnbreaking);
        return (meetsRequirements && hasPickaxe(itemStack));
    }

    private static final List<String> requiredTooltipsPickaxe = Arrays.asList("I", "1", "III", "II", "I");

    public static boolean hasPickaxe(ItemStack stack) {
        List<ITextComponent> tooltips = stack.getTooltip(Minecraft.getInstance().player, ITooltipFlag.TooltipFlags.NORMAL);
        Set<String> foundTooltips = new HashSet<>();
        for (ITextComponent text : tooltips) {
            if (text != null)
                foundTooltips.add(text.getString());
        }
        for (String tooltip : requiredTooltipsPickaxe) {
            if (!foundTooltips.contains(tooltip))
                return false;
        }
        return true;
    }

    public static boolean isBootsZ5(ItemStack itemStack) {
        if (itemStack.getItem() != Items.NETHERITE_BOOTS)
            return false;
        Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(itemStack);
        boolean hasProtection = (enchantments.containsKey(Enchantments.PROTECTION) && enchantments.get(Enchantments.PROTECTION) == 5);
        return hasProtection;
    }

    public static boolean isLegginsZ5(ItemStack itemStack) {
        if (itemStack.getItem() != Items.NETHERITE_LEGGINGS)
            return false;
        Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(itemStack);
        boolean hasProtection = (enchantments.containsKey(Enchantments.PROTECTION) && enchantments.get(Enchantments.PROTECTION) == 5);
        return hasProtection;
    }

    public static boolean isChestZ5(ItemStack itemStack) {
        if (itemStack.getItem() != Items.NETHERITE_CHESTPLATE)
            return false;
        Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(itemStack);
        boolean hasProtection = (enchantments.containsKey(Enchantments.PROTECTION) && enchantments.get(Enchantments.PROTECTION) == 5);
        return hasProtection;
    }

    public static boolean isHelmetZ5(ItemStack itemStack) {
        if (itemStack.getItem() != Items.NETHERITE_HELMET)
            return false;
        Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(itemStack);
        boolean hasProtection = (enchantments.containsKey(Enchantments.PROTECTION) && enchantments.get(Enchantments.PROTECTION) == 5);
        return hasProtection;
    }

    public static boolean ShlemEternity(ItemStack itemStack) {
        if (itemStack.getItem() != Items.NETHERITE_HELMET)
            return false;
        Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(itemStack);
        boolean hasAquaAffinity = enchantments.containsKey(Enchantments.AQUA_AFFINITY);
        boolean hasBlastProtection = (enchantments.containsKey(Enchantments.BLAST_PROTECTION) && enchantments.get(Enchantments.BLAST_PROTECTION) >= 5);
        boolean hasFireProtection = (enchantments.containsKey(Enchantments.FIRE_PROTECTION) && enchantments.get(Enchantments.FIRE_PROTECTION) >= 5);
        boolean hasMending = enchantments.containsKey(Enchantments.MENDING);
        boolean hasProjectileProtection = (enchantments.containsKey(Enchantments.PROJECTILE_PROTECTION) && enchantments.get(Enchantments.PROJECTILE_PROTECTION) >= 5);
        boolean hasProtection = (enchantments.containsKey(Enchantments.PROTECTION) && enchantments.get(Enchantments.PROTECTION) >= 5);
        boolean hasRespiration = (enchantments.containsKey(Enchantments.RESPIRATION) && enchantments.get(Enchantments.RESPIRATION) >= 3);
        boolean hasUnbreaking = (enchantments.containsKey(Enchantments.UNBREAKING) && enchantments.get(Enchantments.UNBREAKING) >= 5);
        boolean hasThorns = (enchantments.containsKey(Enchantments.THORNS) && enchantments.get(Enchantments.THORNS) >= 3);
        
        boolean meetsRequirements = (hasAquaAffinity && hasBlastProtection && hasThorns && hasFireProtection && hasMending && hasProjectileProtection && hasProtection && hasRespiration && hasUnbreaking);
        return (meetsRequirements && hasHelmet(itemStack));
    }
    private static final List<String> containsTooltips = Arrays.asList(new String[] { "Непробиваемый I" });

    public static boolean hasHelmet(ItemStack stack) {
        List<ITextComponent> tooltips = stack.getTooltip(Minecraft.getInstance().player, ITooltipFlag.TooltipFlags.NORMAL);        Set<String> foundTooltips = new HashSet<>();
        for (ITextComponent text : tooltips) {
            if (text != null)
                foundTooltips.add(text.getString());
        }
        for (String tooltip : containsTooltips) {
            if (!foundTooltips.contains(tooltip))
                return false;
        }
        return true;
    }
}