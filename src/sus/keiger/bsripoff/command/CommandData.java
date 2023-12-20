package sus.keiger.bsripoff.command;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.CommandSender;

import java.util.List;

public class CommandData
{
    // Private fields.
    private String[] _args;
    private List<String> _tabRecommendations;
    private TextComponent _feedbackComponents;
    private String _feedbackString;
    private CommandStatus _status = CommandStatus.Successful;
    private final CommandSender _sender;


    // Constructors.
    public CommandData(String[] args, CommandSender sender)
    {
        if (args == null)
        {
            throw new IllegalArgumentException("Args are null");
        }
        if (sender == null)
        {
            throw new IllegalArgumentException("Sender is null");
        }

        _args = args;
        _sender = sender;
    }


    // Methods.
    public String[] GetArgs()
    {
        return _args;
    }

    public List<String> GetRecommendations()
    {
        return _tabRecommendations;
    }

    public void SetRecommendations(List<String> recommendations)
    {
        if (recommendations == null)
        {
            throw new IllegalArgumentException("Recommendations are null");
        }

        _tabRecommendations = recommendations;
    }

    public void SetFeedback(TextComponent text)
    {
        _feedbackComponents = text;
    }

    public void SetFeedback(String text)
    {
        _feedbackString = text;
    }

    public TextComponent GetFeedback()
    {
        if (_feedbackComponents != null)
        {
            return _feedbackComponents;
        }
        if (_feedbackString != null)
        {
            NamedTextColor Color = _status == CommandStatus.Successful ?
                    NamedTextColor.WHITE : NamedTextColor.RED;

            switch (_status)
            {
                case Incomplete -> { return Component.text("Incomplete command! %s".formatted(_feedbackString))
                        .color(Color); }
                case Invalid -> { return Component.text("Invalid command! %s".formatted(_feedbackString))
                        .color(Color); }
                default -> { return Component.text(_feedbackString).color(Color); }
            }

        }

        return null;
    }

    public int GetLength()
    {
        return _args.length;
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

    public CommandSender GetSender()
    {
        return _sender;
    }
}