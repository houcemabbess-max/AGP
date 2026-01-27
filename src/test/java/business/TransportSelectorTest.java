package business;

import static org.junit.Assert.*;
import org.junit.Test;

public class TransportSelectorTest {

    @Test
    public void testChooseTransportWithEnoughBudget() {
        Coordinates from = new Coordinates(0, 0);
        Coordinates to = new Coordinates(1, 1);

        Transport transport =
                TransportSelector.chooseTransport(from, to, 50.0);

        assertNotNull(transport);
    }

    @Test
    public void testChooseTransportWithLowBudget() {
        Coordinates from = new Coordinates(0, 0);
        Coordinates to = new Coordinates(1, 1);

        Transport transport =
                TransportSelector.chooseTransport(from, to, 0.0);

        assertTrue(transport instanceof OnFoot);
    }
}
