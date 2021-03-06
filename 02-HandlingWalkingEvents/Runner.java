import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

public class Runner {
    public static void main(String[] args) throws Exception {
        CharStream input = CharStreams.fromStream(System.in);
        HelloLexer lexer = new HelloLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        HelloParser parser = new HelloParser(tokens);
        ParseTree tree = parser.r();
        System.out.println(tree.toStringTree(parser));

        ParseTreeWalker walker = new ParseTreeWalker();
        walker.walk(new HelloBaseListener() {
            @Override public void enterR(HelloParser.RContext ctx) {
                System.out.println("enterR");
            }

            @Override public void exitR(HelloParser.RContext ctx) {
                System.out.println("exitR");
            }

            @Override public void visitTerminal(TerminalNode node) {
                System.out.println("terminal");
            }
        },
        tree);
    }
}
