package business;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AppConfig.class)
public class OfferBuilderTest {

    @Autowired
    private ApplicationContext context;

    @Test
    public void build_shouldCreateOffer_andRespectBudget() {

        // prototype => nouveau builder
        OfferBuilder builder = context.getBean(OfferBuilder.class);

        Hotel hotel = new Hotel(
                1, "Hotel Central", 100.0,
                new Coordinates(0, 0),
                4
        );

        Site site = new Site(
                1, "Musee",
                15.0, 2.0,
                new Coordinates(1, 1),
                TypeSite.MUSEUM,
                "desc"
        );

        Offer offer = builder
                .withCatalog(Arrays.asList(hotel), Arrays.asList(site))
                .withNbDays(2)
                .withBudgetMax(500)
                .build();

        assertNotNull(offer);
        assertTrue(offer.getFinalPrice() <= 500);
        assertFalse(offer.getExcursions().isEmpty());
    }
}
