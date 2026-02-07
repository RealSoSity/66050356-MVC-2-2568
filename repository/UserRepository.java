package repository;

import java.util.List;

import models.User;

public interface UserRepository { // CRUD operations for User
    List<User> findAll();

    User findById(String id);
}
