package sus.keiger.bsripoff;

import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import sus.keiger.bsripoff.player.BSRipoffPlayer;

import java.util.HashMap;

public class ServerManager
{
    // Fields.
    public final World Overworld;
    public final World TheNether;
    public final World TheEnd;


    // Private static fields.
    private Location _lobbySpawnLocation;
    private final HashMap<Player, BSRipoffPlayer> _bsrPlayers = new HashMap<>();


    // Constructors.
    public ServerManager()
    {
        BSRipoff.GetLogger().warning("World count: %d".formatted(BSRipoff.GetServer().getWorlds().size()));
        Overworld = BSRipoff.GetServer().getWorld(NamespacedKey.minecraft("overworld"));
        TheNether = BSRipoff.GetServer().getWorld(NamespacedKey.minecraft("the_nether"));
        TheEnd = BSRipoff.GetServer().getWorld(NamespacedKey.minecraft("the_end"));
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

    public Location GetLobbyLocation()
    {
        return  _lobbySpawnLocation;
    }

    /* Players. */
    public BSRipoffPlayer GetBSRipoffPlayer(Player mcPlayer)
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

    public void OnPlayerJoinEvent(PlayerJoinEvent event)
    {
        AddBSRipoffPlayer(event.getPlayer());
        BSRipoff.GetLogger().warning("Player joined. Now have %d players.".formatted(_bsrPlayers.size()));
    }

    public void OnPlayerQuitEvent(PlayerQuitEvent event)
    {
        RemoveBSRipoffPlayer(event.getPlayer());
        BSRipoff.GetLogger().warning("Player left. Now have %d players.".formatted(_bsrPlayers.size()));
    }
}