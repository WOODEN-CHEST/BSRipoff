package sus.onevsoneserver.ServerPlayer;

import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.*;
import org.bukkit.potion.*;
import sus.onevsoneserver.Main;
import sus.onevsoneserver.extensions.PlayerExt;

import java.util.function.Consumer;

public enum ServerPlayerState
{
    LOBBY,
    IN_GAME,
    SPECTATING
}
