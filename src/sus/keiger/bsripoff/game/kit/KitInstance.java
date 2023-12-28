package sus.keiger.bsripoff.game.kit;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.apache.commons.lang.NullArgumentException;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Vector;
import sus.keiger.bsripoff.BSRipoff;
import sus.keiger.bsripoff.game.*;
import sus.keiger.bsripoff.game.kit.value.KitModifiableValue;
import sus.keiger.bsripoff.player.ActionbarMessage;

public class KitInstance
{
    // Fields.
    public final GamePlayer GPlayer;
    public final Player MCPlayer;
    public final Kit ActiveKit;


    // Private fields.
    /* Super */
    private final KitModifiableValue _maxSuperCharge;
    private int _superCharge = 0;
    private int _previousSuperCharge = 0;
    private final NamespacedKey _superDataNamespacedKey = new NamespacedKey(BSRipoff.NAMESPACE, "is_super");
    private final ActionbarMessage _superNotChargedMessage = new ActionbarMessage(40,
            Component.text("Super not charged!").color(NamedTextColor.RED));
    private final ActionbarMessage _superChargedMessage = new ActionbarMessage(40,
            Component.text("Super charged!").color(NamedTextColor.GREEN));
    private final ActionbarMessage _superActivatedMessage = new ActionbarMessage(40,
            Component.text("Super activated!").color(NamedTextColor.GREEN));

    /* Gadget. */
    private int _gadgetRechargeTimer = 0;
    private int _gadgetUsesLeft = 0;
    private final NamespacedKey _gadgetDataNamespacedKey = new NamespacedKey(BSRipoff.NAMESPACE, "is_gadget");
    private final ActionbarMessage _gadgetNotChargedMessage = new ActionbarMessage(40,
            Component.text("Gadget not charged!").color(NamedTextColor.RED));
    private final ActionbarMessage _gadgetActivatedMessage = new ActionbarMessage(40,
            Component.text("Gadget activated!").color(NamedTextColor.GREEN));

    /* Respawning. */
    private int _immunityTicks;
    private int _respawnTimer;

    /* Healing. */
    private int _healTimer = 0;
    private final KitModifiableValue _repeatedHealTimerLimit = new KitModifiableValue(25);
    private final KitModifiableValue _initialHealTimerLimit = new KitModifiableValue(100);
    private final KitModifiableValue _healAmount = new KitModifiableValue(0.15);


    /* Attributes */
    private final KitModifiableValue _maxHealth;
    private final KitModifiableValue _movementSpeed;
    private final KitModifiableValue _meleeAttackSpeed;
    private final KitModifiableValue _armor;
    private final KitModifiableValue _knockBackResistance;
    private final KitModifiableValue _damageScale = new KitModifiableValue(1);

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

        _maxHealth = new KitModifiableValue(kit.GetMaxHealth());
        _movementSpeed = new KitModifiableValue(kit.GetMovementSpeedHealth());
        _meleeAttackSpeed = new KitModifiableValue(kit.GetMeleeAttackSpeed());
        _armor = new KitModifiableValue(kit.GetArmor());
        _knockBackResistance = new KitModifiableValue(kit.GetKnockbackResistance());
        _maxSuperCharge = new KitModifiableValue(kit.GetMaxSuperCharge());
    }


    // Methods.
    /* Super. */
    public void ChargeSuper()
    {
        ChargeSuper(ActiveKit.GetMaxSuperCharge());
    }

    public void ChargeSuper(float progress)
    {
        ChargeSuper((int)(progress * ActiveKit.GetMaxSuperCharge()));
    }

    public void ChargeSuper(int amount)
    {
        _previousSuperCharge = _superCharge;
        _superCharge = Math.max(0, Math.min(_superCharge + amount, ActiveKit.GetMaxSuperCharge()));
        UpdateSuper();
    }

    public int GetSuperChargeLevel()
    {
        return _superCharge;
    }

    public float GetSuperChargeProgress()
    {
        return (float)_superCharge / (float)ActiveKit.GetMaxSuperCharge();
    }

    public boolean IsSuperCharged()
    {
        return _superCharge == ActiveKit.GetMaxSuperCharge();
    }

    public void SetSuperCharge(int level)
    {
        _previousSuperCharge = _superCharge;
        _superCharge = Math.max(0, Math.min(level, ActiveKit.GetMaxSuperCharge()));
        UpdateSuper();
    }

    public void SetSuperCharge(float progress)
    {
        _previousSuperCharge = _superCharge;
        _superCharge = Math.max(0,
                Math.min((int)(progress * ActiveKit.GetMaxSuperCharge()), ActiveKit.GetMaxSuperCharge()));
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
            GPlayer.BSRPlayer.ActionBarManager.AddMessage(_superNotChargedMessage);
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
            GPlayer.BSRPlayer.ActionBarManager.AddMessage(_gadgetNotChargedMessage);
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


    /* Helper methods. */
    @SuppressWarnings("ConstantConditions")
    public void UpdateAttributes()
    {
        MCPlayer.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(_maxHealth.GetValue());
        MCPlayer.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(_movementSpeed.GetValue());
        MCPlayer.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(_meleeAttackSpeed.GetValue());
        MCPlayer.getAttribute(Attribute.GENERIC_ARMOR).setBaseValue(_armor.GetValue());
        MCPlayer.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE).setBaseValue(_knockBackResistance.GetValue());
    }

    public boolean IsEligibleForCritical()
    {
        return (MCPlayer.getFallDistance() > 0f) && !MCPlayer.isSprinting()
                && (MCPlayer.getAttackCooldown() >= 0.9f);
    }


    /* Events. */
    public void OnLoadEvent()
    {
        ActiveKit.OnLoadEvent(this);
        Kit.MarkKitAsUsed(ActiveKit);

        MCPlayer.clearActivePotionEffects();
        MCPlayer.clearTitle();
        MCPlayer.setGameMode(GameMode.SURVIVAL);
        MCPlayer.setHealth(_maxHealth.GetValue());
        MCPlayer.setGameMode(GameMode.SURVIVAL);
        MCPlayer.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(0d);
        MCPlayer.setVelocity(new Vector(0d, 0d, 0d));
        MCPlayer.setFallDistance(0f);

        SetSuperCharge(0);
        UpdateGadgetItem();
        UpdateAttributes();
    }

    public void OnUnloadEvent()
    {
        ActiveKit.OnUnloadEvent(this);
        Kit.UnMarkKitAsUsed(ActiveKit);
    }

    public void OnTickEvent()
    {
        ActiveKit.OnTickEvent(this);

        TickImmunity();
        TickRespawn();
        TickAttributes();
        TickGadget();
        TickHealth();

        MCPlayer.setFoodLevel(10);
    }

    public void OnRespawnEvent()
    {
        ActiveKit.OnRespawnEvent(this);

        _immunityTicks = 80; // 4 seconds.
        MCPlayer.setGameMode(GameMode.SURVIVAL);
        _game.OnPlayerRespawnEvent(GPlayer);
    }

    public void OnPlayerDropItemEvent(PlayerDropItemEvent event)
    {
        ActiveKit.OnPlayerDropItemEvent(this, event);
    }

    public void OnPlayerDamageEntityEvent(EntityDamageByEntityEvent event)
    {
        event.setDamage(event.getDamage() * _damageScale.GetValue());
        ActiveKit.OnPlayerDamageEntityEvent(this, event);
        ResetHealTimer();
    }

    public void OnPlayerDeathEvent(PlayerDeathEvent event)
    {
        _game.OnPlayerDeathEvent(GPlayer, event);
        ActiveKit.OnPlayerDeathEvent(this, event);

        MCPlayer.setGameMode(GameMode.SPECTATOR);
    }

    public void OnPlayerInteractEvent(PlayerInteractEvent event)
    {
        ActiveKit.OnPlayerInteractEvent(this, event);

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
        ActiveKit.OnPlayerTakeDamageEvent(this, event);
        ResetHealTimer();
    }



    // Protected methods.
    protected void TickRespawn()
    {
        if (_respawnTimer == Integer.MAX_VALUE)
        {
            return;
        }

        _respawnTimer--;
        if (_respawnTimer == 0)
        {
            OnRespawnEvent();
        }
    }

    protected void TickImmunity()
    {
        MCPlayer.setInvulnerable(_immunityTicks > 0);
        _immunityTicks--;
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
                GPlayer.BSRPlayer.ActionBarManager.AddMessage(_superChargedMessage);
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
        ActiveKit.ActivateSuper(this);

        Location EffectLocation = MCPlayer.getLocation().clone().add(0d, 1d, 0d);

        MCPlayer.getWorld().playSound(EffectLocation,
                Sound.BLOCK_ENCHANTMENT_TABLE_USE, SoundCategory.PLAYERS, 1f, 1f);
        MCPlayer.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, EffectLocation, 30, 0.1d, 0.1d, 0.1d, 1d);
        GPlayer.BSRPlayer.ActionBarManager.AddMessage(_superActivatedMessage);
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
        ActiveKit.ActivateGadget(this);

        MCPlayer.getWorld().playSound(MCPlayer.getLocation(), Sound.BLOCK_SHULKER_BOX_OPEN,
                SoundCategory.PLAYERS, 1f, 2f);
        GPlayer.BSRPlayer.ActionBarManager.AddMessage(_gadgetActivatedMessage);

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
        BSRipoff.GetLogger().warning("Triggered reset timer");
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
