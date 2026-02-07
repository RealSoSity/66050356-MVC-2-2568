package repository;

import java.util.List;

import models.Rumour;

public interface RumourRepository { // CRUD operations for Rumour
    List<Rumour> findAll();

    Rumour findById(String id);

    void save(Rumour rumour);

    void update(Rumour rumour);

}
