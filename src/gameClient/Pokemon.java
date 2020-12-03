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

    public void setEdge(edge_data _edge) {
        this._edge = _edge;
    }
}
