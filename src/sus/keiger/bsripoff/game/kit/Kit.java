package sus.keiger.bsripoff.game.kit;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.attribute.Attribute;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.util.Vector;
import sus.keiger.bsripoff.BSRipoff;

public abstract class Kit
{
    // Static fields.
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
    public static final double MIN_KNOCKBACK_RESISTANCE =0d;
    public static final double MAX_KNOCKBACK_RESISTANCE = 1d;


    // Fields.
    public final Inventory StartingInventory = Bukkit.createInventory(null, 9 * 4);


    // Private fields.
    /* Attributes. */
    public double _maxHealth = 20d;
    private double _movementSpeed = 0.1d;
    private double _meleeAttackDamage = 0d;
    private double _meleeAttackSpeed = 4d;
    private double _armor = 0d;
    private double _knockBackResistance = 0d;


    /* Super. */
    private int _maxSuperCharge = 100;


    /* Inventory. */
    private boolean _isInventoryDroppable = false;


    // Constructors.
    public Kit()
    {
        _maxHealth = 4d;
    }


    // Methods.
    public void OnLoad(KitInstance instance)
    {
        SetAttributesOfPlayer(instance);

        instance.MCPlayer.clearActivePotionEffects();

        instance.MCPlayer.setExp(0f);
        instance.MCPlayer.setLevel(0);

        instance.MCPlayer.clearTitle();

        instance.MCPlayer.setGameMode(GameMode.SURVIVAL);

        instance.MCPlayer.setVelocity(new Vector(0d, 0d, 0d));
        instance.MCPlayer.setFallDistance(0f);

        instance.MCPlayer.getInventory().setContents(StartingInventory.getContents());
    }

    public void Tick(KitInstance instance)
    {

    }

    public void OnDeath(KitInstance instance)
    {

    }

    public void OnRespawn(KitInstance instance)
    {
        BSRipoff.Pla
    }

    public void StaticTick()
    {

    }

    public void Unload(KitInstance instance)
    {

    }


    /* Attributes. */
    public double GetMaxHealth()
    {
        return _maxHealth;
    }

    public double GetMovementSpeed()
    {
        return _movementSpeed;
    }

    public double GetMeleeAttackDamage()
    {
        return _meleeAttackDamage;
    }

    public double GetMeleeAttackSpeed()
    {
        return _meleeAttackSpeed;
    }

    public double GetArmor()
    {
        return _armor;
    }

    public double GetKnockbackResistance()
    {
        return _knockBackResistance;
    }


    /* Inventory. */
    public boolean GetIsInventoryDroppable()
    {
        return _isInventoryDroppable;
    }

    public void OnItemDropEvent(KitInstance instance, PlayerDropItemEvent event)
    {
        if (!_isInventoryDroppable)
        {
            event.setCancelled(true);
        }
    }

    public void OnInventoryChangeEvent(KitInstance instance, InventoryInteractEvent event)
    {

    }


    // Protected methods.
    /* Attributes. */
    protected void SetMaxHealth(double value)
    {
        _maxHealth = Math.max(MIN_MAX_HEALTH, Math.min(value, MAX_MAX_HEALTH));
    }

    protected void SetMovementSpeed(double value)
    {
        _movementSpeed = Math.max(MIN_MOVEMENT_SPEED, Math.min(value, MAX_MOVEMENT_SPEED));
    }

    protected void SetMeleeAttackDamage(double value)
    {
        _meleeAttackDamage = Math.max(MIN_MELEE_ATTACK_DAMAGE, Math.min(value, MAX_MELEE_ATTACK_DAMAGE));
    }

    protected void SetMeleeAttackSpeed(double value)
    {
        _meleeAttackSpeed = Math.max(MIN_MELEE_ATTACK_SPEED, Math.min(value, MAX_MELEE_ATTACK_SPEED));
    }

    protected void SetArmor(double value)
    {
        _armor = Math.max(MIN_ARMOR, Math.min(value, MAX_ARMOR));
    }

    protected void SetKnockbackResistance(double value)
    {
        _knockBackResistance = Math.max(MIN_KNOCKBACK_RESISTANCE, Math.min(value, MAX_KNOCKBACK_RESISTANCE));
    }

    /* Inventory. */
    protected void SetIsInventoryDroppable(boolean value)
    {
        _isInventoryDroppable = value;
    }


    /* Helper methods. */
    protected void SetAttributesOfPlayer(KitInstance instance)
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