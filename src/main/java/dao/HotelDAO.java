package dao;

import java.util.List;
import business.Hotel;
import business.Island;

/**
 * DAO pour l'accès aux hôtels.
 */
public interface HotelDAO {

    // Méthodes de base
    Hotel findById(int id);

    List<Hotel> findAll();

    List<Hotel> findByIsland(Island island);

    // Filtres (utilisés par JSF : fourchette de prix, confort, etc.)
    List<Hotel> findByPricePerDayBetween(double min, double max);

    List<Hotel> findByStarsAtLeast(int minStars);

    List<Hotel> findByIslandAndPricePerDayBetween(
            Island island,
            double min,
            double max
    );

    List<Hotel> findByIslandAndStarsAtLeast(
            Island island,
            int minStars
    );
}
