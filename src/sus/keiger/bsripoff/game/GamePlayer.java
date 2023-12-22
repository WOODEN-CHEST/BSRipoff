package sus.keiger.bsripoff.game;

import org.apache.commons.lang.NullArgumentException;
import sus.keiger.bsripoff.player.BSRipoffPlayer;

public class GamePlayer
{
    // Fields.
    public final BSRipoffPlayer BSRPlayer;


    // Constructors.
    public GamePlayer(BSRipoffPlayer bsrPlayer)
    {
        if (bsrPlayer == null)
        {
            throw new NullArgumentException("bsrPlayer is null");
        }

        BSRPlayer = bsrPlayer;
    }
}