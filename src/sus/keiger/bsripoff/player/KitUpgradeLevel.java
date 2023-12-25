package sus.keiger.bsripoff.player;

import org.apache.commons.lang.NullArgumentException;
import sus.keiger.bsripoff.game.kit.KitUpgradeDefinition;

public class KitUpgradeLevel
{
    // Private fields.
    private int _equipped = 1;
    private int _unlocked = 1;
    private KitUpgradeDefinition _definition;


    // Constructors.
    public KitUpgradeLevel(KitUpgradeDefinition definition)
    {
        if (definition == null)
        {
            throw new NullArgumentException("definition is null");
        }

        _definition = definition;
    }


    // Methods.
    public int GetEquipped() { return _equipped; }
    public void SetEquipped(int level)
    {
        _equipped = Math.max(KitUpgradeDefinition.MIN_LEVEL, Math.max(level, _definition.GetMaxLevel()));
    }
}