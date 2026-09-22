package les4;

import io.github.cdimascio.dotenv.Dotenv;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.List;

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

        Session session = sessionFactory.openSession();
        session.beginTransaction();

        User user1 = new User("Анна");
        User user2 = new User("Пётр");
        User user3 = new User("Виктор");

        session.persist(user1);
        session.persist(user2);
        session.persist(user3);

        Post post1 = new Post("Изучаем Hibernate", user1);
        Post post2 = new Post("Работаем с PostgreSQL", user1);
        Post post3 = new Post("Изучаем Java", user1);

        Post post4 = new Post("Spring Framework", user2);
        Post post5 = new Post("REST API", user2);

        Post post6 = new Post("Docker", user3);
        Post post7 = new Post("Git", user3);

        session.persist(post1);
        session.persist(post2);
        session.persist(post3);
        session.persist(post4);
        session.persist(post5);
        session.persist(post6);
        session.persist(post7);

        session.getTransaction().commit();

        Session session2 = sessionFactory.openSession();

        List<Post> posts = session2.createQuery(
                //"SELECT p FROM Post p", Post.class
                "SELECT p FROM Post p JOIN FETCH p.author", Post.class
        ).getResultList();

        System.out.println("\nПосты и их авторы:");

        for (Post post : posts) {
            System.out.println(
                    post.getTitle() + " - " + post.getAuthor().getName()
            );
        }

        session2.close();

        System.out.println("Пользователи и посты сохранены.");

        sessionFactory.close();
    }
}