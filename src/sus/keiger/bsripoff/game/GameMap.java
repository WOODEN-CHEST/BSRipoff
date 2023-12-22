package sus.keiger.bsripoff.game;

import org.apache.commons.lang.NullArgumentException;
import org.bukkit.Location;
import org.bukkit.util.Vector;

public class GameMap
{
    // Fields.
    public final Location SourceLocation;
    public final Vector Size;

    // Private fields.
    private Location SpawnLocation;


    // Constructors.
    public GameMap(Location sourceLocation, Vector size)
    {
        if (sourceLocation == null)
        {
            throw new NullArgumentException("map sourceLocation is null");
        }
        if (size == null)
        {
            throw new NullArgumentException("map size is null");
        }

        SourceLocation = sourceLocation;
        Size = size;

        //SpawnLocation = new Location(SourceLocation.getWorld(), );
    }
}