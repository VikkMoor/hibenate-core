package les2;

import io.github.cdimascio.dotenv.Dotenv;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class Main {

    public static void main(String[] args) {

        Dotenv dotenv = Dotenv.load();

        Configuration configuration = new Configuration()
                .configure()
                .setProperty(
                        "hibernate.connection.password",
                        dotenv.get("DB_PASSWORD")
                );

        SessionFactory sessionFactory =
                configuration.buildSessionFactory();

        Movie movie = new Movie(
                "The Matrix",
                "Sci-Fi",
                1999
        );

        System.out.println("1. TRANSIENT");
        System.out.println("Объект создан через new:");
        System.out.println(movie);

        // --------------------------------------------------
        // 2. TRANSIENT -> PERSISTENT
        // --------------------------------------------------

        Session session = sessionFactory.openSession();
        session.beginTransaction();

        session.persist(movie);

        System.out.println("\n2. PERSISTENT");
        System.out.println("Объект передан в session.persist():");
        System.out.println(movie);

        session.getTransaction().commit();

        // --------------------------------------------------
        // 3. PERSISTENT -> DETACHED
        // --------------------------------------------------

        session.close();

        System.out.println("\n3. DETACHED");
        System.out.println("Сессия закрыта:");
        System.out.println(movie);

        // --------------------------------------------------
        // 4. Изменение detached-объекта
        // --------------------------------------------------

        movie.setTitle("The Matrix Reloaded");

        System.out.println("\n4. DETACHED после изменения");
        System.out.println("Объект в памяти:");
        System.out.println(movie);

        Session checkSession = sessionFactory.openSession();

        Movie movieFromDb = checkSession.find(Movie.class, movie.getId());

        System.out.println("Объект в БД:");
        System.out.println(movieFromDb);

        checkSession.close();

        // --------------------------------------------------
        // 5. DETACHED -> PERSISTENT через merge()
        // --------------------------------------------------

        session = sessionFactory.openSession();
        session.beginTransaction();

        Movie managedMovie = session.merge(movie);

        System.out.println("\n5. PERSISTENT после merge()");
        System.out.println("Объект после merge():");
        System.out.println(managedMovie);

        managedMovie.setGenre("Action");

        System.out.println("Изменили persistent-объект:");
        System.out.println(managedMovie);

        session.getTransaction().commit();
        session.close();

        Session checkSessionAfterMerge = sessionFactory.openSession();

        Movie movieFromDbAfterMerge =
                checkSessionAfterMerge.find(Movie.class, movie.getId());

        System.out.println("\nБД после commit():");
        System.out.println(movieFromDbAfterMerge);

        checkSessionAfterMerge.close();

        // --------------------------------------------------
        // 6. PERSISTENT -> REMOVED
        // --------------------------------------------------

        session = sessionFactory.openSession();
        session.beginTransaction();

        Movie movieForDelete =
                session.find(Movie.class, movie.getId());

        System.out.println("\n6. PERSISTENT перед удалением");
        System.out.println(movieForDelete);

        session.remove(movieForDelete);

        System.out.println("\n7. REMOVED");
        System.out.println("Объект передан в session.remove():");
        System.out.println(movieForDelete);

        session.getTransaction().commit();

        System.out.println("\n8. Удаление завершено");
        System.out.println("Транзакция закоммичена. Запись должна исчезнуть из БД.");

        session.close();
        sessionFactory.close();
    }
}