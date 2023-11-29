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
    @Override
    public void onDisable()
    {
        // add shit that happen when plugin/server disabled
    }


    // Static methods.
    public static BSRipoff GetPlugin()
    {
        return s_pluginInstance;
    }
}