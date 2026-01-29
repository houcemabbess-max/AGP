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

    // ===== Champs formulaire =====
    private Integer nbDays;
    private Integer minPrice;
    private Integer maxPrice;
    private String comfort;
    private String descriptionSite;

    // ===== Résultats =====
    private final List<Object> offers = new ArrayList<>();

    /** Bouton "Construire des offres" */
    public void buildOffers() {
        offers.clear();

        // nbDays optionnel, mais si rempli => > 0
        if (nbDays != null && nbDays <= 0) {
            msg(FacesMessage.SEVERITY_ERROR, "Le nombre de jours doit être supérieur à 0.");
            return;
        }

        // budgets optionnels, mais si remplis => >= 0
        if (minPrice != null && minPrice < 0) {
            msg(FacesMessage.SEVERITY_ERROR, "Le budget minimum ne peut pas être négatif.");
            return;
        }

        if (maxPrice != null && maxPrice < 0) {
            msg(FacesMessage.SEVERITY_ERROR, "Le budget maximum ne peut pas être négatif.");
            return;
        }

        // règle demandée : min <= max
        if (minPrice != null && maxPrice != null && minPrice > maxPrice) {
            msg(FacesMessage.SEVERITY_ERROR, "Le budget minimum doit être inférieur ou égal au budget maximum.");
            return;
        }

        // (optionnel) confort peut être obligatoire ou non — je laisse optionnel
        // if (comfort == null || comfort.trim().isEmpty()) { ... }

        // DB pas branchée
        msg(FacesMessage.SEVERITY_WARN, "Catalogue/DB non branché : aucune offre générée pour le moment.");
    }

    /** Bouton "Réinitialiser" (IMPORTANT : même nom que dans offer.xhtml) */
    public void reset() {
        nbDays = null;
        minPrice = null;
        maxPrice = null;
        comfort = null;
        descriptionSite = null;
        offers.clear();

        msg(FacesMessage.SEVERITY_INFO, "Formulaire réinitialisé.");
    }

    private void msg(FacesMessage.Severity sev, String text) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(sev, text, null));
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
