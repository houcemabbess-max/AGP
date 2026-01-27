package business;

import java.util.ArrayList;
import java.util.List;

public class Excursion {
    private final Hotel hotelDeparture;
    private final Hotel hotelArrival;
    private final List<VisitStep> steps;

    private double totalDuration;
    private double totalCost;

    public Excursion(Hotel hotel) {
        this.hotelDeparture = hotel;
        this.hotelArrival = hotel;
        this.steps = new ArrayList<>();
        this.totalDuration = 0.0;
        this.totalCost = 0.0;
    }

    public void addStep(VisitStep step) {
        if (steps.size() >= 3) {
            throw new IllegalStateException("Max 3 sites per excursion");
        }
        steps.add(step);
        totalDuration += step.getTotalDuration();
        totalCost += step.getTotalCost();
    }

    public Hotel getHotelDeparture() { return hotelDeparture; }
    public Hotel getHotelArrival() { return hotelArrival; }

    public List<VisitStep> getSteps() { return steps; }
    public double getTotalDuration() { return totalDuration; }
    public double getTotalCost() { return totalCost; }
}
