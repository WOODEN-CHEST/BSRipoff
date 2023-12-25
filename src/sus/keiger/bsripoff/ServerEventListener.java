package sus.keiger.bsripoff;

import com.destroystokyo.paper.event.server.ServerTickStartEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import sus.keiger.bsripoff.game.Game;
import sus.keiger.bsripoff.game.kit.Kit;
import sus.keiger.bsripoff.player.BSRipoffPlayer;


public class ServerEventListener implements Listener
{
    /* Player. */
    @EventHandler
    public void OnPlayerJoinEvent(PlayerJoinEvent event)
    {
        BSRipoff.GetServerManager().OnPlayerJoinEvent(event);
    }

    @EventHandler
    public void OnPlayerQuitEvent(PlayerQuitEvent event)
    {
        BSRipoff.GetServerManager().OnPlayerQuitEvent(event);
    }

    @EventHandler
    public void OnPlayerDropItemEvent(PlayerDropItemEvent event)
    {

    }

    @EventHandler
    public void OnPlayerInventoryEvent(InventoryEvent event)
    {
        if (!(event.getInventory().getHolder() instanceof Player))
        {
            return;
        }
    }

    /* Server. */
    @EventHandler
    public void OnTickEvent(ServerTickStartEvent event)
    {
        Kit.TickKits();
        Game.TickGames();
        BSRipoff.GetServerManager().OnTickEvent(event);
    }
}