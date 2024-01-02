package sus.keiger.bsripoff.player;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.apache.commons.lang.NullArgumentException;

import java.util.ArrayList;
import java.util.HashMap;

public class ActionbarManager
{
    // Static fields.
    public static final int MAX_MESSAGES = 10;


    // Private fields.
    private final HashMap<Integer, ActionbarMessage> _actionbarMessages = new HashMap<>();
    private Component _combinedActionbarMessage;
    private boolean _actionbarChanged = false;



    // Constructors.
    public ActionbarManager() { }


    // Methods.
    public void Tick(BSRipoffPlayer bsrPlayer)
    {
        if (_actionbarMessages.size() == 0)
        {
            return;
        }

        ArrayList<ActionbarMessage> MessagesToRemove = new ArrayList<>();

        for (ActionbarMessage Message : _actionbarMessages.values() )
        {
            if (Message.Tick())
            {
                MessagesToRemove.add(Message);
                _actionbarChanged = true;
            }
        }

        if (MessagesToRemove.size() > 0)
        {
            for (ActionbarMessage Message : MessagesToRemove)
            {
                _actionbarMessages.remove(Message);
            }
        }

        if (_actionbarChanged)
        {
            BuildActionbarMessage();
        }

        if (_actionbarMessages.size() != 0)
        {
            bsrPlayer.MCPlayer.sendActionBar(_combinedActionbarMessage);
        }
    }

    public void AddMessage(ActionbarMessage message)
    {
        if (message == null)
        {
            throw new NullArgumentException("message is null");
        }
        if (_actionbarMessages.size() >= MAX_MESSAGES)
        {
            return;
        }
        _actionbarMessages.put(message.ID, message);
        _actionbarChanged = true;
    }


    // Private methods.
    private void BuildActionbarMessage()
    {
        TextComponent.Builder Builder = Component.text();

        boolean HadElement = false;
        for (ActionbarMessage Message : _actionbarMessages.values())
        {
            if (HadElement)
            {
                Builder.append(Component.text(" | ").color(NamedTextColor.WHITE));
            }
            Builder.append(Message.GetContents());
            HadElement = true;
        }

        _combinedActionbarMessage = Builder.build();
    }

}
