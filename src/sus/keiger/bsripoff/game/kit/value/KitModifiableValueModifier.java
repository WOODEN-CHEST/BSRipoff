package sus.keiger.bsripoff.game.kit.value;

import org.apache.commons.lang.NullArgumentException;

public class KitModifiableValueModifier
{
    // Fields.
    public final KitModifiableValueOperator Operator;
    public final double Value;


    // Private fields.
    private int _ticksLeft;


    // Constructors.
    public KitModifiableValueModifier(KitModifiableValueOperator operator, double value, int lifetime)
    {
        if (operator == null)
        {
            throw new NullArgumentException("Operator is null");
        }

        Operator = operator;
        Value = value;
        _ticksLeft = lifetime;
    }


    // Methods.
    public boolean Tick()
    {
        _ticksLeft--;
        return _ticksLeft <= 0;
    }

    public int GetRemainingLifetime()
    {
        return _ticksLeft;
    }

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