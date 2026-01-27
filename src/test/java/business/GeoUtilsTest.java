package business;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class GeoUtilsTest {

    @Test
    public void testDistanceSamePoint() {
        Coordinates c = new Coordinates(10, 20);
        assertEquals(0.0, GeoUtils.distance(c, c), 0.0001);
    }

    @Test
    public void testDistanceSimple() {
        Coordinates c1 = new Coordinates(0, 0);
        Coordinates c2 = new Coordinates(1, 1);
        assertEquals(Math.sqrt(2), GeoUtils.distance(c1, c2), 0.0001);
    }

    @Test
    public void testDistanceSymmetry() {
        Coordinates c1 = new Coordinates(2, 5);
        Coordinates c2 = new Coordinates(7, 9);
        assertEquals(GeoUtils.distance(c1, c2), GeoUtils.distance(c2, c1), 0.0001);
    }
}
