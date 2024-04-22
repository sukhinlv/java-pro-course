# Задание
- Разверните локально postgresql БД, создайте таблицу users (id bigserial primary key, username varchar(255) unique);
- Создайте Maven проект и подключите к нему: драйвер postgresql, hickaricp, spring context.
- Создайте пул соединений в виде Spring бина
- Создайте класс User (Long id, String username)
- Реализуйте в виде бина класс UserDao который позволяет выполнять CRUD операции над пользователями
- Реализуйте в виде бина UserService, который позволяет: создавать, удалять, получать одного, получать всех пользователей из базы данных
- Создайте SpringContext, получите из него бин UserService и выполните все возможные операции

# Запуск приложения

`mvn clean install -pl hw04`

Для запуска нужен установленный Docker и доступ в интернет для скачивания образа postgres:15-alpine