package sus.keiger.bsripoff.player;

import net.kyori.adventure.text.TextComponent;
import org.apache.commons.lang.NullArgumentException;

public class ActionbarMessage
{
    // Fields.
    public final int LifespanTicks;


    // Private fields.
    private int _timeExistedTicks = 0;
    private int _id;
    private TextComponent _contents;


    // Constructors.
    public ActionbarMessage(int lifespanTicks,TextComponent contents)
    {
        if (contents == null)
        {
            throw new NullArgumentException("contents are null");
        }

        LifespanTicks = lifespanTicks;
        _contents = contents;
    }


    // Methods.
    public boolean Tick()
    {
        _timeExistedTicks++;
        return _timeExistedTicks > LifespanTicks;
    }

    public void SetContents(TextComponent contents)
    {
        if (contents == null)
        {
            throw new NullArgumentException("contents are null");
        }

        _contents = contents;
    }

    public TextComponent GetContents() { return _contents; }

    public void SetTimeExisted(int value)
    {
        _timeExistedTicks = Math.max(0, value);
    }
}