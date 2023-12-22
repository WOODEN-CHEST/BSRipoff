package sus.keiger.bsripoff.command;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.apache.commons.lang.NullArgumentException;
import org.bukkit.command.CommandSender;

import java.util.List;

public class CommandData
{
    // Fields.
    public final String Label;
    public final String Command;
    public final CommandSender Sender;
    public TextComponent FeedbackComponents = null;
    public String FeedbackString = null;
    public List<String> _tabRecommendations = null;
    int Index = 0;


    // Private fields.
    private CommandStatus _status = CommandStatus.Successful;



    // Constructors.
    public CommandData(String label, String[] args, CommandSender sender)
    {
        if (args == null)
        {
            throw new IllegalArgumentException("Args are null");
        }
        if (sender == null)
        {
            throw new IllegalArgumentException("Sender is null");
        }
        if (label == null)
        {
            throw new IllegalArgumentException("Label is null");
        }

        Command = String.join(" ", args);
        Sender = sender;
        Label = label;
    }


    // Methods.
    public List<String> GetRecommendations()
    {
        return _tabRecommendations;
    }

    public void SetRecommendations(List<String> recommendations)
    {
        if (recommendations == null)
        {
            throw new NullArgumentException("Recommendations are null");
        }

        _tabRecommendations = recommendations;
    }

    public TextComponent GetFeedback()
    {
        if (FeedbackComponents != null)
        {
            return FeedbackComponents;
        }
        if (FeedbackString != null)
        {
            NamedTextColor Color = _status == CommandStatus.Successful ?
                    NamedTextColor.WHITE : NamedTextColor.RED;


            return Component.text(FeedbackString).color(Color);
        }

        return null;
    }

    public void SetStatus(CommandStatus status)
    {
        if  (status == null)
        {
            throw new IllegalArgumentException("Status is null");
        }

        _status = status;
    }

    public CommandStatus GetStatus()
    {
        return _status;
    }

    public boolean IsMoreDataAvailable()
    {
        return Index < Command.length();
    }

    public void MoveIndexToNextNonWhitespace()
    {
        while ((Index < Command.length()) && (Character.isWhitespace(Command.charAt(Index))))
        {
            Index++;
        }
    }

    public String ReadWord()
    {
        StringBuilder WordBuilder = new StringBuilder();

        while ((Index < Command.length()) && !Character.isWhitespace(Command.charAt(Index)))
        {
            WordBuilder.append(Command.charAt(Index));
            Index++;
        }

        return WordBuilder.toString();
    }
}