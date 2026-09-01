# Hibernate Core

Учебный проект по изучению Hibernate, JPA и работы с PostgreSQL.

Каждый урок находится в отдельном Java-пакете.

---

## Структура проекта

```text
src/main/java/
├── les1/
│   ├── x.java
│   └── y.java
│
├── les2/
│   ├── n.java
│   └── m.java
└── ...
```

## Технологии
* Java 21
* Hibernate ORM
* JPA
* PostgreSQL
* Maven
* Docker
* DBeaver
* IntelliJ IDEA

## Настройка подключения к PostgreSQL

Данные для подключения к PostgreSQL хранятся в локальном файле `.env`:

```text
DB_PASSWORD=your_postgresql_password
```
Для примера конфигурации в репозитории используется `.env.example`.

После клонирования проекта необходимо создать собственный `.env` на основе этого файла и указать пароль локального PostgreSQL.

---

## Les1. Hibernate CRUD

Реализована работа с сущностью Movie через Hibernate и PostgreSQL.

Изучено:
* создание JPA-сущности с помощью @Entity;
* аннотации @Table, @Id, @GeneratedValue, @Column;
* настройка Hibernate;
* подключение Hibernate к PostgreSQL;
* SessionFactory;
* генерация SQL-запросов Hibernate.

### Сущность Movie

Содержит поля:

| Поле          | Тип      | Описание        |
| ------------- | -------- | --------------- |
| `id`          | `Long`   | идентификатор   |
| `title`       | `String` | название фильма |
| `genre`       | `String` | жанр            |
| `releaseYear` | `int`    | год выпуска     |


Для хранения используется таблица movies в PostgreSQL.

Реализованы CRUD-операции.