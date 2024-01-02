package sus.keiger.bsripoff.game.kit;

import com.destroystokyo.paper.event.player.PlayerJumpEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.apache.commons.lang.NullArgumentException;
import org.bukkit.*;
import org.bukkit.attribute.*;
import org.bukkit.entity.Player;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;
import sus.keiger.bsripoff.BSRipoff;
import sus.keiger.bsripoff.game.*;
import sus.keiger.bsripoff.game.kit.value.KitModifiableValue;
import sus.keiger.bsripoff.player.ActionbarMessage;

public abstract class KitInstance
{
    // Fields.
    public final GamePlayer GPlayer;
    public final Player MCPlayer;
    public final Kit ActiveKit;


    // Private static fields.
    private static final int MSG_ID_SUPER_NOT_CHARGED = 578309956;
    private static final int MSG_ID_SUPER_CHARGED = -19746234;
    private static final int MSG_ID_SUPER_ACTIVATED = -492790;
    private static final int MSG_ID_GADGET_NOT_CHARGED = 1294012944;
    private static final int MSG_ID_GADGET_ACTIVATED = -9000348;
    private static final int IMMUNITY_TICKS = 80; // 4 seconds.



    // Private fields.
    /* Inventory. */
    private boolean _isInventoryDroppable = false;


    /* Super */
    private final KitModifiableValue _maxSuperCharge = new KitModifiableValue(100d);
    private int _superCharge = 0;
    private int _previousSuperCharge = 0;
    private final NamespacedKey _superDataNamespacedKey = new NamespacedKey(BSRipoff.NAMESPACE, "is_super");


    /* Gadget. */
    private int _gadgetRechargeTimer = 0;
    private int _gadgetUsesLeft = 0;
    private final NamespacedKey _gadgetDataNamespacedKey = new NamespacedKey(BSRipoff.NAMESPACE, "is_gadget");


    /* Respawning. */
    private int _immunityTicks;
    private int _respawnTimer;


    /* Healing. */
    private int _healTimer = 0;
    private final KitModifiableValue _repeatedHealTimerLimit = new KitModifiableValue(25d); // 1.25 seconds between heals.
    private final KitModifiableValue _initialHealTimerLimit = new KitModifiableValue(100d); // 5 seconds to start healing.
    private final KitModifiableValue _healAmount = new KitModifiableValue(0.15d); // 15% health per heal.


    /* Attributes */
    private final KitModifiableValue _maxHealth = new KitModifiableValue(20d);
    private final KitModifiableValue _movementSpeed = new KitModifiableValue(0.1d);
    private final KitModifiableValue _meleeAttackSpeed = new KitModifiableValue(4d);
    private final KitModifiableValue _armor = new KitModifiableValue(0d);
    private final KitModifiableValue _knockBackResistance = new KitModifiableValue(0d);
    private final KitModifiableValue _damageScale = new KitModifiableValue(1d);


    /* Game. */
    private final Game _game;


    // Constructors.
    public KitInstance(GamePlayer gamePlayer, Game game, Kit kit)
    {
        if (gamePlayer == null)
        {
            throw new NullArgumentException("bsrPlayer is null");
        }
        if (kit == null)
        {
            throw new NullArgumentException("kit is null");
        }
        if (game == null)
        {
            throw new NullArgumentException("kit is null");
        }

        GPlayer = gamePlayer;
        MCPlayer = gamePlayer.MCPlayer;
        ActiveKit = kit;
        _game = game;
    }


    // Methods.
    /* Super. */
    public void ChargeSuper()
    {
        ChargeSuper((int)_maxSuperCharge.GetValue());
    }

    public void ChargeSuper(float progress)
    {
        ChargeSuper((int)(progress * _maxSuperCharge.GetValue()));
    }

    public void ChargeSuper(int amount)
    {
        _previousSuperCharge = _superCharge;
        _superCharge = Math.max(0, Math.min(_superCharge + amount, (int)_maxSuperCharge.GetValue()));
        UpdateSuper();
    }

    public int GetSuperChargeLevel()
    {
        return _superCharge;
    }

    public float GetSuperChargeProgress()
    {
        return (float)_superCharge / (float)_maxSuperCharge.GetValue();
    }

    public boolean IsSuperCharged()
    {
        return _superCharge == (int)_maxSuperCharge.GetValue();
    }

    public void SetSuperCharge(int level)
    {
        _previousSuperCharge = _superCharge;
        _superCharge = Math.max(0, Math.min(level, (int)_maxSuperCharge.GetValue()));
        UpdateSuper();
    }

    public void SetSuperCharge(float progress)
    {
        _previousSuperCharge = _superCharge;
        _superCharge = Math.max(0,
                Math.min((int)(progress * _maxSuperCharge.GetValue()), (int)_maxSuperCharge.GetValue()));
        UpdateSuper();
    }

    public void SetSuperItem(int slot)
    {
        ItemStack Item = new ItemStack(Material.BOOK, 1);
        Item.editMeta(meta -> meta.getPersistentDataContainer().set(
            _superDataNamespacedKey, PersistentDataType.BOOLEAN, true));

        MCPlayer.getInventory().setItem(slot, Item);
    }

    public void TryUseSuper()
    {
        if (IsSuperCharged())
        {
            ActivateSuper();
        }
        else
        {
            GPlayer.BSRPlayer.PlayErrorSound();
            GPlayer.BSRPlayer.ActionBarManager.AddMessage(new ActionbarMessage(40,
                    Component.text("Super not charged!").color(NamedTextColor.RED), MSG_ID_SUPER_NOT_CHARGED));
        }
    }

    /* Gadget. */
    public int GetGadgetUsesLeft() { return _gadgetUsesLeft; }

    public void SetGadgetUsesLeft(int value)
    {
        _gadgetUsesLeft = value;
        UpdateGadgetItem();
    }

    public void SetGadgetRechargeTimeLeft(int value)
    {
        _gadgetRechargeTimer = value;
        UpdateGadgetItem();
    }

    public void RechargeGadget()
    {
        _gadgetRechargeTimer = 0;
        UpdateGadgetItem();
    }

    public boolean IsGadgetCharged() { return (_gadgetRechargeTimer <= 0) && (_gadgetUsesLeft > 0); }

    public void TryUseGadget()
    {
        if (IsGadgetCharged())
        {

            ActivateGadget();
        }
        else
        {
            GPlayer.BSRPlayer.PlayErrorSound();
            GPlayer.BSRPlayer.ActionBarManager.AddMessage(new ActionbarMessage(40,
                    Component.text("Gadget not charged!").color(NamedTextColor.RED), MSG_ID_GADGET_NOT_CHARGED));
        }
    }

    public void SetGadgetItem(int slot)
    {
        ItemStack Item = new ItemStack(Material.BOOK, 1);
        Item.editMeta(meta -> meta.getPersistentDataContainer().set(
                _gadgetDataNamespacedKey, PersistentDataType.BOOLEAN, true));

        MCPlayer.getInventory().setItem(slot, Item);
    }


    /* Attributes. */
    public KitModifiableValue GetMaxHealth() { return _maxHealth; }

    public KitModifiableValue GetMovementSpeed() { return _movementSpeed; }

    public KitModifiableValue GetMeleeAttackDamage() { return _damageScale; }

    public KitModifiableValue GetMeleeAttackSpeed() { return _meleeAttackSpeed; }

    public KitModifiableValue GetArmor() { return _armor; }

    public KitModifiableValue GetKnockbackResistance() { return _knockBackResistance; }

    public KitModifiableValue GetDamageScale() { return _damageScale; }

    @SuppressWarnings("ConstantConditions")
    public void UpdateAttributes()
    {
        MCPlayer.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(_maxHealth.GetValue());
        MCPlayer.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(_movementSpeed.GetValue());
        MCPlayer.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(_meleeAttackSpeed.GetValue());
        MCPlayer.getAttribute(Attribute.GENERIC_ARMOR).setBaseValue(_armor.GetValue());
        MCPlayer.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE).setBaseValue(_knockBackResistance.GetValue());
    }


    /* Game. */
    public Game GetGame() { return _game; }


    /* Respawning. */
    public int GetRespawnTimer() { return _respawnTimer; }

    public void SetRespawnTimer(int value) { _respawnTimer = value; }

    public int GetImmunityTicks() { return _immunityTicks; }

    public void SetImmunityTicks(int value) { _immunityTicks = Math.max(0, value); }


    /* Health. */
    public void SetHealthTimer(int value) { _healTimer = value; }

    public int GetHealthTimer() { return _healTimer; }

    public KitModifiableValue GetInitialHealthTimerLimit() { return _initialHealTimerLimit; }
    public KitModifiableValue GetRepeatedHealthTimerLimit() { return _repeatedHealTimerLimit; }

    public KitModifiableValue GetHealAmount() { return _healAmount; }


    /* Inventory */
    public abstract void CreateInventory();

    public ItemStack GetItemByMaterial(Material material)
    {
        for (ItemStack Item : MCPlayer.getInventory())
        {
            if ((Item != null) && (Item.getType() == material))
            {
                return Item;
            }
        }

        return null;
    }

    public ItemStack GetItemByPersistentData(NamespacedKey key)
    {
        for (ItemStack Item : MCPlayer.getInventory())
        {
            if ((Item != null) && Item.getPersistentDataContainer().has(key))
            {
                return Item;
            }
        }

        return null;
    }

    public boolean GetIsInventoryDroppable() { return _isInventoryDroppable; }

    public void SetIsInventoryDroppable(boolean value) { _isInventoryDroppable = value; }


    /* Helper methods. */
    public boolean IsAttackCritical()
    {
        return (MCPlayer.getFallDistance() > 0f) && !MCPlayer.isSprinting()
                && (MCPlayer.getAttackCooldown() >= 0.9f);
    }


    /* Events. */
    public void OnLoadEvent()
    {
        MCPlayer.getInventory().clear();
        CreateInventory();
        SetSuperCharge(0);
        UpdateGadgetItem();
        UpdateAttributes();

        _maxSuperCharge.ClearModifiers();
        _healAmount.ClearModifiers();
        _initialHealTimerLimit.ClearModifiers();
        _repeatedHealTimerLimit.ClearModifiers();
        _maxHealth.ClearModifiers();
        _movementSpeed.ClearModifiers();
        _meleeAttackSpeed.ClearModifiers();
        _damageScale.ClearModifiers();
        _armor.ClearModifiers();
        _knockBackResistance.ClearModifiers();

        MCPlayer.clearActivePotionEffects();
        MCPlayer.clearTitle();
        MCPlayer.setGameMode(GameMode.SURVIVAL);
        MCPlayer.setHealth(_maxHealth.GetValue());
        MCPlayer.setGameMode(GameMode.SURVIVAL);
        MCPlayer.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(0d);
        MCPlayer.setVelocity(new Vector(0d, 0d, 0d));
        MCPlayer.setFallDistance(0f);
        MCPlayer.setFireTicks(0);
        MCPlayer.setInvisible(false);

        MCPlayer.addPotionEffect(
                new PotionEffect(PotionEffectType.SLOW_DIGGING, Integer.MAX_VALUE, 0, true, false, false));
        for (AttributeModifier Modifier : MCPlayer.getAttribute(Attribute.GENERIC_ATTACK_SPEED).getModifiers())
        {
            MCPlayer.getAttribute(Attribute.GENERIC_ATTACK_SPEED).removeModifier(Modifier);
        }

        Kit.MarkKitAsUsed(ActiveKit);
    }

    public void OnUnloadEvent()
    {
        Kit.UnMarkKitAsUsed(ActiveKit);
    }

    public void OnTickEvent()
    {
        TickImmunity();
        TickRespawn();
        TickAttributes();
        TickGadget();
        TickHealth();

        _maxSuperCharge.Tick();

        MCPlayer.setFoodLevel(10);
        MCPlayer.setSaturation(0f);
    }

    public void OnRespawnEvent()
    {
        _immunityTicks = IMMUNITY_TICKS;
        MCPlayer.setGameMode(GameMode.SURVIVAL);
        _game.OnPlayerRespawnEvent(GPlayer);
    }

    public void OnPlayerDropItemEvent(PlayerDropItemEvent event)
    {
        if (!_isInventoryDroppable)
        {
            event.setCancelled(true);
        }
    }

    public void OnPlayerDamageEntityEvent(EntityDamageByEntityEvent event)
    {
        event.setDamage(event.getDamage() * _damageScale.GetValue());
        ResetHealTimer();
    }

    public void OnPlayerDeathEvent(PlayerDeathEvent event)
    {
        MCPlayer.setGameMode(GameMode.SPECTATOR);
        _game.OnPlayerDeathEvent(GPlayer, event);
    }

    public void OnPlayerInteractEvent(PlayerInteractEvent event)
    {
        if ((event.getItem() == null) || !event.getAction().isRightClick())
        {
            return;
        }

        if (event.getItem().getPersistentDataContainer().has(_superDataNamespacedKey))
        {
            TryUseSuper();
        }
        else if (event.getItem().getPersistentDataContainer().has(_gadgetDataNamespacedKey))
        {
            TryUseGadget();
        }
    }

    public void OnPlayerTakeDamageEvent(EntityDamageEvent event)
    {
        ResetHealTimer();
    }

    public void OnPlayerShootBowEvent(EntityShootBowEvent event)
    {
        ResetHealTimer();
    }

    public void OnPlayerItemConsumeEvent(PlayerItemConsumeEvent event)
    {
        ResetHealTimer();
    }

    public void OnPlayerJumpEvent(PlayerJumpEvent event) { }

    public void OnPlayerMoveEvent(PlayerMoveEvent event) { }

    public void OnPlayerPickupItemEvent(EntityPickupItemEvent event) { }

    public void OnInventoryClickEvent(InventoryClickEvent event) { }

    public void OnPlayerSwapHandItemsEvent(PlayerSwapHandItemsEvent event) { }

    public void OnPlayerLaunchProjectileEvent(ProjectileLaunchEvent event) { }

    public void OnPlayerPlaceBlockEvent(BlockPlaceEvent event)
    {
        ResetHealTimer();
    }

    public void OnPlayerBreakBlockEvent(BlockBreakEvent event) { }


    // Protected methods.
    protected void TickRespawn()
    {
        if ((_respawnTimer == Integer.MAX_VALUE) || (_respawnTimer == 0))
        {
            return;
        }

        _respawnTimer--;
        if (_respawnTimer == 0)
        {
            OnRespawnEvent();
        }
        else
        {
            _game.OnPlayerTickDuringRespawnEvent(GPlayer, _respawnTimer);
        }
    }

    protected void TickImmunity()
    {
        if (_immunityTicks == 0)
        {
            return;
        }

        _immunityTicks--;
        if (_immunityTicks == 0)
        {
            MCPlayer.setInvulnerable(false);
        }
    }

    protected void UpdateSuper()
    {
        ItemStack SuperItem = GetItemByPersistentData(_superDataNamespacedKey);
        if (SuperItem == null)
        {
            return;
        }

        if (IsSuperCharged())
        {
            SuperItem.setType(Material.ENCHANTED_BOOK);
            SuperItem.editMeta(meta -> meta.displayName(Component.text("Super")
                    .color(NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false)));

            if (_previousSuperCharge != _superCharge)
            {
                MCPlayer.playSound(MCPlayer.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 0.4f, 1f);
                GPlayer.BSRPlayer.ActionBarManager.AddMessage(new ActionbarMessage(40,
                        Component.text("Super charged!").color(NamedTextColor.GREEN), MSG_ID_SUPER_CHARGED));
            }
        }
        else
        {
            SuperItem.setType(Material.BOOK);
            SuperItem.editMeta(meta -> meta.displayName(Component.text("Super")
                    .color(NamedTextColor.DARK_GRAY).decoration(TextDecoration.ITALIC, false)));
        }

        MCPlayer.setLevel(_superCharge);
        MCPlayer.setExp(Math.max(0f, Math.min(GetSuperChargeProgress() - 0.0001f, 1f)));
    }

    protected void ActivateSuper()
    {
        Location EffectLocation = MCPlayer.getLocation().clone().add(0d, 1d, 0d);

        MCPlayer.getWorld().playSound(EffectLocation,
                Sound.BLOCK_ENCHANTMENT_TABLE_USE, SoundCategory.PLAYERS, 1f, 1f);
        MCPlayer.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, EffectLocation, 30, 0.1d, 0.1d, 0.1d, 1d);
        GPlayer.BSRPlayer.ActionBarManager.AddMessage(new ActionbarMessage(40,
                Component.text("Super activated!").color(NamedTextColor.GREEN), MSG_ID_SUPER_ACTIVATED));
        SetSuperCharge(0);

        ResetHealTimer();
    }

    protected void UpdateGadgetItem()
    {
        ItemStack GadgetItem = GetItemByPersistentData(_gadgetDataNamespacedKey);
        if (GadgetItem == null)
        {
            return;
        }

        if (_gadgetUsesLeft == 0)
        {
            MCPlayer.getInventory().remove(GadgetItem);
            return;
        }

        if (IsGadgetCharged())
        {
            GadgetItem.setType(Material.LIME_DYE);
            GadgetItem.editMeta(meta -> meta.displayName(Component.text("Gadget (%d left)"
                            .formatted(_gadgetUsesLeft))
                    .color(NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false)));
        }
        else
        {
            GadgetItem.setType(Material.GRAY_DYE);
            GadgetItem.editMeta(meta -> meta.displayName(Component.text("Gadget (%d left)"
                            .formatted(_gadgetUsesLeft))
                    .color(NamedTextColor.DARK_GRAY).decoration(TextDecoration.ITALIC, false)));
        }
    }

    protected void ActivateGadget()
    {
        MCPlayer.getWorld().playSound(MCPlayer.getLocation(), Sound.BLOCK_SHULKER_BOX_OPEN,
                SoundCategory.PLAYERS, 1f, 2f);
        GPlayer.BSRPlayer.ActionBarManager.AddMessage(new ActionbarMessage(40,
                Component.text("Gadget activated!").color(NamedTextColor.GREEN), MSG_ID_GADGET_ACTIVATED));

        ResetHealTimer();
    }

    protected void TickGadget()
    {
        _gadgetRechargeTimer--;
        if (_gadgetRechargeTimer == 0)
        {
            UpdateGadgetItem();
        }
    }

    protected void TickHealth()
    {
        _healTimer--;
        _healAmount.Tick();
        _initialHealTimerLimit.Tick();
        _repeatedHealTimerLimit.Tick();

        if ((_healTimer == 0) && (MCPlayer.getHealth() < _maxHealth.GetValue()))
        {
            MCPlayer.setHealth(Math.max(0, Math.min(
                    MCPlayer.getHealth() +  _maxHealth.GetValue() * _healAmount.GetValue(), _maxHealth.GetValue())));

            _healTimer = (int)_repeatedHealTimerLimit.GetValue();

            MCPlayer.playSound(MCPlayer.getLocation(), Sound.BLOCK_NOTE_BLOCK_XYLOPHONE,
                    SoundCategory.PLAYERS, 0.2f, 2f);
        }
    }

    protected void ResetHealTimer()
    {
        _healTimer = (int)_initialHealTimerLimit.GetValue();
    }

    protected void TickAttributes()
    {
        _maxHealth.Tick();
        _movementSpeed.Tick();
        _damageScale.Tick();
        _meleeAttackSpeed.Tick();
        _armor.Tick();
        _knockBackResistance.Tick();

        UpdateAttributes();
    }
}
