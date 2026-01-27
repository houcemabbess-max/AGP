package business;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class VisitStepTest {

    @Test
    public void testVisitStepCalculationsWithBus() {
        Coordinates from = new Coordinates(0, 0);
        Site site = new Site(1, "Musee", 15.0, 2.0, new Coordinates(1, 1), TypeSite.MUSEUM, "desc");
        Transport bus = new Bus();

        VisitStep step = new VisitStep(from, site, bus);

        double expectedDistance = Math.sqrt(2);
        double expectedTransportDuration = expectedDistance / 60.0;
        double expectedTransportCost = expectedDistance * 0.5;
        double expectedTotalDuration = expectedTransportDuration + 2.0;
        double expectedTotalCost = expectedTransportCost + 15.0;

        assertEquals(expectedDistance, step.getDistance(), 0.0001);
        assertEquals(expectedTransportDuration, step.getTransportDuration(), 0.0001);
        assertEquals(expectedTransportCost, step.getTransportCost(), 0.0001);
        assertEquals(expectedTotalDuration, step.getTotalDuration(), 0.0001);
        assertEquals(expectedTotalCost, step.getTotalCost(), 0.0001);
    }

    @Test
    public void testVisitStepOnFootTransportIsFree() {
        Coordinates from = new Coordinates(0, 0);
        Site site = new Site(2, "Plage", 0.0, 3.0, new Coordinates(1, 1), TypeSite.BEACH, "desc");
        Transport foot = new OnFoot();

        VisitStep step = new VisitStep(from, site, foot);

        assertEquals(0.0, step.getTransportCost(), 0.0001);
        assertEquals(0.0, step.getTotalCost(), 0.0001); // entrée 0 + transport 0
    }
}

