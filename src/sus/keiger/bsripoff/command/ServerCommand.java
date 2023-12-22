package sus.keiger.bsripoff.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabExecutor;

import sus.keiger.bsripoff.BSRipoff;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;


public class ServerCommand extends CommandNode implements TabExecutor
{
    // Fields.
    public final String Label;



    // Constructors.
    public ServerCommand(String label, BiConsumer<CommandData, List<Object>> executor)
    {
        super(executor);

        if (label == null)
        {
            throw new IllegalArgumentException();
        }
        if (BSRipoff.GetPlugin().getCommand(label) == null)
        {
            throw new IllegalArgumentException("Command \"%s\" not found".formatted(label));
        }

        PluginCommand Command = BSRipoff.GetPlugin().getCommand(label);
        if (Command != null)
        {
            Command.setTabCompleter(this);
            Command.setExecutor(this);
        }

        Label = label;
    }


    // Methods.
    // Static methods.
    /* Bunch of static methods to help parse commands. */
    /* Parsing methods. */
//    public static List<Player> SelectPlayers(String selector, CommandSender sender)
//    {
//        ArrayList<Player> SelectedPlayers = new ArrayList<>();
//
//        switch (selector)
//        {
//            case "@s" ->
//            {
//                if (sender instanceof Player)
//                {
//                    SelectedPlayers.add((Player)sender);
//                }
//            }
//            case "@r" ->
//            {
//                List<Player> Players = new ArrayList<>(BSRipoff.GetServer().getOnlinePlayers());
//                if (Players.size() == 0) break;
//
//                int Index = BSRipoff.GetPlugin().Rng.nextInt(0, Players.size());
//                SelectedPlayers.add(Players.get(Index));
//            }
//            case "@p" ->
//            {
//                if (!(sender instanceof Entity))
//                {
//                    break;
//                }
//
//                Entity Sender = (Entity)sender;
//                double ClosestDistance = Double.POSITIVE_INFINITY;
//                double CurrentDistance = 0f;
//                Player ClosestPlayer = null;
//
//                for (Player MCPlayer : BSRipoff.GetServer().getOnlinePlayers())
//                {
//                    CurrentDistance = Sender.getLocation().distance(MCPlayer.getLocation());
//                    if ((CurrentDistance < ClosestDistance) && (Sender != MCPlayer))
//                    {
//                        ClosestDistance = CurrentDistance;
//                        ClosestPlayer = MCPlayer;
//                    }
//                }
//
//                if (ClosestPlayer != null)
//                {
//                    SelectedPlayers.add(ClosestPlayer);
//                }
//            }
//            case "@a" -> SelectedPlayers.addAll(BSRipoff.GetServer().getOnlinePlayers());
//            default ->
//            {
//                Player SelectedPlayer = BSRipoff.GetServer().getPlayerExact(selector);
//                if (SelectedPlayer != null)
//                {
//                    SelectedPlayers.add(SelectedPlayer);
//                }
//            }
//        }
//
//        return SelectedPlayers;
//    }
//
//    public static double ParseNumber(String number, double relativeNumber) throws NumberFormatException
//    {
//        if (number.startsWith("~") && !Double.isNaN(relativeNumber))
//        {
//            return Double.parseDouble(number.substring(1)) + relativeNumber;
//        }
//        return Double.parseDouble(number);
//    }
//
//    /* Suggestion methods. */
//    public static List<String> SuggestSelectors()
//    {
//        return Arrays.asList("@s", "@p", "@r", "@a");
//    }
//
//    public static List<String> SuggestPlayers()
//    {
//        return SuggestPlayers(null);
//    }
//
//    public static List<String> SuggestPlayers(Collection<String> excludedPlayers)
//    {
//        ArrayList<String> Players = new ArrayList<>();
//
//        for (Player MCPlayer : BSRipoff.GetServer().getOnlinePlayers())
//        {
//            if ((excludedPlayers == null) || (excludedPlayers.contains(MCPlayer.getName())))
//            {
//                Players.add(MCPlayer.getName());
//            }
//        }
//
//        return Players;
//    }
//
//    public static List<String> SuggestPosition(int coordinateCount, Location relativeLocation)
//    {
//        ArrayList<String> Locations = new ArrayList<>();
//
//        if (coordinateCount >= 3)
//        {
//            Locations.add("~ ~ ~");
//        }
//        else if (coordinateCount == 2)
//        {
//            Locations.add("~ ~");
//        }
//        else
//        {
//            Locations.add("~");
//        }
//
//        if (relativeLocation == null)
//        {
//            return Locations;
//        }
//
//        if (coordinateCount >= 3)
//        {
//            Locations.add("%.2f %.2f %.2f"
//                    .formatted(relativeLocation.getX(), relativeLocation.getY(), relativeLocation.getZ()));
//        }
//        else if (coordinateCount == 2)
//        {
//            Locations.add("%.2f %.2f".formatted(relativeLocation.getY(), relativeLocation.getZ()));
//        }
//        else
//        {
//            Locations.add(Double.toString(relativeLocation.getZ()));
//        }
//
//        return Locations;
//    }

    // Private methods.
    private void VerifyLabel(String label)
    {
        if (!Label.equals(label))
        {
            throw new IllegalStateException("Label mismatch. Command's label is \"%s\", got \"%s\"."
                    .formatted(Label, label));
        }
    }


    // Inherited methods.
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        VerifyLabel(label);
        CommandData Data = new CommandData(Label, args, sender);
        ExecuteCommand(Data, new ArrayList<Object>());

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
        CommandData Data = new CommandData(Label, args, sender);
        TabCommand(Data, new ArrayList<Object>());

        return Data.GetRecommendations();
    }

    @Override
    public List<String> GetSelfSuggestions(CommandData data)
    {
        return Collections.singletonList(Label);
    }

    @Override
    protected CommandBelongState ParseCommand(CommandData data, List<Object> parsedData)
    {
        return CommandBelongState.BelongComplete;
    }
}