package controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

@ManagedBean(name = "searchBean")
@ViewScoped
public class SearchBean implements Serializable {

    private static final long serialVersionUID = 1L;

    // ==========================
    // ANCIENS CHAMPS (compat)
    // ==========================
    private String type = "";   // existait déjà dans ton bean
    private String value = "";  // existait déjà dans ton bean

    // ==========================
    // NOUVEAUX CHAMPS (demandés)
    // ==========================
    private String typeRecherche = "";   // "SITE" | "HOTEL"
    private String critereHotel = "";    // "NOM" | "ETOILES" | "PRIX"

    // SITE
    private String motCleSite = "";

    // HOTEL
    private String nomHotel = "";
    private Integer etoiles;             // 1..5
    private Double prixMinNuit;          // >=0
    private Double prixMaxNuit;          // >=0

    // Résultats
    private List<String> results = new ArrayList<>();

    // ==========================
    // ACTIONS
    // ==========================

    /** Compat: si ta page appelle encore action="#{searchBean.search}" */
    public void search() {
        rechercher();
    }

    /** Version demandée (plus propre) */
    public void rechercher() {
        results.clear();
        FacesContext ctx = FacesContext.getCurrentInstance();

        // Synchronisation si ta page utilise encore "type/value"
        // (ex: type="Site (mot-clé)" ou "Hôtel (nom)" etc.)
        if ((typeRecherche == null || typeRecherche.isEmpty()) && type != null && !type.isEmpty()) {
            // si ton type contient "Site" / "Hôtel" on devine
            if (type.toLowerCase().contains("site")) typeRecherche = "SITE";
            if (type.toLowerCase().contains("hôtel") || type.toLowerCase().contains("hotel")) typeRecherche = "HOTEL";
        }

        // -------- Validation typeRecherche --------
        if (typeRecherche == null || typeRecherche.trim().isEmpty()) {
            error("Veuillez choisir un type de recherche (Site ou Hôtel).");
            return;
        }

        // -------- SITE --------
        if ("SITE".equals(typeRecherche)) {
            // si tu utilises l'ancien champ value comme mot-clé
            if ((motCleSite == null || motCleSite.trim().isEmpty()) && value != null && !value.trim().isEmpty()) {
                motCleSite = value;
            }

            if (motCleSite == null || motCleSite.trim().isEmpty()) {
                error("Veuillez saisir un mot-clé pour rechercher un site.");
                return;
            }

            info("Recherche SITE validée (DB non branchée).");
            return;
        }

        // -------- HOTEL --------
        if (!"HOTEL".equals(typeRecherche)) {
            error("Type de recherche invalide.");
            return;
        }

        if (critereHotel == null || critereHotel.trim().isEmpty()) {
            error("Veuillez choisir un critère pour l'hôtel (Nom, Étoiles, Prix/nuit).");
            return;
        }

        switch (critereHotel) {
            case "NOM":
                if (nomHotel == null || nomHotel.trim().isEmpty()) {
                    // compat: ancien champ value
                    if (value != null && !value.trim().isEmpty()) nomHotel = value;
                }
                if (nomHotel == null || nomHotel.trim().isEmpty()) {
                    error("Veuillez saisir le nom de l'hôtel.");
                    return;
                }
                break;

            case "ETOILES":
                if (etoiles == null || etoiles < 1 || etoiles > 5) {
                    error("Le nombre d'étoiles doit être entre 1 et 5.");
                    return;
                }
                break;

            case "PRIX":
                if (prixMinNuit != null && prixMinNuit < 0) {
                    error("Le prix minimum par nuit ne peut pas être négatif.");
                    return;
                }
                if (prixMaxNuit != null && prixMaxNuit < 0) {
                    error("Le prix maximum par nuit ne peut pas être négatif.");
                    return;
                }
                if (prixMinNuit != null && prixMaxNuit != null && prixMinNuit > prixMaxNuit) {
                    error("Le prix minimum par nuit doit être inférieur ou égal au prix maximum.");
                    return;
                }
                if (prixMinNuit == null && prixMaxNuit == null) {
                    error("Veuillez saisir au moins un prix (min ou max).");
                    return;
                }
                break;

            default:
                error("Critère hôtel invalide.");
                return;
        }

        info("Recherche HÔTEL validée (DB non branchée).");
    }

    public void reset() {
        type = "";
        value = "";

        typeRecherche = "";
        critereHotel = "";

        motCleSite = "";
        nomHotel = "";
        etoiles = null;
        prixMinNuit = null;
        prixMaxNuit = null;

        results.clear();
    }

    // ==========================
    // MESSAGES
    // ==========================
    private void error(String msg) {
        FacesContext.getCurrentInstance().addMessage(null,
            new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, null));
    }

    private void info(String msg) {
        FacesContext.getCurrentInstance().addMessage(null,
            new FacesMessage(FacesMessage.SEVERITY_INFO, msg, null));
    }

    // ==========================
    // GETTERS / SETTERS
    // ==========================

    // --- anciens (compat) ---
    public String getType() { return type; }
    public void setType(String type) { this.type = type == null ? "" : type; }

    public String getValue() { return value; }
    public void setValue(String value) { this.value = value == null ? "" : value; }

    // --- nouveaux ---
    public String getTypeRecherche() { return typeRecherche; }
    public void setTypeRecherche(String typeRecherche) {
        this.typeRecherche = typeRecherche == null ? "" : typeRecherche;
        // synchro vers ancien champ
        this.type = this.typeRecherche;
    }

    public String getCritereHotel() { return critereHotel; }
    public void setCritereHotel(String critereHotel) { this.critereHotel = critereHotel == null ? "" : critereHotel; }

    public String getMotCleSite() { return motCleSite; }
    public void setMotCleSite(String motCleSite) { this.motCleSite = motCleSite == null ? "" : motCleSite; }

    public String getNomHotel() { return nomHotel; }
    public void setNomHotel(String nomHotel) { this.nomHotel = nomHotel == null ? "" : nomHotel; }

    public Integer getEtoiles() { return etoiles; }
    public void setEtoiles(Integer etoiles) { this.etoiles = etoiles; }

    public Double getPrixMinNuit() { return prixMinNuit; }
    public void setPrixMinNuit(Double prixMinNuit) { this.prixMinNuit = prixMinNuit; }

    public Double getPrixMaxNuit() { return prixMaxNuit; }
    public void setPrixMaxNuit(Double prixMaxNuit) { this.prixMaxNuit = prixMaxNuit; }

    public List<String> getResults() { return results; }
    public void setResults(List<String> results) { this.results = results == null ? new ArrayList<>() : results; }
}
