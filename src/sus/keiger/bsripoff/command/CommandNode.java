package sus.keiger.bsripoff.command;

import org.apache.commons.lang.NullArgumentException;
import sus.keiger.bsripoff.BSRipoff;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;

public abstract class CommandNode
{
    // Protected fields.
    protected BiConsumer<CommandData, List<Object>> Executor;


    // Private fields.
    private final List<CommandNode> _subNodes = new ArrayList<>();


    // Constructors.
    public CommandNode(BiConsumer<CommandData, List<Object>> executor)
    {
        Executor = executor;
    }


    // Methods.
    public CommandBelongState ExecuteCommand(CommandData data, List<Object> parsedData)
    {
        // Verify that this is the correct node.
        CommandBelongState BelongState = ParseCommand(data, parsedData);
        if (BelongState == CommandBelongState.NotBelong)
        {
            return BelongState;
        }

        if (data.IsMoreDataAvailable())
        {
            // Test trailing arguments.
            if (_subNodes.size() == 0)
            {
                data.SetStatus(CommandStatus.Unsuccessful);
                data.FeedbackString = "Trailing command arguments: \"%s\"".formatted(
                        data.Command.substring(data.Index).trim());
                return BelongState;
            }

            // Try to pass control to sub-nodes.
            int SavedIndex = data.Index;
            for (CommandNode SubNode : _subNodes)
            {
                if (SubNode.ExecuteCommand(data, parsedData) != CommandBelongState.NotBelong)
                {
                    return BelongState;
                }
                data.Index = SavedIndex;
            }

            TellExecuteError(data, parsedData);
            return BelongState;
        }

        // Otherwise try to execute this node.
        if (Executor != null)
        {
            Executor.accept(data, parsedData);
            return BelongState;
        }

        // Otherwise tell error.
        TellExecuteError(data, parsedData);
        return BelongState;
    }

    public CommandBelongState TabCommand(CommandData data, List<Object> parsedData)
    {
        // Verify that this is the correct node.
        CommandBelongState BelongState = ParseCommand(data, parsedData);
        if (BelongState == CommandBelongState.NotBelong)
        {
            return BelongState;
        }

        // If node belongs but is not completed, grab full control.
        // Based on logic, an incomplete node does not have any arguments left.
        // This is important as knowing it changes which logic needs to be tested.
        if (BelongState == CommandBelongState.BelongIncomplete)
        {
            data.SetRecommendations(GetSelfSuggestions(data));
            return BelongState;
        }

        // Otherwise allow passing control to sub-nodes.
        // Index == 0 accounts for the ServerCommand node case.
        if (data.IsMoreDataAvailable() || (data.Index == 0))
        {
            // Try to pass control to sub-nodes.
            int SavedIndex = data.Index;
            for (CommandNode SubNode : _subNodes)
            {
                if (SubNode.TabCommand(data, parsedData) != CommandBelongState.NotBelong)
                {
                    return BelongState;
                }
                data.Index = SavedIndex;
            }

            // Suggest sub-nodes if control wasn't passed.
            List<String> Suggestions = new ArrayList<>();
            for (CommandNode SubNode : _subNodes)
            {
                Suggestions.addAll(SubNode.GetSelfSuggestions(data));
            }

            data.SetRecommendations(Suggestions);
            return BelongState;
        }

        data.SetRecommendations(Collections.EMPTY_LIST);
        return BelongState;
    }

    public abstract List<String> GetSelfSuggestions(CommandData data);

    public void AddSubNode(CommandNode subNode)
    {
        if (subNode == null)
        {
            throw new NullArgumentException("subNode is null");
        }

        _subNodes.add(subNode);
    }


    // Protected methods.
    protected abstract CommandBelongState ParseCommand(CommandData data, List<Object> parsedData);


    // Private methods.
    private void TellExecuteError(CommandData data, List<Object> parsedData)
    {
        if (_subNodes.size() == 0)
        {
            data.SetStatus(CommandStatus.Unsuccessful);
            data.FeedbackString = "Badly formatted command node: no sub-nodes and no executor. " +
                    "This is a bug and should be reported to the server's admins.";
            BSRipoff.GetLogger().warning(("Badly made command? Command \"%s\" with args".formatted(data.Label) +
                    "\"%s\" got a case with no executor and no sub-nodes.").formatted(data.Command));
            return;
        }

        List<String> Suggestions = new ArrayList<>();
        for (CommandNode SubNode : _subNodes)
        {
            Suggestions.addAll(SubNode.GetSelfSuggestions(data));
        }

        data.SetStatus(CommandStatus.Unsuccessful);
        data.FeedbackString = "%s! Expected [%s]".formatted(
                data.Command.trim().equals("") ? "Incomplete command" : "Invalid command",
                String.join(" | ",  Suggestions)
        );
    }
}