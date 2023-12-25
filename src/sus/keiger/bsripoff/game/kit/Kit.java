package sus.keiger.bsripoff.game.kit;

import org.apache.commons.lang.NullArgumentException;
import org.bukkit.GameMode;
import org.bukkit.attribute.Attribute;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.*;


public abstract class Kit
{
    // Static fields.
    /* Inventory slots. */
    /* https://www.spigotmc.org/wiki/raw-slot-ids/ */
    public static final int SLOT_RECIPE_RESULT = 0;
    public static final int SLOT_RECIPE_TOP_LEFT = 1;
    public static final int SLOT_RECIPE_TOP_RIGHT = 2;
    public static final int SLOT_RECIPE_BOTTOM_LEFT = 3;
    public static final int SLOT_RECIPE_BOTTOM_RIGHT = 4;

    public static final int SLOT_ARMOR_HEAD = 5;
    public static final int SLOT_ARMOR_CHEST = 6;
    public static final int SLOT_ARMOR_LEGS = 7;
    public static final int SLOT_ARMOR_FEET = 8;

    public static final int SLOT_ROW1_COLUMN1 = 9;
    public static final int SLOT_ROW1_COLUMN2 = 10;
    public static final int SLOT_ROW1_COLUMN3 = 11;
    public static final int SLOT_ROW1_COLUMN4 = 12;
    public static final int SLOT_ROW1_COLUMN5 = 13;
    public static final int SLOT_ROW1_COLUMN6 = 14;
    public static final int SLOT_ROW1_COLUMN7 = 15;
    public static final int SLOT_ROW1_COLUMN8 = 16;
    public static final int SLOT_ROW1_COLUMN9 = 17;
    public static final int SLOT_ROW2_COLUMN1 = 18;
    public static final int SLOT_ROW2_COLUMN2 = 19;
    public static final int SLOT_ROW2_COLUMN3 = 20;
    public static final int SLOT_ROW2_COLUMN4 = 21;
    public static final int SLOT_ROW2_COLUMN5 = 22;
    public static final int SLOT_ROW2_COLUMN6 = 23;
    public static final int SLOT_ROW2_COLUMN7 = 24;
    public static final int SLOT_ROW2_COLUMN8 = 25;
    public static final int SLOT_ROW2_COLUMN9 = 26;
    public static final int SLOT_ROW3_COLUMN1 = 27;
    public static final int SLOT_ROW3_COLUMN2 = 28;
    public static final int SLOT_ROW3_COLUMN3 = 29;
    public static final int SLOT_ROW3_COLUMN4 = 30;
    public static final int SLOT_ROW3_COLUMN5 = 31;
    public static final int SLOT_ROW3_COLUMN6 = 32;
    public static final int SLOT_ROW3_COLUMN7 = 33;
    public static final int SLOT_ROW3_COLUMN8 = 34;
    public static final int SLOT_ROW3_COLUMN9 = 35;

    public static final int SLOT_HOTBAR1 = 36;
    public static final int SLOT_HOTBAR2 = 37;
    public static final int SLOT_HOTBAR3 = 38;
    public static final int SLOT_HOTBAR4 = 39;
    public static final int SLOT_HOTBAR5 = 40;
    public static final int SLOT_HOTBAR6 = 41;
    public static final int SLOT_HOTBAR7 = 42;
    public static final int SLOT_HOTBAR8 = 43;
    public static final int SLOT_HOTBAR9 = 44;

    public static final int SLOT_OFFHAND = 45;


    /* Attributes. */
    public static final double MIN_MAX_HEALTH = 0d;
    public static final double MAX_MAX_HEALTH = 1000d;
    public static final double MIN_MOVEMENT_SPEED = 0d;
    public static final double MAX_MOVEMENT_SPEED = 10d;
    public static final double MIN_MELEE_ATTACK_DAMAGE = 0d;
    public static final double MAX_MELEE_ATTACK_DAMAGE = 1_000_000d;
    public static final double MIN_MELEE_ATTACK_SPEED =0d;
    public static final double MAX_MELEE_ATTACK_SPEED = 1_000_000d;
    public static final double MIN_ARMOR =0d;
    public static final double MAX_ARMOR = 20d;
    public static final double MIN_KNOCKBACK_RESISTANCE = 0d;
    public static final double MAX_KNOCKBACK_RESISTANCE = 1d;

    /* Kits. */
    public static final Kit SWARD = new SwardKit();


    // Fields.
    public final KitClass Classification;

    /* Inventory. */
    public boolean IsInventoryDroppable = false;


    /* Super. */
    public int MaxSuperCharge = 100;

    /* Kit info. */
    public final ItemStack Icon;
    public final String Name;
    public final String Description;


    // Private static fields.
    private static final HashMap<Kit,Integer> s_activeKits = new HashMap<>();
    private static final HashMap<String, Kit> s_registeredKits = new HashMap<>();


    // Private fields.
    /* Attributes. */
    private double _maxHealth = 20d;
    private double _movementSpeed = 0.1d;
    private double _meleeAttackDamage = 0d;
    private double _meleeAttackSpeed = 4d;
    private double _armor = 0d;
    private double _knockBackResistance = 0d;


    /* Kit info. */
    private KitStats Stats;


    /* Upgrades. */
    private final HashMap<String, KitUpgradeDefinition> _upgrades = new HashMap<>();
    private final HashMap<String, KitGadgetDefinition> _gadgets = new HashMap<>();
    private final HashMap<String, KitStarPowerDefinition> _starPowers = new HashMap<>();


    // Constructors.
    public Kit(KitClass kitClass, ItemStack icon, String name, String description, KitStats stats)
    {
        if (kitClass == null)
        {
            throw new NullArgumentException("kitClass is null");
        }
        if (icon == null)
        {
            throw new NullArgumentException("icon is null");
        }
        if (name == null)
        {
            throw new NullArgumentException("name is null");
        }
        if (description == null)
        {
            throw new NullArgumentException("description is null");
        }
        if (stats == null)
        {
            throw new NullArgumentException("stats are null");
        }

        Classification = kitClass;
        Icon = icon;
        Name = name;
        Description = description;
    }


    // Static methods.
    public static void TickKits()
    {
        for (Kit DefinedKit : s_activeKits.keySet())
        {
            DefinedKit.StaticTick();
        }
    }

    public static void MarkKitAsUsed(Kit kit)
    {
        if (kit == null)
        {
            throw new NullArgumentException("kit is null");
        }

        if (s_activeKits.containsKey(kit))
        {
            s_activeKits.put(kit, s_activeKits.get(kit) + 1);
        }
        else
        {
            s_activeKits.put(kit, 1);
        }
    }

    public static void UnMarkKitAsUsed(Kit kit)
    {
        if (kit == null)
        {
            throw new NullArgumentException("kit is null");
        }

        if (!s_activeKits.containsKey(kit))
        {
            return;
        }

        s_activeKits.put(kit, s_activeKits.get(kit) - 1);

        if (s_activeKits.get(kit) <= 0)
        {
            s_activeKits.remove(kit);
        }
    }

    public static void RegisterKit(Kit kit)
    {
        if (kit == null)
        {
            throw new NullArgumentException("kit is null");
        }

        s_registeredKits.put(kit.Name, kit);
    }

    public static Collection<Kit> GetRegisteredKits()
    {
        return s_registeredKits.values();
    }

    public static Kit GetKitByName(String name)
    {
        if (name == null)
        {
            throw new NullArgumentException("name is null");
        }

        return s_registeredKits.get(name);
    }


    // Methods.
    /* Events. */
    public void Load(KitInstance instance)
    {
        SetAttributesOfPlayer(instance);

        instance.MCPlayer.clearActivePotionEffects();

        instance.MCPlayer.setExp(0f);
        instance.MCPlayer.setLevel(0);

        instance.MCPlayer.clearTitle();

        instance.MCPlayer.setGameMode(GameMode.SURVIVAL);
        instance.MCPlayer.setHealth(_maxHealth);

        instance.MCPlayer.setVelocity(new Vector(0d, 0d, 0d));
        instance.MCPlayer.setFallDistance(0f);

        instance.MCPlayer.getInventory().clear();
        CreateInventory(instance.MCPlayer.getInventory());
    }

    public abstract void CreateInventory(Inventory inventory);

    public void Tick(KitInstance instance)
    {

    }

    public void StaticTick() { }

    public void OnDeath(KitInstance instance)
    {
        instance.MCPlayer.setGameMode(GameMode.SPECTATOR);
    }

    public void OnRespawn(KitInstance instance)
    {
        instance.MCPlayer.setHealth(_maxHealth);
        instance.MCPlayer.setGameMode(GameMode.SURVIVAL);
    }

    public void Unload(KitInstance instance)
    {

    }

    /* Inventory. */
    public boolean GetIsInventoryDroppable()
    {
        return IsInventoryDroppable;
    }

    public void OnItemDropEvent(KitInstance instance, PlayerDropItemEvent event)
    {
        if (!IsInventoryDroppable)
        {
            event.setCancelled(true);
        }
    }

    public void SetIsInventoryDroppable(boolean value)
    {
        IsInventoryDroppable = value;
    }

    /* Getters and setters. */
    public double GetMaxHealth() { return _maxHealth; }

    public void SetMaxHealth(double value)
    {
        _maxHealth = Math.max(MIN_MAX_HEALTH, Math.max(value, MAX_MAX_HEALTH));
    }

    public double GetMovementSpeedHealth() { return _movementSpeed; }

    public void SetMovementSpeedHealth(double value)
    {
        _movementSpeed = Math.max(MIN_MOVEMENT_SPEED, Math.max(value, MAX_MOVEMENT_SPEED));
    }

    public double GetMeleeAttackDamage() { return _meleeAttackDamage; }

    public void SetMeleeAttackDamage(double value)
    {
        _meleeAttackDamage = Math.max(MIN_MELEE_ATTACK_DAMAGE, Math.max(value, MAX_MELEE_ATTACK_DAMAGE));
    }

    public double GetMeleeAttackSpeed() { return _meleeAttackSpeed; }

    public void SetMeleeAttackSpeed(double value)
    {
        _meleeAttackSpeed = Math.max(MIN_MELEE_ATTACK_SPEED, Math.max(value, MAX_MELEE_ATTACK_SPEED));
    }

    public double GetArmor() { return _armor; }

    public void SetArmor(double value)
    {
        _armor = Math.max(MIN_ARMOR, Math.max(value, MAX_ARMOR));
    }

    public double GetKnockbackResistance() { return _knockBackResistance; }

    public void SetKnockbackResistance(double value)
    {
        _knockBackResistance = Math.max(MIN_KNOCKBACK_RESISTANCE, Math.max(value, MAX_KNOCKBACK_RESISTANCE));
    }


    /* Upgrades. */
    public void AddUpgrade(String internalName, KitUpgradeDefinition upgrade)
    {
        if (internalName == null)
        {
            throw new NullArgumentException("internalName is null");
        }
        if (upgrade == null)
        {
            throw new NullArgumentException("upgrade is null");
        }

        _upgrades.put(internalName, upgrade);
    }

    public Collection<KitUpgradeDefinition> GetUpgrades() { return _upgrades.values(); }

    public KitUpgradeDefinition GetUpgrade(String internalName)
    {
        if (internalName == null)
        {
            throw new NullArgumentException("internalName is null");
        }

        return _upgrades.get(internalName);
    }

    public void AddGadget(String internalName, KitGadgetDefinition gadget)
    {
        if (internalName == null)
        {
            throw new NullArgumentException("internalName is null");
        }
        if (gadget == null)
        {
            throw new NullArgumentException("gadget is null");
        }

        _gadgets.put(internalName, gadget);
    }

    public Collection<KitGadgetDefinition> GetGadgets() { return _gadgets.values(); }

    public KitGadgetDefinition GetGadget(String internalName)
    {
        if (internalName == null)
        {
            throw new NullArgumentException("internalName is null");
        }

        return _gadgets.get(internalName);
    }


    public void AddStarPower(String internalName, KitStarPowerDefinition starPower)
    {
        if (internalName == null)
        {
            throw new NullArgumentException("internalName is null");
        }
        if (starPower == null)
        {
            throw new NullArgumentException("starPower is null");
        }

        _starPowers.put(internalName, starPower);
    }

    public Collection<KitStarPowerDefinition> GetStarPowers() { return _starPowers.values(); }

    public KitStarPowerDefinition GetStarPower(String internalName)
    {
        if (internalName == null)
        {
            throw new NullArgumentException("internalName is null");
        }

        return _starPowers.get(internalName);
    }


    /* Helper methods. */
    @SuppressWarnings("ConstantConditions")
    public void SetAttributesOfPlayer(KitInstance instance)
    {
        instance.MCPlayer.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(_maxHealth);
        instance.MCPlayer.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(_movementSpeed);
        instance.MCPlayer.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(_meleeAttackDamage);
        instance.MCPlayer.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(_meleeAttackSpeed);
        instance.MCPlayer.getAttribute(Attribute.GENERIC_ARMOR).setBaseValue(_armor);
        instance.MCPlayer.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE).setBaseValue(_knockBackResistance);
    }

    // Private methods.
}