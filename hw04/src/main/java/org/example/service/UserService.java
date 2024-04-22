package org.example.service;

import org.example.dao.UserDao;
import org.example.dao.model.User;
import org.example.error.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class UserService {

    private final UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public User getUserById(long id) {
        return userDao.findById(id)
                .orElseThrow(() -> new NotFoundException("User with id %d not found".formatted(id)));
    }

    public void saveUser(User user) {
        Objects.requireNonNull(user);
        userDao.save(user);
    }

    public void deleteUserById(long id) {
        userDao.deleteById(id);
    }

    public List<User> getAllUsers() {
        return userDao.findAll();
    }
}
