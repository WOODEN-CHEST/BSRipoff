package sus.keiger.bsripoff.player;

import org.apache.commons.lang.NullArgumentException;
import sus.keiger.bsripoff.BSRipoff;
import sus.keiger.bsripoff.game.kit.Kit;
import sus.keiger.bsripoff.game.kit.KitGadgetDefinition;
import sus.keiger.bsripoff.game.kit.KitUpgradeDefinition;

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



    public boolean TryPurchaseGadget(Kit kit, String gadgetName)
    {
        if (kit == null)
        {
            throw new NullArgumentException("kit is null");
        }
        if (gadgetName == null)
        {
            throw new NullArgumentException("gadgetName is null");
        }

        KitUpgradeDefinition GadgetDefinition = kit.GetUpgrade(gadgetName);
        if (GadgetDefinition == null)
        {
            return false;
        }

        if (!_equippedGadgets.containsKey(gadgetName))
        {
            BSRipoff.GetLogger().warning("Kit gadget \"%s\" exists for a kit but not ".formatted(gadgetName) +
                    "for a BSRPlayerGameData instance, this should be impossible.");
            return false;
        }

        if (_equippedGadgets.get(gadgetName))
        {
            return false;
        }

        int UpgradeCost = UpgradeDefinition.GetUpgradeCostCoins(SelfLevel + 1, kit.Classification);
        if (_coins < UpgradeCost)
        {
            return false;
        }

        RemoveCoins(UpgradeCost);
        _kitUpgrades.put(upgradeName, SelfLevel + 1);
        return true;
    }
}