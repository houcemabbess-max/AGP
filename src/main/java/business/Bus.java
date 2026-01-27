package business;

public class Bus extends Transport {

    public Bus() {
        super(60.0, 0.5);
    }

    @Override
    public String getType() {
        return "BUS";
    }
}
