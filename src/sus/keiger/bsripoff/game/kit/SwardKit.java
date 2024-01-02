package sus.keiger.bsripoff.game.kit;

import net.kyori.adventure.text.Component;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import sus.keiger.bsripoff.game.Game;
import sus.keiger.bsripoff.game.GamePlayer;
import sus.keiger.bsripoff.game.kit.upgrades.KitUpgradeDefinition;

public class SwardKit extends Kit
{
    // Static fields.
    public static final String UPGRADE_HEALTH_NAME = "Health";
    public static final String UPGRADE_SUPER_LENGTH_NAME = "Super Length";
    public static final String UPGRADE_ATTACK_DAMAGE_NAME = "Attack Damage";


    // Constructors.
    public SwardKit()
    {
        super(KitClass.Grass,
              new ItemStack(Material.IRON_SWORD, 1),
              "Sward",
              "A simple yet versatile brawler. Swards wears basic armor, wields a basic sword, but has " +
              "an easily chargeable super which temporarily boosts its offensive and defensive capabilities.",
              new KitStats(3, 3, 2, 4));

        AddUpgrade(new KitUpgradeDefinition(UPGRADE_HEALTH_NAME, "Sward's health points",
                30, 10, 1f, 30, 0, 1.35f, 6));
        AddUpgrade(new KitUpgradeDefinition(UPGRADE_SUPER_LENGTH_NAME, "Sward's health points",
                27, 9, 1f, 30, 0, 1.27f, 6));
        AddUpgrade(new KitUpgradeDefinition(UPGRADE_ATTACK_DAMAGE_NAME, "Sward's health points",
                25, 8, 1f, 30, 0, 1.23f, 6));
    }


    // Inherited methods.
    @Override
    public KitInstance CreateInstance(GamePlayer gPlayer, Game game)
    {
        return new SwardKitInstance(gPlayer, game);
    }
}
