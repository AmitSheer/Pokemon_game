package api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class EdgeData implements edge_data {
    @Expose
    @SerializedName(value = "src")
    private int _src;
    @Expose
    @SerializedName(value = "dest")
    private int _dest;
    @Expose
    @SerializedName(value = "w")
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
        return "{" +
                "\"src\":" + _src +
                ", \"dest\":" + _dest +
                ", \"w\":" + _w +
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
