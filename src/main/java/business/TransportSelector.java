package business;

/**
 * Classe métier qui choisit le meilleur moyen de transport
 * selon la distance et le budget restant.
 */
public class TransportSelector {

    public static Transport chooseTransport(
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

            // si le coût dépasse le budget, on ignore ce transport
            if (cost > remainingBudget) {
                continue;
            }

            // score simple : coût + durée pondérée
            double score = cost + (duration * 5);

            if (score < bestScore) {
                bestScore = score;
                bestTransport = transport;
            }
        }

        return bestTransport;
    }
}
