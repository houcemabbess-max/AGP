package business;

import static org.junit.Assert.*;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;

public class OfferBuilderTest {

    @Test
    public void build_shouldCreateOffer_withHotel_andAtLeastOneExcursion() {
        // --- Données (en mémoire)
        Hotel h1 = new Hotel(1, "H1", 100.0, new Coordinates(0, 0), 4);
        Hotel h2 = new Hotel(2, "H2", 300.0, new Coordinates(10, 10), 2);
        List<Hotel> hotels = Arrays.asList(h1, h2);

        Site s1 = new Site(1, "Musee", 15.0, 2.0, new Coordinates(1, 1), TypeSite.MUSEUM, "desc");
        Site s2 = new Site(2, "Plage", 0.0, 3.0, new Coordinates(2, 2), TypeSite.BEACH, "desc");
        Site s3 = new Site(3, "Parc", 5.0, 1.0, new Coordinates(1, 2), TypeSite.PARK, "desc");
        List<Site> sites = Arrays.asList(s1, s2, s3);

        // --- Construction de l'offre
        Offer offer = new OfferBuilder(hotels, sites)
                .withNbDays(2)
                .withBudgetMax(800)                 // budget assez grand
                .withMaxSitesPerDay(3)
                .withMaxHoursPerDay(8.0)
                .preferTypes(TypeSite.MUSEUM, TypeSite.BEACH)
                .build();

        // --- Vérifications (assertions)
        assertNotNull(offer);
        assertNotNull(offer.getHotels());
        assertNotNull(offer.getExcursions());

        // 1) au moins 1 hôtel choisi
        assertTrue(offer.getHotels().size() >= 1);

        // 2) au moins 1 excursion (si budget/temps le permet)
        assertTrue(offer.getExcursions().size() >= 1);

        // 3) prix final positif
        assertTrue(offer.getFinalPrice() > 0);
    }

    @Test
    public void build_shouldRespectMax3SitesPerDay() {
        Hotel h = new Hotel(1, "H", 50.0, new Coordinates(0, 0), 3);
        List<Hotel> hotels = Arrays.asList(h);

        // 6 sites (pour forcer plusieurs choix)
        List<Site> sites = Arrays.asList(
                new Site(1, "S1", 0, 1, new Coordinates(1, 0), TypeSite.PARK, ""),
                new Site(2, "S2", 0, 1, new Coordinates(2, 0), TypeSite.PARK, ""),
                new Site(3, "S3", 0, 1, new Coordinates(3, 0), TypeSite.PARK, ""),
                new Site(4, "S4", 0, 1, new Coordinates(4, 0), TypeSite.PARK, ""),
                new Site(5, "S5", 0, 1, new Coordinates(5, 0), TypeSite.PARK, ""),
                new Site(6, "S6", 0, 1, new Coordinates(6, 0), TypeSite.PARK, "")
        );

        Offer offer = new OfferBuilder(hotels, sites)
                .withNbDays(1)
                .withBudgetMax(1000)
                .withMaxSitesPerDay(3)   // règle
                .withMaxHoursPerDay(20)
                .build();

        // Vérifier que chaque excursion ne dépasse pas 3 étapes
        for (Excursion ex : offer.getExcursions()) {
            assertTrue(ex.getSteps().size() <= 3);
        }
    }

    @Test
    public void build_shouldNotExceedBudgetMax_whenBudgetIsTight() {
        Hotel h = new Hotel(1, "H", 100.0, new Coordinates(0, 0), 3);
        List<Hotel> hotels = Arrays.asList(h);

        // Sites chers
        List<Site> sites = Arrays.asList(
                new Site(1, "S1", 200, 2, new Coordinates(1, 1), TypeSite.MUSEUM, ""),
                new Site(2, "S2", 200, 2, new Coordinates(2, 2), TypeSite.MUSEUM, "")
        );

        double budgetMax = 250; // très serré

        Offer offer = new OfferBuilder(hotels, sites)
                .withNbDays(1)
                .withBudgetMax(budgetMax)
                .withMaxSitesPerDay(3)
                .withMaxHoursPerDay(8)
                .build();

        // Le prix final doit être <= budgetMax (ou au moins ne pas dépasser trop, selon ta règle)
        assertTrue(offer.getFinalPrice() <= budgetMax + 0.0001);
    }
}
