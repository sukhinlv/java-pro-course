package org.example.dao;

import java.util.List;
import java.util.Optional;

public interface AbstractRepository<T, K> {

    Optional<T> findById(K id);
    void save(T t);
    void deleteById(K id);
    List<T> findAll();
}
