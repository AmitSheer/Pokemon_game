package gameClient;

import java.util.LinkedList;
import java.util.List;
import api.directed_weighted_graph;
import api.DWGraph_DS;

public class World {
    private List<Pokemon> _pokemons;
    private directed_weighted_graph _graph;

    public World(){
        _pokemons = new LinkedList<>();
        _graph = new DWGraph_DS();
    }

    public String getPokemons(){
        return _pokemons.toString();
    }
}
