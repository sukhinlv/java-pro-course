package org.example;


import org.example.config.AppConfig;
import org.example.dao.model.User;
import org.example.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.List;

class AppTest {

    @Test
    void run_app() {
        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext()) {
            new TestcontainersInitializer().initialize(context);
            context.register(AppConfig.class);
            context.refresh();

            UserService userService = context.getBean("userService", UserService.class);

            User someUser = new User(123L, "Семенов Иван Петрович");
            User anotherUser = new User(124L, "Петрова Мария Ивановна");

            userService.saveUser(someUser);
            userService.saveUser(anotherUser);

            List<User> allUsers = userService.getAllUsers();
            System.out.println("User list: " + allUsers);

            System.out.println("User with id 123: " + userService.getUserById(123L));

            System.out.println("Deleting user with id 123");
            userService.deleteUserById(someUser.getId());
            System.out.println("Updated user list: " + userService.getAllUsers());
        }
    }
}
