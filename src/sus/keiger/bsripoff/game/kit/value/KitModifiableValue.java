package sus.keiger.bsripoff.game.kit.value;

import org.apache.commons.lang.NullArgumentException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class KitModifiableValue
{
    // Private fields.
    private double _baseValue;
    private final HashMap<Integer, KitModifiableValueModifier> _modifiers = new HashMap<>();


    // Constructors.
    public KitModifiableValue(double baseValue)
    {
        _baseValue = baseValue;
    }


    // Methods.
    public void Tick()
    {
        if (_modifiers.size() == 0)
        {
            return;
        }

        ArrayList<KitModifiableValueModifier> ModifiersToRemove = new ArrayList<>();

        for (KitModifiableValueModifier Modifier : _modifiers.values())
        {
            if (Modifier.Tick())
            {
                ModifiersToRemove.add(Modifier);
            }
        }

        for (KitModifiableValueModifier Modifier : ModifiersToRemove)
        {
            _modifiers.remove(Modifier.ID);
        }
    }

    public double GetBaseValue()
    {
        return _baseValue;
    }

    public void SetBaseValue(double value)
    {
        _baseValue = value;
    }

    public double GetValue()
    {
        if (_modifiers.size() == 0)
        {
            return _baseValue;
        }

        double ResultingValue = _baseValue;

        // Fist add, then multiply, then raise, finally divide.
        for (KitModifiableValueModifier Modifier : _modifiers.values())
        {
            if (Modifier.Operator == KitModifiableValueOperator.Add)
            {
                ResultingValue += Modifier.Value;
            }
        }
        for (KitModifiableValueModifier Modifier : _modifiers.values())
        {
            if (Modifier.Operator == KitModifiableValueOperator.Multiply)
            {
                ResultingValue *= Modifier.Value;
            }
        }
        for (KitModifiableValueModifier Modifier : _modifiers.values())
        {
            if (Modifier.Operator == KitModifiableValueOperator.Exponent)
            {
                ResultingValue = Math.pow(ResultingValue, Modifier.Value);
            }
        }
        for (KitModifiableValueModifier Modifier : _modifiers.values())
        {
            if (Modifier.Operator == KitModifiableValueOperator.Divide)
            {
                ResultingValue /= Modifier.Value;
            }
        }

        return ResultingValue;
    }

    public void ClearModifiers()
    {
        _modifiers.clear();
    }

    public void AddModifier(KitModifiableValueModifier modifier)
    {
        if (modifier == null)
        {
            throw new NullArgumentException("modifier is null");
        }

        _modifiers.put(modifier.ID, modifier);
    }

    public void RemoveModifier(KitModifiableValueModifier modifier)
    {
        if (modifier == null)
        {
            throw new NullArgumentException("modifier is null");
        }

        _modifiers.remove(modifier.ID);
    }

    public void RemoveModifier(int id)
    {
        _modifiers.remove(id);
    }
}