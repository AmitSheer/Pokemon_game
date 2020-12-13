package gameClient2.GameLogic;

import api.edge_data;
import api.geo_location;

import java.util.Objects;

public class Pokemon {
    private int _type;
    private double _value;
    private edge_data _edge;
    private int _src;
    private int _dest;
    private int _id;
    private geo_location _pos;
    private int _trainerId;

    public Pokemon(int key, double value, geo_location pos, int type, edge_data e){
        _id = key;
        _pos = pos;
        _value = value;
        _type=type;
        _value = value;
        setEdge(e);
    }

    public Pokemon() {
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

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public geo_location getLocation() {
        return _pos;
    }

    public void set_pos(geo_location _pos) {
        this._pos = _pos;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pokemon pokemon = (Pokemon) o;
        return _type == pokemon._type &&
                Double.compare(pokemon._value, _value) == 0 &&
                Objects.equals(_edge, pokemon._edge) &&
                Objects.equals(_pos, pokemon._pos);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_type, _value, _edge, _src, _dest, _id, _pos);
    }

    @Override
    public String toString() {
        return "Pokemon{" +
                "_type=" + _type +
                ", _value=" + _value +
                ", _edge=" + _edge +
                ", _src=" + _src +
                ", _dest=" + _dest +
                ", _id=" + _id +
                ", _pos=" + _pos +
                '}';
    }

    public void setTrainerId(int _trainerId) {
        this._trainerId = _trainerId;
    }

    public int getTrainerId() {
        return _trainerId;
    }
}
