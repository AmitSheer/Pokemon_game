package api;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.PriorityQueue;

class Dijkstra {
    /**
     * implementation of Dijkstra algorithm for weighted graph
     *
     * @param graph         to run on
     * @param start         start node
     * @param nodeKeyToFind key of the node to finish in
     */
    public void dijkstra(directed_weighted_graph graph, node_data start, Integer nodeKeyToFind) {
        PriorityQueue<node_data> a = new PriorityQueue<>(new CompareToForQueue());
        start.setWeight(0);
        HashSet<Integer> visited = new HashSet<>();
        a.add(start);
        while (a.size() > 0 && visited.size() != graph.nodeSize()) {
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
    }

    public void reset(Collection<node_data> nodes) {
        nodes.forEach(node -> {
            node.setTag(-1);
            node.setWeight(Integer.MAX_VALUE);
            node.setInfo(Integer.toString(node.getKey()));
        });
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
