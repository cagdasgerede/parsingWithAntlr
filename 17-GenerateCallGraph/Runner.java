import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;

public class Runner {
    public static void main(String[] args) throws Exception {
        CharStream input = CharStreams.fromStream(System.in);
        
        CymbolLexer lexer = new CymbolLexer(input);
        
        CommonTokenStream tokens = new CommonTokenStream(lexer);

        CymbolParser parser = new CymbolParser(tokens);

        ParseTree tree = parser.file();
        System.out.println("Parse Tree: " + tree.toStringTree(parser));

        System.out.println("------------------------------------------");

        ParseTreeWalker walker = new ParseTreeWalker();

        MyCymbolBaseListener listener = new MyCymbolBaseListener(); 
        walker.walk(listener, tree);

        System.out.println(listener.graph.toString());
        System.out.println(listener.graph.toDOT());
    }
}


class MyCymbolBaseListener extends CymbolBaseListener {
    static class Graph {
        Set<String> nodes = new OrderedHashSet<String>();
        MultiMap<String, String> edges = new MultiMap<String, String>();

        public void edge(String source, String target) {
            edges.put(source, target);
        }

        public String toDOT() {
            StringBuilder buf = new StringBuilder();
            buf.append("digraph G {\n");
            buf.append("    ranksep=.25;\n");
            buf.append("    edge [arrowsize=.5]\n");
            buf.append("    node [shape=circle, fontname=\"ArialNarrow\",\n");
            buf.append("            fontsize=12, fixedsize=true, height=.45];\n");
            buf.append("    ");
            for (String node : nodes) {
                buf.append(node);
                buf.append("; ");
            }
            buf.append("\n");
            for (String src : edges.keySet()) {
                for (String trg : edges.get(src)) {
                    buf.append(" ");
                    buf.append(src);
                    buf.append(" -> ");
                    buf.append(trg);
                    buf.append(";\n");
                }
            }

            buf.append("}\n");
            return buf.toString();
        }
    }

    Graph graph = new Graph();
    String currentFunctionName = null;


	@Override public void enterFunctionDecl(CymbolParser.FunctionDeclContext ctx) {
        currentFunctionName = ctx.ID().getText();
        graph.nodes.add(currentFunctionName);
    }

    @Override public void exitCall(CymbolParser.CallContext ctx) {
        String funcName =  ctx.ID().getText();
        graph.edge(currentFunctionName, funcName);
    }
}




class MultiMap<K, V>
{
	private Map<K, Collection<V>> map = new HashMap<>();

	/**
	* Add the specified value with the specified key in this multimap.
	*/
	public void put(K key, V value) {
		if (map.get(key) == null)
			map.put(key, new ArrayList<V>());

		map.get(key).add(value);
	}

	/**
	* Associate the specified key with the given value if not
	* already associated with a value
	*/
	public void putIfAbsent(K key, V value) {
		if (map.get(key) == null)
			map.put(key, new ArrayList<>());

		// if value is absent, insert it
		if (!map.get(key).contains(value)) {
			map.get(key).add(value);
		}
	}

	/**
	* Returns the Collection of values to which the specified key is mapped,
	* or null if this multimap contains no mapping for the key.
	*/
	public Collection<V> get(Object key) {
		return map.get(key);
	}

	/**
	* Returns a Set view of the keys contained in this multimap.
	*/
	public Set<K> keySet() {
		return map.keySet();
	}

	/**
	* Returns a Set view of the mappings contained in this multimap.
	*/
	public Set<Map.Entry<K, Collection<V>>> entrySet() {
		return map.entrySet();
	}

	/**
	* Returns a Collection view of Collection of the values present in
	* this multimap.
	*/
	public Collection<Collection<V>> values() {
		return map.values();
	}

	/**
	* Returns true if this multimap contains a mapping for the specified key.
	*/
	public boolean containsKey(Object key) {
		return map.containsKey(key);
	}

	/**
	* Removes the mapping for the specified key from this multimap if present
	* and returns the Collection of previous values associated with key, or
	* null if there was no mapping for key.
	*/
	public Collection<V> remove(Object key) {
		return map.remove(key);
	}

	/**
	* Returns the number of key-value mappings in this multimap.
	*/
	public int size() {
		int size = 0;
		for (Collection<V> value: map.values()) {
			size += value.size();
		}
		return size;
	}

	/**
	* Returns true if this multimap contains no key-value mappings.
	*/
	public boolean isEmpty() {
		return map.isEmpty();
	}

	/**
	* Removes all of the mappings from this multimap.
	*/
	public void clear() {
		map.clear();
	}

	/**
	* Removes the entry for the specified key only if it is currently
	* mapped to the specified value and return true if removed
	*/
	public boolean remove(K key, V value) {
		if (map.get(key) != null) // key exists
			return map.get(key).remove(value);

		return false;
	}

	/**
	* Replaces the entry for the specified key only if currently
	* mapped to the specified value and return true if replaced
	*/
	public boolean replace(K key, V oldValue, V newValue) {

		if (map.get(key) != null) {
			if (map.get(key).remove(oldValue))
				return map.get(key).add(newValue);
		}
		return false;
	}
}
