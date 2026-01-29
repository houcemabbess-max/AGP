package business;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * OfferBuilder :
 * Construit une Offer à partir d'un catalogue (hotels + sites).
 *
 * Spring (XML) :
 * - OfferBuilder est créé par Spring
 * - TransportSelector est injecté via un setter (property)
 */
public class OfferBuilder {

    // Dépendance injectée par Spring (setter injection)
    private TransportSelector transportSelector;

    // Constructeur vide obligatoire pour Spring XML
    public OfferBuilder() {
    }

    // Setter utilisé dans applicationContext.xml
    public void setTransportSelector(TransportSelector transportSelector) {
        this.transportSelector = transportSelector;
    }

    // ---------------- Catalogue ----------------
    private List<Hotel> availableHotels = new ArrayList<>();
    private List<Site> availableSites = new ArrayList<>();

    // ---------------- Paramètres ----------------
    private int nbDays = 1;
    private double budgetMax = Double.MAX_VALUE;
    private int maxSitesPerDay = 3;
    private double maxHoursPerDay = 8.0;

    // Options simples
    private final Set<TypeSite> preferredTypes = new HashSet<>();
    private boolean prioritizeCheap = true;

    // Poids pour le score
    private double wType = 30.0;
    private double wDistance = 10.0;
    private double wEntry = 2.0;
    private double wDuration = 1.5;

    // ---------------- Configuration ----------------

    public OfferBuilder withCatalog(List<Hotel> hotels, List<Site> sites) {
        this.availableHotels = (hotels != null) ? hotels : new ArrayList<>();
        this.availableSites = (sites != null) ? sites : new ArrayList<>();
        return this;
    }

    public OfferBuilder withNbDays(int nbDays) {
        this.nbDays = Math.max(1, nbDays);
        return this;
    }

    public OfferBuilder withBudgetMax(double budgetMax) {
        this.budgetMax = Math.max(0, budgetMax);
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

    public OfferBuilder prioritizeCheap(boolean value) {
        this.prioritizeCheap = value;
        return this;
    }

    // ---------------- Construction ----------------

    public Offer build() {

        if (availableHotels.isEmpty()) {
            throw new IllegalStateException("Aucun hotel disponible pour construire une offre.");
        }

        Offer offer = new Offer(nbDays);

        Hotel chosenHotel = chooseBestHotel();
        offer.addHotel(chosenHotel);

        double remainingBudget =
                budgetMax - (chosenHotel.getPricePerDay() * nbDays);

        Set<Integer> usedSiteIds = new HashSet<>();

        for (int day = 1; day <= nbDays; day++) {
            if (remainingBudget <= 0) break;

            Excursion excursion = new Excursion(chosenHotel);
            Coordinates currentPoint = chosenHotel.getCoordinates();
            double dayHours = 0.0;
            int count = 0;

            while (count < maxSitesPerDay) {

                Site next = chooseBestNextSite(
                        currentPoint,
                        remainingBudget,
                        (maxHoursPerDay - dayHours),
                        usedSiteIds
                );

                if (next == null) break;

                // Transport choisi via TransportSelector injecté par Spring
                Transport transport = transportSelector.chooseTransport(
                        currentPoint,
                        next.getCoordinates(),
                        remainingBudget
                );

                if (transport == null) break;

                VisitStep step = new VisitStep(currentPoint, next, transport);

                if (step.getTotalCost() > remainingBudget) {
                    usedSiteIds.add(next.getId());
                    continue;
                }
                if (dayHours + step.getTotalDuration() > maxHoursPerDay) {
                    usedSiteIds.add(next.getId());
                    continue;
                }

                excursion.addStep(step);

                usedSiteIds.add(next.getId());
                remainingBudget -= step.getTotalCost();
                dayHours += step.getTotalDuration();
                currentPoint = next.getCoordinates();

                count++;
            }

            if (!excursion.getSteps().isEmpty()) {
                offer.addExcursion(excursion);
            }
        }

        return offer;
    }

    // ---------------- Méthodes privées ----------------

    private Hotel chooseBestHotel() {
        Hotel best = availableHotels.get(0);
        for (Hotel h : availableHotels) {
            if (h.getPricePerDay() < best.getPricePerDay()) {
                best = h;
            }
        }
        return best;
    }

    private Site chooseBestNextSite(
            Coordinates from,
            double remainingBudget,
            double remainingHours,
            Set<Integer> usedSiteIds
    ) {
        Site best = null;
        double bestScore = Double.MAX_VALUE;

        for (Site s : availableSites) {
            if (usedSiteIds.contains(s.getId())) continue;
            if (s.getEntryPrice() > remainingBudget) continue;
            if (s.getDuration() > remainingHours) continue;

            double score = siteScore(from, s);

            if (prioritizeCheap) {
                score += s.getEntryPrice() * 2.0;
            }

            if (score < bestScore) {
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

        return (wDistance * dist)
                + (wEntry * s.getEntryPrice())
                + (wDuration * s.getDuration())
                - typeBonus;
    }
}
