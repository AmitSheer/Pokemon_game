package api;

import java.util.*;
import api.*;

public class DWGraph_DS implements directed_weighted_graph{
    //Holds the nodes in order to have access to the nodes in O(1)
    private final HashMap<Integer,node_data> nodes;
    //counts the number of edges
    private final HashMap<Integer,HashMap<Integer,edge_data>> edges;
    //[out,in] connections
    private final HashMap<Integer,List<Integer>> edgesIds;
    //counts the numbers of changes in graph
    private Integer mc;
    private Integer edgeCount;

    /**
     * init
     */
    public DWGraph_DS(){
        this.nodes = new HashMap<Integer, node_data>();
        this.edges  = new HashMap<>();
        this.edgesIds  = new HashMap<>();
        this.mc = 0;
        this.edgeCount = 0;
    }

    /**
     * returns nodes data with specific key
     * @param key - the node_id
     * @return
     */
    @Override
    public node_data getNode(int key) {
        return this.nodes.get(key);
    }


    /**
     * gets the value of an edge between two nodes
     * @param src node key
     * @param dest node key
     * @return the value of the edge
     */
    @Override
    public edge_data getEdge(int src, int dest) {
        return this.edges.get(src).get(dest);
    }

    @Override
    public void addNode(node_data n) {
        if(!this.nodes.containsKey(n.getKey()))
            this.mc++;
        this.nodes.putIfAbsent(n.getKey(),n);
        this.edgesIds.putIfAbsent(n.getKey(),new LinkedList<>());
        this.edgesIds.get(n.getKey()).add(-1);
        this.edges.putIfAbsent(n.getKey(),new HashMap<>());
    }

    /**
     * creates new connection between nodes
     * @param src
     * @param dest
     * @param w weight of an edge
     */
    @Override
    public void connect(int src, int dest, double w) {
        if (w>=0&&src!=dest&&this.nodes.containsKey(src)&&this.nodes.containsKey(dest)) {
            //checks if this is a new edge
            if(this.getEdge(src,dest) == null){
                this.edgeCount++;
                this.edgesIds.get(src).add(dest);
                this.edgesIds.get(dest).add(this.edgesIds.get(dest).size(),src);
                this.edges.get(src).put(dest,new EdgeData(src,dest,w));
            }else if(w!=this.getEdge(src, dest).getWeight()){
                this.mc++;
                this.edges.get(src).put(dest,new EdgeData(src,dest,w));
            }
        }
    }

    /**
     *
     * @return all of the nodes in O(1)
     */
    @Override
    public Collection<node_data> getV() {
        return this.nodes.values();
    }

    @Override
    public Collection<edge_data> getE(int node_id) {
        return this.edges.get(node_id).values();
    }

    /**
     * deletes specific node from graph
     * and removes all of the edges associated with it
     * @param key of node to remove
     * @return the removed node
     */
    @Override
    public node_data removeNode(int key) {
        node_data node = nodes.get(key);
        try{
            Collection<edge_data> ni  = this.getE(key);
            //remove all connected edges
            while(ni.iterator().hasNext()){
                edge_data edge = ni.iterator().next();
                removeEdge(edge.getSrc(), edge.getDest());
            }
            for (HashMap<Integer, edge_data> edge: this.edges.values()) {
                if(edge.containsKey(key))
                    this.removeEdge(edge.get(key).getSrc(),key);
            }
            //remove node from value list, and nodes
            this.nodes.remove(key);
            this.mc++;
        }catch(Exception ignore){
        }
        return node;
    }

    /**
     * delete an edge from node
     * @param src
     * @param dest
     */
    @Override
    public edge_data removeEdge(int src, int dest) {
        edge_data edgeData = null;
        if (src!=dest&&this.nodes.containsKey(src)&&this.nodes.containsKey(dest)) {
                if(this.edges.get(src).containsKey(dest)) {
                    edgeData = this.edges.get(src).remove(dest);
                    this.edgesIds.get(src).remove(dest);
                    this.edgesIds.get(dest).remove(src);
                    this.edgeCount--;
                    this.mc++;
                }
        }
        return edgeData;
    }

    @Override
    public int nodeSize() {
        return this.nodes.size();
    }

    @Override
    public int edgeSize() {
        return this.edgeCount;
    }

    @Override
    public int getMC() {
        return this.mc;
    }

    @Override
    public String toString() {
        return "DWGraph_DS{" +
                "nodes=" + nodes.toString() +
                ", edgeCount=" + edgeCount +
                ", mc=" + mc +
                '}';
    }

}
