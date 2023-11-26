package sus.keiger.bsripoff;

import org.bukkit.plugin.java.JavaPlugin;
import sus.keiger.bsripoff.command.manage.ManageCommand;

public class BSRipoff extends JavaPlugin
{
    // Private static fields.
    private static BSRipoff s_pluginInstance;
    private ManageCommand _manageCommand;


    // Private fields.
    private final ServerEventListener _eventListener = new ServerEventListener();



    // Constructors.
    public BSRipoff()
    {
        s_pluginInstance = this;
    }


    // Inherited .
    @Override
    public void onEnable()
    {
        getServer().getPluginManager().registerEvents(_eventListener, this);
        _manageCommand = new ManageCommand();
    }


    // Static methods.
    public static BSRipoff GetPlugin()
    {
        return s_pluginInstance;
    }
}