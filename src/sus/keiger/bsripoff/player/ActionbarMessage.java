package sus.keiger.bsripoff.player;

import net.kyori.adventure.text.TextComponent;
import org.apache.commons.lang.NullArgumentException;

public class ActionbarMessage
{
    // Fields.
    public final int LifespanTicks;
    public final TextComponent Contents;


    // Private fields.
    private int _timeExistedTicks = 0;

    // Constructors.
    public ActionbarMessage(int lifespanTicks,TextComponent contents)
    {
        if (contents == null)
        {
            throw new NullArgumentException("contents are null");
        }

        LifespanTicks = lifespanTicks;
        Contents = contents;
    }


    // Methods.
    public boolean Tick()
    {
        _timeExistedTicks++;
        return _timeExistedTicks > LifespanTicks;
    }
}