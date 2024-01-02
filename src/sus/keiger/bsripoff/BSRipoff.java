package sus.keiger.bsripoff;

import org.bukkit.GameMode;
import org.bukkit.GameRule;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;
import sus.keiger.bsripoff.command.manage.ManageCommand;

import java.util.Random;
import java.util.logging.Logger;

public class BSRipoff extends JavaPlugin
{
    // Static fields.
    public static final int TICKS_IN_SECOND = 20;
    public static final String NAMESPACE = "bsr";


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


    // Private methods.
    private void SetupWorldSettings()
    {
        SetGamerules();

        for (World ServerWorld : getServer().getWorlds())
        {
            ServerWorld.getWorldBorder().setCenter(0d, 0d);
            ServerWorld.getWorldBorder().setSize(15_000_000d);
            ServerWorld.getWorldBorder().setDamageAmount(1d);
            ServerWorld.getWorldBorder().setDamageBuffer(1d);
        }

        Manager.Overworld.setTime(6000);
        GetServer().setDefaultGameMode(GameMode.ADVENTURE);
        new ServerManager();
    }

    private void SetGamerules()
    {
        for (World ServerWorld : GetServer().getWorlds())
        {
            ServerWorld.setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, false);
            ServerWorld.setGameRule(GameRule.BLOCK_EXPLOSION_DROP_DECAY, false);
            ServerWorld.setGameRule(GameRule.COMMAND_BLOCK_OUTPUT, false);
            ServerWorld.setGameRule(GameRule.COMMAND_MODIFICATION_BLOCK_LIMIT, Integer.MAX_VALUE);
            ServerWorld.setGameRule(GameRule.DISABLE_ELYTRA_MOVEMENT_CHECK, false);
            ServerWorld.setGameRule(GameRule.DISABLE_RAIDS, true);
            ServerWorld.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
            ServerWorld.setGameRule(GameRule.DO_ENTITY_DROPS, false);
            ServerWorld.setGameRule(GameRule.DO_FIRE_TICK, false);
            ServerWorld.setGameRule(GameRule.DO_INSOMNIA, false);
            ServerWorld.setGameRule(GameRule.DO_IMMEDIATE_RESPAWN, true);
            ServerWorld.setGameRule(GameRule.DO_LIMITED_CRAFTING, true);
            ServerWorld.setGameRule(GameRule.DO_MOB_LOOT, false);
            ServerWorld.setGameRule(GameRule.DO_MOB_SPAWNING, false);
            ServerWorld.setGameRule(GameRule.DO_PATROL_SPAWNING, false);
            ServerWorld.setGameRule(GameRule.DO_TILE_DROPS, false);
            ServerWorld.setGameRule(GameRule.DO_TRADER_SPAWNING, false);
            ServerWorld.setGameRule(GameRule.DO_VINES_SPREAD, false);
            ServerWorld.setGameRule(GameRule.DO_WEATHER_CYCLE, false);
            ServerWorld.setGameRule(GameRule.DROWNING_DAMAGE, true);
            ServerWorld.setGameRule(GameRule.ENDER_PEARLS_VANISH_ON_DEATH, true);
            ServerWorld.setGameRule(GameRule.FALL_DAMAGE, true);
            ServerWorld.setGameRule(GameRule.FIRE_DAMAGE, true);
            ServerWorld.setGameRule(GameRule.FORGIVE_DEAD_PLAYERS, true);
            ServerWorld.setGameRule(GameRule.FREEZE_DAMAGE, true);
            ServerWorld.setGameRule(GameRule.GLOBAL_SOUND_EVENTS, false);
            ServerWorld.setGameRule(GameRule.KEEP_INVENTORY, true);
            ServerWorld.setGameRule(GameRule.LAVA_SOURCE_CONVERSION, false);
            ServerWorld.setGameRule(GameRule.LOG_ADMIN_COMMANDS, true);
            ServerWorld.setGameRule(GameRule.MAX_COMMAND_CHAIN_LENGTH, Integer.MAX_VALUE);
            ServerWorld.setGameRule(GameRule.MAX_ENTITY_CRAMMING, 0);
            ServerWorld.setGameRule(GameRule.MOB_EXPLOSION_DROP_DECAY, false);
            ServerWorld.setGameRule(GameRule.MOB_GRIEFING, false);
            ServerWorld.setGameRule(GameRule.NATURAL_REGENERATION, false);
            ServerWorld.setGameRule(GameRule.PLAYERS_SLEEPING_PERCENTAGE, 0);
            ServerWorld.setGameRule(GameRule.RANDOM_TICK_SPEED, 0);
            ServerWorld.setGameRule(GameRule.REDUCED_DEBUG_INFO, false);
            ServerWorld.setGameRule(GameRule.SEND_COMMAND_FEEDBACK, false);
            ServerWorld.setGameRule(GameRule.SHOW_DEATH_MESSAGES, false);
            ServerWorld.setGameRule(GameRule.SNOW_ACCUMULATION_HEIGHT, 0);
            ServerWorld.setGameRule(GameRule.SPAWN_RADIUS, 0);
            ServerWorld.setGameRule(GameRule.SPECTATORS_GENERATE_CHUNKS, false);
            ServerWorld.setGameRule(GameRule.TNT_EXPLOSION_DROP_DECAY, false);
            ServerWorld.setGameRule(GameRule.UNIVERSAL_ANGER, false);
            ServerWorld.setGameRule(GameRule.WATER_SOURCE_CONVERSION, true);

        }
    }


    // Inherited methods.
    @Override
    public void onEnable()
    {
        getServer().getPluginManager().registerEvents(_eventListener, this);
        Manager = new ServerManager();
        ManageCommand.CreateCommand();

        SetupWorldSettings();
    }
}