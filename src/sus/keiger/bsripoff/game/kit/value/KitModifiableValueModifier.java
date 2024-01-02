package sus.keiger.bsripoff.game.kit.value;

import org.apache.commons.lang.NullArgumentException;
import sus.keiger.bsripoff.BSRipoff;

public class KitModifiableValueModifier
{
    // Fields.
    public final KitModifiableValueOperator Operator;
    public final double Value;
    public final int LifespanTicks;
    public final int ID;


    // Private fields.
    private int _ticksLeft;


    // Constructors.
    public KitModifiableValueModifier(KitModifiableValueOperator operator, double value, int lifetime)
    {
        this(operator, value, lifetime, BSRipoff.GetPlugin().Rng.nextInt());
    }


    public KitModifiableValueModifier(KitModifiableValueOperator operator, double value, int lifespan, int id)
    {
        if (operator == null)
        {
            throw new NullArgumentException("Operator is null");
        }

        Operator = operator;
        Value = value;
        LifespanTicks = lifespan;
        _ticksLeft = LifespanTicks;
        ID = id;
    }


    // Methods.
    public boolean Tick()
    {
        _ticksLeft--;
        return _ticksLeft >= 0;
    }

    public int GetRemainingLifetime()
    {
        return _ticksLeft;
    }

    public void SetRemainingLifetime(int value) { _ticksLeft = value; }

    public double ModifyValue(double resultingValue)
    {
        return switch (Operator)
            {
                case Add -> resultingValue + Value;
                case Multiply -> resultingValue * Value;
                case Divide -> resultingValue / Value;
                case Exponent -> Math.pow(resultingValue, Value);
                default -> resultingValue;
            };
    }
}