package business;

public class Hotel {
    private int id;
    private String name;
    private double pricePerDay;
    private Coordinates coordinates;
    private int stars;

    public Hotel(int id, String name, double pricePerDay, Coordinates coordinates, int stars) {
        this.id = id;
        this.name = name;
        this.pricePerDay = pricePerDay;
        this.coordinates = coordinates;
        this.stars = stars;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPricePerDay() {
        return pricePerDay;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public int getStars() {
        return stars;
    }

    @Override
    public String toString() {
        return "Hotel{name='" + name + "', stars=" + stars + ", pricePerDay=" + pricePerDay + "}";
    }
}
