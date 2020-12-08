package gameClient2.GameLogic;


import api.edge_data;

import java.util.Comparator;

public class CompareToEdge implements Comparator<edge_data> {
    @Override
    public int compare(edge_data o1, edge_data o2) {
        if (o1.getWeight() == o2.getWeight())
            return 0;
        else if (o1.getWeight() < o2.getWeight())
            return -1;
        return 1;
    }
}