import java.util.Hashtable;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

public class Runner {
    public static void main(String[] args) throws Exception {
        CharStream input = CharStreams.fromStream(System.in);
        
        // Grammar file is Prog.g4. That's why we have ProgLexer
        ProgLexer lexer = new ProgLexer(input);
        
        CommonTokenStream tokens = new CommonTokenStream(lexer);

        // Grammar file is Prog.g4. That's why we have ProgParser
        ProgParser parser = new ProgParser(tokens);

        // Grammar start symbol is prog. That's why we have a method prog().
        ParseTree tree = parser.prog();
        System.out.println("Parse Tree: " + tree.toStringTree(parser));

        System.out.println("------------------------------------------");

        ParseTreeWalker walker = new ParseTreeWalker();

        MyProgBaseListener listener = new MyProgBaseListener(); 
        walker.walk(listener, tree);
    }
}


class MyProgBaseListener extends ProgBaseListener {
    Hashtable<ProgParser.EContext, Integer> eContextToValue =
        new Hashtable<ProgParser.EContext, Integer>();
    Hashtable<String, Integer> symbolTable = new Hashtable<String, Integer>();

    @Override public void exitExpo(ProgParser.ExpoContext ctx) {
        int value;

        ProgParser.EContext eLeft = ctx.e(0);
        ProgParser.EContext eRight = ctx.e(1);

        int eValueOfLeft = eContextToValue.get(eLeft);
        int eValueOfRight = eContextToValue.get(eRight);

        value = (int) Math.pow(eValueOfLeft, eValueOfRight); // Let's ignore the precision loss for now.
        eContextToValue.put(ctx, value);
    }

	@Override public void exitMulDiv(ProgParser.MulDivContext ctx) {
        int value;

        ProgParser.EContext eLeft = ctx.e(0);
        ProgParser.EContext eRight = ctx.e(1);

        int eValueOfLeft = eContextToValue.get(eLeft);
        int eValueOfRight = eContextToValue.get(eRight);

        // The type of ctx.op is Token.
        switch(ctx.op.getType()) {
            case ProgParser.MUL:
                value = eValueOfLeft * eValueOfRight;
            break;
            case ProgParser.DIV:
                value = eValueOfLeft / eValueOfRight;
            break;
            default:
                throw new RuntimeException("Unexpected operator type");
        }

        eContextToValue.put(ctx, value);
    }

	@Override public void exitAddSub(ProgParser.AddSubContext ctx) {
        int value;

        ProgParser.EContext eLeft = ctx.e(0);
        ProgParser.EContext eRight = ctx.e(1);

        int eValueOfLeft = eContextToValue.get(eLeft);
        int eValueOfRight = eContextToValue.get(eRight);

        // The type of ctx.op is Token.
        switch(ctx.op.getType()) {
            case ProgParser.ADD:
                value = eValueOfLeft + eValueOfRight;
            break;
            case ProgParser.SUB:
                value = eValueOfLeft - eValueOfRight;
            break;
            default:
                throw new RuntimeException("Unexpected operator type");
        }

        eContextToValue.put(ctx, value);
    }

	@Override public void exitInt(ProgParser.IntContext ctx) {
        eContextToValue.put(ctx, Integer.parseInt(ctx.getText()));
    }

    @Override public void exitId(ProgParser.IdContext ctx) {
        String id = ctx.ID().getText();
        int value = 0;
        if (symbolTable.containsKey(id)) {
            value = symbolTable.get(id);
        }
        eContextToValue.put(ctx, value);
    }

    @Override public void exitPrint(ProgParser.PrintContext ctx) {
        System.out.println(eContextToValue.get(ctx.e()));
    }

    @Override public void exitAssign(ProgParser.AssignContext ctx) {
        int value = eContextToValue.get(ctx.e());
        String id = ctx.ID().getText();
        symbolTable.put(id, value);
    }
}
