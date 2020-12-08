package gameClient2.GameLogic;

import api.*;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;


/**
 * goes to catch all pokemon
 */
public class PokemonTrainer {
    private edge_data _curr_edge;
    private List<node_data> _pathToPokemon;
    private node_data _curr_node;
    private node_data _next_node;
    private directed_weighted_graph _gg;
    private geo_location _pos;

    private double _speed;
    private int _id;
    private double _value;


    public PokemonTrainer(int start_node, directed_weighted_graph gg) {
        this._gg = gg;
        this._curr_node = this._gg.getNode(start_node);
        this._pos = _curr_node.getLocation();
        this._id = -1;
        this._pathToPokemon = new LinkedList<>();
        set_speed(0);
    }

    public void setPathToPokemon(List<node_data> pathToPokemon) {
        //pathToPokemon.remove(0);
        this._pathToPokemon = pathToPokemon;
        this.set_next_node(this._pathToPokemon.remove(0).getKey());
        //this._curr_edge = _gg.getEdge(this.get_curr_node(),this._next_node.getKey());
    }

    public void moveTrainer(){
        if(_pathToPokemon.size()!=0) _pathToPokemon.remove(0);
    }

    public int getNextNode() {
        int ans = -2;
        if (this._curr_edge == null) {
            ans = -1;
        } else {
            ans = this._curr_edge.getDest();
        }
        return ans;
    }

    public void update(String json) {
        JSONObject line;
        try {
            // "GameServer":{"graph":"A0","pokemons":3,"agents":1}}
            line = new JSONObject(json);
            JSONObject ttt = line.getJSONObject("Agent");
            int id = ttt.getInt("id");
            if(id==this.getID() || this.getID() == -1) {
                if(this.getID() == -1) {_id = id;}
                double speed = ttt.getDouble("speed");
                String p = ttt.getString("pos");
                GeoLocations pp = new GeoLocations(p);
                int src = ttt.getInt("src");
                int dest = ttt.getInt("dest");
                if(_pathToPokemon.size()>0&&dest==-1){
                    set_next_node(_pathToPokemon.remove(0).getKey());
                }else if(dest==-1){
                    set_next_node(-1);
                }
                double value = ttt.getDouble("value");
                this.setLocation(new GeoLocations(p));
                this.set_curr_node(src);
                this.set_speed(speed);
                //this.set_next_node(dest);
                this.set_money(value);
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    public int getID() {
        return _id;
    }

    public void setID(int ID) {
        this._id = ID;
    }

    public int get_curr_node() {
        return _curr_node.getKey();
    }

    public void set_curr_node(int _curr_node) {
        this._curr_node = _gg.getNode(_curr_node);
    }

    public double get_speed() {
        return _speed;
    }

    public void set_speed(double _speed) {
        this._speed = _speed;
    }

    public double get_money() {
        return _value;
    }

    public void set_money(double _value) {
        this._value = _value;
    }


    public void set_next_node(int _next_node) {
        this._next_node = _gg.getNode(_next_node);
        if(_next_node==-1){
            _curr_edge=null;
        }else{
            this._curr_edge = _gg.getEdge(this.get_curr_node(),_next_node);
        }
    }
    public void set_next_node() {
        this._next_node = _gg.getNode(_pathToPokemon.remove(0).getKey());
    }

    public geo_location getLocation() {
        return _pos;
    }

    public void setLocation(geo_location _pos) {
        this._pos = _pos;
    }

    public void update(PokemonTrainer next) {
    }
}
