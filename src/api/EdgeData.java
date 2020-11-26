package api;

import java.util.Objects;

public class EdgeData implements edge_data {
    private int _src;
    private int _dest;
    private double _w;
    private String _info;
    private int _tag;

    public EdgeData(int src,int dest, double w){
        this._dest = dest;
        this._src = src;
        this._w = w;
    }


    @Override
    public int getSrc() {
        return _src;
    }

    @Override
    public int getDest() {
        return _dest;
    }

    @Override
    public double getWeight() {
        return _w;
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
    public String toString() {
        return "EdgeData{" +
                "_src=" + _src +
                ", _dest=" + _dest +
                ", _w=" + _w +
                ", _info='" + _info + '\'' +
                ", _tag=" + _tag +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EdgeData)) return false;
        EdgeData edgeData = (EdgeData) o;
        return _src == edgeData._src &&
                _dest == edgeData._dest &&
                Double.compare(edgeData._w, _w) == 0 &&
                _tag == edgeData._tag &&
                Objects.equals(_info, edgeData._info);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_src, _dest, _w, _info, _tag);
    }
}
