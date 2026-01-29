package business;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Arrays;
import java.util.List;

public class MainTestAGP {
    public static void main(String[] args) {

        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(AppConfig.class);

        OfferBuilder builder = context.getBean(OfferBuilder.class);

        Hotel hotel = new Hotel(
                1, "Hotel Central", 100.0,
                new Coordinates(10.0, 20.0),
                4
        );

        Site site1 = new Site(
                1, "Musee National",
                15.0, 2.0,
                new Coordinates(11.0, 21.0),
                TypeSite.MUSEUM,
                "Musee historique"
        );

        Site site2 = new Site(
                2, "Plage Centrale",
                0.0, 3.0,
                new Coordinates(12.0, 22.0),
                TypeSite.BEACH,
                "Belle plage"
        );

        List<Hotel> hotels = Arrays.asList(hotel);
        List<Site> sites = Arrays.asList(site1, site2);

        Offer offer = builder
                .withCatalog(hotels, sites)
                .withNbDays(5)
                .withBudgetMax(800)
                .build();

        System.out.println("---- OFFER (SPRING + BUILDER) ----");
        System.out.printf("Final price = %.2f €%n", offer.getFinalPrice());

        context.close();
    }
}
