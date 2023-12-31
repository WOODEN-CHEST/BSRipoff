package sus.keiger.bsripoff.command.manage;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import sus.keiger.bsripoff.BSRipoff;
import sus.keiger.bsripoff.command.*;
import sus.keiger.bsripoff.player.ActionbarMessage;
import sus.keiger.bsripoff.player.BSRPlayerState;

import java.util.HashSet;
import java.util.List;


public class ManageCommand
{
    public static ServerCommand CreateCommand()
    {
        ServerCommand Command = new ServerCommand("manage", null);

        // Add nodes.
        Command.AddSubNode(CreateStateNode());
        Command.AddSubNode(CreateSpawnNode());
        Command.AddSubNode(CreateTestGameNode());
        Command.AddSubNode(CreateActionbarNode());

        return Command;
    }


    // Private static methods.
    /* Creation. */
    private static CommandNode CreateStateNode()
    {
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

        return StateNode;
    }

    private static CommandNode CreateSpawnNode()
    {
        CommandNode SpawnNode = new KeywordNode("spawn", null);

        CommandNode GetNode = new KeywordNode("get", ManageCommand::GetSpawn);
        SpawnNode.AddSubNode(GetNode);
        CommandNode SetNode = new KeywordNode("set", null);
        SpawnNode.AddSubNode(SetNode);
        CommandNode GotoNode = new KeywordNode("goto", ManageCommand::GotoSpawn);
        SpawnNode.AddSubNode(GotoNode);

        CommandNode LocationDataNode = new LocationNode(CommandOption.Optional, ManageCommand::SetSpawn);
        SetNode.AddSubNode(LocationDataNode);

        return SpawnNode;
    }

    private static CommandNode CreateTestGameNode()
    {
        CommandNode TestGameNode = new KeywordNode("testgame", null);

        CommandNode JoinNode = new KeywordNode("join", ManageCommand::JoinGame);
        TestGameNode.AddSubNode(JoinNode);

        CommandNode LeaveNode = new KeywordNode("leave", ManageCommand::LeaveGame);
        TestGameNode.AddSubNode(LeaveNode);

        return TestGameNode;
    }

    private static CommandNode CreateActionbarNode()
    {
        CommandNode ActionbarNode = new KeywordNode("actionbar", null);

        CommandNode TimeNode = new NumberNode(null);
        ActionbarNode.AddSubNode(TimeNode);

        CommandNode StrNode = new TextNode(ManageCommand::AddActionbar);
        TimeNode.AddSubNode(StrNode);

        return ActionbarNode;
    }


    /* Functionality. */
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
                    MCPlayer.getName(), BSRipoff.GetServerManager().GetBSRPlayer(MCPlayer).GetState().toString()
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
            BSRipoff.GetServerManager().GetBSRPlayer(MCPlayer).SetState(State);
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

    private static void GetSpawn(CommandData data, List<Object> parsedData)
    {
        data.FeedbackString = "Spawn is at %.2f %.2f %.2f".formatted(
                BSRipoff.GetServerManager().GetSpawnLocation().getX(),
                BSRipoff.GetServerManager().GetSpawnLocation().getY(),
                BSRipoff.GetServerManager().GetSpawnLocation().getZ()
        ).replace(',', '.');
    }

    private static void SetSpawn(CommandData data, List<Object> parsedData)
    {
        Location NewLocation = (Location)parsedData.get(0);

        BSRipoff.GetServerManager().SetLobbyLocation(NewLocation);
        data.FeedbackString = "Set the spawn to %.2f %.2f %.2f %.2f° %.2f°".formatted(
                NewLocation.getX(), NewLocation.getY(), NewLocation.getZ(),
                NewLocation.getYaw(), NewLocation.getPitch()
        ).replace(',', '.');
    }

    private static void GotoSpawn(CommandData data, List<Object> parsedData)
    {
        if (data.Sender instanceof Entity EntitySender)
        {
            EntitySender.teleport(BSRipoff.GetServerManager().GetSpawnLocation());
            data.FeedbackString = "Teleported to spawn";
        }
    }

    private static void JoinGame(CommandData data, List<Object> parsedData)
    {
        BSRipoff.GetServerManager().GetBSRPlayer((Player)data.Sender).GameData.JoinGame(
                BSRipoff.GetServerManager().TestingGame);
        data.FeedbackString = "Joined game";
    }

    private static void LeaveGame(CommandData data, List<Object> parsedData)
    {
        BSRipoff.GetServerManager().GetBSRPlayer((Player)data.Sender).GameData.LeaveAnyGame();
        data.FeedbackString = "Left game";
    }

    private static void AddActionbar(CommandData data, List<Object> parsedData)
    {
        int Time = (int)((double)parsedData.get(0));
        String Text = (String)parsedData.get(1);

        BSRipoff.GetServerManager().GetBSRPlayer((Player)data.Sender).ActionBarManager.AddMessage(
                new ActionbarMessage(Time, Component.text(Text).color(NamedTextColor.DARK_PURPLE))
        );

        data.FeedbackString = "Added actionbar with text \"%s\" and duration %d".formatted(Text, Time);
    }
}
