package sus.keiger.bsripoff.player;

import com.destroystokyo.paper.event.server.ServerTickStartEvent;
import org.apache.commons.lang.NullArgumentException;
import org.bukkit.GameMode;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerDropItemEvent;

import sus.keiger.bsripoff.BSRipoff;
import sus.keiger.bsripoff.game.GamePlayer;
import sus.keiger.bsripoff.game.kit.Kit;

public class BSRipoffPlayer
{
    // Fields.
    public final Player MCPlayer;
    public final BSRPlayerGameData GameData = new BSRPlayerGameData();


    // Private fields.
    private BSRPlayerState _state;
    private ActionbarManager _actionbarManager = new ActionbarManager();


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
        SetState(BSRPlayerState.InLobby);
        _selectedKit = Kit.SWARD;
    }


    // Methods.
    /* Getters and setters. */
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


    /* Actionbar. */
    public void AddActionbarMessage(ActionbarMessage message)
    {
        _actionbarManager.AddMessage(message);
    }


    /* Helper methods. */

    /* Events. */
    public void OnTickEvent(ServerTickStartEvent event)
    {
        _actionbarManager.Tick(this);

        if (_state == BSRPlayerState.InGame)
        {
            _gamePlayer.OnTickEvent(event);
            return;
        }
    }

    public void OnPlayerDropItemEvent(PlayerDropItemEvent event)
    {

    }


    // Private methods.
    /* State. */
    @SuppressWarnings("ConstantConditions")
    private void SetStateLobby()
    {
        MCPlayer.setGameMode(GameMode.ADVENTURE);

        MCPlayer.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(20d);
        MCPlayer.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.1);
        MCPlayer.getAttribute(Attribute.GENERIC_ARMOR).setBaseValue(0d);
        MCPlayer.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(4d);
        MCPlayer.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE).setBaseValue(0d);
        MCPlayer.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(0d);

        MCPlayer.setInvulnerable(true);
        MCPlayer.clearActivePotionEffects();
        MCPlayer.clearTitle();

        MCPlayer.teleport(BSRipoff.GetServerManager().GetSpawnLocation());
    }

    private void SetStateInGame()
    {

    }
}