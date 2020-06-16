import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

public class Runner {
    public static void main(String[] args) throws Exception {
        CharStream input = CharStreams.fromStream(System.in);
        
        // Grammar file is Expr.g4. That's why we have ExprLexer
        ExprLexer lexer = new ExprLexer(input);
        
        CommonTokenStream tokens = new CommonTokenStream(lexer);

        // Grammar file is Expr.g4. That's why we have ExprParser
        ExprParser parser = new ExprParser(tokens);

        // Grammar start symbol is e. That's why we have a method e().
        ParseTree tree = parser.e();
        System.out.println(tree.toStringTree(parser));

        ParseTreeWalker walker = new ParseTreeWalker();

        // Grammar file is Expr.g4. That's why we have ExprBaseListener
        MyExprBaseListener listener = new MyExprBaseListener(lexer.getVocabulary()); 
        walker.walk(listener, tree);

        // Total
        System.out.println("*******************************");
        System.out.println("Total sum: " + listener.getTotal());
    }
}

class MyExprBaseListener extends ExprBaseListener {
    Vocabulary v;

    public MyExprBaseListener(Vocabulary v) {
        this.v = v;
    }

    int totalSum = 0;

	@Override public void enterS(ExprParser.SContext ctx) {}
	@Override public void exitS(ExprParser.SContext ctx) { }
	@Override public void enterE(ExprParser.EContext ctx) { }
	@Override public void exitE(ExprParser.EContext ctx) { }
	@Override public void enterEveryRule(ParserRuleContext ctx) { }
	@Override public void exitEveryRule(ParserRuleContext ctx) { }
	@Override public void visitTerminal(TerminalNode node) {

        Token token = node.getSymbol();
        int tokenIndex = token.getTokenIndex();
        int tokenLine = token.getLine();
        int tokenStartIndex = token.getStartIndex();
        int tokenStopIndex = token.getStopIndex();
        int tokenCharPositionInLine = token.getCharPositionInLine();
        String tokenText = token.getText();
        int tokenType = token.getType();

        System.out.println("========== visitTerminal ==========");
        System.out.printf(
            "Token values: token index: %d, start: %d, stop: %d = text: '%s', <type: %d>, line: %d : charPos: %d\n",
            tokenIndex, tokenStartIndex, tokenStopIndex, tokenText, tokenType, tokenLine, tokenCharPositionInLine
        );


        System.out.println("Symbol from node.getSymbol(): " + node.getSymbol());
        System.out.println("Text from node.getText(): " + node.getText());
        

        System.out.println("Literal Name from vocab.getLiteralName: " + v.getLiteralName(tokenType)); //  null for 10, + for +,
        System.out.println("Token Type from vocab.getDisplayName: " + v.getDisplayName(tokenType)); // INT for 10, + for +,
        System.out.println("Symbolic Name from vocab.getSymbolicName: " + v.getSymbolicName(tokenType)); // INT for 10, + for +,

        if (v.getDisplayName(tokenType) == "INT") {
            totalSum += Integer.valueOf(tokenText);
        }
    }
    @Override public void visitErrorNode(ErrorNode node) { }
    
    public int getTotal() {
        return totalSum;
    }
}