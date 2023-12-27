package sus.keiger.bsripoff.player;

import org.apache.commons.lang.NullArgumentException;
import sus.keiger.bsripoff.game.kit.Kit;
import sus.keiger.bsripoff.game.kit.upgrades.KitGadgetDefinition;
import sus.keiger.bsripoff.game.kit.upgrades.KitUpgradeDefinition;

import java.util.HashMap;

public class PlayerKitData
{
    // Fields.
    public final Kit GameKit;


    // Private fields.
    private final HashMap<String, KitUpgradeLevel> _upgrades = new HashMap<>();
    private final HashMap<String, KitGadgetLevel> _gadgets = new HashMap<>();
    private final HashMap<String, KitStarPowerLevel> _starPowers = new HashMap<>();
    private boolean _isKitUnlocked = false;


    // Constructors.
    public PlayerKitData(Kit kit)
    {
        if (kit == null)
        {
            throw new NullArgumentException("kit is null");
        }

        GameKit = kit;

        for (KitUpgradeDefinition Upgrade : GameKit.GetUpgrades())
        {
            _upgrades.put(Upgrade.Name, new KitUpgradeLevel(Upgrade));
        }
        for (KitGadgetDefinition Upgrade : GameKit.GetGadgets())
        {
            _gadgets.put(Upgrade.Name, new KitGadgetLevel());
        }
        for (KitUpgradeDefinition Upgrade : GameKit.GetUpgrades())
        {
            _starPowers.put(Upgrade.Name, new KitStarPowerLevel());
        }
    }


    // Methods.
    public KitUpgradeLevel GetUpgradeLevel(String upgradeName)
    {
        if (upgradeName == null)
        {
            throw new NullArgumentException("upgradeName is null");
        }

        return _upgrades.get(upgradeName);
    }

    public KitGadgetLevel GetGadgetLevel(String gadgetName)
    {
        if (gadgetName == null)
        {
            throw new NullArgumentException("gadgetName is null");
        }

        return _gadgets.get(gadgetName);
    }

    public KitStarPowerLevel GetStarPowerLevel(String starPowerName)
    {
        if (starPowerName == null)
        {
            throw new NullArgumentException("starPowerName is null");
        }

        return _starPowers.get(starPowerName);
    }

    public boolean GetIsKitUnlocked() { return _isKitUnlocked; }

    public void SetIsKitUnlocked(boolean value)
    {
        _isKitUnlocked = value;
    }
}