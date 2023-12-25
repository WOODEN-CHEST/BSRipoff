package sus.keiger.bsripoff.game.kit;

public class KitStats
{
    // Static fields.
    public static int STAT_MIN_VALUE = 1;
    public static int STAT_MAX_VALUE = 6;


    // Fields.
    public final int Offense;
    public final int Defense;
    public final int Utility;
    public final int Versatility;


    // Constructors.
    public KitStats(int offense, int defense, int utility, int versatility)
    {
        Offense = Math.max(STAT_MIN_VALUE, Math.min(offense, STAT_MAX_VALUE));
        Defense = Math.max(STAT_MIN_VALUE, Math.min(defense, STAT_MAX_VALUE));
        Utility = Math.max(STAT_MIN_VALUE, Math.min(utility, STAT_MAX_VALUE));
        Versatility = Math.max(STAT_MIN_VALUE, Math.min(versatility, STAT_MAX_VALUE));
    }
}