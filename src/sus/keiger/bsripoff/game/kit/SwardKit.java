package sus.keiger.bsripoff.game.kit;

import net.kyori.adventure.text.Component;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class SwardKit extends Kit
{
    // Private fields.
    private int _superTimer;


    // Constructors.
    public SwardKit()
    {
        super(KitClass.Grass,
                new ItemStack(Material.IRON_SWORD, 1),
                "Sward",
                "A simple yet versatile brawler. Swards wears basic armor, wields a basic sword, but has" +
                        "an easily chargeable super which temporarily boosts its offensive and defensive capabilities.",
                new KitStats(3, 3, 2, 5));

        //AddUpgrade(new KitUpgradeDefinition("Health", "Sward's health points", 30, 10, 1f, 30, 0, 1.35f, 6));

        SetMaxHealth(20d);
        SetMovementSpeed(0.1d);
        SetArmor(0d);
        SetMeleeAttackSpeed(0d);
        SetKnockbackResistance(0d);
    }


    // Private methods.
    private void EnableSuper(KitInstance instance)
    {
        ItemStack Helmet = instance.GetItemByMaterial(Material.IRON_HELMET);
        ItemStack Chestplate = instance.GetItemByMaterial(Material.IRON_CHESTPLATE);
        ItemStack Leggings = instance.GetItemByMaterial(Material.IRON_LEGGINGS);
        ItemStack Boots = instance.GetItemByMaterial(Material.IRON_BOOTS);
        ItemStack Sword = instance.GetItemByMaterial(Material.IRON_SWORD);

        Helmet.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
        Helmet.addEnchantment(Enchantment.OXYGEN, 1);
        Chestplate.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
        Leggings.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
        Boots.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
        Boots.addEnchantment(Enchantment.PROTECTION_FALL, 1);

        Sword.addEnchantment(Enchantment.DAMAGE_ALL, 2);

        instance.MCPlayer.getWorld().playSound(instance.MCPlayer.getLocation(), Sound.ITEM_ARMOR_EQUIP_IRON,
                SoundCategory.PLAYERS, 1f, 1f);
    }

    private void DisableSuper(KitInstance instance)
    {
        ItemStack Helmet = instance.GetItemByMaterial(Material.IRON_HELMET);
        ItemStack Chestplate = instance.GetItemByMaterial(Material.IRON_CHESTPLATE);
        ItemStack Leggings = instance.GetItemByMaterial(Material.IRON_LEGGINGS);
        ItemStack Boots = instance.GetItemByMaterial(Material.IRON_BOOTS);
        ItemStack Sword = instance.GetItemByMaterial(Material.IRON_SWORD);

        Helmet.removeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL);
        Helmet.removeEnchantment(Enchantment.OXYGEN);
        Chestplate.removeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL);
        Leggings.removeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL);
        Boots.removeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL);
        Boots.removeEnchantment(Enchantment.PROTECTION_FALL);

        Sword.removeEnchantment(Enchantment.DAMAGE_ALL);

        instance.MCPlayer.getWorld().playSound(instance.MCPlayer.getLocation(), Sound.ITEM_ARMOR_EQUIP_IRON,
                SoundCategory.PLAYERS, 1f, 1f);
    }

    private void SuperTick(KitInstance instance)
    {
        _superTimer--;
        if (_superTimer == 0)
        {
            DisableSuper(instance);
        }

        instance.MCPlayer.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE,
                instance.MCPlayer.getLocation().clone().add(0d, 1d, 0d), 1, 0.1d, 0.1d, 0.1d, 1d);
    }


    // Inherited methods.
    @Override
    public void CreateInventory(KitInstance instance, Inventory inventory)
    {
        ItemStack Helmet = new ItemStack(Material.IRON_HELMET, 1);

        FormatEquipment(Helmet, true, Component.text("Helmet"));
        ItemStack Chestplate = new ItemStack(Material.IRON_CHESTPLATE, 1);
        FormatEquipment(Chestplate, true, Component.text("Chestplate"));
        ItemStack Leggings = new ItemStack(Material.IRON_LEGGINGS, 1);
        FormatEquipment(Leggings, true, Component.text("Leggings"));
        ItemStack Boots = new ItemStack(Material.IRON_BOOTS, 1);
        FormatEquipment(Boots, true, Component.text("Boots"));

        ItemStack Sword = new ItemStack(Material.IRON_SWORD, 1);
        FormatEquipment(Sword, true, Component.text("Sword"));
        Sword.removeAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE);
        Sword.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE,
                new AttributeModifier("damage", 6d, AttributeModifier.Operation.ADD_NUMBER));
        Sword.addAttributeModifier(Attribute.GENERIC_ATTACK_SPEED,
                new AttributeModifier("speed", 1.6d, AttributeModifier.Operation.ADD_NUMBER));

        inventory.setItem(SLOT_ARMOR_HEAD, Helmet);
        inventory.setItem(SLOT_ARMOR_CHEST, Chestplate);
        inventory.setItem(SLOT_ARMOR_LEGS, Leggings);
        inventory.setItem(SLOT_ARMOR_FEET, Boots);
        inventory.setItem(SLOT_HOTBAR1, Sword);
        instance.SetSuperItem(SLOT_HOTBAR2);

        SetMaxSuperCharge(100);
    }

    /* Events. */
    @Override
    public void OnLoadEvent(KitInstance instance)
    {
        super.OnLoadEvent(instance);

        if (_superTimer >= 0)
        {
            DisableSuper(instance);
        }
        _superTimer = -1;
    }

    @Override
    public void ActivateSuper(KitInstance instance)
    {
        super.ActivateSuper(instance);

        _superTimer = 160;
        EnableSuper(instance);
    }

    @Override
    public void OnTickEvent(KitInstance instance)
    {
        super.OnTickEvent(instance);

        if (_superTimer >= 0)
        {
            SuperTick(instance);
        }
    }

    @Override
    public void OnPlayerDamageEntityEvent(KitInstance instance, EntityDamageByEntityEvent event)
    {
        super.OnPlayerDamageEntityEvent(instance, event);

        if (instance.MCPlayer.getAttackCooldown() < 0.75f)
        {
            return;
        }

        instance.ChargeSuper(instance.IsEligibleForCritical() ? 15 : 10);
    }
}
