package sus.keiger.bsripoff.player;

import com.destroystokyo.paper.event.server.ServerTickStartEvent;
import org.apache.commons.lang.NullArgumentException;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerDropItemEvent;

import org.bukkit.event.player.PlayerInteractEvent;
import sus.keiger.bsripoff.BSRipoff;

public class BSRipoffPlayer
{
    // Fields.
    public final Player MCPlayer;
    public final BSRPlayerGameData GameData;
    public final ActionbarManager ActionBarManager = new ActionbarManager();


    // Private fields.
    private BSRPlayerState _state;


    // Constructors.
    public BSRipoffPlayer(Player mcPlayer)
    {
        if (mcPlayer == null)
        {
            throw new NullArgumentException("MCPlayer is null");
        }

        MCPlayer = mcPlayer;
        GameData = new BSRPlayerGameData(this);
        SetState(BSRPlayerState.InLobby);
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
            throw new NullArgumentException("State may not be null.");
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

    /* Sound. */
    public void PlayErrorSound()
    {
        MCPlayer.playSound(MCPlayer.getLocation(), Sound.BLOCK_NOTE_BLOCK_DIDGERIDOO,
                SoundCategory.PLAYERS, 0.8f, 0.8f);
    }


    /* Events. */
    public void OnTickEvent(ServerTickStartEvent event)
    {
        ActionBarManager.Tick(this);

        if (_state == BSRPlayerState.InGame)
        {
            GameData.GetGamePlayer().OnTickEvent(event);
            return;
        }
    }

    public void OnPlayerDropItemEvent(PlayerDropItemEvent event)
    {
        if (_state == BSRPlayerState.InGame)
        {
            GameData.GetGamePlayer().OnPlayerDropItemEvent(event);
        }
    }

    public void OnPlayerDamageEntityEvent(EntityDamageByEntityEvent event)
    {
        if (_state == BSRPlayerState.InGame)
        {
            GameData.GetGamePlayer().OnPlayerDamageEntityEvent(event);
        }
    }

    public void OnPlayerDeathEvent(PlayerDeathEvent event)
    {
        if (_state == BSRPlayerState.InGame)
        {
            GameData.GetGamePlayer().OnPlayerDeathEvent(event);
        }
    }
    public void OnPlayerInteractEvent(PlayerInteractEvent event)
    {
        if (_state == BSRPlayerState.InGame)
        {
            GameData.GetGamePlayer().OnPlayerInteractEvent(event);
        }
    }

    public void OnPlayerTakeDamageEvent(EntityDamageEvent event)
    {
        if (_state == BSRPlayerState.InGame)
        {
            GameData.GetGamePlayer().OnPlayerTakeDamageEvent(event);
        }
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

        MCPlayer.getInventory().clear();

        MCPlayer.teleport(BSRipoff.GetServerManager().GetSpawnLocation());
    }

    private void SetStateInGame() { }
}