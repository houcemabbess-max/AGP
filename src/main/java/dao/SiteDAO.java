package dao;

import java.util.List;
import business.Site;
import business.Island;
import business.tools.TypeSite;

/**
 * DAO pour l'accès aux sites touristiques.
 */
public interface SiteDAO {

    // Méthodes de base
    Site findById(int id);

    List<Site> findAll();

    List<Site> findByIsland(Island island);

    // Filtres (fourchette de prix, type, durée)
    List<Site> findByEntryPriceBetween(double min, double max);

    List<Site> findByDurationBetween(double min, double max);

    List<Site> findByType(TypeSite type);

    List<Site> findByIslandAndType(Island island, TypeSite type);

    // Recherche textuelle (mot-clé JSF)
    List<Site> searchByKeywords(String keywords);

    List<Site> searchByIslandAndKeywords(
            Island island,
            String keywords
    );
}
