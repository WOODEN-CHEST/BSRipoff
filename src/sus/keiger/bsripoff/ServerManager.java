package sus.keiger.bsripoff;

import com.destroystokyo.paper.event.server.ServerTickStartEvent;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import sus.keiger.bsripoff.game.Game;
import sus.keiger.bsripoff.game.TestGame;
import sus.keiger.bsripoff.player.BSRipoffPlayer;

import java.util.Collection;
import java.util.HashMap;

public class ServerManager
{
    // Fields.
    public final World Overworld;
    public final World TheNether;
    public final World TheEnd;

    public final Game TestingGame;


    // Private static fields.
    private Location _lobbySpawnLocation;
    private final HashMap<Player, BSRipoffPlayer> _bsrPlayers = new HashMap<>();


    // Constructors.
    public ServerManager()
    {
        Overworld = BSRipoff.GetServer().getWorld(NamespacedKey.minecraft("overworld"));
        TheNether = BSRipoff.GetServer().getWorld(NamespacedKey.minecraft("the_nether"));
        TheEnd = BSRipoff.GetServer().getWorld(NamespacedKey.minecraft("the_end"));
        _lobbySpawnLocation = new Location(Overworld, 0d, 0d, 0d);

        TestingGame = new TestGame(null);
    }


    // Methods.
    /* Server. */
    public Server GetServer()
    {
        return BSRipoff.GetServer();
    }

    /* Lobby. */
    public void SetLobbyLocation(Location location)
    {
        if (location == null)
        {
            throw new IllegalArgumentException("Location is null");
        }

        _lobbySpawnLocation = location;
    }

    public Location GetSpawnLocation()
    {
        return  _lobbySpawnLocation;
    }

    /* Players. */
    public BSRipoffPlayer GetBSRPlayer(Player mcPlayer)
    {
        if (mcPlayer == null)
        {
            throw new IllegalArgumentException("MCPlayer is null");
        }

        return _bsrPlayers.get(mcPlayer);
    }

    public void AddBSRipoffPlayer(Player mcPlayer)
    {
        if (_bsrPlayers.containsKey(mcPlayer))
        {
            return;
        }

        BSRipoffPlayer BSRPlayer = new BSRipoffPlayer(mcPlayer);
        _bsrPlayers.put(mcPlayer, BSRPlayer);
    }

    public void RemoveBSRipoffPlayer(Player mcPlayer)
    {
        _bsrPlayers.remove(mcPlayer);
    }

    public void RemoveBSRipoffPlayer(BSRipoffPlayer bsrPlayer)
    {
        _bsrPlayers.remove(bsrPlayer.MCPlayer);
    }

    public Collection<BSRipoffPlayer> GetPlayers()
    {
        return _bsrPlayers.values();
    }

    /* Events. */
    public void OnTickEvent(ServerTickStartEvent event)
    {
        for (BSRipoffPlayer BSRPlayer : _bsrPlayers.values())
        {
            BSRPlayer.OnTickEvent(event);
        }
    }

    public void OnPlayerJoinEvent(PlayerJoinEvent event)
    {
        AddBSRipoffPlayer(event.getPlayer());
    }

    public void OnPlayerQuitEvent(PlayerQuitEvent event)
    {
        RemoveBSRipoffPlayer(event.getPlayer());
    }
}