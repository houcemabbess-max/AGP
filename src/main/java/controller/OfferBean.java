package controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

@ManagedBean(name = "offerBean")
@ViewScoped
public class OfferBean implements Serializable {

    private static final long serialVersionUID = 1L;

    // ===== Champs formulaire (liés à offer.xhtml) =====
    private Integer nbDays;          // Nombre de jours (optionnel)
    private Integer minPrice;        // Budget min
    private Integer maxPrice;        // Budget max
    private String comfort;          // Confort / rythme
    private String descriptionSite;  // Mot-clé (site)

    // ===== Résultats =====
    private final List<Object> offers = new ArrayList<>();

    // ===== Actions =====

    /** Bouton "Construire des offres" */
    public void buildOffers() {
        offers.clear();
        FacesContext context = FacesContext.getCurrentInstance();

        // 1) Nombre de jours : optionnel, mais si rempli => >= 1
        if (nbDays != null && nbDays <= 0) {
            context.addMessage(null, new FacesMessage(
                    FacesMessage.SEVERITY_ERROR,
                    "Le nombre de jours doit être supérieur à 0.",
                    null));
            return;
        }

        // 2) Budgets : pas négatifs
        if (minPrice != null && minPrice < 0) {
            context.addMessage(null, new FacesMessage(
                    FacesMessage.SEVERITY_ERROR,
                    "Le budget minimum ne peut pas être négatif.",
                    null));
            return;
        }

        if (maxPrice != null && maxPrice < 0) {
            context.addMessage(null, new FacesMessage(
                    FacesMessage.SEVERITY_ERROR,
                    "Le budget maximum ne peut pas être négatif.",
                    null));
            return;
        }

        // 3) Règle demandée : budgetMin <= budgetMax
        if (minPrice != null && maxPrice != null && minPrice > maxPrice) {
            context.addMessage(null, new FacesMessage(
                    FacesMessage.SEVERITY_ERROR,
                    "Le budget minimum doit être inférieur ou égal au budget maximum.",
                    null));
            return;
        }

        // (Optionnel) si tu veux rendre confort obligatoire, décommente :
        // if (comfort == null || comfort.trim().isEmpty()) {
        //     context.addMessage(null, new FacesMessage(
        //             FacesMessage.SEVERITY_ERROR,
        //             "Veuillez choisir un confort / rythme.",
        //             null));
        //     return;
        // }

        // ✅ Pour l’instant pas de DB / catalogue branché => on met juste un message
        context.addMessage(null, new FacesMessage(
                FacesMessage.SEVERITY_INFO,
                "Critères validés. (Catalogue/DB non branché : aucune offre générée pour le moment.)",
                null));
    }

    /** Bouton "Réinitialiser" */
    public void reset() {
        nbDays = null;
        minPrice = null;
        maxPrice = null;
        comfort = null;
        descriptionSite = null;
        offers.clear();
    }

    // ===== Getters / Setters =====

    public Integer getNbDays() { return nbDays; }
    public void setNbDays(Integer nbDays) { this.nbDays = nbDays; }

    public Integer getMinPrice() { return minPrice; }
    public void setMinPrice(Integer minPrice) { this.minPrice = minPrice; }

    public Integer getMaxPrice() { return maxPrice; }
    public void setMaxPrice(Integer maxPrice) { this.maxPrice = maxPrice; }

    public String getComfort() { return comfort; }
    public void setComfort(String comfort) { this.comfort = comfort; }

    public String getDescriptionSite() { return descriptionSite; }
    public void setDescriptionSite(String descriptionSite) { this.descriptionSite = descriptionSite; }

    public List<Object> getOffers() { return offers; }
}
