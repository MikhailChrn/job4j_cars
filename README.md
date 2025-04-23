## Проект "Веб-Сервис АвтоМаг".

### Описание задачи:
Цель проекта - разработка веб-приложения для продажи машин.

Основной функционал включает:

1. Основная страница. таблица со всеми объявлениям машин на продажу.
2. На странице реализована кнопка. добавить новое объявление.
3. Реализован переход на страницу добавления.
4. Реализован выбор марки машин, тип кузова и двигателя.
5. Объявление имеет статус продано. или нет.
7. Реализовано существование пользователей в приложении. только тот пользователь, который подал объявление, может вносить изменения.

### Используемые технологии:
+ Java 17
+ Thymeleaf
+ Spring Boot
+ Hibernate

### Окружение:
+ Java 17
+ Maven
+ PostgreSQL

### Запуск приложения

1. Создайте базу данных PostgreSQL
``` sql
CREATE USER cars WITH PASSWORD 'cars';
CREATE DATABASE cars
GRANT ALL PRIVILEGES ON DATABASE cars to cars;
```

2. Клонируйте репозиторий
``` bash
cd job4j_cars
git clone https://github.com/MikhailChrn/job4j_cars
```

3. Соберите проект с помощью Maven:
``` bash
mvn clean install 
```

4. Запустите приложение:
``` bash
mvn spring-boot:run
```
5. После запуска:
Проект доступен по адресу: [http://localhost:8080](http://localhost:8080)

### Взаимодействие с приложением

1. Вход

![Вход](img/screenshots/01_enter.png)

2. Главная

![Главная](img/screenshots/02_main.png)

3. Добавить новое объявление

![Создать](img/screenshots/03_add_new_car.png)

4. Просмотр существующего объявления

![Просмотр1](img/screenshots/04_post_view.png)

5. Просмотр существующего объявления 

![Просмотр2](img/screenshots/05_owner_post_view.png)

6. Изменение цены объявления

![Цена](img/screenshots/06_price_change.png)
![Цена](img/screenshots/07_price_change.png)


### Контакты

mikhail.cherneyev@yandex.ru
