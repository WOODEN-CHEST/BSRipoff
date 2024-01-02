package sus.keiger.bsripoff.game;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.title.Title;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.event.entity.PlayerDeathEvent;
import sus.keiger.bsripoff.BSRipoff;
import sus.keiger.bsripoff.player.ActionbarMessage;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

public class TwoTeamGame extends Game
{
    // Static fields.
    private static final NamedTextColor BLUE_TEAM_TEXT_COLOR = NamedTextColor.BLUE;
    private static final NamedTextColor RED_TEAM_TEXT_COLOR = NamedTextColor.RED;



    // Private static fields.
    private static final int PRE_GAME_TIME = 200; // 10 seconds.
    private static final int POST_GAME_TIME = 200; // 10 seconds.
    private static final int STARTING_TEXT_ID = 418920003;
    private static final int RESPAWN_TEXT_ID = -10387;


    // Private fields.
    private final HashSet<GamePlayer> _bluePlayers = new HashSet<>();
    private final HashSet<GamePlayer> _redPlayers = new HashSet<>();
    private int _gameStateTimer;
    private int _respawnTimeTicks = 100; // 5 seconds.


    // Constructors.
    public TwoTeamGame(GameMap map)
    {
        super(map);
    }


    // Private methods.
    private void TeleportTeamToSpawn(Collection<Location> spawns, Collection<GamePlayer> players)
    {
        Iterator<GamePlayer> PlayerIterator = players.iterator();
        for (Location SpawnLocation : spawns)
        {
            if (PlayerIterator.hasNext())
            {
                PlayerIterator.next().MCPlayer.teleport(SpawnLocation);
            }
            else
            {
                break;
            }
        }
    }

    private void TellAllTeamsStartTime()
    {
        TellTeamStartTime(_bluePlayers, BLUE_TEAM_TEXT_COLOR);
        TellTeamStartTime(_redPlayers, RED_TEAM_TEXT_COLOR);
    }

    private void TellTeamStartTime(Collection<GamePlayer> teamPlayers, NamedTextColor teamColor)
    {
        Component TeamText = Component.text("Starting in %d".formatted(_gameStateTimer / BSRipoff.TICKS_IN_SECOND))
                .color(teamColor);

        for (GamePlayer GPlayer : _bluePlayers)
        {
            GPlayer.BSRPlayer.ActionBarManager.AddMessage(new ActionbarMessage(20, TeamText, STARTING_TEXT_ID));
            GPlayer.MCPlayer.playSound(GPlayer.MCPlayer.getLocation(), Sound.BLOCK_NOTE_BLOCK_HAT,
                    SoundCategory.PLAYERS, 1f, 2f);
        }
    }


    // Inherited methods.
    protected int GetRespawnTime() { return _respawnTimeTicks; }

    protected void SetRespawnTime(int value) { _respawnTimeTicks = Math.max(0, value); }

    /* Game phase events. */
    @Override
    protected void OnPreStartEvent()
    {
        super.OnPreStartEvent();

        _gameStateTimer = PRE_GAME_TIME;
        TellAllTeamsStartTime();
    }

    @Override
    protected void OnStartEvent()
    {
        super.OnStartEvent();
        TeleportTeamToSpawn(GetMap().GetBlueSpawnLocations(), _bluePlayers);
        TeleportTeamToSpawn(GetMap().GetRedSpawnLocations(), _redPlayers);
    }

    @Override
    protected void OnEndEvent()
    {
        _gameStateTimer = POST_GAME_TIME;
    }

    @Override
    protected void OnPostEndEvent()
    {

    }


    /* Ticking. */
    @Override
    protected void TickPreGame()
    {
        if (_gameStateTimer <= 0)
        {
            Start();
        }
        else if ((_gameStateTimer <= 100) && ((_gameStateTimer & BSRipoff.TICKS_IN_SECOND) == 0))
        {
            TellAllTeamsStartTime();
        }
        _gameStateTimer--;
    }

    @Override
    protected void TickPostGame()
    {
        if (_gameStateTimer <= 0)
        {
            PostEnd();
        }
        _gameStateTimer--;
    }


    /* Events. */
    @Override
    public void OnPlayerDeathEvent(GamePlayer gPlayer, PlayerDeathEvent event)
    {
        super.OnPlayerDeathEvent(gPlayer, event);

        if (_respawnTimeTicks == 0)
        {
            gPlayer.GetKitInstance().OnRespawnEvent();
        }
        else
        {
            gPlayer.GetKitInstance().SetRespawnTimer(_respawnTimeTicks);
        }
    }

    @Override
    public void OnPlayerTickDuringRespawnEvent(GamePlayer gPlayer, int respawnTimer)
    {
        super.OnPlayerTickDuringRespawnEvent(gPlayer, respawnTimer);

        if ((respawnTimer %  BSRipoff.TICKS_IN_SECOND) == 0)
        {
            gPlayer.BSRPlayer.ActionBarManager.AddMessage(new ActionbarMessage(20,
                    Component.text("Respawning in %d".formatted(respawnTimer / BSRipoff.TICKS_IN_SECOND))
                            .color(_bluePlayers.contains(gPlayer) ? BLUE_TEAM_TEXT_COLOR : RED_TEAM_TEXT_COLOR),
                    RESPAWN_TEXT_ID));
        }
    }

    @Override
    public void OnPlayerRespawnEvent(GamePlayer gPlayer)
    {
        gPlayer.MCPlayer.showTitle(Title.title(Component.text(""), Component.text("Respawned!").color(
                _bluePlayers.contains(gPlayer) ? BLUE_TEAM_TEXT_COLOR : RED_TEAM_TEXT_COLOR
        )));
    }
}
