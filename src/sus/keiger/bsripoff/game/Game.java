package sus.keiger.bsripoff.game;

import org.apache.commons.lang.NullArgumentException;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import sus.keiger.bsripoff.BSRipoff;
import sus.keiger.bsripoff.player.BSRPlayerState;
import sus.keiger.bsripoff.player.BSRipoffPlayer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public abstract class Game
{
    // Protected fields.
    protected boolean IsJoiningDuringGameAllowed = false;


    // Private static fields.
    private static final ArrayList<Game> s_games = new ArrayList<>();


    // Private fields.
    private final HashMap<BSRipoffPlayer, GamePlayer> _players = new HashMap<>();
    private GameState _state = GameState.Lobby;
    private GameMap _map;


    // Constructors.
    protected Game(GameMap map)
    {
        _map = map;
    }


    // Static methods.
    public static void AddGame(Game game)
    {
        if (game == null)
        {
            throw new NullArgumentException("game is null.");
        }

        if (!s_games.contains(game))
        {
            s_games.add(game);
        }
    }

    public static void RemoveGame(Game game)
    {
        if (game == null)
        {
            throw new NullArgumentException("game is null.");
        }

        s_games.remove(game);
    }

    public static void TickGames()
    {
        for (Game ActiveGame : s_games)
        {
            ActiveGame.Tick();
        }
    }


    // Methods.
    /* Getters and setters. */
    public GamePlayer AddPlayer(BSRipoffPlayer bsrPlayer)
    {
        if (bsrPlayer == null)
        {
            throw new NullArgumentException("bsrPlayer is null");
        }
        if ((_state != GameState.Lobby) && (!IsJoiningDuringGameAllowed))
        {
            return null;
        }
        if (_players.containsKey(bsrPlayer))
        {
            return _players.get(bsrPlayer);
        }

        GamePlayer GPlayer = new GamePlayer(bsrPlayer, this);
        _players.put(bsrPlayer, GPlayer);

        if (_state == GameState.PreGame)
        {
            GPlayer.BSRPlayer.SetState(BSRPlayerState.InGame);
            GPlayer.MCPlayer.setInvulnerable(true);
            OnPreStartEventPlayer(GPlayer);
        }
        else if (_state == GameState.InGame)
        {
            OnStartEventPlayer(GPlayer);
        }

        return GPlayer;
    }

    public void RemovePlayer(BSRipoffPlayer bsrPlayer)
    {
        if (bsrPlayer == null)
        {
            throw new NullArgumentException("bsrPlayer is null");
        }

        _players.remove(bsrPlayer);
    }

    public GamePlayer GetPlayer(BSRipoffPlayer bsrPlayer)
    {
        if (bsrPlayer == null)
        {
            throw new NullArgumentException("bsrPlayer is null");
        }

        return _players.get(bsrPlayer);
    }

    public Collection<GamePlayer> GetPlayers() { return _players.values(); }

    public GameState GetState()
    {
        return _state;
    }

    public GameMap GetMap()
    {
        return _map;
    }

    public void SetMap(GameMap map)
    {
        _map = map;
    }

    /* Game events. */
    public final void PreStart()
    {
        if ((_state != GameState.Lobby) || (_players.size() == 0))
        {
            return;
        }
        _state = GameState.PreGame;

        OnPreStartEvent();
        for (GamePlayer GPlayer : _players.values())
        {
            OnPreStartEventPlayer(GPlayer);
        }
    }

    public final void Start()
    {
        if (_state != GameState.PreGame)
        {
            return;
        }
        _state = GameState.InGame;

        OnStartEvent();
        for (GamePlayer GPlayer : _players.values())
        {
            OnStartEventPlayer(GPlayer);
        }
    }

    public final void End()
    {
        if (_state != GameState.InGame)
        {
            return;
        }
        _state = GameState.PostGame;

        OnEndEvent();
        for (GamePlayer GPlayer : _players.values())
        {
            OnEndEventPlayer(GPlayer);
        }
    }

    public final void PostEnd()
    {
        if (_state != GameState.PreGame)
        {
            return;
        }
        _state = GameState.Completed;

        OnPostEndEvent();
        for (GamePlayer GPlayer : _players.values())
        {
            OnPostEndEventPlayer(GPlayer);
        }

        if (_map != null)
        {
            _map.CopyFromSource();
        }
    }

    public final void Tick()
    {
        switch (_state)
        {
            case PreGame -> TickPreGame();
            case InGame -> TickInGame();
            case PostGame -> TickPostGame();
        }
    }

    public void OnPlayerDeathEvent(GamePlayer gamePlayer, PlayerDeathEvent event) { }

    public void OnPlayerRespawnEvent(GamePlayer gPlayer) { }

    public void OnPlayerQuitEvent(PlayerQuitEvent event)
    {
        BSRipoffPlayer BSRPlayer = BSRipoff.GetServerManager().GetBSRPlayer(event.getPlayer());

        if ((_state == GameState.InGame) || (_state== GameState.PreGame) || (_state == GameState.PostGame))
        {
            OnPlayerQuitDuringGameEvent(_players.get(BSRPlayer));
        }
        else if (_state == GameState.Lobby)
        {
            OnPlayerQuitDuringLobbyStageEvent(_players.get(BSRPlayer));
        }

        _players.remove(BSRPlayer);
        if ((_players.size() == 0) && ((_state == GameState.InGame)
                || (_state== GameState.PreGame) || (_state == GameState.PostGame)))
        {
            OnZeroPlayersDuringGameEvent();
        }
    }

    public void OnPlayerDropItemEvent(PlayerDropItemEvent event) { }

    public void OnPlayerDealDamageEvent(EntityDamageByEntityEvent event) { }

    public void OnPlayerTickDuringRespawnEvent(GamePlayer gPlayer, int respawnTimer) { }


    // Protected methods.
    protected final void ForceSetState(GameState state)
    {
        if (state == null)
        {
            throw new NullArgumentException("state is null");
        }

        _state = state;
    }



    /* Ticking. */
    protected void TickPreGame() { }
    protected void TickInGame() { }

    protected void TickPostGame() { }


    /* Events. */
    protected void OnPreStartEvent() { }

    protected void OnPreStartEventPlayer(GamePlayer gPlayer) { }

    protected void OnStartEvent() { }

    protected void OnStartEventPlayer(GamePlayer gPlayer)
    {
        if (_map != null)
        {
            gPlayer.MCPlayer.setPlayerWeather(_map.GetWeather());
            gPlayer.MCPlayer.setPlayerTime(_map.GetTime(), false);
        }

        gPlayer.BSRPlayer.SetState(BSRPlayerState.InGame);
        gPlayer.MCPlayer.setInvulnerable(false);
        gPlayer.CreateKitInstance();
        gPlayer.GetKitInstance().OnLoadEvent();
    }

    protected void OnEndEvent() { }
    protected void OnEndEventPlayer(GamePlayer gPlayer)
    {
        gPlayer.BSRPlayer.SetState(BSRPlayerState.InGame);
        gPlayer.MCPlayer.setInvulnerable(true);
        gPlayer.GetKitInstance().OnUnloadEvent();
    }

    protected void OnPostEndEvent() { }

    protected void OnPostEndEventPlayer(GamePlayer gPlayer)
    {
        gPlayer.BSRPlayer.SetState(BSRPlayerState.InLobby);
    }

    protected void OnPlayerQuitDuringLobbyStageEvent(GamePlayer gPlayer) { }

    protected void OnPlayerQuitDuringGameEvent(GamePlayer gPlayer) { }

    protected void OnZeroPlayersDuringGameEvent()
    {
        End();
        PostEnd();
    }
}