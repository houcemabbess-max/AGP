package business;

import java.util.ArrayList;
import java.util.List;

public class Offer {

    private List<Hotel> hotels;
    private List<Excursion> excursions;
    private double finalPrice;
    private int nbDays;

    public Offer(int nbDays) {
        this.nbDays = nbDays;
        this.hotels = new ArrayList<>();
        this.excursions = new ArrayList<>();
        this.finalPrice = 0.0;
    }

    public void addHotel(Hotel hotel) {
        hotels.add(hotel);
        finalPrice += hotel.getPricePerDay() * nbDays;
    }

    public void addExcursion(Excursion excursion) {
        excursions.add(excursion);
        finalPrice += excursion.getTotalCost();
    }

    // ---------- GETTERS (OBLIGATOIRES POUR LES TESTS) ----------

    public List<Hotel> getHotels() {
        return hotels;
    }

    public List<Excursion> getExcursions() {
        return excursions;
    }

    public double getFinalPrice() {
        return finalPrice;
    }

    public int getNbDays() {
        return nbDays;
    }
}
