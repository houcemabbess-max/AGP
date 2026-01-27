package business;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * OfferBuilder = classe de la couche Business qui CONSTRUIT une Offer automatiquement
 * en appliquant des règles métier (budget, max 3 sites/jour, durée max/jour, préférences).
 *
 * Idée : ce n'est pas "si/alors" simple :
 * - on calcule un score pour choisir hôtel + sites
 * - on respecte des contraintes (budget/temps)
 * - on choisit le transport en comparant plusieurs options (via TransportSelector)
 */
public class OfferBuilder {

    // Données disponibles (catalogue)
    private final List<Hotel> availableHotels;
    private final List<Site> availableSites;

    // Paramètres de construction
    private int nbDays = 1;
    private double budgetMax = Double.MAX_VALUE;
    private int maxSitesPerDay = 3;
    private double maxHoursPerDay = 8.0;

    // Préférences simples (optionnelles)
    private final Set<TypeSite> preferredTypes = new HashSet<>();
    private boolean prioritizeCheap = true; // si true => on favorise les sites pas chers

    // Pondérations (score) : tu peux ajuster si ton prof veut “plus complexe”
    private double wType = 30.0;     // bonus si type préféré
    private double wDistance = 10.0; // pénalité distance
    private double wEntry = 2.0;     // pénalité prix d’entrée
    private double wDuration = 1.5;  // pénalité durée visite

    public OfferBuilder(List<Hotel> hotels, List<Site> sites) {
        this.availableHotels = hotels != null ? hotels : new ArrayList<>();
        this.availableSites = sites != null ? sites : new ArrayList<>();
    }

    // --- Fluent setters (optionnels mais pratiques)
    public OfferBuilder withNbDays(int nbDays) {
        this.nbDays = Math.max(1, nbDays);
        return this;
    }

    public OfferBuilder withBudgetMax(double budgetMax) {
        this.budgetMax = budgetMax > 0 ? budgetMax : 0;
        return this;
    }

    public OfferBuilder withMaxSitesPerDay(int max) {
        this.maxSitesPerDay = Math.max(1, max);
        return this;
    }

    public OfferBuilder withMaxHoursPerDay(double hours) {
        this.maxHoursPerDay = Math.max(1.0, hours);
        return this;
    }

    public OfferBuilder preferTypes(TypeSite... types) {
        if (types != null) {
            for (TypeSite t : types) {
                if (t != null) preferredTypes.add(t);
            }
        }
        return this;
    }

    public OfferBuilder prioritizeCheap(boolean value) {
        this.prioritizeCheap = value;
        return this;
    }

    /**
     * Construit l'offre :
     * 1) Choisir un hôtel (score hôtel)
     * 2) Pour chaque jour : construire une excursion (max 3 sites + maxHoursPerDay + budget)
     * 3) Retourner l'Offer finale
     */
    public Offer build() {
        if (availableHotels.isEmpty()) {
            throw new IllegalStateException("Aucun hotel disponible pour construire une offre.");
        }

        Offer offer = new Offer(nbDays);

        // 1) Choix hôtel (un seul hôtel dans ton modèle actuel)
        Hotel chosenHotel = chooseBestHotel();
        offer.addHotel(chosenHotel);

        // Budget restant après hôtel
        double remainingBudget = budgetMax - (chosenHotel.getPricePerDay() * nbDays);

        // 2) Excursions jour par jour
        Set<Integer> usedSiteIds = new HashSet<>(); // pour éviter de revisiter le même site (optionnel)

        for (int day = 1; day <= nbDays; day++) {
            if (remainingBudget <= 0) break;

            Excursion excursion = new Excursion(chosenHotel);

            Coordinates currentPoint = chosenHotel.getCoordinates();
            double dayHours = 0.0;
            int count = 0;

            while (count < maxSitesPerDay) {
                // Choisir le meilleur site restant selon score + contraintes
                Site next = chooseBestNextSite(currentPoint, remainingBudget, (maxHoursPerDay - dayHours), usedSiteIds);

                if (next == null) break; // rien de faisable

                // Choix transport (comparaison Bus/Foot/Boat) selon coût + temps sous budget
                Transport transport = TransportSelector.chooseTransport(currentPoint, next.getCoordinates(), remainingBudget);
                if (transport == null) break;

                VisitStep step = new VisitStep(currentPoint, next, transport);

                // Vérifier contraintes (budget + temps)
                if (step.getTotalCost() > remainingBudget) {
                    usedSiteIds.add(next.getId()); // on le marque comme “pas possible” dans ce contexte
                    continue;
                }
                if (dayHours + step.getTotalDuration() > maxHoursPerDay) {
                    usedSiteIds.add(next.getId());
                    continue;
                }

                // Ajouter l'étape
                excursion.addStep(step);
                usedSiteIds.add(next.getId());

                remainingBudget -= step.getTotalCost();
                dayHours += step.getTotalDuration();
                currentPoint = next.getCoordinates();
                count++;
            }

            // Ajouter excursion seulement si elle contient au moins 1 visite
            if (!excursion.getSteps().isEmpty()) {
                offer.addExcursion(excursion);
            }
        }

        return offer;
    }

    // ----------------------------
    // Choix de l'hôtel (score)
    // ----------------------------
    private Hotel chooseBestHotel() {
        // Score simple “un peu intelligent” :
        // - favoriser prix/jour bas
        // - favoriser stars élevés
        // - favoriser position "centrale" (proche des sites en moyenne)
        return availableHotels.stream()
                .max(Comparator.comparingDouble(this::hotelScore))
                .orElseThrow(() -> new IllegalStateException("Impossible de choisir un hotel."));
    }

    private double hotelScore(Hotel h) {
        double price = h.getPricePerDay();
        int stars = h.getStars();

        // centralité = moyenne des distances aux sites (plus petit = mieux)
        double avgDist = averageDistanceToSites(h.getCoordinates());

        // Score : étoiles (bonus) - prix (pénalité) - distance moyenne (pénalité)
        // (tu peux ajuster les poids)
        return (stars * 20.0) - (price * 0.5) - (avgDist * 10.0);
    }

    private double averageDistanceToSites(Coordinates hotelCoord) {
        if (availableSites.isEmpty()) return 0.0;
        double sum = 0.0;
        for (Site s : availableSites) {
            sum += GeoUtils.distance(hotelCoord, s.getCoordinates());
        }
        return sum / availableSites.size();
    }

    // ---------------------------------------
    // Choix du prochain site (score + filtre)
    // ---------------------------------------
    private Site chooseBestNextSite(
            Coordinates from,
            double remainingBudget,
            double remainingHours,
            Set<Integer> usedSiteIds
    ) {
        Site best = null;
        double bestScore = -Double.MAX_VALUE;

        for (Site s : availableSites) {
            if (usedSiteIds.contains(s.getId())) continue;

            // Filtrage simple : si le prix d'entrée dépasse déjà le budget restant => pas possible
            if (s.getEntryPrice() > remainingBudget) continue;

            // Estimer au minimum la durée visit (sans transport) : si déjà trop long => pas possible
            if (s.getDuration() > remainingHours) continue;

            double score = siteScore(from, s);

            // Un site très cher devient encore plus pénalisé si on priorise "cheap"
            if (prioritizeCheap) {
                score -= (s.getEntryPrice() * 2.0);
            }

            if (score > bestScore) {
                bestScore = score;
                best = s;
            }
        }

        return best;
    }

    private double siteScore(Coordinates from, Site s) {
        double dist = GeoUtils.distance(from, s.getCoordinates());

        double typeBonus = 0.0;
        if (!preferredTypes.isEmpty() && preferredTypes.contains(s.getType())) {
            typeBonus = wType;
        }

        // score = bonus type - pénalités distance/prix/durée
        return typeBonus
                - (wDistance * dist)
                - (wEntry * s.getEntryPrice())
                - (wDuration * s.getDuration());
    }
}
