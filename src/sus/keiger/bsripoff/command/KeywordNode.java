package sus.keiger.bsripoff.command;

import org.apache.commons.lang.NullArgumentException;

import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;

public class KeywordNode extends CommandNode
{
    // Fields.
    public final String Keyword;


    // Constructors.
    public KeywordNode(String keyword, BiConsumer<CommandData, List<Object>> executor)
    {
        super(executor);

        if (keyword == null)
        {
            throw new NullArgumentException("Keyword is null");
        }
        if (keyword.length() == 0)
        {
            throw new IllegalArgumentException("Keyword may not be empty.");
        }

        Keyword = keyword;
    }


    // Inherited methods.
    @Override
    public List<String> GetSelfSuggestions(CommandData data)
    {
        return Collections.singletonList(Keyword);
    }

    @Override
    protected CommandBelongState ParseCommand(CommandData data, List<Object> parsedData)
    {
        data.MoveIndexToNextNonWhitespace();
        return Keyword.equals(data.ReadWord()) ? CommandBelongState.BelongComplete : CommandBelongState.NotBelong;
    }
}
