package sus.keiger.bsripoff.command.manage;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import sus.keiger.bsripoff.BSRipoff;
import sus.keiger.bsripoff.command.*;
import sus.keiger.bsripoff.player.BSRPlayerState;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;


public class ManageCommand
{
    public static ServerCommand CreateCommand()
    {
        ServerCommand Command = new ServerCommand("manage", null);

        /* State node. */
        CommandNode StateNode = new KeywordNode("state", null);
        CommandNode StateGetNode = new KeywordNode("get", null);
        StateNode.AddSubNode(StateGetNode);
        CommandNode StateSetNode = new KeywordNode("set", null);
        StateNode.AddSubNode(StateSetNode);

        CommandNode StateGetSelectorNode = new PlayerSelectorNode(true, 1, ManageCommand::GetState);
        StateGetNode.AddSubNode(StateGetSelectorNode);

        CommandNode StateSetSelectorNode = new PlayerSelectorNode(true, 1, null);
        StateSetNode.AddSubNode(StateSetSelectorNode);

        CommandNode StateSelectSetNode = new ListNode(ManageCommand::SetState,
                BSRPlayerState.InLobby.toString(), BSRPlayerState.InGame.toString());
        StateSetSelectorNode.AddSubNode(StateSelectSetNode);


        // Add root nodes.
        Command.AddSubNode(StateNode);

        return Command;
    }


    // Private static methods.
    @SuppressWarnings("unchecked")
    private static void GetState(CommandData data, List<Object> parsedData)
    {
        HashSet<Player> Players = (HashSet<Player>)parsedData.get(0);

        if (Players.size() == 0)
        {
            data.FeedbackString = "No players found";
            data.SetStatus(CommandStatus.Unsuccessful);
        }
        else if (Players.size() > 1)
        {
            data.FeedbackString = "Expected 1 player, got %d".formatted(Players.size());
            data.SetStatus(CommandStatus.Unsuccessful);
        }
        else
        {
            Player MCPlayer = Players.iterator().next();
            data.FeedbackString = "State of player %s is \"%s\"".formatted(
                    MCPlayer.getName(), BSRipoff.GetServerManager().GetBSRipoffPlayer(MCPlayer).GetState().toString()
            );
        }
    }

    @SuppressWarnings("unchecked")
    private static void SetState(CommandData data, List<Object> parsedData)
    {
        // Index 0 = Players, Index 1 = mode
        HashSet<Player> Players = (HashSet<Player>)parsedData.get(0);
        String StateString = (String)parsedData.get(1);

        BSRPlayerState State = BSRPlayerState.valueOf(StateString);

        for (Player MCPlayer : Players)
        {
            BSRipoff.GetServerManager().GetBSRipoffPlayer(MCPlayer).SetState(State);
        }

        if (Players.size() == 0)
        {
            data.FeedbackString = "No players found";
            data.SetStatus(CommandStatus.Unsuccessful);
        }
        else if (Players.size() == 1)
        {
            data.FeedbackString = "Set the state of %s to \"%s\"".formatted(
                    Players.iterator().next().getName(), StateString);
        }
        else
        {
            data.FeedbackString = "Set the state of %d players to \"%s\"".formatted(Players.size(), StateString);
        }
    }
}
