package sus.keiger.bsripoff.player.ui;

import org.apache.commons.lang.NullArgumentException;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import sus.keiger.bsripoff.player.BSRipoffPlayer;

import java.util.function.Consumer;


public abstract class UserInterfaceElement
{
    // Private fields.
    private ItemStack _item;
    private int _slot;
    private Consumer<UserInterfaceElement> _updateHandler;



    // Constructors.
    public UserInterfaceElement(ItemStack item, int slot)
    {
        if (item == null)
        {
            throw new NullArgumentException("item is null");
        }

        _item = item;
        _slot = slot;
    }


    // Methods.
    public void OnInventoryClickEvent(InventoryClickEvent event, UserInterfacePage page, BSRipoffPlayer bsrPlayer)
    {
        if (event.isLeftClick())
        {
            if (event.isShiftClick())
            {
                OnShiftLeftClickEvent(event, page, bsrPlayer);
            }
            else
            {
                OnLeftClickEvent(event, page, bsrPlayer);
            }
        }
        else if (event.isRightClick())
        {
            if (event.isShiftClick())
            {
                OnShiftRightClickEvent(event, page, bsrPlayer);
            }
            else
            {
                OnRightClickEvent(event, page, bsrPlayer);
            }
        }
    }

    public ItemStack GetItem() { return _item; }

    public void EditItem(Consumer<ItemStack> editMethod)
    {
        if (editMethod == null)
        {
            throw new NullArgumentException("editMethod is null");
        }

        editMethod.accept(_item);

        if (_updateHandler != null)
        {
            _updateHandler.accept(this);
        }
    }

    public int GetSlot() { return _slot; }

    public void SetUpdateHandler(Consumer<UserInterfaceElement> handler) { _updateHandler = handler; }



    // Protected methods.
    protected abstract void OnLeftClickEvent(InventoryClickEvent event, UserInterfacePage page, BSRipoffPlayer bsrPlayer);

    protected abstract void OnRightClickEvent(InventoryClickEvent event, UserInterfacePage page, BSRipoffPlayer bsrPlayer);

    protected abstract void OnShiftLeftClickEvent(InventoryClickEvent event, UserInterfacePage page, BSRipoffPlayer bsrPlayer);

    protected abstract void OnShiftRightClickEvent(InventoryClickEvent event, UserInterfacePage page, BSRipoffPlayer bsrPlayer);
}