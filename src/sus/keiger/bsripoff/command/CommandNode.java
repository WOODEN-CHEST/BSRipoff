//package sus.keiger.bsripoff.command;

//import java.util.Arrays;
//import java.util.HashSet;
//import java.util.List;
//import java.util.function.Consumer;

//public class CommandNode
//{
//    // Private fields.
//    private final HashSet<CommandNode> _subNodes = new();
//    private String _name;
//    private Consumer<CommandData> _executeFunction;
//
//
//
//    // Constructors.
//    public CommandNode(String name, Consumer<CommandData> executeFunction)
//    {
//        _name = name;
//        _executeFunction = executeFunction;
//    }
//
//
//    // Methods.
//    public void Execute(CommandData data)
//    {
//        for (CommandNode Node : _subNodes)
//        {
//            if (Node._name.equals(args[index]))
//            {
//                Node.Execute(args, index + 1);
//            }
//        }
//    }
//
//    public void Tab(CommandData data)
//    {
//
//    }
//
//    public void SetSubNodes(CommandNode... subNodes)
//    {
//        _subNodes.clear();
//
//        if (subNodes == null || subNodes.length == 0)
//        {
//            return;
//        }
//
//        _subNodes.addAll(Arrays.asList(subNodes));
//    }
//
//    public List<String> GetRecommendations()
//    {
//
//    }
//
//
//    // Protected methods.
//    protected boolean IsSubNode(CommandData data)
//    {
//        if (_name == null)
//        {
//            return false;
//        }
//
//        return _name.equals(data)
//    }
//}