package gameClient2;

import api.*;
import gameClient.Pokemon;
import gameClient.PokemonTrainer;
import gameClient_byProf.CL_Pokemon;

import java.util.Iterator;
import java.util.List;

/**
 * Manages the game, initializing graph, pokemon, pokemon trainers
 */
public class GameManager {
    private dw_graph_algorithms _algo;
    private directed_weighted_graph _graph;
    private List<Pokemon> _pokemons;
    private List<PokemonTrainer> _trainers;
    public GameManager(dw_graph_algorithms algo) {
        this._algo = algo;
    }

    public List<Pokemon> get_pokemons() {
        return _pokemons;
    }

    public void set_pokemons(List<Pokemon> pokemons) {
        this._pokemons = pokemons;
    }

    public dw_graph_algorithms get_algo() {
        return _algo;
    }

    public void set_algo(dw_graph_algorithms _algo) {
        this._algo = _algo;
    }

    public List<PokemonTrainer> get_trainers() {
        return _trainers;
    }

    public void set_trainers(List<PokemonTrainer> _trainers) {
        this._trainers = _trainers;
    }

    public directed_weighted_graph get_graph() {
        return _graph;
    }

    public void set_graph(directed_weighted_graph _graph) {
        this._graph = _graph;
    }
    /////////////////////////////////////////////////////////////////////////////////////

    public static void updateEdge(Pokemon fr, directed_weighted_graph g) {
        //	oop_edge_data ans = null;
        Iterator<node_data> itr = g.getV().iterator();
        while(itr.hasNext()) {
            node_data v = itr.next();
            Iterator<edge_data> iter = g.getE(v.getKey()).iterator();
            while(iter.hasNext()) {
                edge_data e = iter.next();
                boolean f = isOnEdge(fr.getLocation(), e,fr.getType(), g);
                if(f) {fr.setEdge(e);}
            }
        }
    }


    private static boolean isOnEdge(geo_location p, geo_location src, geo_location dest ) {

        boolean ans = false;
        double dist = src.distance(dest);
        double d1 = src.distance(p) + p.distance(dest);
        if(dist>d1-EPS2) {ans = true;}
        return ans;
    }
    private static boolean isOnEdge(geo_location p, int s, int d, directed_weighted_graph g) {
        geo_location src = g.getNode(s).getLocation();
        geo_location dest = g.getNode(d).getLocation();
        return isOnEdge(p,src,dest);
    }
    private static boolean isOnEdge(geo_location p, edge_data e, int type, directed_weighted_graph g) {
        int src = g.getNode(e.getSrc()).getKey();
        int dest = g.getNode(e.getDest()).getKey();
        if(type<0 && dest>src) {return false;}
        if(type>0 && src>dest) {return false;}
        return isOnEdge(p,src, dest, g);
    }
}
