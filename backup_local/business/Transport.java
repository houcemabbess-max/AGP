package business;

public abstract class Transport {
    protected double speedKmH;
    protected double pricePerKm;

    protected Transport(double speedKmH, double pricePerKm) {
        this.speedKmH = speedKmH;
        this.pricePerKm = pricePerKm;
    }

    public abstract String getType();

    public double getSpeedKmH() {
        return speedKmH;
    }

    public double getPricePerKm() {
        return pricePerKm;
    }
    @Override
    public String toString() {
        return getType();
    }

}
