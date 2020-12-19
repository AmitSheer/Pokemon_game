package GameLogic;

import api.Tarjan;
import api.edge_data;
import api.node_data;

import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;

/**
 * finds the shortest path to Pokemon
 */
public class ShortestPathAlgo {
    /**
     * Find the shortest path to Pokemon
     * by taking into account pokemons on same graph and such
     * @param _gm to run on
     */
    public static synchronized void findShortestForAgents(GameManager _gm){
        PriorityQueue<Game.TrainerToPath> trainersToPokemonsDist = new PriorityQueue<>(new Game.CompareToEdge());
        boolean isConnected = _gm.getAlgo().isConnected();
        //gets all distance from point Trainer to Pokemon and adds it to PriorityQueue
        for (int i = 0; i < _gm.getPokemons().size(); i++) {
            for (PokemonTrainer trainer : _gm.getTrainers()) {
                List<node_data> path;
                int nodeToCalculatePathFrom;
                //Check if Trainer is on edge
                if(trainer.get_curr_edge()==null){
                    nodeToCalculatePathFrom = trainer.get_curr_node();
                }else {
                    nodeToCalculatePathFrom = trainer.get_dest();
                }
                path = _gm.getAlgo().shortestPath(nodeToCalculatePathFrom,_gm.getPokemons().get(i).getEdge().getSrc());
                path.add(_gm.getGraph().getNode(_gm.getPokemons().get(i).getEdge().getDest()));
                double dist = _gm.getGraph().getNode(_gm.getPokemons().get(i).getEdge().getSrc()).getWeight() + _gm.getPokemons().get(i).getEdge().getWeight();
                if(isConnected){
                    trainersToPokemonsDist.add(new Game.TrainerToPath(trainer.getID(), _gm.getPokemons().get(i).getEdge().getSrc(), dist, path, _gm.getPokemons().get(i)));
                }else{
                    _gm.getAlgo().isConnected();
                    for (int j = 0; j < Tarjan.getSccNodes().size(); j++) {
                        if(Tarjan.getSccNodes().get(j).contains(nodeToCalculatePathFrom)
                                &&Tarjan.getSccNodes().get(j).contains(
                                _gm.getPokemons().get(i).getEdge().getDest())){
                            trainersToPokemonsDist.add(new Game.TrainerToPath(trainer.getID(), _gm.getPokemons().get(i).getEdge().getSrc(), dist, path, _gm.getPokemons().get(i)));
                            break;
                        }
                    }
                }
            }
        }

        HashSet<Integer> trainersFilled = new HashSet<>();
        HashSet<Pokemon> pokemonFilled = new HashSet<>();
        HashSet<edge_data> edges = new HashSet<>();


        Game.TrainerToPath trainerToPokemon = null;
        while(pokemonFilled.size()< _gm.getPokemons().size()&&trainersFilled.size()< _gm.getTrainers().size()&&trainersToPokemonsDist.size()!=0){
            trainerToPokemon  = trainersToPokemonsDist.remove();
            //check if the trainer already has a new path or not
            //check if pokemon is already chosen
            //check if the queue isn't empty
            //check if the distance is not max Integer, meaning the graph is not scc and trainer cant visit pokemon
            //check if a chosen pokemon is on the same edge as another pokemon
            while((trainersFilled.contains(trainerToPokemon.getSrc())||pokemonFilled.contains(trainerToPokemon.get_pokemon())||
                    trainerToPokemon.getWeight()==Integer.MAX_VALUE|| edges.contains(trainerToPokemon.get_pokemon().getEdge()))&&trainersToPokemonsDist.size()>0){
                try{
                    trainerToPokemon = trainersToPokemonsDist.remove();
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
            trainersFilled.add(trainerToPokemon.getSrc());
            pokemonFilled.add(trainerToPokemon.get_pokemon());
            edges.add(trainerToPokemon.get_pokemon().getEdge());
            _gm.updateTrainerPath(trainerToPokemon.get_path(),trainerToPokemon.getSrc());
            _gm.getTrainer(trainerToPokemon.getSrc()).setNextPoke(trainerToPokemon.get_pokemon());
        }
    }
}
