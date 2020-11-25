package api;

public class Geo_Locations implements geo_location {
    private double _x;
    private double _y;
    private double _z;

    public Geo_Locations(double x, double y, double z){
        _x = x;
        _y = y;
        _z = z;
    }
    public Geo_Locations(geo_location g){
        _x = g.x();
        _y = g.y();
        _z = g.z();
    }

    @Override
    public double x() {
        return _x;
    }

    @Override
    public double y() {
        return _y;
    }

    @Override
    public double z() {
        return _y;
    }

    @Override
    public double distance(geo_location g) {
        double dx = this._x - g.x();
        double dy = this._y - g.y();
        double dz = this._z - g.z();
        return Math.sqrt(dx*dx+dy*dy+dz*dz);
    }
}
