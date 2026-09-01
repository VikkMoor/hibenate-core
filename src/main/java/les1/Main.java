package les1;


import io.github.cdimascio.dotenv.Dotenv;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.util.List;

public class Main {

    public static void main(String[] args) {

        Dotenv dotenv = Dotenv.load();

        SessionFactory factory = new Configuration()
                .configure()
                .setProperty(
                        "hibernate.connection.password",
                        dotenv.get("DB_PASSWORD")
                )
                .buildSessionFactory();

        // CREATE
        Session session = factory.openSession();

        Transaction transaction = session.beginTransaction();

        Movie movie1 = new Movie("Интерстеллар", "Sci-Fi", 2014);
        session.persist(movie1);

        Movie movie2 = new Movie("Матрица", "Sci-Fi", 1999);
        session.persist(movie2);

        Movie movie3 = new Movie("Маска", "Comedy", 1994);
        session.persist(movie3);

        transaction.commit();
        session.close();


        // READ: все фильмы
        session = factory.openSession();

        List<Movie> movies = session.createQuery(
                "SELECT m FROM Movie m", Movie.class
        ).getResultList();

        for (Movie movie : movies) {
            System.out.println(
                    movie.getId() + " | " +
                            movie.getTitle() + " | " +
                            movie.getGenre() + " | " +
                            movie.getReleaseYear()
            );
        }


        // READ: фильмы определённого жанра
        List<Movie> comedyMovies = session.createQuery(
                        "SELECT m FROM Movie m WHERE m.genre = :genre",
                        Movie.class
                )
                .setParameter("genre", "Comedy")
                .getResultList();

        System.out.println("\nФильмы жанра Comedy:");

        for (Movie movie : comedyMovies) {
            System.out.println(
                    movie.getId() + " | " +
                            movie.getTitle() + " | " +
                            movie.getGenre() + " | " +
                            movie.getReleaseYear()
            );
        }

        session.close();


        // UPDATE
        session = factory.openSession();

        transaction = session.beginTransaction();

        Movie movieToUpdate = session.get(Movie.class, 1L);

        if (movieToUpdate != null) {
            movieToUpdate.setTitle("Интерстеллар: Обновлённое название");
        }

        transaction.commit();
        session.close();


        // DELETE
        session = factory.openSession();

        transaction = session.beginTransaction();

        Movie movieToDelete = session.get(Movie.class, 3L);

        if (movieToDelete != null) {
            session.remove(movieToDelete);
        }

        transaction.commit();
        session.close();


        // READ: оставшиеся фильмы
        session = factory.openSession();

        List<Movie> remainingMovies = session.createQuery(
                "SELECT m FROM Movie m", Movie.class
        ).getResultList();

        System.out.println("\nОставшиеся фильмы:");

        for (Movie movie : remainingMovies) {
            System.out.println(
                    movie.getId() + " | " +
                            movie.getTitle() + " | " +
                            movie.getGenre() + " | " +
                            movie.getReleaseYear()
            );
        }

        session.close();

        factory.close();
    }
}