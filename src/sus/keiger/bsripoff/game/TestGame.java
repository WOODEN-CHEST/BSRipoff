package sus.keiger.bsripoff.game;

public class TestGame extends Game
{
    public TestGame(GameMap map)
    {
        super(map);

        IsJoiningDuringGameAllowed = true;

        ForceSetState(GameState.InGame);
    }
}