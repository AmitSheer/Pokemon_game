package gameClient2.GameLogic;

import api.node_data;
import api.*;
import java.util.List;
import java.util.PriorityQueue;
import java.util.concurrent.Callable;

public class PathFinder implements Callable<Void> {
    public static PriorityQueue<TrainerToPath> trainersToPokemonsDist = new PriorityQueue<>(new CompareToEdge());
    private PokemonTrainer _trainer;
    private Pokemon _pokemons;
    private dw_graph_algorithms _algo;
    public PathFinder(PokemonTrainer trainer, Pokemon pokemon, directed_weighted_graph graph) {
        _trainer = trainer;
        _pokemons= pokemon;
        _algo = new DWGraph_Algo();
        _algo.init(graph);
    }

    @Override
    public Void call() throws Exception {
            List<node_data> path = this._algo.shortestPath(_trainer.get_curr_node(), _pokemons.getEdge().getSrc());
            path.add(this._algo.getGraph().getNode(_pokemons.getEdge().getDest()));
            double dist = this._algo.getGraph().getNode(_pokemons.getEdge().getSrc()).getWeight();// + this._pokemons.get(i).getEdge().getWeight();
            trainersToPokemonsDist.add(new TrainerToPath(_trainer.getID(), _pokemons.getEdge().getSrc(), dist, path, _pokemons.get_id()));
        return null;
    }
}
