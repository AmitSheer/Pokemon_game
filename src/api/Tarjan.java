package api;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

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
                tarjan(node);
        }
        return sccCount == 1;
    }

    private static void tarjan(node_data node) {
        node.setTag(id);
        ids[node.getTag()] = lows[node.getTag()] = node.getTag();
        id++;
        stack.push(node);
        onStack[node.getTag()] = true;
        for (edge_data to : graph.getE(node.getKey())) {
            node_data curr = graph.getNode(to.getDest());
            if (curr.getTag() == -1) {
                tarjan(curr);
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
