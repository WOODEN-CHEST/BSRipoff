package sus.keiger.bsripoff.game.kit;

import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public enum KitClass
{
    Grass("Grass", NamedTextColor.GREEN, new ItemStack(Material.GRASS_BLOCK, 1), 1f),
    Stone("Stone", NamedTextColor.WHITE, new ItemStack(Material.STONE, 1), 1.25f),
    Prismarine("Prismarine", NamedTextColor.BLUE, new ItemStack(Material.DARK_PRISMARINE, 1), 1.5625f),
    Purpur("Purpur", NamedTextColor.DARK_PURPLE, new ItemStack(Material.PURPUR_BLOCK, 1), 1.953125f),
    Bedrock("Bedrock", NamedTextColor.DARK_GRAY, new ItemStack(Material.BEDROCK, 1), 2.44140625f);


    // Fields.
    public final String ClassName;
    public final float PriceMultiplier;
    public final TextColor ClassColor;
    public final ItemStack Icon;


    // Constructors.
    KitClass(String className, TextColor classColor, ItemStack classIcon, float priceMultiplier)
    {
        ClassName = className;
        ClassColor = classColor;
        Icon =  classIcon;
        PriceMultiplier = priceMultiplier;
    }
}
