package sus.keiger.bsripoff;

import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.entity.Player;
import sus.keiger.bsripoff.player.BSRipoffPlayer;

import java.util.HashMap;

public class ServerManager
{
    // Private fields.
    private static Location _lobbySpawnLocation;

    private static final HashMap<Player, BSRipoffPlayer> _bsrPlayers = new HashMap<>();


    // Methods.
    public static void SetLobbyLocation(Location location)
    {
        if (location == null)
        {
            throw new IllegalArgumentException("Location is null");
        }

        _lobbySpawnLocation = location;
    }

    public static Location GetLobbyLocation()
    {
        return  _lobbySpawnLocation;
    }

    public static BSRipoffPlayer GetBSRipoffPlayer(Player mcPlayer)
    {
        if (mcPlayer == null)
        {
            throw new IllegalArgumentException("MCPlayer is null");
        }

        return _bsrPlayers.get(mcPlayer);
    }

    public static World GetOverworld()
    {
        return BSRipoff.GetPlugin().getServer().getWorld(
                new NamespacedKey(NamespacedKey.MINECRAFT, "overworld"));
    }

    public static World GetTheNether()
    {
        return BSRipoff.GetPlugin().getServer().getWorld(
                new NamespacedKey(NamespacedKey.MINECRAFT, "the_nether"));
    }

    public static World GetTheEnd()
    {
        return BSRipoff.GetPlugin().getServer().getWorld(
                new NamespacedKey(NamespacedKey.MINECRAFT, "the_end"));
    }
}