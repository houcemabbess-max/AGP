package business;

public class VisitStep {
    private final Site site;
    private final Transport transport;

    private final double distance;
    private final double transportDuration; // heures
    private final double totalDuration;     // heures (transport + visite)
    private final double transportCost;     // €
    private final double totalCost;         // € (transport + entrée)

    /**
     * Crée une étape de visite en calculant automatiquement :
     * distance, durée transport, coût transport, durée totale, coût total.
     *
     * @param fromCoord coordonnées du point de départ (ex: hôtel ou site précédent)
     */
    public VisitStep(Coordinates fromCoord, Site site, Transport transport) {
        this.site = site;
        this.transport = transport;

        this.distance = GeoUtils.distance(fromCoord, site.getCoordinates());

        // Durée transport (heures) = distance / vitesse(km/h)
        this.transportDuration = (transport.getSpeedKmH() <= 0)
                ? 0.0
                : this.distance / transport.getSpeedKmH();

        // Coût transport = distance * prix/km
        this.transportCost = this.distance * transport.getPricePerKm();

        // Totaux
        this.totalDuration = this.transportDuration + site.getDuration();
        this.totalCost = this.transportCost + site.getEntryPrice();
    }

    public Site getSite() { return site; }
    public Transport getTransport() { return transport; }

    public double getDistance() { return distance; }
    public double getTransportDuration() { return transportDuration; }
    public double getTotalDuration() { return totalDuration; }

    public double getTransportCost() { return transportCost; }
    public double getTotalCost() { return totalCost; }

    @Override
    public String toString() {
        return "VisitStep -> site: " + site
             + ", transport: " + transport.getType()
             + ", distance: " + String.format("%.2f", distance) + " km"
             + ", price: " + String.format("%.2f", totalCost) + " €";
    }

}
