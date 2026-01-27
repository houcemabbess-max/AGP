package business;

public class OnFoot extends Transport {

    public OnFoot() {
        super(5.0, 0.0);
    }

    @Override
    public String getType() {
        return "ON_FOOT";
    }
}