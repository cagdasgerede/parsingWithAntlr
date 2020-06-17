import java.util.Hashtable;

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
        ParseTree tree = parser.s();
        System.out.println("Parse Tree: " + tree.toStringTree(parser));

        ParseTreeWalker walker = new ParseTreeWalker();

        // Grammar file is Expr.g4. That's why we have ExprBaseListener
        MyExprBaseListener listener = new MyExprBaseListener(); 
        walker.walk(listener, tree);
    }
}

class MyExprBaseListener extends ExprBaseListener {
    Hashtable<ExprParser.EContext, Integer> eContextToValue =
        new Hashtable<ExprParser.EContext, Integer>();

	@Override public void enterS(ExprParser.SContext ctx) {}

    @Override public void exitS(ExprParser.SContext ctx) {
        System.out.println("Final result: " + eContextToValue.get(ctx.e()));
    }
    
	@Override public void enterE(ExprParser.EContext ctx) {}

	@Override public void exitE(ExprParser.EContext ctx) {
        int INTvalue = Integer.parseInt(ctx.INT().getText());
        int value;

        if (ctx.op != null) {
            int eValue = eContextToValue.get(ctx.e());

            // The type of ctx.op is Token.
            switch(ctx.op.getType()) {
                case ExprParser.ADD:
                    value = INTvalue + eValue;
                break;
                case ExprParser.SUB:
                    value = INTvalue - eValue;
                break;
                default:
                    throw new RuntimeException("Unexpected operator type");
            }
        } else {
            value = INTvalue;
        }

        eContextToValue.put(ctx, value);
    }

    @Override public void enterEveryRule(ParserRuleContext ctx) {}

    @Override public void exitEveryRule(ParserRuleContext ctx) {}

	@Override public void visitTerminal(TerminalNode node) {}

    @Override public void visitErrorNode(ErrorNode node) {}
}