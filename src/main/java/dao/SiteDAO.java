package dao;

import java.util.List;
import business.Site;
import business.Island;
import business.TypeSite;

public interface SiteDAO {
    Site findById(int id);
    List<Site> findAll();
    List<Site> findByIsland(Island island);

    List<Site> findByEntryPriceBetween(double min, double max);
    List<Site> findByDurationBetween(double min, double max);

    List<Site> findByType(TypeSite type);
    List<Site> findByIslandAndType(Island island, TypeSite type);

    List<Site> searchByKeywords(String keywords);
}