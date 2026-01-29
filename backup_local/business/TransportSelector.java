package business;

import org.springframework.stereotype.Component;

@Component
public class TransportSelector {

    public Transport chooseTransport(
            Coordinates from,
            Coordinates to,
            double remainingBudget
    ) {
        double distance = GeoUtils.distance(from, to);

        Transport bestTransport = new OnFoot();
        double bestScore = Double.MAX_VALUE;

        Transport[] transports = {
                new Bus(),
                new OnFoot(),
                new Boat()
        };

        for (Transport transport : transports) {
            double cost = distance * transport.getPricePerKm();
            double duration = distance / transport.getSpeedKmH();

            if (cost > remainingBudget) continue;

            double score = cost + (duration * 5);

            if (score < bestScore) {
                bestScore = score;
                bestTransport = transport;
            }
        }
        return bestTransport;
    }
}
