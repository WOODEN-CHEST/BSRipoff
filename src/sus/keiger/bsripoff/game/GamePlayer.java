package sus.keiger.bsripoff.game;

import com.destroystokyo.paper.event.server.ServerTickStartEvent;
import org.apache.commons.lang.NullArgumentException;
import org.bukkit.entity.Player;
import sus.keiger.bsripoff.game.kit.KitInstance;
import sus.keiger.bsripoff.player.BSRipoffPlayer;

public class GamePlayer
{
    // Fields.
    public final BSRipoffPlayer BSRPlayer;
    public final Player MCPlayer;
    public final Game CurrentGame;


    // Private fields.
    private KitInstance _kitInstance;


    // Constructors.
    public GamePlayer(BSRipoffPlayer bsrPlayer, Game game)
    {
        if (bsrPlayer == null)
        {
            throw new NullArgumentException("bsrPlayer is null");
        }
        if (game == null)
        {
            throw new NullArgumentException("game is null");
        }

        BSRPlayer = bsrPlayer;
        MCPlayer = BSRPlayer.MCPlayer;
        CurrentGame = game;
    }


    // Methods.
    /* Getters and setters. */
    public GameState GetGameState()
    {
        return CurrentGame.GetState();
    }

    public KitInstance GetKitInstance()
    {
        return _kitInstance;
    }


    /* Events. */
    public void OnTickEvent(ServerTickStartEvent event)
    {
        if (CurrentGame.GetState() == GameState.InGame)
        {
            _kitInstance.Tick();
        }
    }
}