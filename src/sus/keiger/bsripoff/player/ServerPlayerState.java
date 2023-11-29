package sus.keiger.bsripoff.player;

import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.*;
import org.bukkit.potion.*;
import sus.BSRipoff.Main;
import sus.BSRipoff.extensions.PlayerExt;

import java.util.function.Consumer;

// Player state - whether they are in the lobby, playing in match, or spectating a match

public enum ServerPlayerState
{
    LOBBY,
    IN_GAME,
    SPECTATING
}

// as always, i have no fucking clue if anything here works or wont break everything just because i didnt write one word
