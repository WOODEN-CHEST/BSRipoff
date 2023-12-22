package sus.keiger.bsripoff.command;

import org.apache.commons.lang.NullArgumentException;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class ListNode extends CommandNode
{
    // Private fields.
    private final List<String> _options;
    private final Supplier<List<String>> _optionSupplier;


    // Constructors.
    public ListNode(BiConsumer<CommandData, List<Object>> executor, String ... options)
    {
        super(executor);

        if (options == null)
        {
            throw new NullArgumentException("options are null");
        }
        if (options.length == 0)
        {
            throw new IllegalArgumentException("Length of options is 0");
        }

        _options = List.of(options);
        _optionSupplier = null;
    }

    public ListNode(Supplier<List<String>> optionSupplier, BiConsumer<CommandData, List<Object>> executor)
    {
        super(executor);

        if (optionSupplier == null)
        {
            throw new NullArgumentException("optionSupplier are null");
        }

        _options = null;
        _optionSupplier = optionSupplier;
    }


    // Inherited methods.
    @Override
    public List<String> GetSelfSuggestions(CommandData data)
    {
        return _options != null ? _options : _optionSupplier.get();
    }

    @Override
    protected CommandBelongState ParseCommand(CommandData data, List<Object> parsedData)
    {
        data.MoveIndexToNextNonWhitespace();

        List<String> Options = _options != null ? _options : _optionSupplier.get();
        String Word = data.ReadWord();

        if (Options.contains(Word))
        {
            parsedData.add(Word);
            return CommandBelongState.BelongComplete;
        }
        return CommandBelongState.NotBelong;
    }
}