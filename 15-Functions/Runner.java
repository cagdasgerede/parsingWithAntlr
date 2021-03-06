import java.util.Hashtable;
import java.util.Stack;

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
    Stack<Integer> stack = new Stack<Integer>();

    Hashtable<String, Integer> symbolTable = new Hashtable<String, Integer>();

    boolean insideFunctionDeclaration = false;

    @Override public void exitExpo(ProgParser.ExpoContext ctx) {
        int eValueOfRight = stack.pop();
        int eValueOfLeft = stack.pop();

        int value = (int) Math.pow(eValueOfLeft, eValueOfRight); // Let's ignore the precision loss for now.
        stack.push(value);
    }

	@Override public void exitMulDiv(ProgParser.MulDivContext ctx) {
        if (insideFunctionDeclaration) {
            return;
        }

        int eValueOfRight = stack.pop();
        int eValueOfLeft = stack.pop();
        int value;

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

        stack.push(value);
    }

	@Override public void exitAddSub(ProgParser.AddSubContext ctx) {
        if (insideFunctionDeclaration) {
            return;
        }

        int eValueOfRight = stack.pop();
        int eValueOfLeft = stack.pop();
        int value;

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

        stack.push(value);
    }

	@Override public void exitInt(ProgParser.IntContext ctx) {
        if (insideFunctionDeclaration) {
            return;
        }

        stack.push(Integer.valueOf(ctx.getText()));
    }

    @Override public void exitId(ProgParser.IdContext ctx) {
        if (insideFunctionDeclaration) {
            return;
        }

        String id = ctx.ID().getText();
        int value = 0;
        if (symbolTable.containsKey(id)) {
            value = symbolTable.get(id);
        }
        stack.push(value);
    }

    @Override public void exitPrint(ProgParser.PrintContext ctx) {
        if (insideFunctionDeclaration) {
            return;
        }

        if (!stack.isEmpty()) {
            System.out.println(stack.pop());
        }
    }

    @Override public void exitAssign(ProgParser.AssignContext ctx) {
        if (insideFunctionDeclaration) {
            return;
        }

        int value = stack.pop();
        String id = ctx.ID().getText();
        symbolTable.put(id, value);
    }


    @Override public void enterFuncDec(ProgParser.FuncDecContext ctx) {
        insideFunctionDeclaration = true;
    }

    @Override public void exitFuncDec(ProgParser.FuncDecContext ctx) {
        idToFunctionDeclaration.put(ctx.ID().getText(), ctx.prog());
        insideFunctionDeclaration = false;
    }

    Hashtable<String, ParseTree> idToFunctionDeclaration =
        new Hashtable<String, ParseTree>();

    @Override public void exitFuncCall(ProgParser.FuncCallContext ctx) {
        ParseTree tree = idToFunctionDeclaration.get(ctx.ID().getText());

        ParseTreeWalker walker = new ParseTreeWalker();
        MyProgBaseListener listener = new MyProgBaseListener(); 
        walker.walk(listener, tree);
    }
}
