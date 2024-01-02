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
import org.bukkit.inventory.ItemStack;
import sus.keiger.bsripoff.game.Game;
import sus.keiger.bsripoff.game.GamePlayer;

public class SwardKitInstance extends KitInstance
{
    // Private fields.
    private int _superTimer;


    // Constructors.
    public SwardKitInstance(GamePlayer gamePlayer, Game game)
    {
        super(gamePlayer, game, Kit.SWARD);

        GetMaxHealth().SetBaseValue(20d + GPlayer.GetPlayerGameData().GetPlayerKitData(Kit.SWARD)
                .GetUpgradeLevel(SwardKit.UPGRADE_HEALTH_NAME).GetEquipped() - 1 * 0.5d);
    }


    // Private methods.
    private void EnableSuper()
    {
        ItemStack Helmet = GetItemByMaterial(Material.IRON_HELMET);
        ItemStack Chestplate = GetItemByMaterial(Material.IRON_CHESTPLATE);
        ItemStack Leggings = GetItemByMaterial(Material.IRON_LEGGINGS);
        ItemStack Boots = GetItemByMaterial(Material.IRON_BOOTS);
        ItemStack Sword = GetItemByMaterial(Material.IRON_SWORD);

        Helmet.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
        Helmet.addEnchantment(Enchantment.OXYGEN, 1);
        Chestplate.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
        Leggings.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
        Boots.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
        Boots.addEnchantment(Enchantment.PROTECTION_FALL, 1);

        Sword.addEnchantment(Enchantment.DAMAGE_ALL, 2);

        MCPlayer.getWorld().playSound(MCPlayer.getLocation(), Sound.ITEM_ARMOR_EQUIP_IRON,
                SoundCategory.PLAYERS, 1f, 1f);
    }


    private void DisableSuper()
    {
        ItemStack Helmet = GetItemByMaterial(Material.IRON_HELMET);
        ItemStack Chestplate = GetItemByMaterial(Material.IRON_CHESTPLATE);
        ItemStack Leggings = GetItemByMaterial(Material.IRON_LEGGINGS);
        ItemStack Boots = GetItemByMaterial(Material.IRON_BOOTS);
        ItemStack Sword = GetItemByMaterial(Material.IRON_SWORD);

        Helmet.removeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL);
        Helmet.removeEnchantment(Enchantment.OXYGEN);
        Chestplate.removeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL);
        Leggings.removeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL);
        Boots.removeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL);
        Boots.removeEnchantment(Enchantment.PROTECTION_FALL);

        Sword.removeEnchantment(Enchantment.DAMAGE_ALL);

        MCPlayer.getWorld().playSound(MCPlayer.getLocation(), Sound.ITEM_ARMOR_EQUIP_IRON,
                SoundCategory.PLAYERS, 1f, 1f);
    }

    private void SuperTick()
    {
        if (_superTimer == 0)
        {
            return;
        }

        _superTimer--;
        if (_superTimer > 0)
        {
            MCPlayer.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE,
                    MCPlayer.getLocation().clone().add(0d, 1d, 0d), 1, 0.1d, 0.1d, 0.1d, 1d);
        }
        else if (_superTimer == 0)
        {
            DisableSuper();
        }
    }


    // Inherited methods.
    @Override
    public void CreateInventory()
    {
        ItemStack Helmet = new ItemStack(Material.IRON_HELMET, 1);

        Kit.FormatEquipment(Helmet, true, Component.text("Helmet"));
        ItemStack Chestplate = new ItemStack(Material.IRON_CHESTPLATE, 1);
        Kit.FormatEquipment(Chestplate, true, Component.text("Chestplate"));
        ItemStack Leggings = new ItemStack(Material.IRON_LEGGINGS, 1);
        Kit.FormatEquipment(Leggings, true, Component.text("Leggings"));
        ItemStack Boots = new ItemStack(Material.IRON_BOOTS, 1);
        Kit.FormatEquipment(Boots, true, Component.text("Boots"));

        ItemStack Sword = new ItemStack(Material.IRON_SWORD, 1);
        Kit.FormatEquipment(Sword, true, Component.text("Sword"));
        Sword.removeAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE);
        Sword.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE,
                new AttributeModifier("damage", 6d, AttributeModifier.Operation.ADD_NUMBER));
        Sword.addAttributeModifier(Attribute.GENERIC_ATTACK_SPEED,
                new AttributeModifier("speed", -2.4d, AttributeModifier.Operation.ADD_NUMBER));

        MCPlayer.getInventory().setItem(Kit.SLOT_ARMOR_HEAD, Helmet);
        MCPlayer.getInventory().setItem(Kit.SLOT_ARMOR_CHEST, Chestplate);
        MCPlayer.getInventory().setItem(Kit.SLOT_ARMOR_LEGS, Leggings);
        MCPlayer.getInventory().setItem(Kit.SLOT_ARMOR_FEET, Boots);
        MCPlayer.getInventory().setItem(Kit.SLOT_HOTBAR1, Sword);
        SetSuperItem(Kit.SLOT_HOTBAR2);
    }

    /* Events. */
    @Override
    public void OnLoadEvent()
    {
        super.OnLoadEvent();
        _superTimer = 0;
    }

    @Override
    public void ActivateSuper()
    {
        super.ActivateSuper();

        _superTimer = 160;
        EnableSuper();
    }

    @Override
    public void OnTickEvent()
    {
        super.OnTickEvent();
        SuperTick();
    }

    @Override
    public void OnPlayerDamageEntityEvent(EntityDamageByEntityEvent event)
    {
        super.OnPlayerDamageEntityEvent(event);

        if (MCPlayer.getAttackCooldown() < 0.75f)
        {
            return;
        }

        ChargeSuper(IsAttackCritical() ? 15 : 10);
    }
}
