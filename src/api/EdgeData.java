package api;

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
}
