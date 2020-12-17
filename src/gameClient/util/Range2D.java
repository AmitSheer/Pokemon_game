package gameClient.util;
import api.GeoLocation;
import api.geo_location;

/**
 * This class represents a 2D Range, composed from two 1D Ranges.
 */
public class Range2D {
	private Range _y_range;
	private Range _x_range;

	public Range2D(Range x, Range y) {
		_x_range = new Range(x);
		_y_range = new Range(y);
	}
	public Range2D(Range2D w) {
		_x_range = new Range(w._x_range);
		_y_range = new Range(w._y_range);
	} 
	public GeoLocation getPortion(geo_location p) {
		double x = _x_range.getPortion(p.x());
		double y = _y_range.getPortion(p.y());
		return new GeoLocation(x,y,0);
	}
	public GeoLocation fromPortion(geo_location p) {
		double x = _x_range.fromPortion(p.x());
		double y = _y_range.fromPortion(p.y());
		return new GeoLocation(x,y,0);
	}	
}
