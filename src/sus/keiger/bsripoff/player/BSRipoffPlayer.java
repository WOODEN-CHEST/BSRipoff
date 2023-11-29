package sus.keiger.bsripoff.player;

import org.bukkit.entity.Player;

public class BSRipoffPlayer
{
    // Private fields.
    private Player _mcPlayer;


    // Constructors.
    public BSRipoffPlayer(Player mcPlayer)
    {
        if (mcPlayer == null)
        {
            throw new IllegalArgumentException("MCPlayer is null");
        }

        _mcPlayer = mcPlayer;
    }

    // Methods.
    public Player GetMCPlayer()
    {
        return _mcPlayer;
    }
}