package sus.keiger.bsripoff;

import org.bukkit.Server;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Random;
import java.util.logging.Logger;

public class BSRipoff extends JavaPlugin
{
    // Fields.
    public final ServerManager Manager;
    public final Random Rng = new Random();


    // Private static fields.
    private static BSRipoff s_pluginInstance;


    // Private fields.
    private final ServerEventListener _eventListener = new ServerEventListener();



    // Constructors.
    public BSRipoff()
    {
        s_pluginInstance = this;
        Manager = new ServerManager();
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
    }
}