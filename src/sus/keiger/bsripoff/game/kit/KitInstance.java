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
}
