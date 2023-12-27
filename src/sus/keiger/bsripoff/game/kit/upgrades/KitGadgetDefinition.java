package sus.keiger.bsripoff.game.kit.upgrades;

import org.apache.commons.lang.NullArgumentException;
import sus.keiger.bsripoff.game.kit.KitClass;

public class KitGadgetDefinition
{
    // Static fields.
    public static final int COST = 400;


    // Fields.
    public final String Name;
    public final String Description;


    // Constructors.
    public KitGadgetDefinition(String name, String description)
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
    }


    // Methods.
    public int GetCost(KitClass kitClass)
    {
        return (int)(COST * kitClass.PriceMultiplier);
    }
}