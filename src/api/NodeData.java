package api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Objects;

/**
 * data on node in graph
 */
public class NodeData implements node_data {
    @Expose
    @SerializedName(value = "id")
    private Integer _key;
    private String _info;
    private int _tag;
    @Expose
    @SerializedName(value = "pos")
    private geo_location _location;
    private double _w;
    //holds all of the connections to the node and their weight
    public NodeData(int key) {
        this._key = key;
        this._info = String.valueOf(key);
        this._tag = 0;
        this._w = 0;
        _location = new GeoLocation();
    }
    public NodeData(int key, geo_location e) {
        this._key = key;
        this._info = String.valueOf(key);
        this._tag = 0;
        this._w = 0;
        _location = e;
    }

    @Override
    public int getKey() {
        return _key;
    }

    @Override
    public geo_location getLocation() {
        return _location;
    }

    @Override
    public void setLocation(geo_location p) {
        _location = new GeoLocation(p);

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
                Objects.equals(_location, nodeData._location);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_key, _info, _tag, _location, _w);
    }

    @Override
    public String toString() {
        return "{" +
                "\"id\":" + _key +
                ", \"pos\":" + _location.toString() +
                "}";
    }
}
