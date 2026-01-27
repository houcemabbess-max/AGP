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

    // Champs du formulaire
    private String type = "";     // IMPORTANT: existe + getter/setter
    private String value = "";

    // Résultats
    private List<String> results = new ArrayList<>();

    // Action: Rechercher
    public void search() {
        results.clear();

        // Pour l’instant tu n’as pas branché la DB, donc on n’affiche rien.
        // On met juste un message d’info.
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO,
                        "Recherche lancée",
                        "Base de données non connectée : aucun résultat pour le moment."));
    }

    // Action: Réinitialiser
    public void reset() {
        type = "";
        value = "";
        results.clear();

        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO,
                        "Réinitialisation",
                        "Critères effacés."));
    }

    // Getters / Setters (OBLIGATOIRES pour JSF EL)
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = (type == null) ? "" : type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = (value == null) ? "" : value;
    }

    public List<String> getResults() {
        return results;
    }

    public void setResults(List<String> results) {
        this.results = (results == null) ? new ArrayList<>() : results;
    }
}
