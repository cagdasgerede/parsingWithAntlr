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

    @Override public void exitS(ExprParser.SContext ctx) {
        System.out.println("Final result: " + eContextToValue.get(ctx.e()));
    }

    @Override public void exitExpo(ExprParser.ExpoContext ctx) {
        int value;

        ExprParser.EContext eLeft = ctx.e(0);
        ExprParser.EContext eRight = ctx.e(1);

        int eValueOfLeft = eContextToValue.get(eLeft);
        int eValueOfRight = eContextToValue.get(eRight);

        value = (int) Math.pow(eValueOfLeft, eValueOfRight); // Let's ignore the precision loss for now.
        eContextToValue.put(ctx, value);
    }

	@Override public void exitMulDiv(ExprParser.MulDivContext ctx) {
        int value;

        ExprParser.EContext eLeft = ctx.e(0);
        ExprParser.EContext eRight = ctx.e(1);

        int eValueOfLeft = eContextToValue.get(eLeft);
        int eValueOfRight = eContextToValue.get(eRight);

        // The type of ctx.op is Token.
        switch(ctx.op.getType()) {
            case ExprParser.MUL:
                value = eValueOfLeft * eValueOfRight;
            break;
            case ExprParser.DIV:
                value = eValueOfLeft / eValueOfRight;
            break;
            default:
                throw new RuntimeException("Unexpected operator type");
        }

        eContextToValue.put(ctx, value);
    }

	@Override public void exitAddSub(ExprParser.AddSubContext ctx) {
        int value;

        ExprParser.EContext eLeft = ctx.e(0);
        ExprParser.EContext eRight = ctx.e(1);

        int eValueOfLeft = eContextToValue.get(eLeft);
        int eValueOfRight = eContextToValue.get(eRight);

        // The type of ctx.op is Token.
        switch(ctx.op.getType()) {
            case ExprParser.ADD:
                value = eValueOfLeft + eValueOfRight;
            break;
            case ExprParser.SUB:
                value = eValueOfLeft - eValueOfRight;
            break;
            default:
                throw new RuntimeException("Unexpected operator type");
        }

        eContextToValue.put(ctx, value);
    }

	@Override public void exitInt(ExprParser.IntContext ctx) {
        eContextToValue.put(ctx, Integer.valueOf(ctx.getText()));
    }
}
