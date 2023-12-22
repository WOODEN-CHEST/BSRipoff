package sus.keiger.bsripoff.player;

import org.apache.commons.lang.NullArgumentException;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import sus.keiger.bsripoff.BSRipoff;
import sus.keiger.bsripoff.game.GamePlayer;
import sus.keiger.bsripoff.game.kit.Kit;

public class BSRipoffPlayer
{
    // Fields.
    public final Player MCPlayer;


    // Private fields.
    private BSRPlayerState _state;

    /* Game. */
    private GamePlayer _gamePlayer = null;
    private Kit _selectedKit;


    // Constructors.
    public BSRipoffPlayer(Player mcPlayer)
    {
        if (mcPlayer == null)
        {
            throw new IllegalArgumentException("MCPlayer is null");
        }

        MCPlayer = mcPlayer;
        SetState(BSRPlayerState.InGame); // CHANGE TO InLobby IN FINAL THING.
        _selectedKit = Kit.SWARD;
    }


    // Methods.
    public BSRPlayerState GetState()
    {
        return _state;
    }

    public void SetState(BSRPlayerState state)
    {
        if (state == null)
        {
            throw new IllegalArgumentException("State may not be null.");
        }

        _state = state;

        switch (state)
        {
            case InLobby:
                SetStateLobby();
                break;

            case InGame:
                SetStateInGame();
                break;

            default:
                throw new IllegalStateException("Unknown player state: \"%s\"".formatted(state.toString()));
        }
    }

    public GamePlayer GetGamePlayer()
    {
        return _gamePlayer;
    }

    public Kit GetSelectedKit()
    {
        return _selectedKit;
    }

    public void SetSelectedKit(Kit kit)
    {
        if (kit == null)
        {
            throw new NullArgumentException("Kit is null.");
        }

        _selectedKit = kit;
    }


    /* Helper methods. */

    // Private methods.
    private void SetStateLobby()
    {

    }

    private void SetStateInGame()
    {

    }
}