package sus.keiger.bsripoff.command;

import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;

public class TextNode extends CommandNode
{
    // Constructors.
    public TextNode(BiConsumer<CommandData, List<Object>> executor)
    {
        super(executor);
    }


    // Inherited methods.
    @Override
    public List<String> GetSelfSuggestions(CommandData data)
    {
        return List.of("text");
    }

    @Override
    protected CommandBelongState ParseCommand(CommandData data, List<Object> parsedData)
    {
        data.MoveIndexToNextNonWhitespace();
        if (!data.IsMoreDataAvailable())
        {
            return CommandBelongState.NotBelong;
        }

        parsedData.add(data.Command.substring(data.Index));
        data.Index = data.Command.length();
        return CommandBelongState.BelongComplete;
    }
}
