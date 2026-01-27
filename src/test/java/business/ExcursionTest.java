package business;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class ExcursionTest {

    @Test
    public void testExcursionTotals() {
        Hotel hotel = new Hotel(1, "Hotel", 100.0, new Coordinates(10, 20), 4);
        Excursion excursion = new Excursion(hotel);

        Site s1 = new Site(1, "S1", 10.0, 1.0, new Coordinates(11, 21), TypeSite.MUSEUM, "");
        Site s2 = new Site(2, "S2", 0.0, 2.0, new Coordinates(12, 22), TypeSite.BEACH, "");

        Transport bus = new Bus();
        Transport foot = new OnFoot();

        VisitStep step1 = new VisitStep(hotel.getCoordinates(), s1, bus);
        VisitStep step2 = new VisitStep(s1.getCoordinates(), s2, foot);

        excursion.addStep(step1);
        excursion.addStep(step2);

        assertEquals(step1.getTotalCost() + step2.getTotalCost(), excursion.getTotalCost(), 0.0001);
        assertEquals(step1.getTotalDuration() + step2.getTotalDuration(), excursion.getTotalDuration(), 0.0001);
    }

    @Test(expected = IllegalStateException.class)
    public void testMaxThreeSitesRule() {
        Hotel hotel = new Hotel(1, "Hotel", 100.0, new Coordinates(0, 0), 3);
        Excursion excursion = new Excursion(hotel);
        Transport bus = new Bus();

        Site s1 = new Site(1, "S1", 0, 1, new Coordinates(1,1), TypeSite.PARK, "");
        Site s2 = new Site(2, "S2", 0, 1, new Coordinates(2,2), TypeSite.PARK, "");
        Site s3 = new Site(3, "S3", 0, 1, new Coordinates(3,3), TypeSite.PARK, "");
        Site s4 = new Site(4, "S4", 0, 1, new Coordinates(4,4), TypeSite.PARK, "");

        excursion.addStep(new VisitStep(hotel.getCoordinates(), s1, bus));
        excursion.addStep(new VisitStep(s1.getCoordinates(), s2, bus));
        excursion.addStep(new VisitStep(s2.getCoordinates(), s3, bus));

        // 4e ajout => doit échouer
        excursion.addStep(new VisitStep(s3.getCoordinates(), s4, bus));
    }
}

