package sus.keiger.bsripoff.player;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.apache.commons.lang.NullArgumentException;

import java.util.ArrayList;

public class ActionbarManager
{
    // Private fields.
    private ArrayList<ActionbarMessage> _actionbarMessages;
    private TextComponent _combinedActionbarMessage;
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

        for (ActionbarMessage Message : _actionbarMessages)
        {
            if (Message.Tick())
            {
                MessagesToRemove.add(Message);
                _actionbarChanged = true;
            }
        }

        _actionbarMessages.removeAll(MessagesToRemove);

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

        _actionbarMessages.add(message);
        _actionbarChanged = true;
    }


    // Private methods.

    private void BuildActionbarMessage()
    {
        TextComponent.Builder Builder = Component.text();

        boolean HadElement = false;
        for (ActionbarMessage Message : _actionbarMessages)
        {
            Builder.append(Message.Contents);
            if (HadElement)
            {
                Builder.append(Component.text(" | ").color(NamedTextColor.WHITE));
            }
            HadElement = true;
        }

        _combinedActionbarMessage = Builder.build();
    }

}
