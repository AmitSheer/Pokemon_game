package api;

import java.util.*;

/**
 * implementation of Tarjan algorithm
 */
public class Tarjan {
    private static int[] ids;
    private static int[] lows;
    private static boolean[] onStack;
    private static int id;
    private static Stack<node_data> stack;
    private static List<List<Integer>> sccNodes;
    private static int sccCount;
    private static directed_weighted_graph graph;
    private static List<List<Integer>> _scc;
    private static List<Integer> _foundScc;
    private static int index;


    public static List<List<Integer>> getSccNodes() {
        return sccNodes;
    }

    public static boolean init(directed_weighted_graph g) {
        sccNodes = new LinkedList<>();
        ids = new int[g.nodeSize()];
        lows = new int[g.nodeSize()];
        onStack = new boolean[g.nodeSize()];
        stack = new Stack<>();
        id = 0;
        sccCount = 0;
        graph = g;
        reset(graph);
        if (g.nodeSize() == 0) return true;
        for (node_data node : g.getV()) {
            if (node.getTag() == -1)
                dfs(node);
        }
        return sccCount == 1;
    }

    public static List<Integer> connectedComponent(directed_weighted_graph g, node_data node) {
        Hashtable<Integer,Integer> ids = new Hashtable<Integer,Integer>();
        Hashtable<Integer,Integer> lows = new Hashtable<Integer,Integer>();
        _scc = new ArrayList<List<Integer>>();
        _foundScc = new ArrayList<Integer>();
        stack = new Stack<>();
        graph = g;
        index = 0;
        reset(graph);
        if (g.nodeSize() == 0) return new ArrayList<>();
        dfsNonRecursive(g, node,lows,ids);
        for (List<Integer> scc: _scc) {
            if(scc.contains(node.getKey()))
                return scc;
        }
        return new ArrayList<Integer>();
    }

    public static List<List<Integer>> connectedComponents(directed_weighted_graph g) {
        Hashtable<Integer,Integer> ids = new Hashtable<Integer,Integer>();
        Hashtable<Integer,Integer> lows = new Hashtable<Integer,Integer>();
        _scc = new ArrayList<List<Integer>>();
        _foundScc = new ArrayList<Integer>();
        stack = new Stack<>();
        graph = g;
        index = 0;
        reset(graph);
        if (g.nodeSize() == 0) return new ArrayList<>();
        for (node_data node : g.getV()) {
            if (!_foundScc.contains(node.getKey()))
                dfsNonRecursive(graph, node, lows, ids);
        }
        return _scc;
    }

    private static void dfsNonRecursive(directed_weighted_graph g, node_data node, Hashtable<Integer,Integer> lows, Hashtable<Integer,Integer> ids){
        Stack<Integer> stack = new Stack<>();
        Hashtable<Integer,List<Integer>> scc = new Hashtable<Integer, List<Integer>>();
        stack.push(node.getKey());
        while(!stack.isEmpty()){
            int n = stack.peek();
            if(!ids.containsKey(n)){
                ids.put(n, index);
                lows.put(n, index);
                List<Integer> l =  new ArrayList<>();
                l.add(n);
                scc.put(index,l);
                index += 1;
            }
            boolean recursive = true;
            for (edge_data dest: g.getE(n)) {
                if(!ids.containsKey(dest.getDest())){
                    stack.push(dest.getDest());
                    recursive = false;
                    break;
                }
            }
            if (recursive){
                int low = lows.get(n);
                for(edge_data dest: g.getE(n)){
                    if (!_foundScc.contains(dest.getDest())){
                        lows.replace(n, Math.min(lows.get(n), lows.get(dest.getDest())));
                    }
                }
                stack.pop();
                if(lows.get(n).equals(ids.get(n))){
                    _scc.add(scc.get(lows.get(n)));
                    _foundScc.addAll(scc.get(lows.get(n)));
                }else{
                    if(!scc.containsKey(lows.get(n))){
                        List<Integer> l =  new ArrayList<>();
                        l.add(n);
                        scc.put(n,l);
                    }
                    List<Integer> temp = scc.get(lows.get(n));
                    for (Integer key: scc.get(low)) {
                        if(!temp.contains(key))
                            temp.add(key);
                    }
                    for(int key: scc.get(low)){
                        lows.replace(key, lows.get(n));
                    }
                }
            }
        }
    }

    private static void dfs(node_data node) {
        node.setTag(id);
        ids[node.getTag()] = lows[node.getTag()] = node.getTag();
        id++;
        stack.push(node);
        onStack[node.getTag()] = true;
        for (edge_data to : graph.getE(node.getKey())) {
            node_data curr = graph.getNode(to.getDest());
            if (curr.getTag() == -1) {
                dfs(curr);
                lows[node.getTag()] = Math.min(lows[node.getTag()], lows[curr.getTag()]);
            } else if (onStack[curr.getTag()])
                lows[node.getTag()] = Math.min(lows[node.getTag()], lows[curr.getTag()]);
        }
        List<Integer> components = new ArrayList<>();
        if (ids[node.getTag()] == lows[node.getTag()]) {
            while (!stack.empty()) {
                node_data nodeFromStack = stack.pop();
                components.add(nodeFromStack.getKey());
                onStack[nodeFromStack.getTag()] = false;
                lows[nodeFromStack.getTag()] = ids[nodeFromStack.getTag()];
                if (nodeFromStack.getTag() == node.getTag()) break;
            }
            sccNodes.add(components);
            sccCount++;
        }
    }

    private static void reset(directed_weighted_graph graph) {
        graph.getV().forEach(node -> {
            node.setTag(-1);
            node.setWeight(0);
            node.setInfo(Integer.toString(node.getKey()));
            for (edge_data edge :
                    graph.getE(node.getKey())) {
                edge.setTag(0);
            }
        });
    }
}
