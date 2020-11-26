package api;

import java.util.*;


public class Algo_DWGraph implements dw_graph_algorithms {
    private directed_weighted_graph graph;
    @Override
    public void init(directed_weighted_graph g) {
        graph = g;
    }

    @Override
    public directed_weighted_graph getGraph() {
        return graph;
    }

    @Override
    public directed_weighted_graph copy() {
        directed_weighted_graph copiedGraph = new DWGraph_DS();
        //copy all nodes into new nodes, with new pointers
        for (node_data node : graph.getV()) {
            copiedGraph.addNode(new NodeData(node.getKey()));
            for (edge_data edge: graph.getE(node.getKey())) {
                if (copiedGraph.getNode(edge.getDest())!=null){
                    copiedGraph.connect(edge.getSrc(),edge.getDest(),edge.getWeight());
                }else{
                    copiedGraph.addNode(new NodeData(edge.getDest()));
                    copiedGraph.connect(edge.getSrc(),edge.getDest(),edge.getWeight());
                }
            }
        }
        return copiedGraph;
    }

    @Override
    public boolean isConnected() {
        BFS.reset(graph);
//        for (node_data node :
//                this.graph.getV()) {
//            if(!BFS.bfs(this.graph, node))return false;
//        }

        return BFS.init(this.graph);
    }

    @Override
    public double shortestPathDist(int src, int dest) {
        if (this.graph.getV().size() == 0 || this.graph.getNode(src) == null || this.graph.getNode(src) == null) {
            return -1;
        }
        Dijkstra.reset(this.graph.getV());
        Dijkstra.dijkstra(this.graph, graph.getNode(src), dest);
        if (this.graph.getNode(dest).getWeight() == Integer.MAX_VALUE){
            return -1;
        }else{
            return this.graph.getNode(dest).getWeight();
        }
    }

    @Override
    public List<node_data> shortestPath(int src, int dest) {
        List<node_data> nodePathToDest = new LinkedList<>();
        Dijkstra.reset(graph.getV());
        Dijkstra.dijkstra(this.graph,this.graph.getNode(src),dest);
        String [] strPath = this.graph.getNode(dest).getInfo().split(",");
        for (String nodeKey : strPath) {
            nodePathToDest.add(this.graph.getNode(Integer.parseInt(nodeKey)));
        }
        return nodePathToDest;    }

    @Override
    public boolean save(String file) {
        return false;
    }

    @Override
    public boolean load(String file) {
        return false;
    }


    static class Dijkstra {
        /**
         * implementation of Dijkstra algorithm for weighted graph
         * @param graph to run on
         * @param start start node
         * @param nodeKeyToFind key of the node to finish in
         * @return the number of nodes visited
         */
        public static int dijkstra(directed_weighted_graph graph, node_data start, Integer nodeKeyToFind) {
            PriorityQueue<node_data> a = new PriorityQueue<>(new CompareToForQueue());
            start.setWeight(0);
            HashSet<Integer> visited = new HashSet<>();
            a.add(start);
            while (a.size() > 0 && visited.size()!= graph.nodeSize()) {
                node_data curr = a.remove();
                if (!visited.contains(curr.getKey())) {
                    visited.add(curr.getKey());
                    if (nodeKeyToFind == curr.getKey()) break;
                    //calculates the new value of the distance according to the distance from current node to its connected nodes
                    for (edge_data edge : graph.getE(curr.getKey())) {
                        if ((curr.getWeight() + edge.getWeight()) < graph.getNode(edge.getDest()).getWeight()) {
                            graph.getNode(edge.getDest()).setWeight(curr.getWeight() + edge.getWeight());
                            graph.getNode(edge.getDest()).setInfo(curr.getInfo() + "," + edge.getDest());
                            a.add(graph.getNode(edge.getDest()));
                        }
                    }
                }
            }
            return visited.size();
        }

        public static void reset(Collection<node_data> nodes) {
            nodes.forEach(node -> {node.setTag(-1);node.setWeight(Integer.MAX_VALUE); node.setInfo(Integer.toString(node.getKey()));});

        }
        /**
         * comparator for the priority queue used in the dijkstra function
         */
        static class CompareToForQueue implements Comparator<node_data> {
            @Override
            public int compare(node_data o1, node_data o2) {
                if (o1.getWeight() == o2.getWeight())
                    return 0;
                else if (o1.getWeight() < o2.getWeight())
                    return -1;
                return 1;
            }
        }
    }

    static class BFS{
        static int[] ids;
        static int[] lows;
        static boolean [] onStack;
        static Stack<Integer> stack;
        static int id;
        static int sccCount;
        static directed_weighted_graph graph;

        public static boolean init(directed_weighted_graph g){
            ids = new int[g.nodeSize()];
            lows  =new int[g.nodeSize()];
            onStack = new boolean[g.nodeSize()];
            stack = new Stack();
            id = 0;
            sccCount = 0;
            graph = g;
            int i = 0;
            for (node_data node : g.getV()) {
                if(node.getTag()==-1) dfs(node);
                if(sccCount>0&&id<(g.nodeSize()-1)) return false;
                i++;
            }
            return true;
        }

        private static void dfs(node_data node) {
            node.setTag(id);
            ids[node.getTag()] = lows[node.getTag()] = node.getTag();
            id++;
            stack.push(node.getTag());
            onStack[node.getTag()] = true;
            for (edge_data to : graph.getE(node.getKey())) {
                node_data curr = graph.getNode(to.getDest());
                if(curr.getTag()==-1) {
                    dfs(curr);
                    lows[node.getTag()] = Math.min(lows[node.getTag()],lows[curr.getTag()]);
                }else if(onStack[curr.getTag()])
                    lows[node.getTag()] = Math.min(lows[node.getTag()],lows[curr.getTag()]);
            }
            if(ids[node.getTag()]==lows[node.getTag()]){
                while(!stack.empty()){
                    int node_id = stack.pop();
                    onStack[node_id] = false;
                    lows[node_id] = ids[node_id];
                    if(node_id==node.getTag()) break;
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
            graph.getV().forEach(node -> {node.setTag(-1);node.setWeight(0); node.setInfo(Integer.toString(node.getKey()));
                for (edge_data edge :
                       graph.getE(node.getKey()) ) {
                    edge.setTag(0);
                }});
        }
    }
}
