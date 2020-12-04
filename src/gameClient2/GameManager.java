package gameClient2;

import Server.Game_Server_Ex2;
import api.*;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import gameClient2.Pokemon;
import gameClient2.PokemonTrainer;

import java.util.*;

/**
 * Manages the game, initializing graph, pokemon, pokemon trainers
 */
public class GameManager {
    private dw_graph_algorithms _algo;
    private directed_weighted_graph _graph;
    private HashMap<Integer,Pokemon> _pokemons;
    private List<PokemonTrainer> _trainers;

    public GameManager() {
        this._algo = new DWGraph_Algo();
    }

    public void init(int scenario){
        for (PokemonTrainer trainer :
                _trainers) {
        }

    }

    public Collection<Pokemon> getPokemons() {
        return _pokemons.values();
    }

    public void setPokemons(List<Pokemon> pokemons) {
        this._pokemons = new HashMap<>();
        pokemons.forEach(pokemon -> this._pokemons.putIfAbsent(pokemon.getKey(),pokemon));
    }

    public void setPokemons(String pokemons) {
        setPokemons(json2Pokemons(pokemons));
    }

    public dw_graph_algorithms getAlgo() {
        return _algo;
    }

    public void setAlgo(dw_graph_algorithms _algo) {
        this._algo = _algo;
    }

    public List<PokemonTrainer> getTrainers() {
        return _trainers;
    }

    public void setTrainers(List<PokemonTrainer> _trainers) {
        this._trainers = _trainers;
    }
    public void setTrainers(String _trainers) {
    }
    public void setTrainers(int numOfTrainers) {
        for (int i = 0; i < _pokemons.size(); i++) {

        }
    }

    public directed_weighted_graph getGraph() {
        return _graph;
    }

    public void setGraph(directed_weighted_graph _graph) {
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
        if(dist>d1) {ans = true;}
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

    public static ArrayList<Pokemon> json2Pokemons(String fs) {
        ArrayList<Pokemon> ans = new  ArrayList<Pokemon>();
        JsonArray allPokemons = JsonParser.parseString(fs).getAsJsonObject().getAsJsonArray("Pokemons");
        for(int i=0;i<allPokemons.size();i++) {
            JsonObject pp = allPokemons.get(i).getAsJsonObject();
            JsonObject pk = pp.get("Pokemon").getAsJsonObject();
            int t = pk.get("type").getAsInt();
            double v = pk.get("value").getAsDouble();
            String [] p = pk.get("pos").getAsString().split(",");
            geo_location g = new GeoLocations(Double.parseDouble(p[0]),Double.parseDouble(p[1]),Double.parseDouble(p[2]));
            Pokemon f = new Pokemon(i,v,g,t);
            ans.add(f);
        }
        return ans;
    }



    public static void main(String[] args) {
        GameManager manager = new GameManager();
        game_service game_service = Game_Server_Ex2.getServer(2);
        manager.setPokemons(game_service.getPokemons());
    }


}
