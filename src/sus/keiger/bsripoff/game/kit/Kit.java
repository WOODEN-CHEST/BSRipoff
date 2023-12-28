package sus.keiger.bsripoff.game.kit;

import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextDecoration;
import org.apache.commons.lang.NullArgumentException;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import sus.keiger.bsripoff.BSRipoff;
import sus.keiger.bsripoff.game.Game;
import sus.keiger.bsripoff.game.GamePlayer;
import sus.keiger.bsripoff.game.kit.upgrades.KitGadgetDefinition;
import sus.keiger.bsripoff.game.kit.upgrades.KitStarPowerDefinition;
import sus.keiger.bsripoff.game.kit.upgrades.KitUpgradeDefinition;

import java.util.*;
// Import EVERYTHING.

public abstract class Kit
{
    // Static fields.
    public static final long KIT_COST = 1500L;

    /* Inventory slots, figured out through trial and error because online sources are wrong. */
    public static final int SLOT_ARMOR_HEAD = 39;
    public static final int SLOT_ARMOR_CHEST = 38;
    public static final int SLOT_ARMOR_LEGS = 37;
    public static final int SLOT_ARMOR_FEET = 36;

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

    public static final int SLOT_HOTBAR1 = 0;
    public static final int SLOT_HOTBAR2 = 1;
    public static final int SLOT_HOTBAR3 = 2;
    public static final int SLOT_HOTBAR4 = 3;
    public static final int SLOT_HOTBAR5 = 4;
    public static final int SLOT_HOTBAR6 = 5;
    public static final int SLOT_HOTBAR7 = 6;
    public static final int SLOT_HOTBAR8 = 7;
    public static final int SLOT_HOTBAR9 = 8;

    public static final int SLOT_OFFHAND = 40;

    /* Kits. */
    public static final Kit SWARD = new SwardKit();


    // Fields.
    public final KitClass Classification;

    /* Inventory. */
    public boolean IsInventoryDroppable = false;

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
    private double _movementSpeed =0.1d;
    private double _meleeAttackSpeed =4d;
    private double _armor = 0d;
    private double _knockBackResistance = 0d;

    /* Super. */
    private int _maxSuperCharge = 100;

    /* Gadget. */
    private int _gadgetUses = 3;
    private int _gadgetRechargeTime = 300; // 15 seconds.

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

    public static Kit GetRegisteredKitByName(String name)
    {
        if (name == null)
        {
            throw new NullArgumentException("name is null");
        }

        return s_registeredKits.get(name);
    }


    /* Helper methods. */
    public static void AddAllFlags(ItemStack item)
    {
        item.addItemFlags(ItemFlag.HIDE_ITEM_SPECIFICS, ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_DESTROYS,
                ItemFlag.HIDE_DYE, ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_DESTROYS,
                ItemFlag.HIDE_PLACED_ON, ItemFlag.HIDE_ARMOR_TRIM);
    }

    public static void FormatEquipment(ItemStack item, boolean isUnbreakable, TextComponent name)
    {
        item.setUnbreakable(isUnbreakable);
        item.editMeta(meta -> meta.displayName(name.decoration(TextDecoration.ITALIC, false)));
        AddAllFlags(item);
    }


    // Methods.
    public KitInstance CreateInstance(GamePlayer gPlayer, Game game)
    {
        return new KitInstance(gPlayer, game, this);
    }

    /* Events (called at specific times). */
    public void Load(KitInstance instance)
    {
        CreateInventory(instance, instance.MCPlayer.getInventory());
    }

    public abstract void CreateInventory(KitInstance instance, Inventory inventory);

    public void Tick(KitInstance instance) { }

    public void StaticTick() { }

    public void OnPlayerDeathEvent(KitInstance instance, PlayerDeathEvent event) { }

    public void OnRespawn(KitInstance instance) { }

    public void OnPlayerDamageEntityEvent(KitInstance instance, EntityDamageEvent event) { }

    public void Unload(KitInstance instance) { }

    public void OnPlayerDropItemEvent(KitInstance instance, PlayerDropItemEvent event)
    {
        if (!IsInventoryDroppable)
        {
            event.setCancelled(true);
        }
    }

    public void OnPlayerInteractEvent(PlayerInteractEvent event) { }



    /* Attributes. */
    public double GetMaxHealth() { return _maxHealth; }

    public void SetMaxHealth(double value) { _maxHealth = value; }

    public double GetMovementSpeedHealth() { return _movementSpeed; }

    public void SetMovementSpeed(double value) { _movementSpeed = value; }

    public double GetMeleeAttackSpeed() { return _meleeAttackSpeed; }
    public void SetMeleeAttackSpeed(double value) { _meleeAttackSpeed = value; }

    public double GetArmor() { return _armor; }

    public void SetArmor(double value) { _armor = value; }

    public double GetKnockbackResistance() { return _knockBackResistance; }

    public void SetKnockbackResistance(double value) { _knockBackResistance = value; }


    /* Super. */
    public int GetMaxSuperCharge() { return _maxSuperCharge; }

    public void SetMaxSuperCharge(int value)
    {
        _maxSuperCharge = Math.max(0, value);
    }

    public void ActivateSuper(KitInstance instance)
    {

    }


    /* Gadget. */
    public int GetGadgetUses() { return _gadgetUses; }

    public void SetGadgetUses(int value) { _gadgetUses = value; }

    public int GetGadgetRechargeTime() { return _gadgetRechargeTime; }

    public void SetGadgetRechargeTime(int value) { _gadgetRechargeTime = value; }

    public void ActivateGadget(KitInstance instance) { }


    /* Upgrades. */
    public void AddUpgrade(KitUpgradeDefinition upgrade)
    {
        if (upgrade == null)
        {
            throw new NullArgumentException("upgrade is null");
        }

        _upgrades.put(upgrade.Name, upgrade);
    }

    public Collection<KitUpgradeDefinition> GetUpgrades() { return _upgrades.values(); }

    public KitUpgradeDefinition GetUpgrade(String name)
    {
        if (name == null)
        {
            throw new NullArgumentException("name is null");
        }

        return _upgrades.get(name);
    }

    public void AddGadget(KitGadgetDefinition gadget)
    {
        if (gadget == null)
        {
            throw new NullArgumentException("gadget is null");
        }

        _gadgets.put(gadget.Name, gadget);
    }

    public Collection<KitGadgetDefinition> GetGadgets() { return _gadgets.values(); }

    public KitGadgetDefinition GetGadget(String name)
    {
        if (name == null)
        {
            throw new NullArgumentException("name is null");
        }

        return _gadgets.get(name);
    }

    public void AddStarPower(KitStarPowerDefinition starPower)
    {
        if (starPower == null)
        {
            throw new NullArgumentException("starPower is null");
        }

        _starPowers.put(starPower.Name, starPower);
    }

    public Collection<KitStarPowerDefinition> GetStarPowers() { return _starPowers.values(); }

    public KitStarPowerDefinition GetStarPower(String name)
    {
        if (name == null)
        {
            throw new NullArgumentException("name is null");
        }

        return _starPowers.get(name);
    }

    public long GetKitCost() { return (long)(KIT_COST * Classification.PriceMultiplier); }
}