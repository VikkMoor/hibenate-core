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

---

## Запуск проекта

### 1. Настройка базы данных

Проект использует PostgreSQL, запущенный в Docker-контейнере.

Для подключения к базе данных используются параметры из файла `.env`.

Создайте в корне проекта файл `.env` на основе `.env.example`:

```env
DB_PASSWORD=your_postgresql_password
```

Файл `.env` содержит пароль для подключения к PostgreSQL и не добавляется в Git.

### 2. Настройка Hibernate

Основные настройки подключения к PostgreSQL находятся в:

```text
src/main/resources/hibernate.cfg.xml
```

Пароль базы данных передаётся в Hibernate из переменной окружения `DB_PASSWORD`.

### 3. Запуск отдельного урока

Каждый учебный пример находится в отдельном пакете:

```text
les1
les2
...
```

Для запуска конкретного урока необходимо открыть соответствующий класс `Main`.

Перед запуском в `hibernate.cfg.xml` должна быть указана сущность текущего урока в `<mapping>`.

Например, для урока 1:

```xml
<mapping class="les1.Movie"/>
```

Для урока 2:

```xml
<mapping class="les2.Movie"/>
```

Одновременно регистрировать одноимённые сущности из разных пакетов не следует, поскольку Hibernate использует имя сущности `Movie`.

### 4. Запуск

После настройки `.env`, PostgreSQL и `hibernate.cfg.xml` запустите соответствующий класс `Main` из IntelliJ IDEA.

SQL-запросы Hibernate выводятся в консоль, если в конфигурации включено:

```xml
<property name="hibernate.show_sql">true</property>
```
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

---

### Урок 2. Жизненный цикл Entity

Пакет `les2` содержит пример работы с жизненным циклом JPA Entity в Hibernate.

В примере показаны основные состояния объекта:

- **Transient** — объект создан через `new`, но ещё не связан с Hibernate.
- **Persistent** — объект передан в `session.persist()` и находится под управлением текущей Session.
- **Detached** — Session закрыта, объект больше не связан с Hibernate.
- **Removed** — объект передан в `session.remove()` и будет удалён из базы данных после commit.

В процессе выполнения демонстрируется:

1. Создание нового объекта `Movie`.
2. Сохранение объекта через `session.persist()`.
3. Переход объекта в состояние `detached` после закрытия Session.
4. Изменение detached-объекта без синхронизации с базой данных.
5. Проверка, что изменение detached-объекта само по себе не приводит к `UPDATE`.
6. Возврат объекта под управление Hibernate через `session.merge()`.
7. Синхронизация изменений с базой данных при `commit`.
8. Удаление объекта через `session.remove()`.

В консоли можно увидеть соответствующие SQL-операции Hibernate:

```text
INSERT
SELECT
UPDATE
DELETE
```

Для урока используется отдельная таблица:

```text
movies_les2
```

Это позволяет запускать пример независимо от сущности Movie из предыдущего урока.