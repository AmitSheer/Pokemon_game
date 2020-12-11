package api;

import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

public class Tarjan {
    private static int[] ids;
    private static int[] lows;
    private static boolean[] onStack;
    private static Stack<Integer> stack;
    private static int id;

    public static List<Integer> getSccNodes() {
        return sccNodes;
    }

    private static List<Integer> sccNodes;
    public static int getSccCount() {
        return sccCount;
    }

    private static int sccCount;
    private static directed_weighted_graph graph;

    public static boolean init(directed_weighted_graph g) {
        sccNodes = new LinkedList<>();
        ids = new int[g.nodeSize()];
        lows = new int[g.nodeSize()];
        onStack = new boolean[g.nodeSize()];
        stack = new Stack<>();
        id = 0;
        sccCount = 0;
        graph = g;
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
        stack.push(node.getTag());
        onStack[node.getTag()] = true;
        for (edge_data to : graph.getE(node.getKey())) {
            node_data curr = graph.getNode(to.getDest());
            if (curr.getTag() == -1) {
                tarjan(curr);
                lows[node.getTag()] = Math.min(lows[node.getTag()], lows[curr.getTag()]);
            } else if (onStack[curr.getTag()])
                lows[node.getTag()] = Math.min(lows[node.getTag()], lows[curr.getTag()]);
        }
        if (ids[node.getTag()] == lows[node.getTag()]) {
            sccNodes.add(node.getKey());
            while (!stack.empty()) {
                int node_id = stack.pop();
                onStack[node_id] = false;
                lows[node_id] = ids[node_id];
                if (node_id == node.getTag()) break;
            }
            sccCount++;
        }
    }

//
//        public static boolean bfs(directed_weighted_graph graph, node_data start){
//            Queue<node_data> nodeDataQueue = new LinkedList<>();
//            HashSet<Integer> visited = new HashSet<>();
//            visited.add(start.getKey());
//            nodeDataQueue.add(start);
//            while(!nodeDataQueue.isEmpty()){
//                start = nodeDataQueue.remove();
//                //updates all of neighbors as visited
//                for (edge_data edge: graph.getE(start.getKey())) {
//                    if(!visited.contains(edge.getDest())) {
//                        nodeDataQueue.add(graph.getNode(edge.getDest()));
//                        visited.add(edge.getDest());
//                    }
//                }
//            }
//            return visited.size()==graph.nodeSize();
//        }

    public static void reset(directed_weighted_graph graph) {
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
