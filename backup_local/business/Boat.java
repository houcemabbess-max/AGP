package business;

public class Boat extends Transport {

    public Boat() {
        super(25.0, 3.0); // 25 km/h, 3 € / km
    }

    @Override
    public String getType() {
        return "BOAT";
    }
}
