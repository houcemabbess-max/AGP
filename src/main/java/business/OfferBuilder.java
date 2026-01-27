package business;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * OfferBuilder (Business) :
 * Construit automatiquement une Offer à partir d'un catalogue (hotels + sites),
 * en appliquant des règles métier :
 *  - Budget max global
 *  - Nombre de jours
 *  - Max de sites par jour
 *  - Max d'heures par jour
 *  - Choix du transport via TransportSelector (géré par Spring)
 *
 * IMPORTANT (Spring) :
 * - @Component : Spring crée et gère l'objet OfferBuilder
 * - @Scope("prototype") : chaque getBean() renvoie un builder "neuf"
 *   (car OfferBuilder contient de l'état: nbDays, budgetMax, catalogue, etc.)
 */
@Component
@Scope("prototype")
public class OfferBuilder {

    // ----------------------------
    // Dépendances Spring (IoC)
    // ----------------------------

    /**
     * Dépendance métier "complexe" : choisir un transport selon distance/budget.
     * Injectée par Spring (pas de new TransportSelector()).
     */
    private final TransportSelector transportSelector;

    /**
     * Injection par constructeur : Spring fournit automatiquement TransportSelector.
     */
    @Autowired
    public OfferBuilder(TransportSelector transportSelector) {
        this.transportSelector = transportSelector;
    }

    // ----------------------------
    // Catalogue (données d'entrée)
    // ----------------------------

    /**
     * Hôtels disponibles pour construire l'offre (catalogue).
     * Remplis via withCatalog(...).
     */
    private List<Hotel> availableHotels = new ArrayList<>();

    /**
     * Sites disponibles pour construire les excursions (catalogue).
     * Remplis via withCatalog(...).
     */
    private List<Site> availableSites = new ArrayList<>();

    // ----------------------------
    // Paramètres de construction (règles)
    // ----------------------------

    /** Nombre de jours du séjour (>= 1). */
    private int nbDays = 1;

    /** Budget maximum total autorisé. */
    private double budgetMax = Double.MAX_VALUE;

    /** Nombre maximum de sites visités par jour. */
    private int maxSitesPerDay = 3;

    /** Durée maximale (heures) de visite/transport par jour. */
    private double maxHoursPerDay = 8.0;

    // ----------------------------
    // Préférences (optionnelles)
    // ----------------------------

    /**
     * Types de sites préférés (optionnel).
     * Tu peux l'utiliser si tu veux un scoring plus "intelligent".
     * (Ici on garde simple, mais c'est prêt.)
     */
    private final Set<TypeSite> preferredTypes = new HashSet<>();

    /**
     * Si true, on pénalise davantage les sites chers (entryPrice).
     */
    private boolean prioritizeCheap = true;

    // ----------------------------
    // Pondérations (scoring)
    // ----------------------------

    /** Bonus (ou pénalité) sur le type préféré (optionnel). */
    private double wType = 30.0;

    /** Pénalité liée à la distance. */
    private double wDistance = 10.0;

    /** Pénalité liée au prix d'entrée. */
    private double wEntry = 2.0;

    /** Pénalité liée à la durée de visite. */
    private double wDuration = 1.5;

    // ----------------------------
    // "Fluent API" : configuration
    // ----------------------------

    /**
     * Donne le catalogue au builder : indispensable avant build().
     */
    public OfferBuilder withCatalog(List<Hotel> hotels, List<Site> sites) {
        this.availableHotels = (hotels != null) ? hotels : new ArrayList<>();
        this.availableSites = (sites != null) ? sites : new ArrayList<>();
        return this;
    }

    /**
     * Fixe le nombre de jours (>= 1).
     */
    public OfferBuilder withNbDays(int nbDays) {
        this.nbDays = Math.max(1, nbDays);
        return this;
    }

    /**
     * Fixe le budget max (>= 0).
     */
    public OfferBuilder withBudgetMax(double budgetMax) {
        this.budgetMax = Math.max(0, budgetMax);
        return this;
    }

    /**
     * Fixe le nombre max de sites par jour (>= 1).
     */
    public OfferBuilder withMaxSitesPerDay(int max) {
        this.maxSitesPerDay = Math.max(1, max);
        return this;
    }

    /**
     * Fixe la durée max par jour (>= 1.0).
     */
    public OfferBuilder withMaxHoursPerDay(double hours) {
        this.maxHoursPerDay = Math.max(1.0, hours);
        return this;
    }

    /**
     * Active/désactive la préférence "pas cher".
     */
    public OfferBuilder prioritizeCheap(boolean value) {
        this.prioritizeCheap = value;
        return this;
    }

    // ----------------------------
    // Construction principale
    // ----------------------------

    /**
     * Construit l'offre complète :
     * 1) choisit un hôtel
     * 2) calcule budget restant
     * 3) pour chaque jour : crée une excursion et choisit des sites faisables
     * 4) choisit un transport pour chaque étape (via TransportSelector)
     * 5) ajoute excursions à l'Offer
     */
    public Offer build() {
        // Sécurité : impossible de construire sans hôtels
        if (availableHotels.isEmpty()) {
            throw new IllegalStateException("Aucun hotel disponible pour construire une offre.");
        }

        // 1) Créer l'offre
        Offer offer = new Offer(nbDays);

        // 2) Choisir et ajouter l'hôtel
        Hotel chosenHotel = chooseBestHotel();
        offer.addHotel(chosenHotel);

        // 3) Budget restant après paiement de l'hôtel sur nbDays
        double remainingBudget = budgetMax - (chosenHotel.getPricePerDay() * nbDays);

        // Pour éviter de choisir 2 fois le même site (optionnel mais propre)
        Set<Integer> usedSiteIds = new HashSet<>();

        // 4) Construire une excursion par jour
        for (int day = 1; day <= nbDays; day++) {
            if (remainingBudget <= 0) break; // plus de budget => stop

            // Excursion commence toujours à l'hôtel
            Excursion excursion = new Excursion(chosenHotel);

            // Point de départ courant (au début = hôtel)
            Coordinates currentPoint = chosenHotel.getCoordinates();

            // Temps déjà consommé sur la journée
            double dayHours = 0.0;

            // Nombre de sites déjà ajoutés ce jour
            int count = 0;

            // 5) Ajouter jusqu'à maxSitesPerDay sites faisables
            while (count < maxSitesPerDay) {

                // Choisir le meilleur site faisable selon budget/temps + score
                Site next = chooseBestNextSite(
                        currentPoint,
                        remainingBudget,
                        (maxHoursPerDay - dayHours),
                        usedSiteIds
                );

                if (next == null) break; // aucun site faisable => stop pour ce jour

                // Choisir transport optimal (comparaison Bus/Foot/Boat...)
                Transport transport = transportSelector.chooseTransport(
                        currentPoint,
                        next.getCoordinates(),
                        remainingBudget
                );

                if (transport == null) break;

                // Créer l'étape (départ -> site + transport)
                VisitStep step = new VisitStep(currentPoint, next, transport);

                // Vérifier contraintes budget/temps réelles (après création step)
                if (step.getTotalCost() > remainingBudget) {
                    usedSiteIds.add(next.getId()); // marque comme pas faisable dans ce contexte
                    continue;
                }
                if (dayHours + step.getTotalDuration() > maxHoursPerDay) {
                    usedSiteIds.add(next.getId());
                    continue;
                }

                // Ajouter l'étape à l'excursion
                excursion.addStep(step);

                // Marquer le site comme utilisé
                usedSiteIds.add(next.getId());

                // Mettre à jour budget / temps / position
                remainingBudget -= step.getTotalCost();
                dayHours += step.getTotalDuration();
                currentPoint = next.getCoordinates();

                count++;
            }

            // Ajouter excursion seulement si elle contient au moins une étape
            if (!excursion.getSteps().isEmpty()) {
                offer.addExcursion(excursion);
            }
        }

        return offer;
    }

    // ----------------------------
    // Choix de l'hôtel
    // ----------------------------

    /**
     * Choisit l'hôtel "le meilleur" selon une règle simple.
     * Ici : le moins cher.
     * (Tu peux remplacer par un vrai score si tu veux.)
     */
    private Hotel chooseBestHotel() {
        Hotel best = availableHotels.get(0);
        for (Hotel h : availableHotels) {
            if (h.getPricePerDay() < best.getPricePerDay()) {
                best = h;
            }
        }
        return best;
    }

    // ----------------------------
    // Choix des sites
    // ----------------------------

    /**
     * Choisit le meilleur prochain site en respectant :
     * - pas déjà visité (usedSiteIds)
     * - entryPrice <= budget restant
     * - duration <= heures restantes
     *
     * Puis on utilise un score (plus petit = meilleur).
     */
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

            // contraintes basiques
            if (s.getEntryPrice() > remainingBudget) continue;
            if (s.getDuration() > remainingHours) continue;

            // score = distance + prix + durée (pondérés)
            double score = siteScore(from, s);

            // option : si on veut vraiment "cheap", on pénalise plus le prix
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

    /**
     * Calcule un score simple pour comparer 2 sites.
     * Ici : distance + prix + durée (pondérés).
     * Plus petit => meilleur.
     */
    private double siteScore(Coordinates from, Site s) {
        double dist = GeoUtils.distance(from, s.getCoordinates());

        // Bonus si type préféré (optionnel)
        double typeBonus = 0.0;
        if (!preferredTypes.isEmpty() && preferredTypes.contains(s.getType())) {
            typeBonus = wType;
        }

        // On garde la logique "plus petit = meilleur" :
        // donc on soustrait le bonus (ça réduit le score)
        return (wDistance * dist)
                + (wEntry * s.getEntryPrice())
                + (wDuration * s.getDuration())
                - typeBonus;
    }
}
