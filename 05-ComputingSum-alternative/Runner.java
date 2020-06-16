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
    StringBuffer outputBuffer = new StringBuffer();    

    Hashtable<ParserRuleContext, Integer> contextToNodeCounter = new Hashtable<ParserRuleContext, Integer>();

    int nodeCounter = 1;

	@Override public void enterS(ExprParser.SContext ctx) {
        System.out.println("enterS");

        outputBuffer.append("strict graph G { \n");

        String s = String.format("  %d [label=\"s\"];\n", nodeCounter);
        outputBuffer.append(s);

        contextToNodeCounter.put(ctx, nodeCounter);

        nodeCounter++;
    }

    @Override public void exitS(ExprParser.SContext ctx) {
        System.out.println("exitS");

        outputBuffer.append("}");

        System.out.println("---------------------------------------");
        System.out.println(outputBuffer.toString());
    }
    
	@Override public void enterE(ExprParser.EContext ctx) {
        System.out.println("enterE");

        String s = String.format("  %d [label=\"e\"];\n", nodeCounter);
        outputBuffer.append(s);

        contextToNodeCounter.put(ctx, nodeCounter);

        int parentNodeCounter = contextToNodeCounter.get(ctx.getParent());
        s = String.format("  %d -- %d;\n", parentNodeCounter, nodeCounter);
        outputBuffer.append(s);

        nodeCounter++;
    }

	@Override public void exitE(ExprParser.EContext ctx) { 
        System.out.println("exitE");
    }

    @Override public void enterEveryRule(ParserRuleContext ctx) {
        System.out.println("enter every rule");
        
    }

    @Override public void exitEveryRule(ParserRuleContext ctx) {
        System.out.println("exit every rule");
    }

	@Override public void visitTerminal(TerminalNode node) {
        System.out.println("visitTerminal");

        String s = String.format("  %d [label=\"%s\"];\n", nodeCounter, node.getSymbol().getText());
        outputBuffer.append(s);

        int parentNodeCounter = contextToNodeCounter.get(node.getParent());
        s = String.format("  %d -- %d;\n", parentNodeCounter, nodeCounter);
        outputBuffer.append(s);

        nodeCounter++;
    }

    @Override public void visitErrorNode(ErrorNode node) { }
}