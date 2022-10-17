# Тестовое задание для ШБР Яндекс 2022г.
## Описание
В данном репозитории реализован REST API сервиc, который позволяет выполнять следующие действия путём обращения к API сервиса:
1) /imports - Импортирование новых товаров и/или категорий,
2) /delete{id} - Удаление элемента и всех его дочерних элементов по идентификатору (UUID).
3) /nodes/{id} - Получение информации об элементе по идентификатору.
4) /sales - Получение списка товаров, цена которых была обновлена за последние 24 часа включительно от времени переданном в запросе.
5) /node/{id}/statistic - Получение статистики (истории обновлений) по товару/категории за заданный полуинтервал
## Инструменты
- Java 11
- Spring Boot
- PostgreSql Database
- H2 Database (For test)
- Swagger UI
- Flyway
- Hibernate Envers
## Сборка
mvn clean package
## Запуск (Для запуска необходим установленный Docker)
1) cd docker
2) docker-compose up
## Дополнительно
Доступ к swagger-ui: http://<ip_адрес>/swagger-ui/index.html. Например, http://localhost/swagger-ui/index.html.