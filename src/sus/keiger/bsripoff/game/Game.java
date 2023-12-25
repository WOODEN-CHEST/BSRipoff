package sus.keiger.bsripoff.game;

import org.apache.commons.lang.NullArgumentException;
import sus.keiger.bsripoff.BSRipoff;
import sus.keiger.bsripoff.player.BSRPlayerState;
import sus.keiger.bsripoff.player.BSRipoffPlayer;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class Game
{
    // Protected fields.
    protected boolean IsJoiningDuringGameAllowed = false;
    protected int MaxGameTime = BSRipoff.TICKS_IN_SECOND * 794; // 13 minutes and 14 seconds.


    // Private static fields.
    private static final ArrayList<Game> s_games = new ArrayList<>();


    // Private fields.
    private final HashMap<BSRipoffPlayer, GamePlayer> _players = new HashMap<>();
    private GameState _state = GameState.Lobby;
    private GameMap _map;
    private int _gameTime = 0;



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
    public GamePlayer CreatePlayer(BSRipoffPlayer bsrPlayer)
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

    public GameState GetState()
    {
        return _state;
    }

    public int GetGameTime()
    {
        return _gameTime;
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
    public void PreStart()
    {
        if ((_state != GameState.Lobby) || (_players.size() == 0))
        {
            return;
        }

        _state = GameState.PreGame;
        SetPlayerStates(BSRPlayerState.InGame);

        TeleportPlayersToMap();
    }

    public void Start()
    {

    }

    public void End()
    {

    }

    public void PostEnd()
    {
        SetPlayerStates(BSRPlayerState.InLobby);
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


    // Protected methods.
    protected void TickPreGame()
    {

    }

    protected void TickInGame()
    {
        _gameTime++;
    }

    protected void TickPostGame()
    {

    }

    protected void TeleportPlayersToMap()
    {
        if (_map == null)
        {
            return;
        }

        for (BSRipoffPlayer BSRPlayer : _players.keySet())
        {
            BSRPlayer.MCPlayer.teleport(_map.GetSpawnLocation());
        }
    }

    protected void SetPlayerStates(BSRPlayerState state)
    {
        for (GamePlayer GPlayer : _players.values())
        {
            GPlayer.BSRPlayer.SetState(state);
        }
    }
}