package business;

public class MainTestAGP {
    public static void main(String[] args) {

        Island island = new Island(1, "Ile de la Republique");

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

        Transport bus = new Bus();
        Transport foot = new OnFoot();

        Excursion excursion = new Excursion(hotel);

        // Etape 1 : départ depuis l’hôtel
        VisitStep step1 = new VisitStep(hotel.getCoordinates(), site1, bus);
        excursion.addStep(step1);

        // Etape 2 : départ depuis le site1 (logique réelle)
        VisitStep step2 = new VisitStep(site1.getCoordinates(), site2, foot);
        excursion.addStep(step2);

        Offer offer = new Offer(5);
        offer.addHotel(hotel);
        offer.addExcursion(excursion);

        System.out.println("---- DETAILS EXCURSION ----");
        System.out.println(step1);
        System.out.println(step2);

        System.out.printf("Excursion total cost = %.2f €%n", excursion.getTotalCost());
        System.out.printf("Excursion total duration = %.2f h%n", excursion.getTotalDuration());

        System.out.println("---- OFFER ----");
        System.out.printf("Final price = %.2f €%n", offer.getFinalPrice());
    }
}
