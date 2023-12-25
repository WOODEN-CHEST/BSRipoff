package sus.keiger.bsripoff.player;

import org.apache.commons.lang.NullArgumentException;
import sus.keiger.bsripoff.game.kit.Kit;
import sus.keiger.bsripoff.game.kit.KitGadgetDefinition;
import sus.keiger.bsripoff.game.kit.KitUpgradeDefinition;

import java.util.HashMap;

public class PlayerKitData
{
    // Fields.
    public final Kit GameKit;


    // Private fields.
    private final HashMap<String, KitUpgradeLevel> _upgrades = new HashMap<>();
    private final HashMap<String, KitGadgetLevel> _gadgets = new HashMap<>();
    private final HashMap<String, KitStarPowerLevel> _starPowers = new HashMap<>();


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
    public int GetUpgradeLevel(String upgradeName)
    {
        if (upgradeName == null)
        {
            throw new NullArgumentException("upgradeName is null");
        }

        KitUpgradeLevel Level = _upgrades.get(upgradeName);
        return Level != null ? Level.Equipped : KitUpgradeDefinition.MIN_LEVEL;
    }

    public boolean IsGadgetEquipped(String gadgetName)
    {
        if (gadgetName == null)
        {
            throw new NullArgumentException("gadgetName is null");
        }

        KitGadgetLevel Level = _gadgets.get(gadgetName);
        return Level != null && Level.IsEquipped;
    }

    public boolean IsStarPowerEquipped(String starPowerName)
    {
        if (starPowerName == null)
        {
            throw new NullArgumentException("starPowerName is null");
        }

        KitStarPowerLevel Level = _starPowers.get(starPowerName);
        return Level != null && Level.IsEquipped;
    }

    public long TryPurchaseUpgrade(String upgradeName, long balance)
    {
        if (upgradeName == null)
        {
            throw new NullArgumentException("upgradeName is null");
        }

        KitUpgradeDefinition UpgradeDefinition = GameKit.GetUpgrade(upgradeName);
        if (UpgradeDefinition == null)
        {
            return 0L;
        }

        KitUpgradeLevel Level = _upgrades.get(upgradeName);
        if (Level.Unlocked >= KitUpgradeDefinition.MAX_LEVEL)
        {
            return 0L;
        }

        int UpgradeCost = UpgradeDefinition.GetUpgradeCostCoins(Level.Unlocked + 1, GameKit.Classification);
        if (balance < UpgradeCost)
        {
            return 0L;
        }

        Level.Unlocked++;
        return UpgradeCost;
    }

    public void SetUpgradeLevel(String upgradeName, int level)
    {

    }
}