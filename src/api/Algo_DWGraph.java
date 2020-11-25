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
        }
        Iterator<node_data> nodeIterator = this.graph.getV().iterator();
        while (copiedGraph.edgeSize()!=graph.edgeSize()&&nodeIterator.hasNext()){
            int nodeKey = nodeIterator.next().getKey();
            //will connect all of the nodes as they were connected, the first connection wont
            for (edge_data edge: graph.getE(nodeKey)) {
                copiedGraph.connect(edge.getSrc(),edge.getDest(),edge.getWeight());
            }
        }
        return copiedGraph;
    }

    @Override
    public boolean isConnected() {
        BFS.reset(graph.getV());
        int sum = this.graph.getV().stream().mapToInt(node -> BFS.bfs(this.graph, node)).sum();
        return sum!=(this.graph.nodeSize()*(this.graph.nodeSize()-1));
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
                if (o1.getTag() == o2.getTag())
                    return 0;
                else if (o1.getTag() < o2.getTag())
                    return -1;
                return 1;
            }
        }
    }

    static class BFS{
        public static int bfs(directed_weighted_graph graph, node_data start){
            Queue<node_data> nodeDataQueue = new LinkedList<>();
            start.setTag(0);
            nodeDataQueue.add(start);
            int sum = 0;
            int currentStart = start.getTag();
            start.setTag(start.getTag()+1);
            while(!nodeDataQueue.isEmpty()){
                start = nodeDataQueue.remove();
                //updates all of neighbors as visited
                for (edge_data edge: graph.getE(start.getKey())) {
                    node_data n = graph.getNode(edge.getDest());
                    if(n.getTag()<currentStart) {
                        n.setTag(start.getTag() + 1);
                        n.setInfo(start.getInfo()+","+n.getInfo());
                        nodeDataQueue.add(n);
                        sum++;
                    }
                }
            }
            return sum;
        }

        public static void reset(Collection<node_data> nodes) {
            nodes.forEach(node -> {node.setTag(-1);node.setWeight(0); node.setInfo(Integer.toString(node.getKey()));});
        }
    }
}
