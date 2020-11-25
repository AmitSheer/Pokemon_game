package api;

import java.util.HashMap;

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
        this._tag = Integer.MAX_VALUE;
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
}
