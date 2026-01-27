package com.agp.web;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

@ManagedBean(name = "entryBean")
@SessionScoped
public class EntryBean implements Serializable {

    private static final long serialVersionUID = 1L;

    // ===== Recherche =====
    private String critere = "SITE_TEXTE";
    private String valeur;

    // ===== Offres =====
    // IMPORTANT : NULL => champ vide au départ
    private Integer nbDays = null;
    private Integer minPrice = null;
    private Integer maxPrice = null;
    private String comfort = null;
    private String descriptionSite = null;

    // ===== Résultats =====
    private List<ResultItem> results = new ArrayList<>();

    // ===== Élément sélectionné (page détail) =====
    private ResultItem selected;

    // ===== Actions =====

    public void search() {
        results.clear();

        // Résultats démo
        results.add(new ResultItem(
                "SITE",
                "Piton de la Fournaise",
                "Volcan actif incontournable de l'île.",
                "hero-reunion.jpg",
                "Site",
                "Sud"
        ));
    }

    public void submit() {
        // petit contrôle serveur (au cas où)
        if (nbDays == null) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Veuillez saisir le nombre de jours.", null));
            return;
        }

        results.clear();

        // Offres démo
        results.add(new ResultItem(
                "OFFRE",
                nbDays + " jours - Séjour Relax",
                "Hôtel + activités soft + découverte du lagon.",
                "offers.jpg",
                "Offre",
                "Ouest"
        ));

        results.add(new ResultItem(
                "OFFRE",
                nbDays + " jours - Séjour Intense",
                "Volcan + randonnée + excursions + 4★.",
                "hero-reunion.jpg",
                "Offre",
                "Sud"
        ));

        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO,
                        "Offres générées avec succès.", null));
    }

    // ✅ RESET PROPRE (vide les champs + résultats)
    public void resetOffer() {
        nbDays = null;
        minPrice = null;
        maxPrice = null;
        comfort = null;
        descriptionSite = null;
        results.clear();
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO,
                        "Formulaire réinitialisé.", null));
    }

    // Navigation vers la page détail
    public String openDetail(ResultItem item) {
        this.selected = item;
        return "detail.xhtml?faces-redirect=true";
    }

    // ===== Getters/Setters =====

    public String getCritere() { return critere; }
    public void setCritere(String critere) { this.critere = critere; }

    public String getValeur() { return valeur; }
    public void setValeur(String valeur) { this.valeur = valeur; }

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

    public List<ResultItem> getResults() { return results; }

    public ResultItem getSelected() { return selected; }
    public void setSelected(ResultItem selected) { this.selected = selected; }

    // ===== Classe interne ResultItem =====
    public static class ResultItem implements Serializable {
        private static final long serialVersionUID = 1L;

        private String type;
        private String title;
        private String description;
        private String image;
        private String badge;
        private String zone;

        public ResultItem() {}

        public ResultItem(String type, String title, String description,
                          String image, String badge, String zone) {
            this.type = type;
            this.title = title;
            this.description = description;
            this.image = image;
            this.badge = badge;
            this.zone = zone;
        }

        public String getType() { return type; }
        public void setType(String type) { this.type = type; }

        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }

        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }

        public String getImage() { return image; }
        public void setImage(String image) { this.image = image; }

        public String getBadge() { return badge; }
        public void setBadge(String badge) { this.badge = badge; }

        public String getZone() { return zone; }
        public void setZone(String zone) { this.zone = zone; }
    }
}
