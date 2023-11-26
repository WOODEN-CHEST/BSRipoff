package sus.keiger.bsripoff.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import sus.keiger.bsripoff.BSRipoff;
import java.util.List;


public abstract class ServerCommand implements TabExecutor
{
    // Private fields.
    private final String _label;


    // Constructors.
    public ServerCommand(String label)
    {
        if (label == null)
        {
            throw new IllegalArgumentException();
        }
        if (BSRipoff.GetPlugin().getCommand(label) == null)
        {
            throw new IllegalArgumentException("Command \"%s\" not found".formatted(label));
        }

        BSRipoff.GetPlugin().getCommand(label).setTabCompleter(this);
        BSRipoff.GetPlugin().getCommand(label).setExecutor(this);
        _label = label;
    }


    // Methods.
    public String GetLabel()
    {
        return _label;
    }


    // Protected  methods.
    protected abstract void ExecuteCommand(CommandData data);

    protected abstract void TabCommand(CommandData data);


    // Private methods.
    private void VerifyLabel(String label)
    {
        if (!_label.equals(label))
        {
            throw new IllegalStateException("Label mismatch. Command's label is \"%s\", got \"%s\"."
                    .formatted(_label, label));
        }
    }


    // Inherited methods.
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        VerifyLabel(label);

        CommandData Data = new CommandData(args, sender);
        ExecuteCommand(Data);

        if (Data.GetFeedback() != null)
        {
            sender.sendMessage(Data.GetFeedback());
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args)
    {
        VerifyLabel(label);

        CommandData Data = new CommandData(args, sender);
        TabCommand(Data);

        return Data.GetRecommendations();
    }
}