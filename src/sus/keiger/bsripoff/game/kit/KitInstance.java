package sus.keiger.bsripoff.game.kit;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.apache.commons.lang.NullArgumentException;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Vector;
import sus.keiger.bsripoff.BSRipoff;
import sus.keiger.bsripoff.game.Game;
import sus.keiger.bsripoff.game.GamePlayer;
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
    private int _superCharge;
    private final ItemStack _superItem = new ItemStack(Material.BOOK, 1);;
    private final NamespacedKey _superDataNamespacedKey = new NamespacedKey(BSRipoff.NAMESPACE, "is_super");
    private final ActionbarMessage _superNotChargedMessage = new ActionbarMessage(60,
            Component.text("Super not charged!").color(NamedTextColor.RED));

    /* Gadget. */
    private final ItemStack _gadgetItem = new ItemStack(Material.LIME_DYE, 1);
    private int _gadgetRechargeTimer = 0;
    private int _gadgetUsesLeft = 0;
    private final NamespacedKey _gadgetDataNamespacedKey = new NamespacedKey(BSRipoff.NAMESPACE, "is_gadget");
    private final ActionbarMessage _gadgetNotChargedMessage = new ActionbarMessage(60,
            Component.text("Gadget not charged!").color(NamedTextColor.RED));

    /* Respawning. */
    private int _immunityTicks;
    private int _respawnTimer;

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

        _superItem.editMeta(meta -> meta.getPersistentDataContainer().set(
                _superDataNamespacedKey, PersistentDataType.BOOLEAN, true));
        _gadgetItem.editMeta(meta -> meta.getPersistentDataContainer().set(
                _gadgetDataNamespacedKey, PersistentDataType.BOOLEAN, true));
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
        _superCharge = Math.max(0, Math.min(_superCharge + amount, ActiveKit.GetMaxSuperCharge()));
        MCPlayer.setLevel(_superCharge);
        MCPlayer.setExp(GetSuperChargeProgress());
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
        _superCharge = Math.max(0, Math.min(level, ActiveKit.GetMaxSuperCharge()));
        UpdateSuper();
    }

    public void SetSuperCharge(float progress)
    {
        _superCharge = Math.max(0,
                Math.min((int)(progress * ActiveKit.GetMaxSuperCharge()), ActiveKit.GetMaxSuperCharge()));
        UpdateSuper();
    }

    public ItemStack GetSuperItem() { return _superItem; }

    public void TryUseSuper()
    {
        if (IsSuperCharged())
        {
            MCPlayer.playSound(MCPlayer.getLocation(), Sound.BLOCK_ENCHANTMENT_TABLE_USE,
                    SoundCategory.PLAYERS, 1f, 1f);
            ActiveKit.ActivateSuper(this);
            SetSuperCharge(0);
        }
        else
        {
            MCPlayer.playSound(MCPlayer.getLocation(), Sound.BLOCK_NOTE_BLOCK_DIDGERIDOO,
                    SoundCategory.PLAYERS, 0.8f, 0.8f);
            GPlayer.BSRPlayer.ActionBarManager.AddMessage(_superNotChargedMessage);
        }
    }

    /* Gadget. */
    public int GetGadgetUsesLeft() { return _gadgetUsesLeft; }

    public void SetGadgetUsesLeft(int value)
    {
        _gadgetUsesLeft = value;
        UpdateGadget();
    }

    public void SetGadgetRechargeTimeLeft(int value)
    {
        _gadgetRechargeTimer = value;
        UpdateGadget();
    }

    public void RechargeGadget()
    {
        _gadgetRechargeTimer = 0;
        UpdateGadget();
    }

    public boolean IsGadgetCharged() { return (_gadgetRechargeTimer <= 0) && (_gadgetUsesLeft > 0); }

    public void TryUseGadget()
    {
        if (IsGadgetCharged())
        {
            MCPlayer.playSound(MCPlayer.getLocation(), Sound.BLOCK_SHULKER_BOX_OPEN,
                    SoundCategory.PLAYERS, 1f, 2f);
            ActiveKit.ActivateGadget(this);
        }
        else
        {
            MCPlayer.playSound(MCPlayer.getLocation(), Sound.BLOCK_NOTE_BLOCK_DIDGERIDOO,
                    SoundCategory.PLAYERS, 0.8f, 0.8f);
            GPlayer.BSRPlayer.ActionBarManager.AddMessage(_gadgetNotChargedMessage);
        }
    }

    public ItemStack GetGadgetItem() { return _gadgetItem; }


    /* Getters and setters. */
    public int GetImmunityTicks()
    {
        return _immunityTicks;
    }

    public void SetImmunityTicks(int value)
    {
        _immunityTicks = Math.max(0, value);
    }

    public KitModifiableValue GetMaxHealth() { return _maxHealth; }

    public KitModifiableValue GetMovementSpeedHealth() { return _movementSpeed; }

    public KitModifiableValue GetMeleeAttackDamage() { return _damageScale; }

    public KitModifiableValue GetMeleeAttackSpeed() { return _meleeAttackSpeed; }

    public KitModifiableValue GetArmor() { return _armor; }

    public KitModifiableValue GetKnockbackResistance() { return _knockBackResistance; }

    public KitModifiableValue GetDamageScale() { return _damageScale; }

    public Game GetGame() { return _game; }

    public int GetRespawnTimer() { return _respawnTimer; }

    public void SetRespawnTimer(int value) { _respawnTimer = value; }


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


    /* Events. */
    public void OnLoadEvent()
    {
        MCPlayer.clearActivePotionEffects();
        MCPlayer.setExp(0f);
        MCPlayer.setLevel(0);
        MCPlayer.clearTitle();
        MCPlayer.setGameMode(GameMode.SURVIVAL);
        MCPlayer.setHealth(_maxHealth.GetValue());
        MCPlayer.setGameMode(GameMode.SURVIVAL);
        MCPlayer.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(0d);
        MCPlayer.setVelocity(new Vector(0d, 0d, 0d));
        MCPlayer.setFallDistance(0f);
        MCPlayer.getInventory().clear();

        UpdateSuper();
        UpdateGadget();
        UpdateAttributes();

        Kit.MarkKitAsUsed(ActiveKit);
        ActiveKit.Load(this);
    }

    public void OnUnloadEvent()
    {
        ActiveKit.Unload(this);
        Kit.UnMarkKitAsUsed(ActiveKit);
    }

    public void OnTickEvent()
    {
        MCPlayer.setInvulnerable(_immunityTicks > 0);

        if ((_respawnTimer > 0) && (_respawnTimer != Integer.MAX_VALUE))
        {
            TickRespawn();
        }

        _maxHealth.Tick();
        _movementSpeed.Tick();
        _damageScale.Tick();
        _meleeAttackSpeed.Tick();
        _armor.Tick();
        _knockBackResistance.Tick();
        UpdateAttributes();

        MCPlayer.setFoodLevel(10);

        _gadgetRechargeTimer--;
        if (_gadgetRechargeTimer == 0)
        {
            UpdateGadget();
        }

        ActiveKit.Tick(this);
    }

    public void OnRespawnEvent()
    {
        _immunityTicks = 80; // 4 seconds.
        MCPlayer.setGameMode(GameMode.SURVIVAL);
        _game.OnPlayerRespawnEvent(GPlayer);
        ActiveKit.OnRespawn(this);
    }

    public void OnPlayerDropItemEvent(PlayerDropItemEvent event)
    {
        ActiveKit.OnPlayerDropItemEvent(this, event);
    }

    public void OnPlayerDamageEntityEvent(EntityDamageByEntityEvent event)
    {
        event.setDamage(event.getDamage() * _damageScale.GetValue());
        ActiveKit.OnPlayerDamageEntityEvent(this, event);
    }

    public void OnPlayerDeathEvent(PlayerDeathEvent event)
    {
        _game.OnPlayerDeathEvent(GPlayer, event);
        ActiveKit.OnPlayerDeathEvent(this, event);

        MCPlayer.setGameMode(GameMode.SPECTATOR);
        if (MCPlayer.getLastDeathLocation() != null)
        {
            MCPlayer.teleport(MCPlayer.getLastDeathLocation());
        }
    }

    public void OnPlayerInteractEvent(PlayerInteractEvent event)
    {
        ActiveKit.OnPlayerInteractEvent(event);

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


    // Protected methods.
    protected void TickRespawn()
    {
        _respawnTimer--;
    }


    protected void UpdateSuper()
    {
        if (IsSuperCharged())
        {
            _superItem.setType(Material.ENCHANTED_BOOK);
            _superItem.editMeta(meta -> meta.displayName(Component.text("Super")
                    .color(NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false)));
        }
        else
        {
            _superItem.setType(Material.BOOK);
            _superItem.editMeta(meta -> meta.displayName(Component.text("Super")
                    .color(NamedTextColor.DARK_GRAY).decoration(TextDecoration.ITALIC, false)));
        }
    }

    protected void UpdateGadget()
    {
        if (_gadgetUsesLeft == 0)
        {
            MCPlayer.getInventory().remove(_gadgetItem);
            return;
        }

        if (IsGadgetCharged())
        {
            _gadgetItem.setType(Material.LIME_DYE);
            _gadgetItem.editMeta(meta -> meta.displayName(Component.text("Gadget (%d left)"
                            .formatted(_gadgetUsesLeft))
                    .color(NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false)));
        }
        else
        {
            _gadgetItem.setType(Material.GRAY_DYE);
            _gadgetItem.editMeta(meta -> meta.displayName(Component.text("Gadget (%d left)"
                            .formatted(_gadgetUsesLeft))
                    .color(NamedTextColor.DARK_GRAY).decoration(TextDecoration.ITALIC, false)));
        }
    }
}
