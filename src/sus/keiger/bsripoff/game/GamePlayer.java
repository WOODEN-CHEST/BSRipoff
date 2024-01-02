package sus.keiger.bsripoff.game;

import com.destroystokyo.paper.event.server.ServerTickStartEvent;
import org.apache.commons.lang.NullArgumentException;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import sus.keiger.bsripoff.game.kit.KitInstance;
import sus.keiger.bsripoff.player.BSRPlayerGameData;
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

    public void SetKitInstance(KitInstance instance)
    {
        if (instance == null)
        {
            throw new NullArgumentException("instance is null.");
        }

        _kitInstance = instance;
    }

    public void CreateKitInstance()
    {
        if (_kitInstance == null)
        {
            _kitInstance = BSRPlayer.GameData.GetSelectedKit().CreateInstance(this, CurrentGame);
        }
    }

    public BSRPlayerGameData GetPlayerGameData() { return BSRPlayer.GameData; }


    /* Events. */
    public void OnTickEvent(ServerTickStartEvent event)
    {
        if (CurrentGame.GetState() == GameState.InGame)
        {
            _kitInstance.OnTickEvent();
        }
    }

    public void OnPlayerDropItemEvent(PlayerDropItemEvent event)
    {
        if (CurrentGame.GetState() == GameState.InGame)
        {
            _kitInstance.OnPlayerDropItemEvent(event);
        }
    }

    public void OnPlayerDamageEntityEvent(EntityDamageByEntityEvent event)
    {
        if (CurrentGame.GetState() == GameState.InGame)
        {
            _kitInstance.OnPlayerDamageEntityEvent(event);
        }
    }

    public void OnPlayerDeathEvent(PlayerDeathEvent event)
    {
        if (CurrentGame.GetState() == GameState.InGame)
        {
            _kitInstance.OnPlayerDeathEvent(event);
        }
    }

    public void OnPlayerInteractEvent(PlayerInteractEvent event)
    {
        if (CurrentGame.GetState() == GameState.InGame)
        {
            _kitInstance.OnPlayerInteractEvent(event);
        }
    }

    public void OnPlayerTakeDamageEvent(EntityDamageEvent event)
    {
        if (CurrentGame.GetState() == GameState.InGame)
        {
            _kitInstance.OnPlayerTakeDamageEvent(event);
        }
    }
}