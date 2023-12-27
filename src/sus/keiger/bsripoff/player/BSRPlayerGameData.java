package sus.keiger.bsripoff.player;

import org.apache.commons.lang.NullArgumentException;
import sus.keiger.bsripoff.game.kit.Kit;
import java.util.HashMap;

public class BSRPlayerGameData
{
    // Private fields.
    private long _coins;
    private Kit _selectedKit;
    private final HashMap<Kit, PlayerKitData> _kitData = new HashMap<>();


    // Constructors.
    public BSRPlayerGameData()
    {
        for (Kit GameKit : Kit.GetRegisteredKits())
        {
            _kitData.put(GameKit, new PlayerKitData(GameKit));
        }

        _selectedKit = Kit.SWARD;
        _coins = 0L;
    }


    // Methods.
    /* Kit. */
    public Kit GetSelectedKit()
    {
        return _selectedKit;
    }

    public void SetSelectedKit(Kit kit)
    {
        if (kit == null)
        {
            throw new NullArgumentException("kit is null");
        }

        _selectedKit = kit;
    }

    public PlayerKitData GetPlayerKitData(Kit kit)
    {
        if (kit == null)
        {
            throw new NullArgumentException("kit is null");
        }

        return _kitData.get(kit);
    }

    public PlayerKitData GetPlayerKitData(String kitName)
    {
        if (kitName == null)
        {
            throw new NullArgumentException("kitName is null");
        }

        return _kitData.get(Kit.GetRegisteredKitByName(kitName));
    }


    /* Coins. */
    public long GetCoins() { return _coins; }

    public void AddCoins(long amount)
    {
        SetCoins(_coins + amount);
    }

    public void RemoveCoins(long amount)
    {
        SetCoins(_coins - amount);
    }

    public void SetCoins(long amount)
    {
        _coins = Math.max(0L, amount);
    }
}