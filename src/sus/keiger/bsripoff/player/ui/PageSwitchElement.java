package sus.keiger.bsripoff.player.ui;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import sus.keiger.bsripoff.player.BSRipoffPlayer;

public class PageSwitchElement extends UserInterfaceElement
{
    // Private fields.
    private UserInterfacePageSelection _targetPage;


    // Constructors.
    public PageSwitchElement(ItemStack item, int slot, UserInterfacePageSelection targetPage)
    {
        super(item, slot);
        _targetPage = targetPage;
    }


    // Methods.
    public void SetTargetPage(UserInterfacePageSelection page) { _targetPage = page; }

    public UserInterfacePageSelection GetTargetPage() { return _targetPage; }


    // Private methods.
    private void GoToTargetPage(BSRipoffPlayer bsrPlayer)
    {
        if (_targetPage != null)
        {
            bsrPlayer.UIManager.OpenInterface(_targetPage, this);
        }
    }


    // Inherited methods.
    @Override
    protected void OnLeftClickEvent(InventoryClickEvent event, UserInterfacePage page, BSRipoffPlayer bsrPlayer)
    {
        GoToTargetPage(bsrPlayer);
    }

    @Override
    protected void OnRightClickEvent(InventoryClickEvent event, UserInterfacePage page, BSRipoffPlayer bsrPlayer)
    {
        GoToTargetPage(bsrPlayer);
    }

    @Override
    protected void OnShiftLeftClickEvent(InventoryClickEvent event, UserInterfacePage page, BSRipoffPlayer bsrPlayer)
    {
        GoToTargetPage(bsrPlayer);
    }

    @Override
    protected void OnShiftRightClickEvent(InventoryClickEvent event, UserInterfacePage page, BSRipoffPlayer bsrPlayer)
    {
        GoToTargetPage(bsrPlayer);
    }
}