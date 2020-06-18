import java.util.Hashtable;
import java.util.Stack;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

public class Runner {
    public static void main(String[] args) throws Exception {
        CharStream input = CharStreams.fromStream(System.in);
        
        JSONLexer lexer = new JSONLexer(input);
        
        CommonTokenStream tokens = new CommonTokenStream(lexer);

        JSONParser parser = new JSONParser(tokens);

        ParseTree tree = parser.json();
        // System.out.println("Parse Tree: " + tree.toStringTree(parser));

        System.out.println("------------------------------------------");

        ParseTreeWalker walker = new ParseTreeWalker();

        MyJSONBaseListener listener = new MyJSONBaseListener(); 
        walker.walk(listener, tree);
    }
}


class MyJSONBaseListener extends JSONBaseListener {
    ParseTreeProperty<String> properties = new ParseTreeProperty<String>();
    String getXML(ParseTree ctx) { return properties.get(ctx); }
    void setXML(ParseTree ctx, String s) { properties.put(ctx, s); }

	@Override public void exitAtom(JSONParser.AtomContext ctx) {
        setXML(ctx, ctx.getText());
    }

    @Override public void exitString(JSONParser.StringContext ctx) {
        setXML(ctx, stripQuotes(ctx.getText()));
    }

    @Override public void exitObjectValue(JSONParser.ObjectValueContext ctx) {
        setXML(ctx, getXML(ctx.obj()));
    }

    @Override public void exitArrayValue(JSONParser.ArrayValueContext ctx) {
        setXML(ctx, getXML(ctx.arr()));
    }

    @Override public void exitPair(JSONParser.PairContext ctx) {
        String tag = stripQuotes(ctx.STRING().getText());
        JSONParser.ValueContext vctx = ctx.value();
        String x = String.format("<%s>%s</%s>\n", tag, getXML(vctx), tag);
        setXML(ctx, x);
    }

    @Override public void exitAnObject(JSONParser.AnObjectContext ctx) {
        StringBuilder buf = new StringBuilder();
        buf.append("\n");
        for (JSONParser.PairContext pctx : ctx.pair()) {
            buf.append(getXML(pctx));
        }
        setXML(ctx, buf.toString());
    }

    @Override public void exitEmptyObject(JSONParser.EmptyObjectContext ctx) {
        setXML(ctx, "");
    }

    @Override public void exitArrayOfValues(JSONParser.ArrayOfValuesContext ctx) {
        StringBuilder buf = new StringBuilder();
        buf.append("\n");
        for (JSONParser.ValueContext vctx : ctx.value()) {
            buf.append("<element>");
            buf.append(getXML(vctx));
            buf.append("</element>");
            buf.append("\n");
        }

        setXML(ctx, buf.toString());
    }

    @Override public void exitJson(JSONParser.JsonContext ctx) {
        System.out.println(getXML(ctx.value()));
    }

    String stripQuotes(String str) {
        return str.replaceAll("^\"|\"$", "");
    }
}

