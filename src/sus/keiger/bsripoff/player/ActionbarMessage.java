package sus.keiger.bsripoff.player;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.apache.commons.lang.NullArgumentException;
import sus.keiger.bsripoff.BSRipoff;

public class ActionbarMessage
{
    // Fields.
    public final int LifespanTicks;
    public final int ID;


    // Private fields.
    private int _ticksLeft;
    private Component _contents;


    // Constructors.
    public ActionbarMessage(int lifespanTicks, Component contents)
    {
        this(lifespanTicks, contents, BSRipoff.GetPlugin().Rng.nextInt());
    }

    public ActionbarMessage(int lifespanTicks, Component contents, int id)
    {
        if (contents == null)
        {
            throw new NullArgumentException("contents are null");
        }

        LifespanTicks = lifespanTicks;
        _ticksLeft = LifespanTicks;
        _contents = contents;
        ID = id;
    }


    // Methods.
    public boolean Tick()
    {
        _ticksLeft--;
        return _ticksLeft >= 0;
    }

    public void SetContents(TextComponent contents)
    {
        if (contents == null)
        {
            throw new NullArgumentException("contents are null");
        }

        _contents = contents;
    }

    public Component GetContents() { return _contents; }

    public void SetRemainingLifetime(int value)
    {
        _ticksLeft = Math.max(0, value);
    }

    public int GetRemainingLifetime() { return _ticksLeft; }
}