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

    // ===== Résultats (vide tant que DB non branchée) =====
    private final List<Object> offers = new ArrayList<>();

    // ===== Actions =====

    /** Bouton "Construire des offres" */
    public void buildOffers() {
        offers.clear();

        // Validations simples
        if (nbDays == null || nbDays < 1) {
            msg(FacesMessage.SEVERITY_ERROR, "Nombre de jours invalide (>= 1).");
            return;
        }
        if (comfort == null || comfort.trim().isEmpty()) {
            msg(FacesMessage.SEVERITY_ERROR, "Veuillez choisir un confort.");
            return;
        }
        if (minPrice != null && maxPrice != null && minPrice > maxPrice) {
            msg(FacesMessage.SEVERITY_ERROR, "Budget min ne peut pas être supérieur au budget max.");
            return;
        }

        // ✅ IMPORTANT : tant que DB/catalogue pas branché => ne rien générer
        msg(FacesMessage.SEVERITY_WARN,
            "Catalogue/DB non branché : aucune offre générée pour le moment.");

        // Quand tu branches la DB/catalogue, tu mettras ici :
        // OfferBuilder builder = ... (mais attention: ton OfferBuilder est prévu pour Spring)
        // offers.add(offer);
    }

    /** Bouton "Réinitialiser" */
    public void resetOffer() {
        nbDays = null;
        minPrice = null;
        maxPrice = null;
        comfort = null;
        descriptionSite = null;
        offers.clear();

        msg(FacesMessage.SEVERITY_INFO, "Formulaire réinitialisé.");
    }

    // ===== Utils =====
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
