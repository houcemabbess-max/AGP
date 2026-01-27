package business;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class TransportTest {

    @Test
    public void testBusProperties() {
        Transport bus = new Bus();
        assertEquals("BUS", bus.getType());
        assertEquals(60.0, bus.getSpeedKmH(), 0.0001);
        assertEquals(0.5, bus.getPricePerKm(), 0.0001);
    }

    @Test
    public void testOnFootProperties() {
        Transport foot = new OnFoot();
        assertEquals("ON_FOOT", foot.getType());
        assertEquals(5.0, foot.getSpeedKmH(), 0.0001);
        assertEquals(0.0, foot.getPricePerKm(), 0.0001);
    }
}
