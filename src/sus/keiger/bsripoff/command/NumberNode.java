package sus.keiger.bsripoff.command;

import sus.keiger.bsripoff.BSRipoff;

import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;

public class NumberNode extends CommandNode
{
    // Constructors.
    public NumberNode(BiConsumer<CommandData, List<Object>> executor)
    {
        super(executor);
    }


    // Inherited methods.
    @Override
    public List<String> GetSelfSuggestions(CommandData data)
    {
        return List.of("0.0");
    }

    @Override
    protected CommandBelongState ParseCommand(CommandData data, List<Object> parsedData)
    {
        data.MoveIndexToNextNonWhitespace();
        if (!data.IsMoreDataAvailable())
        {
            return CommandBelongState.NotBelong;
        }

        try
        {
            parsedData.add(Double.parseDouble(data.ReadWord()));
        }
        catch (NumberFormatException e)
        {
            return CommandBelongState.NotBelong;
        }

        return CommandBelongState.BelongComplete;
    }
}