import java.util.Hashtable;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

public class Runner {
    public static void main(String[] args) throws Exception {
        CharStream input = CharStreams.fromStream(System.in);
        
        // Grammar file is Java.g4. That's why we have JavaLexer
        JavaLexer lexer = new JavaLexer(input);
        
        CommonTokenStream tokens = new CommonTokenStream(lexer);

        // Grammar file is Java.g4. That's why we have JavaParser
        JavaParser parser = new JavaParser(tokens);

        // Grammar start symbol is compilationUnit. That's why we have a method compilationUnit().
        ParseTree tree = parser.compilationUnit();
        // System.out.println("Parse Tree: " + tree.toStringTree(parser));

        ParseTreeWalker walker = new ParseTreeWalker();

        // Grammar file is Java.g4. That's why we have JavaBaseListener
        MyJavaBaseListener listener = new MyJavaBaseListener(tokens); 
        walker.walk(listener, tree);

        System.out.println("------------------------");
        System.out.println(listener.rewriter.getText());
    }
}

class MyJavaBaseListener extends JavaBaseListener {
    TokenStreamRewriter rewriter;

    public MyJavaBaseListener(TokenStream tokens) {
        rewriter = new TokenStreamRewriter(tokens);
    }

    @Override public void enterNonVoidMethod(JavaParser.NonVoidMethodContext ctx) {
        lintMethodName(ctx.Identifier());
    }

	@Override public void enterVoidMethod(JavaParser.VoidMethodContext ctx) {
        lintMethodName(ctx.Identifier());
    }
    
	@Override public void enterConstructorMethod(JavaParser.ConstructorMethodContext ctx) {
        lintMethodName(ctx.Identifier());
    }

    void lintMethodName(TerminalNode node) {
        String result = analyze(node.getText());
        if (!result.isEmpty()) {
            rewriter.insertBefore(node.getSymbol(), result);
        }
    }

    String analyze(String methodName) {
        String camelCasePattern = "([a-z]+[A-Z]+\\w+)+";
        if (methodName.matches(camelCasePattern)) {
            return "/* TODO: Change to snake case */ ";
        }
        return "";
    }
}