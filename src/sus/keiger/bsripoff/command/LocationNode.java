package sus.keiger.bsripoff.command;

import org.apache.commons.lang.NullArgumentException;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import sus.keiger.bsripoff.BSRipoff;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.function.BiConsumer;

public class LocationNode extends CommandNode
{
    // Private fields.
    private final CommandOption _requireRotation;


    // Constructors.
    public LocationNode(CommandOption requireRotation, BiConsumer<CommandData, List<Object>> executor)
    {
        super(executor);

        if (requireRotation == null)
        {
            throw new NullArgumentException("requireRotation is null");
        }

        _requireRotation = requireRotation;
    }


    // Private methods.
    private double ParseCoordinate(CommandData data, Location relativeLocation, int coordinateIndex)
    {
        String StringCoordinate = data.ReadWord();
        double Coordinate = 0d;
        double RelativeOffset = 0d;

        if (StringCoordinate.startsWith("~"))
        {
            switch (coordinateIndex)
            {
                case 0 -> Coordinate = relativeLocation.getX();
                case 1 -> Coordinate = relativeLocation.getY();
                case 2 -> Coordinate = relativeLocation.getZ();
                case 3 -> Coordinate = relativeLocation.getYaw();
                case 4 -> Coordinate = relativeLocation.getPitch();
            }

            if (StringCoordinate.equals("~"))
            {
                RelativeOffset = 0d;
            }
            else
            {
                try
                {
                    RelativeOffset = Double.parseDouble(StringCoordinate.substring(1));
                }
                catch (NumberFormatException e)
                {
                    return Double.NaN;
                }
            }
        }
        else
        {
            try
            {
                Coordinate = Double.parseDouble(StringCoordinate);
            }
            catch (NumberFormatException e)
            {
                return Double.NaN;
            }
        }

        return Coordinate + RelativeOffset;
    }


    // Inherited methods.
    @Override
    public List<String> GetSelfSuggestions(CommandData data)
    {
        List<String> Suggestions = new ArrayList<>();

        if (!(data.Sender instanceof Entity SenderEntity))
        {
            return Suggestions;
        }

        Location Loc = SenderEntity.getLocation();

        Suggestions.add(_requireRotation == CommandOption.Disabled ? "~ ~ ~" : "~ ~ ~ ~ ~");
        Suggestions.add(_requireRotation == CommandOption.Disabled ?
                "%.2f %.2f %.2f".formatted(Loc.getX(), Loc.getY(), Loc.getZ()).replace(',', '.') :
                "%.2f %.2f %.2f %.2f %.2f".formatted(Loc.getX(), Loc.getY(), Loc.getZ(), Loc.getPitch(), Loc.getYaw())
                        .replace(',', '.'));

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

        Location ParsedLocation;
        if (data.Sender instanceof Entity EntitySender)
        {
            ParsedLocation = EntitySender.getLocation().clone();
        }
        else
        {
            ParsedLocation = new Location(BSRipoff.GetServerManager().Overworld, 0d, 0d, 0d);
        }

        int ParsedCount = 0;
        for (; ParsedCount < (_requireRotation == CommandOption.Disabled ? 3 : 5); ParsedCount++)
        {
            data.MoveIndexToNextNonWhitespace();
            if (!data.IsMoreDataAvailable())
            {
                break;
            }

            double ParsedValue = ParseCoordinate(data, ParsedLocation, ParsedCount);
            if (Double.isNaN(ParsedValue))
            {
                break;
            }

            switch (ParsedCount)
            {
                case 0 -> ParsedLocation.setX(ParsedValue);
                case 1 -> ParsedLocation.setY(ParsedValue);
                case 2 -> ParsedLocation.setZ(ParsedValue);
                case 3 -> ParsedLocation.setYaw((float)ParsedValue);
                case 4 -> ParsedLocation.setPitch((float)ParsedValue);
            }
        }

        if ((ParsedCount < 3) ||(_requireRotation == CommandOption.Mandatory && ParsedCount < 5))
        {
            return CommandBelongState.NotBelong;
        }
        else
        {
            parsedData.add(ParsedLocation);
            return CommandBelongState.BelongComplete;
        }
    }
}