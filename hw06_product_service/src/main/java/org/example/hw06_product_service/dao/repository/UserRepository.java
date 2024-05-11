package org.example.hw06_product_service.dao.repository;

import org.example.hw06_product_service.dao.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
}
