package business;

public class GeoUtils {

    public static double distance(Coordinates c1, Coordinates c2) {
        double lat = c2.getLatitude() - c1.getLatitude();
        double lon = c2.getLongitude() - c1.getLongitude();
        return Math.sqrt(lat * lat + lon * lon);
    }
}
