package tests;


import api.DWGraph_DS;
import api.NodeData;
import api.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import java.io.*;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BaseTest {
    static DWGraph_DS graph;
    static FileOutputStream f;

    @BeforeAll
    static void mainSetup() throws IOException {
//        File file = new File(path+"/writeOver.json");
//        file.delete();
//        file.createNewFile();
//        file = new File(path+ "/writeOver.txt");
//        file.delete();
//        file.createNewFile();
    }

    @BeforeEach
    void setUp() {
        graph = new DWGraph_DS();
    }

    public void graphCreator(int seed, int size, int edges, int weight){
        Random rnd = new Random(seed);
        Random rndWeight = new Random(weight);
        Random keyRandom = new Random(seed);
        for (int i = 0; i < size; i++) {
            NodeData n = new NodeData(i);
            graph.addNode(n);
        }
        int key,key2;
        while(graph.edgeSize()< edges) {
            key = keyRandom.nextInt(size);
            key2 = keyRandom.nextInt(size);
            graph.connect(key2, key, rndWeight.nextDouble());
        }
    }
}
