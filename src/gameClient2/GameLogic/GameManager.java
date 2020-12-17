package gameClient2.GameLogic;

import api.*;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import gameClient2.gui.GameStatus;
import gameClient2.util.Range;
import gameClient2.util.Range2D;
import gameClient2.util.Range2Range;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Manages the game, initializing graph, pokemon, pokemon trainers
 */
public class GameManager {
    public static final double EPS1 = 0.001, EPS2=EPS1*EPS1, EPS=EPS2;
    private dw_graph_algorithms _algo;
    private directed_weighted_graph _graph;
    private HashSet<Pokemon> _pokemons;
    //used hash map for easy and fast access to trainers
    private static HashMap<Integer, PokemonTrainer> _trainers;
    private GameStatus _gs;
    private String time ="";

    public GameManager() {
        this._algo = new DWGraph_Algo();
        this._trainers = new HashMap<>();
        _gs = new GameStatus();
        _graph = new DWGraph_DS();
        _pokemons = new HashSet<>();
    }

    /**
     *
     * @return time until game ends
     */
    public String getTime() {
        return time;
    }

    /**
     * sets time until game ends
     * @param time to set
     */
    public void setTime(Date time) {
        this.time = time.getMinutes()+":"+time.getSeconds();
    }

    /**
     * updates the path of a specific trainer
     * @param pathToPokemon the path to put
     * @param trainerId trainer id
     */
    public void updateTrainerPath(List<node_data> pathToPokemon, int trainerId){
        _trainers.get(trainerId).setPathToPokemon(pathToPokemon);
    }

    /**
     *
     * @return all pokemons on graph
     */
    public List<Pokemon> getPokemons() {
        return  _pokemons.stream().collect(Collectors.toUnmodifiableList());
    }

    /**
     * sets a new HashSet of Pokemons
     * @param pokemons
     */
    public void setPokemons(HashSet<Pokemon> pokemons) {
        _pokemons = pokemons;
        _pokemons.forEach(pokemon -> updateEdge(pokemon,this.getGraph()));
    }

    /**
     * sets a new HashSet of Pokemons from string
     * @param pokemons
     */
    public void setPokemons(String pokemons) {
        setPokemons(json2Pokemons(pokemons,_graph));
    }

    /**
     *
     * @return current algo used
     */
    public dw_graph_algorithms getAlgo() {
        return _algo;
    }

    /**
     * sets a new algo to be used
     * @param _algo
     */
    public void setAlgo(dw_graph_algorithms _algo) {
        this._algo = _algo;
    }

    /**
     * gets the trainers
     * @return returns the trainers as List
     */
    public List<PokemonTrainer> getTrainers() {
        return _trainers.values().stream().collect(Collectors.toUnmodifiableList());
    }

    /**
     *
     * @param id trainer id
     * @return specific trainer
     */
    public PokemonTrainer getTrainer(int id) {
        return _trainers.get(id);
    }

    /**
     *
     * @param trainers to put in HashMap
     */
    public void setTrainers(List<PokemonTrainer> trainers) {
        for (PokemonTrainer pt : trainers) {
            this._trainers.put(pt.getID(), pt);
        }
    }

    /**
     *
     * @return current graph
     */
    public directed_weighted_graph getGraph() {
        return _graph;
    }

    /**
     * sets a new graph
     * @param _graph
     */
    public void setGraph(directed_weighted_graph _graph) {
        this._graph = _graph;
    }

    /**
     *
     * @return current game status
     */
    public GameStatus getGameStatus() {
        return _gs;
    }

    /**
     * sets a new game status
     * @param gs
     */
    public void setGameStatus(String gs) {
        this._gs.update(gs);
    }

    /////////////////////////////////////////////////////////////////////////////////////

    /**
     * find and updates edge for a pokemon
     * @param fr pokemon to find edge
     * @param g graph to check where the pokemon is located
     */
    public static void updateEdge(Pokemon fr, directed_weighted_graph g) {
        //	oop_edge_data ans = null;
        Iterator<node_data> itr = g.getV().iterator();
        boolean foundEdge = false;
        while(!foundEdge&&itr.hasNext()) {
            node_data v = itr.next();
            Iterator<edge_data> iter = g.getE(v.getKey()).iterator();
            while(iter.hasNext()) {
                edge_data e = iter.next();
                boolean f = isOnEdge(fr.getLocation(), e,fr.getType(), g);
                if(f) {
                    fr.setEdge(e);
                    foundEdge=true;
                    break;
                }
            }
        }
    }

    /**
     * Checks if given pokemon is on edge
     * @param p location of pokemon
     * @param e edge to check
     * @param type the direction the pokemon is going
     * @param g current graph
     * @return true if pokemon is on edge
     */
    private static boolean isOnEdge(geo_location p, edge_data e, int type, directed_weighted_graph g) {
        int src = g.getNode(e.getSrc()).getKey();
        int dest = g.getNode(e.getDest()).getKey();
        if(type<0 && dest>src) {return false;}
        if(type>0 && src>dest) {return false;}
        geo_location srcLocation = g.getNode(src).getLocation();
        geo_location destLocation = g.getNode(dest).getLocation();
        boolean ans = false;
        double dist = srcLocation.distance(destLocation);
        double d1 = srcLocation.distance(p) + p.distance(destLocation);
        if(dist>d1-EPS2) {ans = true;}
        return ans;
//        return isOnEdge(p,srcLocation, destLocation);
    }

    /**
     * converts JSON string of pokemons to a Pokemon Object
     * @param fs JSON of pokemon
     * @param graph to use in order to update edge
     * @return the pokemons in HashSet form
     */
    public static HashSet<Pokemon> json2Pokemons(String fs, directed_weighted_graph graph) {
        HashSet<Pokemon> ans = new HashSet<>();
        JsonArray allPokemons = JsonParser.parseString(fs).getAsJsonObject().getAsJsonArray("Pokemons");
        for(int i=0;i<allPokemons.size();i++) {
            JsonObject pp = allPokemons.get(i).getAsJsonObject();
            JsonObject pk = pp.get("Pokemon").getAsJsonObject();
            int t = pk.get("type").getAsInt();
            double v = pk.get("value").getAsDouble();
            String p = pk.get("pos").getAsString();
            GeoLocation g = new GeoLocation(p);
            Pokemon f = new Pokemon(v,g,t,null);
            updateEdge(f,graph);
            ans.add(f);
        }
        return ans;
    }

    /**
     * converts a JSON and updates the current trainers held in GameManager
     * @param aa JSON of Trainers
     * @param gg graph to start agents
     */
    public static void getTrainers(String aa, directed_weighted_graph gg) {
        try {
            JSONObject ttt = new JSONObject(aa);
            JSONArray ags = ttt.getJSONArray("Agents");
            for(int i=0;i<ags.length();i++) {
                JsonObject trainer = (JsonObject) JsonParser.parseString(ags.get(i).toString()).getAsJsonObject();
                int id = trainer.get("Agent").getAsJsonObject().get("id").getAsInt();
                if(_trainers.get(id)==null){
                    PokemonTrainer c = new PokemonTrainer(0,gg);
                    _trainers.put(id,c);
                }
                _trainers.get(id).update(trainer.toString());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * finds the max range of the graph,
     * meaning finds the max point for(x,y) and the min point of nodes.
     * @param g graph to find the min,max of x,y points
     * @return the new range
     */
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

    /**
     * converts game to size to fit new panel size
     * @param g graph to check
     * @param frame current size of the space to work with
     * @return the new range convertor of points
     */
    public static Range2Range w2f(directed_weighted_graph g, Range2D frame) {
        Range2D world = GraphRange(g);
        Range2Range ans = new Range2Range(world, frame);
        return ans;
    }


}

