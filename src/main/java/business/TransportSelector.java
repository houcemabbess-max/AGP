package business;

import java.util.List;

/**
 * TransportSelector :
 * Choisit le meilleur transport selon distance, coût et durée.
 *
 * Spring (XML) :
 * - Spring crée cet objet (bean)
 * - La liste des transports est injectée via setTransports(...)
 */
public class TransportSelector {

    // Liste injectée par Spring (Bus, Boat, OnFoot...)
    private List<Transport> transports;

    // Constructeur vide (utilisé par Spring)
    public TransportSelector() {
    }

    // Setter utilisé dans applicationContext.xml
    public void setTransports(List<Transport> transports) {
        this.transports = transports;
    }

    public Transport chooseTransport(Coordinates from, Coordinates to, double remainingBudget) {

        // Sécurité : si l'injection n'a pas été faite
        if (transports == null || transports.isEmpty()) {
            throw new IllegalStateException("Liste des transports non injectée par Spring.");
        }

        double distance = GeoUtils.distance(from, to);

        Transport best = null;
        double bestScore = Double.MAX_VALUE;

        for (Transport t : transports) {
            double cost = distance * t.getPricePerKm();
            double duration = distance / t.getSpeedKmH();

            if (cost > remainingBudget) continue;

            double score = cost + duration * 5;

            if (score < bestScore) {
                bestScore = score;
                best = t;
            }
        }

        return best;
    }
}


