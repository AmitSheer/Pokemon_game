package api;


import java.util.Objects;

public class GeoLocations implements geo_location {
    private double _x;
    private double _y;
    private double _z;

    public GeoLocations(){

    }

    public GeoLocations(double x, double y, double z){
        _x = x;
        _y = y;
        _z = z;
    }
    public GeoLocations(geo_location g){
        _x = g.x();
        _y = g.y();
        _z = g.z();
    }
    public GeoLocations(String g){
        String [] pos = g.split(",");
        _x = Double.parseDouble(pos[0]);
        _y = Double.parseDouble(pos[1]);
        _z = Double.parseDouble(pos[2]);
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
        double dx = this.x() - g.x();
        double dy = this.y() - g.y();
        double dz = this.z() - g.z();
        double t = (dx*dx+dy*dy+dz*dz);
        return Math.sqrt(t);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GeoLocations)) return false;
        GeoLocations that = (GeoLocations) o;
        return Double.compare(that._x, _x) == 0 &&
                Double.compare(that._y, _y) == 0 &&
                Double.compare(that._z, _z) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(_x, _y, _z);
    }

    @Override
    public String toString() {
        return  _x +
                "," + _y +
                "," + _z;
    }
}
