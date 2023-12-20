package sus.keiger.bsripoff.command.manage;

import org.bukkit.entity.Player;
import sus.keiger.bsripoff.command.CommandData;
import sus.keiger.bsripoff.command.CommandStatus;
import sus.keiger.bsripoff.command.ServerCommand;

import java.util.Arrays;
import java.util.List;


public class ManageCommand extends ServerCommand
{
    // Private fields.
    private final String _spawnKeyword = "spawn";
    private final String _setKeyword = "set";
    private final String _gotoKeyword = "goto";
    private final String _stateKeyword = "state";


    // Constructors.
    public ManageCommand()
    {
        super("manage");
    }


    // Private methods.
    /* Executing command. */
//    private void ParseSpawn(CommandData data)
//    {
//        if (data.GetLength() == 1)
//        {
//            if (ServerManager.GetLobbyLocation() == null)
//            {
//                data.SetFeedback("Lobby location bist klaber nicht set yet.");
//                data.SetStatus(CommandStatus.Error);
//                return;
//            }
//
//            data.SetFeedback("Lobby location sind %.2fX %.2fY %.2fZ %.2f° %.2f° in %s"
//                    .formatted(ServerManager.GetLobbyLocation().getX(),
//                            ServerManager.GetLobbyLocation().getY(),
//                            ServerManager.GetLobbyLocation().getZ(),
//                            ServerManager.GetLobbyLocation().getPitch(),
//                            ServerManager.GetLobbyLocation().getYaw(),
//                            ServerManager.GetLobbyLocation().getWorld().getName()));
//            return;
//        }
//
//        switch (data.GetArgs()[1])
//        {
//            case _setKeyword:
//                SetSpawn(data);
//                break;
//
//            case _gotoKeyword:
//                GotoSpawn(data);
//
//            default:
//                data.SetStatus(CommandStatus.Invalid);
//                data.SetFeedback("Unknown kommanded argumentieren \"%s\"".formatted(data.GetArgs()[1]));
//        }
//    }
//
//    private void SetSpawn(CommandData data)
//    {
//        if (!(data.GetSender() instanceof Entity))
//        {
//            data.SetFeedback("Cannot set spawn position because command sender is not an entity!");
//            data.SetStatus(CommandStatus.Invalid);
//        }
//
//        ServerManager.SetLobbyLocation(((Entity)data.GetSender()).getLocation());
//        data.SetFeedback("Set the spawn location to %.2fX %.2fY %.2fZ %.2f° %.2f° in %s"
//                .formatted(ServerManager.GetLobbyLocation().getX(),
//                        ServerManager.GetLobbyLocation().getY(),
//                        ServerManager.GetLobbyLocation().getZ(),
//                        ServerManager.GetLobbyLocation().getPitch(),
//                        ServerManager.GetLobbyLocation().getYaw(),
//                        ServerManager.GetLobbyLocation().getWorld().getName()));
//    }
//
//    private void GotoSpawn(CommandData data)
//    {
//        if (!(data.GetSender() instanceof Entity))
//        {
//            return;
//        }
//
//        if (ServerManager.GetLobbyLocation() == null)
//        {
//            data.SetStatus(CommandStatus.Error);
//            data.SetFeedback("Spawn location not set");
//            return;
//        }
//
//        ((Entity)data.GetSender()).teleport(ServerManager.GetLobbyLocation());
//        data.SetFeedback("Teleported to lobby!");
//    }

    private void ExecuteStateCommand(CommandData data)
    {
//        if (data.GetLength() == 1)
//        {
//            data.SetStatus(CommandStatus.Incomplete);
//            data.SetFeedback("Expected player selector.");
//            return;
//        }
//
//        List<Player> SelectedPlayers = ServerCommand.SelectPlayers(data.GetArgs()[1], data.GetSender());
//
//        if (SelectedPlayers.size() == 0)
//        {
//            data.SetStatus(CommandStatus.Error);
//            data.SetFeedback("No players found matching the selector.");
//            return;
//        }
//
//        if (data.GetLength() == 2)
//        {
//            data.SetStatus(CommandStatus.Incomplete);
//            data.SetFeedback("No players found matching the selector.");
//            return;
//        }
//        if (data.GetArgs()[1])
//        {
//            data.SetStatus(CommandStatus.Incomplete);
//            data.SetFeedback("Expected player selector.");
//            return;
//        }
    }


    /* Suggesting command. */
    private void SuggestState(CommandData data)
    {

    }


    // Inherited methods.
    @Override
    protected void ExecuteCommand(CommandData data)
    {
        if (data.GetLength() == 0)
        {
            data.SetFeedback("");
            data.SetStatus(CommandStatus.Incomplete);
            return;
        }

        switch (data.GetArgs()[0])
        {
            case _stateKeyword:
                ExecuteStateCommand(data);
                break;

            default:
                data.SetFeedback("Unknown command kitger. Use deine brain");
                data.SetStatus(CommandStatus.Invalid);
                break;
        }
    }

    @Override
    protected void TabCommand(CommandData data)
    {
        if (data.GetLength() < 1)
        {
            data.SetRecommendations(List.of(_setKeyword));
        }

        if (data.GetArgs()[0].equals(_setKeyword))
        {
            SuggestState(data);
        }
    }
}
