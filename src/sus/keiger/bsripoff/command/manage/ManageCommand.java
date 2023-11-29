package sus.keiger.bsripoff.command.manage;

import org.bukkit.entity.Entity;
import sus.keiger.bsripoff.ServerManager;
import sus.keiger.bsripoff.command.CommandData;
import sus.keiger.bsripoff.command.CommandStatus;
import sus.keiger.bsripoff.command.ServerCommand;

import java.util.Arrays;


public class ManageCommand extends ServerCommand
{
    // Private fields.
    private final String _spawnKeyword = "spawn";
    private final String _setKeyword = "set";
    private final String _gotoKeyword = "goto";


    // Constructors.
    public ManageCommand()
    {
        super("manage");
    }


    // Private methods.
    private void ParseSpawn(CommandData data)
    {
        if (data.GetLength() == 1)
        {
            if (ServerManager.GetLobbyLocation() == null)
            {
                data.SetFeedback("Lobby location bist klaber nicht set yet."); // lmfao
                data.SetStatus(CommandStatus.Errored);
                return;
            }

            data.SetFeedback("Lobby location sind %.2fX %.2fY %.2fZ %.2f° %.2f° in %s"
                    .formatted(ServerManager.GetLobbyLocation().getX(),
                            ServerManager.GetLobbyLocation().getY(),
                            ServerManager.GetLobbyLocation().getZ(),
                            ServerManager.GetLobbyLocation().getPitch(),
                            ServerManager.GetLobbyLocation().getYaw(),
                            ServerManager.GetLobbyLocation().getWorld().getName()));
            return;
        }

        switch (data.GetArgs()[1])
        {
            case _setKeyword:
                SetSpawn(data);
                break;

            case _gotoKeyword:
                GotoSpawn(data);

            default:
                data.SetStatus(CommandStatus.Invalid);
                data.SetFeedback("Unknown kommanded argumentieren \"%s\"".formatted(data.GetArgs()[1]));
        }
    }

    private void SetSpawn(CommandData data)
    {
        if (!(data.GetSender() instanceof Entity))
        {
            data.SetFeedback("Cannot set spawn position because command sender is not an entity!");
            data.SetStatus(CommandStatus.Invalid);
        }

        ServerManager.SetLobbyLocation(((Entity)data.GetSender()).getLocation());
        data.SetFeedback("Set the spawn location to %.2fX %.2fY %.2fZ %.2f° %.2f° in %s"
                .formatted(ServerManager.GetLobbyLocation().getX(),
                        ServerManager.GetLobbyLocation().getY(),
                        ServerManager.GetLobbyLocation().getZ(),
                        ServerManager.GetLobbyLocation().getPitch(),
                        ServerManager.GetLobbyLocation().getYaw(),
                        ServerManager.GetLobbyLocation().getWorld().getName()));
    }

    private void GotoSpawn(CommandData data)
    {
        if (!(data.GetSender() instanceof Entity))
        {
            return;
        }

        if (ServerManager.GetLobbyLocation() == null)
        {
            data.SetStatus(CommandStatus.Errored);
            data.SetFeedback("Spawn location not set");
            return;
        }

        ((Entity)data.GetSender()).teleport(ServerManager.GetLobbyLocation());
        data.SetFeedback("Teleported to lobby!");
    }



    // Inherited methods.
    @Override
    protected void ExecuteCommand(CommandData data)
    {
        if (data.GetLength() == 0)
        {
            data.SetFeedback("Du bist dumm");
            data.SetStatus(CommandStatus.Incomplete);
            return;
        }

        switch (data.GetArgs()[0])
        {
            case _spawnKeyword:
                ParseSpawn(data);
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
            data.SetRecommendations(Arrays.asList(_spawnKeyword));
        }

        if (data.GetArgs()[0].equals(_spawnKeyword))
        {
            data.SetRecommendations(Arrays.asList(_setKeyword, _gotoKeyword));
        }
    }
}
