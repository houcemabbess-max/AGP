package business;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class OfferTest {

    @Test
    public void testOfferFinalPriceHotelPlusExcursion() {
        Hotel hotel = new Hotel(1, "Hotel", 100.0, new Coordinates(10, 20), 4);
        Offer offer = new Offer(5);

        offer.addHotel(hotel); // 100 * 5 = 500

        Excursion excursion = new Excursion(hotel);
        Site s1 = new Site(1, "S1", 15.0, 2.0, new Coordinates(11, 21), TypeSite.MUSEUM, "");
        Transport bus = new Bus();

        VisitStep step1 = new VisitStep(hotel.getCoordinates(), s1, bus);
        excursion.addStep(step1);

        offer.addExcursion(excursion);

        double expected = 500.0 + excursion.getTotalCost();
        assertEquals(expected, offer.getFinalPrice(), 0.0001);
    }
}
