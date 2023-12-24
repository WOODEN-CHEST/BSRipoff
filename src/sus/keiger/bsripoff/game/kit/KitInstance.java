package sus.keiger.bsripoff.game.kit;

import org.apache.commons.lang.NullArgumentException;
import org.bukkit.entity.Player;
import sus.keiger.bsripoff.player.BSRipoffPlayer;

public class KitInstance
{
    // Fields.
    public final BSRipoffPlayer BSRPlayer;
    public final Player MCPlayer;
    public final Kit ActiveKit;


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
        ActiveKit.Tick(this);
    }
}
