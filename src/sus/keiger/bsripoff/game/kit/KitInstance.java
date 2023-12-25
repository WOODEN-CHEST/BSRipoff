package sus.keiger.bsripoff.game.kit;

import org.apache.commons.lang.NullArgumentException;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;
import sus.keiger.bsripoff.player.BSRipoffPlayer;

public class KitInstance
{
    // Fields.
    public final BSRipoffPlayer BSRPlayer;
    public final Player MCPlayer;
    public final Kit ActiveKit;


    // Private fields.
    private int _superCharge;
    private int ImmunityTicks;
    private int RespawnTimer;


    // Constructors.
    public KitInstance(BSRipoffPlayer bsrPlayer, Kit kit)
    {
        if (bsrPlayer == null)
        {
            throw new NullArgumentException("bsrPlayer is null");
        }
        if (kit == null)
        {
            throw new NullArgumentException("kit is null");
        }

        BSRPlayer = bsrPlayer;
        MCPlayer = bsrPlayer.MCPlayer;
        ActiveKit = kit;
    }


    // Methods.
    /* Super. */
    public void ChargeSuper()
    {
        ChargeSuper(ActiveKit.MaxSuperCharge);
    }

    public void ChargeSuper(float progress)
    {
        ChargeSuper((int)(progress * ActiveKit.MaxSuperCharge));
    }

    public void ChargeSuper(int amount)
    {
        _superCharge = Math.max(0, Math.min(_superCharge + amount, ActiveKit.MaxSuperCharge));
        MCPlayer.setLevel(_superCharge);
        MCPlayer.setExp(GetSuperChargeProgress());
    }

    public int GetSuperChargeLevel()
    {
        return _superCharge;
    }

    public float GetSuperChargeProgress()
    {
        return (float)_superCharge / (float)ActiveKit.MaxSuperCharge;
    }

    public boolean IsSuperCharged()
    {
        return _superCharge == ActiveKit.MaxSuperCharge;
    }

    public void SetSuperCharge(int level)
    {
        _superCharge = Math.max(0, Math.min(level, ActiveKit.MaxSuperCharge));
    }

    public void SetSuperCharge(float progress)
    {
        _superCharge = Math.max(0, Math.min((int)(progress * ActiveKit.MaxSuperCharge), ActiveKit.MaxSuperCharge));
    }

    /* Events. */
    public void Load()
    {
        ActiveKit.Load(this);
        Kit.MarkKitAsUsed(ActiveKit);
    }

    public void Unload()
    {
        ActiveKit.Unload(this);
        Kit.UnMarkKitAsUsed(ActiveKit);
    }

    public void Tick()
    {
        MCPlayer.setInvulnerable(ImmunityTicks > 0);

        if (RespawnTimer != Integer.MAX_VALUE)
        {
            TickRespawn();
        }

        ActiveKit.Tick(this);
    }

    public void OnDeathEvent(PlayerDeathEvent event)
    {

    }

    public void Respawn()
    {

    }


    // Private methods.
    private void TickRespawn()
    {
        RespawnTimer--;
    }
}
