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
        System.out.println("Parse Tree: " + tree.toStringTree(parser));

        ParseTreeWalker walker = new ParseTreeWalker();

        // Grammar file is Expr.g4. That's why we have ExprBaseListener
        int tokenTypeForINT = lexer.getTokenType("INT");
        System.out.println("token type for INT: " + tokenTypeForINT);
        MyExprBaseListener listener = new MyExprBaseListener(lexer.getVocabulary(), tokenTypeForINT); 
        walker.walk(listener, tree);

        // Total
        System.out.println("*******************************");
        System.out.println("Total sum: " + listener.getTotal());
    }
}

class MyExprBaseListener extends ExprBaseListener {
    Vocabulary v;
    int tokenTypeForINT;

    public MyExprBaseListener(Vocabulary v, int tokenTypeForINT) {
        this.v = v;
        this.tokenTypeForINT = tokenTypeForINT;

    }

    int totalSum = 0;

	@Override public void enterS(ExprParser.SContext ctx) {}
    @Override public void exitS(ExprParser.SContext ctx) { }
    
    // ctx is a ParserRuleContext object. The EContext class is a child of the ParserRuleContext class.
	@Override public void enterE(ExprParser.EContext ctx) {
        // System.out.println(ctx);
        System.out.println("enterE");
        System.out.println("start token: " + ctx.getStart());
        System.out.println("stop token: " + ctx.getStop());
        System.out.println("0th token: " + ctx.getToken(tokenTypeForINT, 0).getSymbol());
        System.out.println("child count: " + ctx.getChildCount());
        System.out.println("=======");

        totalSum += Integer.valueOf(ctx.getToken(tokenTypeForINT, 0).getText());

    }
	@Override public void exitE(ExprParser.EContext ctx) { 
        System.out.println("exitE");
    }
	@Override public void enterEveryRule(ParserRuleContext ctx) { }
	@Override public void exitEveryRule(ParserRuleContext ctx) { }
	@Override public void visitTerminal(TerminalNode node) {}
    @Override public void visitErrorNode(ErrorNode node) { }
    
    public int getTotal() {
        return totalSum;
    }
}