package gameClient2;

import api.NodeData;
import api.edge_data;
import api.geo_location;

public class Pokemon extends NodeData {
    private int _type;
    private double _value;
    private edge_data _edge;
    private int _src;
    private int _dest;

    public Pokemon(int key, double value, geo_location pos, int type, edge_data e){
        super(key,pos);
        _value = value;
        _type=type;
        _value = value;
        setEdge(e);
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
