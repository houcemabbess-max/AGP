package dao;

import java.util.List;
import business.Island;

/**
 * DAO pour l'accès aux îles.
 */
public interface IslandDAO {

    Island findById(int id);

    Island findByName(String name);

    List<Island> findAll();
}