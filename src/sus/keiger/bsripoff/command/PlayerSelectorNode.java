package sus.keiger.bsripoff.command;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import sus.keiger.bsripoff.BSRipoff;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.function.BiConsumer;

public class PlayerSelectorNode extends CommandNode
{
    // Static fields.
    public static String SELECTOR_SELF = "@s";
    public static String SELECTOR_CLOSEST = "@p";
    public static String SELECTOR_ALL = "@a";
    public static String SELECTOR_RANDOM = "@r";


    // Fields.
    public final boolean IsSpecialSelectorAllowed;
    public final int MaxSelectors;


    // Constructors.
    public PlayerSelectorNode(boolean isSpecialSelectorAllowed, int maxSelectors, BiConsumer<CommandData, List<Object>> executor)
    {
        super(executor);

        IsSpecialSelectorAllowed = isSpecialSelectorAllowed;
        MaxSelectors = Math.max(1, maxSelectors);
    }


    // Private methods.
    private List<Player> SelectPlayers(CommandData data, String selector)
    {
        List<Player> SelectedPlayers = new ArrayList<>();

        if (selector.startsWith("@") && IsSpecialSelectorAllowed)
        {
            SelectFromSpecialSelector(data, selector, SelectedPlayers);
        }
        else
        {
            Player SelectedPlayer = BSRipoff.GetServer().getPlayerExact(selector);
            if (SelectedPlayer != null)
            {
                SelectedPlayers.add(SelectedPlayer);
            }
        }

        return SelectedPlayers;
    }

    private void SelectFromSpecialSelector(CommandData data, String selector, List<Player> players)
    {
        switch (selector)
        {
            case "@s" ->
            {
                if (data.Sender instanceof Player)
                {
                    players.add((Player)data.Sender);
                }
            }
            case "@r" ->
            {
                List<Player> Players = new ArrayList<>(BSRipoff.GetServer().getOnlinePlayers());
                if (Players.size() == 0) break;

                int Index = BSRipoff.GetPlugin().Rng.nextInt(0, Players.size());
                players.add(Players.get(Index));
            }
            case "@p" ->
            {
                Player SelectedPlayer = SelectClosestPlayer(data);
                if (SelectedPlayer != null)
                {
                    players.add(SelectedPlayer);
                }
            }
            case "@a" -> players.addAll(BSRipoff.GetServer().getOnlinePlayers());
        }
    }

    private Player SelectClosestPlayer(CommandData data)
    {
        if (!(data.Sender instanceof Entity Sender))
        {
            return null;
        }

        double ClosestDistance = Double.POSITIVE_INFINITY;
        double CurrentDistance = 0f;
        Player ClosestPlayer = null;

        for (Player MCPlayer : BSRipoff.GetServer().getOnlinePlayers())
        {
            CurrentDistance = Sender.getLocation().distance(MCPlayer.getLocation());
            if ((CurrentDistance < ClosestDistance) && (Sender != MCPlayer))
            {
                ClosestDistance = CurrentDistance;
                ClosestPlayer = MCPlayer;
            }
        }

        return ClosestPlayer;
    }


    // Inherited methods.
    @Override
    public List<String> GetSelfSuggestions(CommandData data)
    {
        List<String> Suggestions = new ArrayList<>();

        if (IsSpecialSelectorAllowed)
        {
            Suggestions.addAll(List.of(SELECTOR_SELF, SELECTOR_CLOSEST, SELECTOR_ALL, SELECTOR_RANDOM, "AAA"));
        }
        Suggestions.addAll(BSRipoff.GetServer().getOnlinePlayers().stream().map(Player::getName).toList());

        return Suggestions;
    }

    @Override
    protected CommandBelongState ParseCommand(CommandData data, List<Object> parsedData)
    {
        data.MoveIndexToNextNonWhitespace();
        if (!data.IsMoreDataAvailable())
        {
            return CommandBelongState.NotBelong;
        }

        HashSet<Player> SelectedPlayers = new HashSet<>();

        for (int i = 0; i < MaxSelectors; i++)
        {
            data.MoveIndexToNextNonWhitespace();
            if (!data.IsMoreDataAvailable())
            {
                break;
            }

            String Selector = data.ReadWord();
            SelectedPlayers.addAll(SelectPlayers(data, Selector));
        }

        parsedData.add(SelectedPlayers);
        return CommandBelongState.BelongComplete;
    }
}
