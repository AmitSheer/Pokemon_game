package gameClient2;

import Server.Game_Server_Ex2;
import api.*;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import gameClient2.util.Range;
import gameClient2.util.Range2D;
import gameClient2.util.Range2Range;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
        return  _pokemons.values();
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
        return dist>d1;
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
            Pokemon f = new Pokemon(i,v,g,t,null);
            ans.add(f);
        }
        return ans;
    }

    public static List<PokemonTrainer> getTrainers(String aa, directed_weighted_graph gg) {
        ArrayList<PokemonTrainer> ans = new ArrayList<PokemonTrainer>();
        try {
            JSONObject ttt = new JSONObject(aa);
            JSONArray ags = ttt.getJSONArray("Agents");
            for(int i=0;i<ags.length();i++) {
                PokemonTrainer c = new PokemonTrainer(0,gg);
                c.update(ags.get(i).toString());
                ans.add(c);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return ans;
    }

    private static Range2D GraphRange(directed_weighted_graph g) {
        Iterator<node_data> itr = g.getV().iterator();
        double x0=0,x1=0,y0=0,y1=0;
        boolean first = true;
        while(itr.hasNext()) {
            geo_location p = itr.next().getLocation();
            if(first) {
                x0=p.x(); x1=x0;
                y0=p.y(); y1=y0;
                first = false;
            }
            else {
                if(p.x()<x0) {x0=p.x();}
                if(p.x()>x1) {x1=p.x();}
                if(p.y()<y0) {y0=p.y();}
                if(p.y()>y1) {y1=p.y();}
            }
        }
        Range xr = new Range(x0,x1);
        Range yr = new Range(y0,y1);
        return new Range2D(xr,yr);
    }

    public static Range2Range w2f(directed_weighted_graph g, Range2D frame) {
        Range2D world = GraphRange(g);
        Range2Range ans = new Range2Range(world, frame);
        return ans;
    }


    public static void main(String[] args) {
        GameManager manager = new GameManager();
        game_service game_service = Game_Server_Ex2.getServer(2);
        manager.setPokemons(game_service.getPokemons());
    }


}
