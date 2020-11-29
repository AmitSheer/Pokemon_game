package gameClient;

import api.edge_data;
import api.geo_location;

public class Pokemon{
    private int _type;
    private double _value;
    private edge_data _edge;
    private geo_location _pos;

    public Pokemon(double value, geo_location e){
        _value = value;
        _pos = e;
    }

    public double getValue() {
        return _value;
    }

    public geo_location getPos() {
        return _pos;
    }

    public edge_data getEdge() {
        return _edge;
    }
}
