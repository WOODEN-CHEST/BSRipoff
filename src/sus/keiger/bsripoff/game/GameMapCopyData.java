package sus.keiger.bsripoff.game;

import org.apache.commons.lang.NullArgumentException;
import org.bukkit.Location;
import org.bukkit.block.Block;

public class GameMapCopyData
{
    // Private fields.
    private final int _sizeX;
    private final int _sizeY;
    private final int _sizeZ;
    private int _x;
    private int _y;
    private int _z;
    private final int _maxBlocksToCopyPerTick;


    // Constructors.
    public GameMapCopyData(int sizeX, int sizeY, int sizeZ)
    {
        this(sizeX, sizeY, sizeZ, 10_000);
    }

    public GameMapCopyData(int sizeX, int sizeY, int sizeZ, int maxBlocksToCopyPerTick)
    {
        _sizeX = sizeX;
        _sizeY = sizeY;
        _sizeZ = sizeZ;
        _maxBlocksToCopyPerTick = maxBlocksToCopyPerTick;
    }


    // Methods.
    public void CopyTick(Location source, Location destination)
    {
        if (source == null)
        {
            throw new NullArgumentException("source location is null");
        }
        if (destination == null)
        {
            throw new NullArgumentException("destination location is null");
        }

        int BlocksCopied = 0;

        while ((BlocksCopied < _maxBlocksToCopyPerTick) && (_x < _sizeX))
        {
            Block DestinationBlock = destination.getWorld().getBlockAt(
                    (int)destination.getX() + _x, (int)destination.getZ() + _y, (int)destination.getZ() + _z);
            Block SourceBlock = source.getWorld().getBlockAt(
                    (int)source.getX() + _x, (int)source.getZ() + _y, (int)source.getZ() + _z);

            DestinationBlock.setType(SourceBlock.getType());
            DestinationBlock.setBlockData(SourceBlock.getBlockData());

            BlocksCopied++;
            IncrementCoordinate();
        }
    }


    // Private methods.
    private void IncrementCoordinate()
    {
        _z++;

        if (_z >= _sizeZ)
        {
            _z = 0;
            _y++;
        }
        if (_y >= _sizeY)
        {
            _y = 0;
            _x++;
        }
    }
}