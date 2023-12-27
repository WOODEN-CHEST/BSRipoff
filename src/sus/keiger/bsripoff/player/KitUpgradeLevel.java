package sus.keiger.bsripoff.player;

import org.apache.commons.lang.NullArgumentException;
import sus.keiger.bsripoff.game.kit.upgrades.KitUpgradeDefinition;

public class KitUpgradeLevel
{
    // Fields.
    public final KitUpgradeDefinition UpgradeDefinition;


    // Private fields.
    private int _equipped = 1;
    private int _unlocked = 1;



    // Constructors.
    public KitUpgradeLevel(KitUpgradeDefinition definition)
    {
        if (definition == null)
        {
            throw new NullArgumentException("definition is null");
        }

        UpgradeDefinition = definition;
    }


    // Methods.
    public int GetEquipped() { return _equipped; }

    public void SetEquipped(int level)
    {
        _equipped = Math.max(KitUpgradeDefinition.MIN_LEVEL, Math.max(level, _unlocked));
    }

    public void ForceSetEquipped(int level)
    {
        _equipped = Math.max(KitUpgradeDefinition.MIN_LEVEL, Math.min(level, UpgradeDefinition.GetMaxLevel()));
    }

    public int GetUnlocked() { return _unlocked; }

    public void SetUnlocked(int level)
    {
        _unlocked = Math.max(KitUpgradeDefinition.MIN_LEVEL, Math.min(level, UpgradeDefinition.GetMaxLevel()));
    }
}