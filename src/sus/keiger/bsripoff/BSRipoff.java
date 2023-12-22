package sus.keiger.bsripoff;

import org.bukkit.Server;
import org.bukkit.plugin.java.JavaPlugin;
import sus.keiger.bsripoff.command.manage.ManageCommand;

import java.util.Random;
import java.util.logging.Logger;

public class BSRipoff extends JavaPlugin
{
    // Fields.
    public ServerManager Manager;
    public final Random Rng = new Random();


    // Private static fields.
    private static BSRipoff s_pluginInstance;


    // Private fields.
    private final ServerEventListener _eventListener = new ServerEventListener();



    // Constructors.
    public BSRipoff()
    {
        s_pluginInstance = this;
    }

    // Static methods.
    public static BSRipoff GetPlugin()
    {
        return s_pluginInstance;
    }

    public static ServerManager GetServerManager()
    {
        return s_pluginInstance.Manager;
    }
    @Override
    public void onDisable()
    {
        // add shit that happen when plugin/server disabled
    }

    public static Server GetServer()
    {
        return s_pluginInstance.getServer();
    }

    public static Logger GetLogger()
    {
        return GetServer().getLogger();
    }


    // Inherited methods.
    @Override
    public void onEnable()
    {
        getServer().getPluginManager().registerEvents(_eventListener, this);
        Manager = new ServerManager();
        ManageCommand.CreateCommand();
    }
}