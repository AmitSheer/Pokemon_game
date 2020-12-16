package gameClient2.GameLogic;

import api.*;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;


/**
 * goes to catch all pokemon
 */
public class PokemonTrainer {
    private EdgeData _curr_edge;
    private List<node_data> _pathToPokemon;
    private node_data _curr_node;
    private node_data _next_node;
    private directed_weighted_graph _gg;
    private int endNodeId;
    private geo_location _pos;
    private Pokemon nextPoke;
    private double _speed;
    private int _id;
    private double _value;
    private int _dest;

    public PokemonTrainer(int start_node, directed_weighted_graph gg) {
        this._gg = gg;
        this._curr_node = this._gg.getNode(start_node);
        this._dest = start_node;
        this._pos = _curr_node.getLocation();
        this._id = -1;
        this._pathToPokemon = new LinkedList<>();
        this.nextPoke = new Pokemon();
        set_speed(0);
    }

    /**
     * returns list of nodes the trainer needs to go through
     * @return
     */
    public List<node_data> getPathToPokemon() {
        return _pathToPokemon;
    }

    /**
     *
     * @return the next pokemon the trainer needs to eat
     */
    public Pokemon getNextPoke() {
        return nextPoke;
    }

    /**
     * sets the next pokemon the trainer needs to eat
     * @param nextPoke
     */
    public void setNextPoke(Pokemon nextPoke) {
        this.nextPoke = nextPoke;
    }

    /**
     * sets the new path the trainer needs to follow
     * @param pathToPokemon
     */
    public void setPathToPokemon(List<node_data> pathToPokemon) {
        this._pathToPokemon = pathToPokemon;
        if(_pathToPokemon.size()!=0)
            this.endNodeId = _pathToPokemon.get(_pathToPokemon.size()-1).getKey();
    }

    /**
     * updates all of the data of the trainer from JSON format string
     * @param json
     */
    public void update(String json) {
        JSONObject line;
        try {
            line = new JSONObject(json);
            JSONObject ttt = line.getJSONObject("Agent");
            int id = ttt.getInt("id");
            if(id==this.getID() || this.getID() == -1) {
                if(this.getID() == -1) {_id = id;}
                double speed = ttt.getDouble("speed");
                String p = ttt.getString("pos");
                GeoLocation pp = new GeoLocation(p);
                int src = ttt.getInt("src");
                int dest = ttt.getInt("dest");
                double value = ttt.getDouble("value");
                this.setLocation(new GeoLocation(p));
                this.set_curr_node(src);
                this.set_speed(speed);
                this.set_next_node(dest);
                this.set_money(value);
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @return trainers id
     */
    public int getID() {
        return _id;
    }

    /**
     * sets trainers id
     * @param ID
     */
    public void setID(int ID) {
        this._id = ID;
    }

    /**
     *
     * @return current node trainer is on
     */
    public int get_curr_node() {
        return _curr_node.getKey();
    }

    /**
     * sets new node trainer is on
     * @param _curr_node
     */
    public void set_curr_node(int _curr_node) {
        this._curr_node = _gg.getNode(_curr_node);
    }

    /**
     * sets the trainer current travel speed
     * @param _speed
     */
    public void set_speed(double _speed) {
        this._speed = _speed;
    }

    /**
     *
     * @return the total value of pokemons the trainer collected
     */
    public double get_money() {
        return _value;
    }

    /**
     * sets new total value of pokemons the trainer collected
     * @param _value
     */
    public void set_money(double _value) {
        this._value = _value;
    }

    /**
     * sets next node to visit
     * @param _next_node
     */
    public void set_next_node(int _next_node) {
        if(_next_node==-1){
            _curr_edge=null;
        }else{
            this._next_node = _gg.getNode(_next_node);
            this._curr_edge = (EdgeData) _gg.getEdge(this.get_curr_node(),_next_node);
        }
        _dest=_next_node;
    }

    /**
     *
     * @return returns trainer current location
     */
    public geo_location getLocation() {
        return _pos;
    }

    /**
     * sets trainer new location
     * @param _pos
     */
    public void setLocation(geo_location _pos) {
        this._pos = _pos;
    }

    /**
     * returns trainer destination
     * @return
     */
    public int get_dest() {
        return _dest;
    }

    /**
     *
     * @return the last node trainer needs to go to
     */
    public int getEndNodeId() {
        return endNodeId;
    }

    /**
     * returns current edge trainer is on
     * @return
     */
    public EdgeData get_curr_edge() {
        return _curr_edge;
    }

    /**
     * sets a new edge the agent is on
     * @param _curr_edge
     */
    public void set_curr_edge(edge_data _curr_edge) {
        this._curr_edge = (EdgeData) _curr_edge;
    }
}
