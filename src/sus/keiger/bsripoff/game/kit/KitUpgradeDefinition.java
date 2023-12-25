package sus.keiger.bsripoff.game.kit;

import org.apache.commons.lang.NullArgumentException;

public class KitUpgradeDefinition
{
    // Static fields.
    public static final int MAX_LEVEL = 6;
    public static final int MIN_LEVEL = 1;


    // Fields.
    public final String Name;
    public final String Description;


    // Private fields.
    private final int _baseCoinCost;
    private final int _arithmeticCoinChange;
    private final float _geometricCoinChange;
    private final int _basePowerPointCost;
    private final int _arithmeticPowerPointChange;
    private final float _geometricPowerPointChange;
    private final int _maxLevel;


    // Constructors.
    public KitUpgradeDefinition(String name,
                                String description,
                                int baseCoinCost,
                                int arithmeticCoinChange,
                                float geometricCoinChange,
                                int basePowerPointCost,
                                int arithmeticPowerPointChange,
                                float geometricPowerPointChange,
                                int maxLevel)
    {
        if (name == null)
        {
            throw new NullArgumentException("name is null");
        }
        if (description == null)
        {
            throw new NullArgumentException("description is null");
        }

        Name = name;
        Description = description;

        _baseCoinCost = baseCoinCost;
        _arithmeticCoinChange = arithmeticCoinChange;
        _geometricCoinChange = geometricCoinChange;

        _basePowerPointCost = basePowerPointCost;
        _arithmeticPowerPointChange = arithmeticPowerPointChange;
        _geometricPowerPointChange = geometricPowerPointChange;

        _maxLevel = Math.max(MIN_LEVEL, Math.min(maxLevel, MAX_LEVEL));
    }


    // Methods.
    public int GetMaxLevel()
    {
        return _maxLevel;
    }

    public int GetUpgradeCostCoins(int level, KitClass kitClass)
    {
        level = Math.max(MIN_LEVEL, Math.min(level, _maxLevel));
        return (int)((_baseCoinCost * Math.pow(_geometricCoinChange, level - 1) + _arithmeticCoinChange * (level - 1))
                * kitClass.PriceMultiplier);
    }

    public int GetPowerPointCost(int level, KitClass kitClass)
    {
        level = Math.max(MIN_LEVEL, Math.min(level, _maxLevel));
        return (int)((_basePowerPointCost * Math.pow(_geometricPowerPointChange, level - 1)
                + _arithmeticPowerPointChange * (level - 1)) * kitClass.PriceMultiplier);
    }
}