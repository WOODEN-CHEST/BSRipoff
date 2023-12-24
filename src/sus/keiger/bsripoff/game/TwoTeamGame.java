package sus.keiger.bsripoff.game;

import org.bukkit.Location;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class TwoTeamGame extends Game
{
    // Private fields.
    private final ArrayList<GamePlayer> _bluePlayers = new ArrayList<>();
    private final ArrayList<GamePlayer> _redPlayers = new ArrayList<>();


    // Constructors.
    public TwoTeamGame(GameMap map)
    {
        super(map);
    }



    // Private methods.
    private void TeleportTeamToSpawn(Collection<Location> spawns, Collection<GamePlayer> players)
    {
        Iterator<GamePlayer> PlayerIterator = players.iterator();
        for (Location SpawnLocation : spawns)
        {
            if (PlayerIterator.hasNext())
            {
                PlayerIterator.next().MCPlayer.teleport(SpawnLocation);
            }
            else
            {
                break;
            }
        }
    }


    // Inherited methods.
    @Override
    public void Start()
    {
        super.Start();

        TeleportTeamToSpawn(GetMap().GetBlueSpawnLocations(), _bluePlayers);
        TeleportTeamToSpawn(GetMap().GetRedSpawnLocations(), _redPlayers);
    }
}
