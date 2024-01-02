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
    private final HashMap<String, Boolean> _gadgets = new HashMap<>();
    private final HashMap<String, Boolean> _starPowers = new HashMap<>();
    private String _equippedGadget = null;
    private String _equippedStarPower = null;
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
            _gadgets.put(Upgrade.Name, false);
        }
        for (KitUpgradeDefinition Upgrade : GameKit.GetUpgrades())
        {
            _starPowers.put(Upgrade.Name, false);
        }
    }


    // Methods.
    /* Upgrades. */
    public KitUpgradeLevel GetUpgradeLevel(String upgradeName)
    {
        if (upgradeName == null)
        {
            throw new NullArgumentException("upgradeName is null");
        }

        return _upgrades.get(upgradeName);
    }


    /* Gadgets. */
    public boolean IsGadgetUnlocked(String gadgetName)
    {
        if (gadgetName == null)
        {
            throw new NullArgumentException("gadgetName is null");
        }

        return _gadgets.getOrDefault(gadgetName, false);
    }

    public void SetIsGadgetUnlocked(String gadgetName, boolean isUnlocked)
    {
        if (gadgetName == null)
        {
            throw new NullArgumentException("gadgetName is null");
        }

        if (_gadgets.containsKey(gadgetName))
        {
            _gadgets.put(gadgetName, isUnlocked);
        }
    }

    public String GetEquippedGadget() { return _equippedGadget; }

    public void SetEquippedGadget(String gadgetName)
    {
        if (gadgetName == null)
        {
            throw new NullArgumentException("gadgetName is null");
        }

        if (!_gadgets.containsKey(gadgetName) || !_gadgets.get(gadgetName))
        {
            return;
        }
        _equippedGadget = gadgetName;
    }

    public void ForceSetEquippedGadget(String gadgetName)
    {
        if (gadgetName == null)
        {
            throw new NullArgumentException("gadgetName is null");
        }

        _equippedGadget = gadgetName;
    }



    /* Star powers. */
    public boolean IsStarPowerUnlocked(String starPowerName)
    {
        if (starPowerName == null)
        {
            throw new NullArgumentException("starPowerName is null");
        }

        return _starPowers.getOrDefault(starPowerName, false);
    }

    public void SetIsStarPowerUnlocked(String starPowerName, boolean isUnlocked)
    {
        if (starPowerName == null)
        {
            throw new NullArgumentException("starPowerName is null");
        }

        if (_starPowers.containsKey(starPowerName))
        {
            _starPowers.put(starPowerName, isUnlocked);
        }
    }

    public String GetEquippedStarPower() { return _equippedStarPower; }

    public void SetEquippedStarPower(String starPowerName)
    {
        if (starPowerName == null)
        {
            throw new NullArgumentException("starPowerName is null");
        }

        if (!_starPowers.containsKey(starPowerName) || !_starPowers.get(starPowerName))
        {
            return;
        }
        _equippedStarPower = starPowerName;
    }

    public void ForceSetEquippedStarPower(String starPowerName)
    {
        if (starPowerName == null)
        {
            throw new NullArgumentException("starPowerName is null");
        }

        _equippedStarPower = starPowerName;
    }


    /* Kit. */
    public boolean GetIsKitUnlocked() { return _isKitUnlocked; }

    public void SetIsKitUnlocked(boolean value)
    {
        _isKitUnlocked = value;
    }
}