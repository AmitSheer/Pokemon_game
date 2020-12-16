package gameClient2.GameLogic;

import api.edge_data;
import api.geo_location;

import java.util.Objects;

public class Pokemon {
    private int _type;
    private double _value;
    private edge_data _edge;
    private geo_location _pos;

    public Pokemon(double value, geo_location pos, int type, edge_data e){
        _pos = pos;
        _value = value;
        _type=type;
        _value = value;
        setEdge(e);
    }

    public Pokemon() {
    }

    /**
     * returns the pokemon type
     * @return type
     */
    public int getType() {
        return _type;
    }

    /**
     * sets the pokemon type
     * @param _type of pokemon
     */
    public void setType(int _type) {
        this._type = _type;
    }

    /**
     * gets current edge
     * @return
     */
    public edge_data getEdge() {
        return _edge;
    }

    /**
     * sets the current edge
     * @param _edge new edge
     */
    public void setEdge(edge_data _edge) {
        this._edge = _edge;
    }


    /**
     *
     * @return  current location
     */
    public geo_location getLocation() {
        return _pos;
    }

    /**
     *  updates current location
     * @param _pos location to put
     */
    public void setLocation(geo_location _pos) {
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
        return Objects.hash(_type, _value, _edge,  _pos);
    }

    @Override
    public String toString() {
        return "Pokemon{" +
                "_type=" + _type +
                ", _value=" + _value +
                ", _edge=" + _edge +
                ", _pos=" + _pos +
                '}';
    }
}
