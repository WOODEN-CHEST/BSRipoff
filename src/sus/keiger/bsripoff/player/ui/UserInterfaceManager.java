package sus.keiger.bsripoff.player.ui;

import org.apache.commons.lang.NullArgumentException;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import sus.keiger.bsripoff.BSRipoff;
import sus.keiger.bsripoff.game.kit.Kit;
import sus.keiger.bsripoff.player.BSRipoffPlayer;

import java.util.HashMap;

public class UserInterfaceManager
{
    // Private fields.
    private final BSRipoffPlayer _bsrPlayer;
    private boolean _isEnabled;
    private final HashMap<UserInterfacePageSelection, UserInterfacePage> _pages = new HashMap<>();
    private UserInterfacePage _currentPage;


    // Constructors.
    public UserInterfaceManager(BSRipoffPlayer bsrPlayer)
    {
        if (bsrPlayer == null)
        {
            throw new NullArgumentException("bsrPlayer is null");
        }

        _bsrPlayer = bsrPlayer;
        CreatePages();
    }


    // Methods.
    /* Ability. */
    public boolean IsEnabled() { return _isEnabled; }

    public void SetEnabled(boolean value)
    {
        _isEnabled = value;
        if (_isEnabled)
        {
            SetMainInventory();
        }
    }


    /* Opening and closing. */
    public void OpenInterface(UserInterfacePageSelection selection)
    {
        OpenInterface(selection, null);
    }

    public void OpenInterface(UserInterfacePageSelection selection, UserInterfaceElement openCause)
    {
        if (selection == null)
        {
            throw new NullArgumentException("selection is null");
        }

        if (_pages.containsKey(selection))
        {
            _currentPage = _pages.get(selection);
            _currentPage.Open(_bsrPlayer, openCause);
        }
    }

    public void CloseInterface()
    {
        _currentPage.Close(_bsrPlayer);
        _currentPage = null;
    }


    /* Inventory. */
    public void SetMainInventory()
    {
        _bsrPlayer.MCPlayer.getInventory().clear();
        _bsrPlayer.MCPlayer.getInventory().setItem(Kit.SLOT_HOTBAR5, new ItemStack(Material.TARGET, 1));
    }


    /* Events. */
    public void OnPlayerDropItemEvent(PlayerDropItemEvent event)
    {
        if (!IsEnabled())
        {
            return;
        }

        event.setCancelled(true);
    }

    public void OnPlayerInteractEvent(PlayerInteractEvent event)
    {
        if (!IsEnabled() || !event.getAction().isRightClick())
        {
            return;
        }

        if (event.getItem() == null)
        {
            return;
        }

        switch (event.getItem().getType())
        {
            case TARGET -> OpenInterface(UserInterfacePageSelection.GameModes);
        }
    }

    public void OnInventoryClickEvent(InventoryClickEvent event)
    {
        if (!IsEnabled())
        {
            return;
        }

        event.setCancelled(true);

        if (_currentPage != null)
        {
            BSRipoff.GetLogger().warning("click event triggered");
            _currentPage.OnInventoryClickEvent(event, _bsrPlayer);
        }
    }

    public void OnTickEvent()
    {
        if (_currentPage != null)
        {
            _currentPage.OnTickEvent(_bsrPlayer);
        }
    }


    // Private methods.
    /* Inventories. */
    private void CreatePages()
    {
        CreateGameModePage();
        Create3v3ModePage();
    }

    private void CreateGameModePage()
    {
        UserInterfacePage Page = new UserInterfacePage(UserInterfacePageType.Small, "Game Mode");

        Page.AddElement(new PageSwitchElement(new ItemStack(Material.DIAMOND_BLOCK),
                UserInterfacePage.SLOT_ROW2COLUMN1, UserInterfacePageSelection.Game3v3));


        _pages.put(UserInterfacePageSelection.GameModes, Page);
    }

    private void Create3v3ModePage()
    {
        UserInterfacePage Page = new UserInterfacePage(UserInterfacePageType.Small, "3v3Mode page");

        Page.AddElement(new PageSwitchElement(new ItemStack(Material.NETHER_WART),
                UserInterfacePage.SLOT_ROW3COLUMN9, UserInterfacePageSelection.GameModes));


        _pages.put(UserInterfacePageSelection.Game3v3, Page);
    }
}