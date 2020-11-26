package api;

import java.util.HashMap;
import java.util.Objects;

import api.*;
public class NodeData implements node_data {
    //makes sure there are no repeating node keys
    private static int nodeCounter = 0;
    private final Integer _key;
    private String _info;
    private int _tag;
    private geo_location location;
    private double _w;
    //holds all of the connections to the node and their weight
    public NodeData(int key) {
        this._key = key;
        this._info = String.valueOf(key);
        this._tag = 0;
        this._w = 0;

        nodeCounter = nodeCounter + key;
    }

    @Override
    public int getKey() {
        return _key;
    }

    @Override
    public geo_location getLocation() {
        return location;
    }

    @Override
    public void setLocation(geo_location p) {
        location = new Geo_Locations(p);

    }

    @Override
    public double getWeight() {
        return _w;
    }

    @Override
    public void setWeight(double w) {
        this._w = w;
    }

    @Override
    public String getInfo() {
        return _info;
    }

    @Override
    public void setInfo(String s) {
        this._info = s;
    }

    @Override
    public int getTag() {
        return _tag;
    }

    @Override
    public void setTag(int t) {
        this._tag = t;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NodeData)) return false;
        NodeData nodeData = (NodeData) o;
        return _tag == nodeData._tag &&
                Double.compare(nodeData._w, _w) == 0 &&
                Objects.equals(_key, nodeData._key) &&
                Objects.equals(_info, nodeData._info) &&
                Objects.equals(location, nodeData.location);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_key, _info, _tag, location, _w);
    }
}
