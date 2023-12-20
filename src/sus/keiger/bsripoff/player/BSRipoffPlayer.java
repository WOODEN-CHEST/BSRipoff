package sus.keiger.bsripoff.player;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;

public class BSRipoffPlayer
{
    // Fields.
    public final Player MCPlayer;


    // Private fields.
    private BSRPlayerState _state;


    // Constructors.
    public BSRipoffPlayer(Player mcPlayer)
    {
        if (mcPlayer == null)
        {
            throw new IllegalArgumentException("MCPlayer is null");
        }

        MCPlayer = mcPlayer;
        SetState(BSRPlayerState.InGame); // CHANGE TO InLobby IN FINAL THING.
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

            default:
                throw new IllegalStateException("Unknown player state: \"%s\"".formatted(state.toString()));
        }
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