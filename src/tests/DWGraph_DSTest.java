package tests;

import api.NodeData;
import api.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Tag("All")
class DWGraph_DSTest extends BaseTest {

    @Test
    void addNode() {
        graph.addNode(new NodeData(0));
        node_data node = graph.getNode(0);
        assertEquals("0", node.getInfo());
        assertEquals(0, node.getKey());
    }
    // Connection Tests
    @Test
    void connect() {
        graph.addNode(new NodeData(0));
        graph.addNode(new NodeData(1));
        graph.connect(0,1,1);
        EdgeData edge = new EdgeData(0,1,1);
        assertEquals(edge,graph.getEdge(0,1));
    }

    @Test
    void connectWithTheSameID() {
        graph.addNode(new NodeData(0));
        graph.connect(0,0,1);
        assertNull(graph.getEdge(0, 0));
    }

    @Test
    void connectWithNegativeWeight() {
        graph.addNode(new NodeData(0));
        graph.addNode(new NodeData(1));
        graph.connect(0,1,-1);
        assertNull(graph.getEdge(0, 1));
    }

    @Test
    void connectNodesWithExistingConnection() {
        graph.addNode(new NodeData(0));
        graph.addNode(new NodeData(1));
        graph.connect(0,1,1);
        graph.connect(1,0,1);
        graph.connect(0,1,1);
        assertEquals(2,graph.edgeSize());
    }

    @Test
    void connectWithNonExistingNode() {
        graph.addNode(new NodeData(0));
        graph.connect(0,1,1);
        assertNull(graph.getEdge(0,1));
    }

    @Test
    void getEdge() {
        graph.addNode(new NodeData(0));
        graph.addNode(new NodeData(1));
        graph.connect(0,1,1);
        assertEquals(new EdgeData(0,1,1), graph.getEdge(0,1));
    }

    @Test
    void getNonExistingEdge() {
        graph.addNode(new NodeData(0));
        graph.addNode(new NodeData(1));
        assertNull(graph.getEdge(0,1));
    }

    @Test
    void getEdgeOfNonExistingNode() {
        graph.addNode(new NodeData(0));
        graph.addNode(new NodeData(1));
        graph.connect(0,1,1);
        assertNull(graph.getEdge(0,5));
    }

    @Test
    void getV() {
        graphCreator(1,10,0,1);
        for (int i = 0; i < 10; i++) {
            assertEquals(i,graph.getNode(i).getKey(),String.valueOf(i));
        }
    }

    @Test
    void GetAllConnectionToSpecificNode() {
        graphCreator(1, 10, 0, 1);
        graph.connect(0,5,1);
        graph.connect(0,2,1);
        assertEquals(2,graph.getE(0).size());
        Collection<edge_data> connections = graph.getE(0);
        assertEquals(graph.getEdge(0,5),connections.toArray()[1]);
        assertEquals(graph.getEdge(0,2),connections.toArray()[0]);
    }

    @Test
    void removeNode() {
        graphCreator(1, 10, 10, 1);
        Collection<edge_data> connections = graph.getE(0);
        graph.removeNode(0);
        for (edge_data edgeData : connections) {
            assertNull(graph.getEdge(0,edgeData.getDest()));
        }
        assertNull(graph.getNode(0));
        assertEquals(9, graph.edgeSize());
        assertEquals(9, graph.nodeSize());

    }

    @Test
    void removeFakeNode() {
        graphCreator(1, 10, 10, 1);
        graph.removeNode(19);
        assertNotNull(graph.getEdge(9,0));
        assertNotNull(graph.getNode(0));
        assertEquals(10, graph.edgeSize());
    }

    @Test
    void removeEdge() {
        graphCreator(1, 10, 10, 1);
        assertNotNull(graph.getEdge(9,0));
        graph.removeEdge(9,0);
        assertNull(graph.getEdge(9,0));
        assertEquals(9, graph.edgeSize());
    }

    @Test
    void removeNotExistingEdge() {
        graphCreator(1, 10, 10, 1);
        assertNull(graph.getEdge(0,5));
        graph.removeEdge(0,5);
        assertNull(graph.getEdge(0,5));
        assertEquals(10, graph.edgeSize());
    }

    @Test
    void removeNotExistingNodeEdge() {
        graphCreator(1, 10, 10, 1);
        assertNull(graph.getEdge(0,5));
        graph.removeEdge(0,11);
        assertNull(graph.getEdge(0,5));
        assertEquals(10, graph.edgeSize());
    }


    @Test
    void nodeSize() {
        graphCreator(1,10,10,1);
        assertEquals(10,graph.nodeSize());
        graph.removeNode(0);
        assertEquals(9,graph.nodeSize());
    }

    @Test
    void nodeSizeAfterDeletingFakeNode() {
        graphCreator(1,10,10,1);
        assertEquals(10,graph.nodeSize());
        graph.removeNode(20);
        assertEquals(10,graph.nodeSize());
    }

    @Test
    void nodeSizeAfterAddingNode() {
        graphCreator(1,10,10,1);
        assertEquals(10,graph.nodeSize());
        graph.addNode(new NodeData(20));
        assertEquals(11,graph.nodeSize());
    }

    @Test
    void edgeSize() {
        graphCreator(1,10,10,1);
        assertEquals(10,graph.edgeSize());
        graph.removeEdge(9,0);
        assertEquals(9,graph.edgeSize());
    }

    @Test
    void edgeSizeAfterDeleteFakeEdge() {
        graphCreator(1,10,10,1);
        assertEquals(10,graph.edgeSize());
        graph.removeEdge(0,11);
        assertEquals(10,graph.edgeSize());
    }

    @Test
    void getMC() {
        graphCreator(1,10,10,1);
        assertEquals(21,graph.getMC());
        graph.removeEdge(9,0);
        assertEquals(22,graph.getMC());
        graph.connect(0,5,1);
        assertEquals(23,graph.getMC());
        int mc = graph.getMC()+graph.getE(0).size()+1;
        graph.removeNode(0);
        assertEquals(mc,graph.getMC());
    }

}