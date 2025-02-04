## Проект "Веб-Сервис АвтоМаг".

### Описание задачи:
Цель проекта - разработка веб-приложения для продажи машин.

Основной функционал включает:


### Используемые технологии:
+ Java 17
+ Spring Boot
+ Thymeleaf
+ PostgreSQL

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

![Вход](screenshots/01_enter.png)

2. Главная

![Главная](screenshots/02_main.png)



### Контакты

mikhail.cherneyev@yandex.ru
