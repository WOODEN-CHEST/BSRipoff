package sus.keiger.bsripoff.game.kit;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import sus.keiger.bsripoff.game.kit.upgrades.KitUpgradeDefinition;

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

    @Override
    public void CreateInventory(KitInstance instance, Inventory inventory)
    {
        ItemStack Helmet = new ItemStack(Material.IRON_HELMET, 1);
        FormatEquipment(Helmet, true, Component.text("Helmet"));
        ItemStack Chestplate = new ItemStack(Material.IRON_CHESTPLATE, 1);
        FormatEquipment(Chestplate, true, Component.text("Chestplate"));
        ItemStack Leggings = new ItemStack(Material.IRON_LEGGINGS, 1);
        FormatEquipment(Leggings, true, Component.text("Leggings"));
        ItemStack Boots = new ItemStack(Material.IRON_BOOTS, 1);
        FormatEquipment(Boots, true, Component.text("Boots"));
        ItemStack Sword = new ItemStack(Material.IRON_SWORD, 1);
        FormatEquipment(Sword, true, Component.text("Sword"));

        inventory.setItem(SLOT_ARMOR_HEAD, Helmet);
        inventory.setItem(SLOT_ARMOR_CHEST, Chestplate);
        inventory.setItem(SLOT_ARMOR_LEGS, Leggings);
        inventory.setItem(SLOT_ARMOR_FEET, Boots);
        inventory.setItem(SLOT_HOTBAR1, Sword);
        inventory.setItem(SLOT_HOTBAR2, instance.GetSuperItem());
    }
}
