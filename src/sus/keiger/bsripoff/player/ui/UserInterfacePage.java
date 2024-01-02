package sus.keiger.bsripoff.player.ui;

import org.apache.commons.lang.NullArgumentException;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import sus.keiger.bsripoff.BSRipoff;
import sus.keiger.bsripoff.player.BSRipoffPlayer;

import java.util.HashMap;

public class UserInterfacePage
{
    // Static fields.
    public static final int SLOT_ROW1COLUMN1 = 0;
    public static final int SLOT_ROW1COLUMN2 = 1;
    public static final int SLOT_ROW1COLUMN3 = 2;
    public static final int SLOT_ROW1COLUMN4 = 3;
    public static final int SLOT_ROW1COLUMN5 = 4;
    public static final int SLOT_ROW1COLUMN6 = 5;
    public static final int SLOT_ROW1COLUMN7 = 6;
    public static final int SLOT_ROW1COLUMN8 = 7;
    public static final int SLOT_ROW1COLUMN9 = 8;

    public static final int SLOT_ROW2COLUMN1 = 9;
    public static final int SLOT_ROW2COLUMN2 = 10;
    public static final int SLOT_ROW2COLUMN3 = 11;
    public static final int SLOT_ROW2COLUMN4 = 12;
    public static final int SLOT_ROW2COLUMN5 = 13;
    public static final int SLOT_ROW2COLUMN6 = 14;
    public static final int SLOT_ROW2COLUMN7 = 15;
    public static final int SLOT_ROW2COLUMN8 = 16;
    public static final int SLOT_ROW2COLUMN9 = 17;

    public static final int SLOT_ROW3COLUMN1 = 18;
    public static final int SLOT_ROW3COLUMN2 = 19;
    public static final int SLOT_ROW3COLUMN3 = 20;
    public static final int SLOT_ROW3COLUMN4 = 21;
    public static final int SLOT_ROW3COLUMN5 = 22;
    public static final int SLOT_ROW3COLUMN6 = 23;
    public static final int SLOT_ROW3COLUMN7 = 24;
    public static final int SLOT_ROW3COLUMN8 = 25;
    public static final int SLOT_ROW3COLUMN9 = 26;

    public static final int SLOT_ROW4COLUMN1 = 27;
    public static final int SLOT_ROW4COLUMN2 = 28;
    public static final int SLOT_ROW4COLUMN3 = 29;
    public static final int SLOT_ROW4COLUMN4 = 30;
    public static final int SLOT_ROW4COLUMN5 = 31;
    public static final int SLOT_ROW4COLUMN6 = 32;
    public static final int SLOT_ROW4COLUMN7 = 33;
    public static final int SLOT_ROW4COLUMN8 = 34;
    public static final int SLOT_ROW4COLUMN9 = 35;

    public static final int SLOT_ROW5COLUMN1 = 36;
    public static final int SLOT_ROW5COLUMN2 = 37;
    public static final int SLOT_ROW5COLUMN3 = 38;
    public static final int SLOT_ROW5COLUMN4 = 39;
    public static final int SLOT_ROW5COLUMN5 = 40;
    public static final int SLOT_ROW5COLUMN6 = 41;
    public static final int SLOT_ROW5COLUMN7 = 42;
    public static final int SLOT_ROW5COLUMN8 = 43;
    public static final int SLOT_ROW5COLUMN9 = 44;

    public static final int SLOT_ROW6COLUMN1 = 45;
    public static final int SLOT_ROW6COLUMN2 = 46;
    public static final int SLOT_ROW6COLUMN3 = 47;
    public static final int SLOT_ROW6COLUMN4 = 48;
    public static final int SLOT_ROW6COLUMN5 = 49;
    public static final int SLOT_ROW6COLUMN6 = 50;
    public static final int SLOT_ROW6COLUMN7 = 51;
    public static final int SLOT_ROW6COLUMN8 = 52;
    public static final int SLOT_ROW6COLUMN9 = 53;


    // Private fields.
    private final Inventory _inventory;
    private final HashMap<Integer, UserInterfaceElement> _elements = new HashMap<>();
    public String _pageName = "Page";



    // Constructors.
    public UserInterfacePage(UserInterfacePageType type)
    {
        this(type, "Default Page Name");
    }

    public UserInterfacePage(UserInterfacePageType interfaceType, String pageName)
    {
        if (interfaceType == null)
        {
            throw new NullArgumentException("interfaceType is null");
        }
        if (pageName == null)
        {
            throw new NullArgumentException("pageName is null");
        }

        switch (interfaceType)
        {
            case Small ->  _inventory = BSRipoff.GetServer().createInventory(null, 9 * 3);
            case Large ->  _inventory = BSRipoff.GetServer().createInventory(null, 9 * 6);
            default ->
                throw new IllegalArgumentException("Unknown interface type \"%s\"".formatted(interfaceType.name()));
        }

        _pageName = pageName;
    }


    // Methods.
    /* Elements. */
    public void AddElement(UserInterfaceElement element)
    {
        if (element == null)
        {
            throw new NullArgumentException("element is null");
        }

        if (_elements.containsKey(element.GetSlot()))
        {
            _elements.get(element.GetSlot()).SetUpdateHandler(null);
        }

        _elements.put(element.GetSlot(), element);
        _inventory.setItem(element.GetSlot(), element.GetItem());
        element.SetUpdateHandler(this::OnItemUpdate);
    }

    public void RemoveElement(UserInterfaceElement element)
    {
        if (element == null)
        {
            throw new NullArgumentException("element is null");
        }

        if (_elements.get(element.GetSlot()) == element)
        {
            _elements.remove(element.GetSlot());
            _inventory.clear(element.GetSlot());
            element.SetUpdateHandler(null);
        }
    }

    public void ClearElements()
    {
        _inventory.clear();

        for (UserInterfaceElement element : _elements.values())
        {
            element.SetUpdateHandler(null);
        }
        _elements.clear();
    }


    /* Opening and closing. */
    public void Open(BSRipoffPlayer bsrPlayer)
    {
        Open(bsrPlayer, null);
    }

    public void Open(BSRipoffPlayer bsrPlayer, UserInterfaceElement openCauseElement)
    {
        bsrPlayer.MCPlayer.openInventory(_inventory);
        bsrPlayer.MCPlayer.getOpenInventory().setTitle(_pageName);
    }

    public void Close(BSRipoffPlayer bsrPlayer)
    {
        bsrPlayer.MCPlayer.getOpenInventory().close();
    }

    public String GetPageName() { return _pageName; }

    public void SetPageName(String name)
    {
        if (name == null)
        {
            throw new NullArgumentException("name is null");
        }

        _pageName = name;
    }



    /* Events. */
    public void OnInventoryClickEvent(InventoryClickEvent event, BSRipoffPlayer bsrPlayer)
    {
        if (event.getClickedInventory() != _inventory)
        {
            return;
        }

        int Slot = event.getSlot();
        if (_elements.containsKey(Slot))
        {
            _elements.get(Slot).OnInventoryClickEvent(event, this, bsrPlayer);
        }
    }

    public void OnTickEvent(BSRipoffPlayer bsrPlayer)  { }


    // Protected methods.
    protected void OnItemUpdate(UserInterfaceElement element)
    {
        AddElement(element);
    }
}