### О курсе

Курс "Java PRO" от Академии Т1.

### Задание 1
Работа с аннотациями

### Задание 2
Работа со стримами

### Задание 3
Многопоточка, пулы потоков

### Задание 4
Спринг без Бут, самодельные репозитории

### Задание 5
Рест-сервис, самодельные репозитории, немного бута

### Задание 6
Два полноценных рест-сервиса. Spring Boot, БД, генерация API на основе спецификаций OpenApi, тестконтейнеры.

Сервисы можно погонять вручную. Для этого через Идею можно запустить два тестовых класса сервисов и подергать их через Сваггер.
- `src/test/java/org/example/hw06_product_service/Hw06ProductServiceTestApplication#main()`
- `src/test/java/org/example/hw06_payment_service/Hw06PaymentServiceTestApplication#main()`

> Нужен установленный Docker и доступ в интернет для скачивания образа postgres:15-alpine

- [Product service swagger-ui](http://localhost:8090/swagger-ui/index.html)
- [Payment service swagger-ui](http://localhost:8095/swagger-ui/index.html)

Идентификаторы тестовых пользователей: 123 и 125

### Задание 7

Задание 7 полностью аналогично Заданию 6, за исключением того, что база поднимается через Flyway.
