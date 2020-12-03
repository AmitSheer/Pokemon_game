package gameClient;

import api.NodeData;
import api.edge_data;
import api.geo_location;

public class Pokemon extends NodeData {
    private int _type;
    private double _value;
    private edge_data _edge;

    public Pokemon(int key, double value, geo_location e, int type){
        super(key,e);
        _value = value;
        _type=type;
        _value = value;
    }

    public int getType() {
        return _type;
    }

    public void setType(int _type) {
        this._type = _type;
    }

    public edge_data getEdge() {
        return _edge;
    }

<<<<<<< HEAD:src/gameClient2/Pokemon.java
    @Override
    public String toString() {
        return "{" +
                "\"type\":" + _type +
                ", \"value\"" + _value +
                ", \"pos\":" + _pos.toString() +
                '}';
=======
    public void setEdge(edge_data _edge) {
        this._edge = _edge;
>>>>>>> 3ade7fb0b9d3603ad1de209b365975056de69365:src/gameClient/Pokemon.java
    }
}
