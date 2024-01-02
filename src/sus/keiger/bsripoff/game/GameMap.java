package sus.keiger.bsripoff.game;

import org.apache.commons.lang.NullArgumentException;
import org.bukkit.Location;
import org.bukkit.WeatherType;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class GameMap
{
    // Fields.
    public final Location MapSourceLocation;
    public final Location MapLocation;
    public final Vector Size;
    public final String Name;


    // Private static fields.
    private static final HashMap<GameMap, GameMapCopyData> _mapsBeingCopied = new HashMap<>();
    private static final int MAX_BLOCKS_TO_COPY_PER_TICK = 10_000;


    // Private fields.
    private Location _spawnLocation;
    private final ArrayList<Location> _blueTeamSpawns = new ArrayList<>();
    private final ArrayList<Location> _redTeamSpawns = new ArrayList<>();
    private final ArrayList<Location> _genericSpawns = new ArrayList<>();
    private WeatherType _weather = WeatherType.CLEAR;
    private long _time = 0L;


    // Constructors.
    public GameMap(Location mapLocation, Vector size, Location mapSourceLocation, Location spawnLocation, String name)
    {
        if (mapSourceLocation == null)
        {
            throw new NullArgumentException("map mapSourceLocation is null");
        }
        if (mapLocation == null)
        {
            throw new NullArgumentException("map mapLocation is null");
        }
        if (size == null)
        {
            throw new NullArgumentException("map size is null");
        }
        if (name == null)
        {
            throw new NullArgumentException("map size is null");
        }

        MapLocation = mapLocation;
        MapSourceLocation = mapSourceLocation;
        Size = size;
        SetSpawnLocation(spawnLocation);
        Name = name;
    }


    // Static methods.
    public static void TickCopying()
    {
        for (Map.Entry<GameMap, GameMapCopyData> CopyEntry : _mapsBeingCopied.entrySet())
        {
            CopyEntry.getValue().CopyTick(CopyEntry.getKey().MapSourceLocation, CopyEntry.getKey().MapLocation);
        }
    }


    // Methods.
    /* Getters and setters. */
    public Location GetSpawnLocation()
    {
        return _spawnLocation;
    }

    public void SetSpawnLocation(Location spawnLocation)
    {
        if (spawnLocation != null)
        {
            _spawnLocation = spawnLocation;
        }
        else
        {
            _spawnLocation = new Location(MapSourceLocation.getWorld(),
                    Size.getX() * 0.5d, Size.getY() * 0.5d, Size.getZ() * 0.5d, 0f, 0f);
        }
    }

    public Collection<Location> GetGenericSpawnLocations()
    {
        return _genericSpawns;
    }

    public void AddGenericSpawnLocation(Location location)
    {
        if (location == null)
        {
            throw new NullArgumentException("location is null");
        }

        _genericSpawns.add(location);
    }

    public void ClearGenericSpawns()
    {
        _genericSpawns.clear();
    }

    public Collection<Location> GetBlueSpawnLocations()
    {
        return _blueTeamSpawns;
    }

    public void AddBlueSpawnLocation(Location location)
    {
        if (location == null)
        {
            throw new NullArgumentException("location is null");
        }

        _blueTeamSpawns.add(location);
    }

    public void ClearBlueSpawns()
    {
        _blueTeamSpawns.clear();
    }

    public Collection<Location> GetRedSpawnLocations()
    {
        return _redTeamSpawns;
    }

    public void AddRedSpawnLocation(Location location)
    {
        if (location == null)
        {
            throw new NullArgumentException("location is null");
        }

        _redTeamSpawns.add(location);
    }

    public void ClearRedSpawns()
    {
        _redTeamSpawns.clear();
    }

    public WeatherType GetWeather() { return _weather; }

    public void SetWeatherType(WeatherType weather)
    {
        if (weather == null)
        {
            throw new NullArgumentException("Weather is null");
        }

        _weather = weather;
    }

    public long GetTime() { return _time; }

    public void SetTime(long value)
    {
        _time = value;
    }


    /* Copying. */
    public void CopyFromSource()
    {
        if (!_mapsBeingCopied.containsKey(this))
        {
            _mapsBeingCopied.put(this, new GameMapCopyData((int)Size.getX(), (int)Size.getY(), (int)Size.getZ()));
        }
    }

    public boolean IsBeingCopied()
    {
        return _mapsBeingCopied.containsKey(this);
    }
}