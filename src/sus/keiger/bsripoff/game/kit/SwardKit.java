package sus.keiger.bsripoff.game.kit;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class SwardKit extends Kit
{
    public SwardKit()
    {
        super(KitClass.Grass,
                new ItemStack(Material.IRON_SWORD, 1),
                "Sward",
                "A simple yet versatile brawler. Swards wears basic armor, wields a basic sword, but has" +
                        "an easily chargeable super which temporarily boosts its offensive and defensive capabilities.",
                new KitStats(3, 3, 2, 5));

        AddUpgrade(new KitUpgradeDefinition("Health", "Sward's health points", 30, 10, 1f, 30, 0, 1.35f, 6));
    }

}
