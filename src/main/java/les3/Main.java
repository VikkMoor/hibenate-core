package les3;

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

        // 1. Сохраняем пользователя
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        User user = new User("Viktoria");
        session.persist(user);

        session.getTransaction().commit();
        session.close();

        System.out.println("\n1. Пользователь сохранён:");
        System.out.println(user);


        // 2. Сохраняем профиль пользователя
        session = sessionFactory.openSession();
        session.beginTransaction();

        UserProfile profile = new UserProfile(
                "viktoria@example.com",
                "+7-900-123-45-67",
                user
        );

        session.persist(profile);

        session.getTransaction().commit();
        session.close();

        System.out.println("\n2. Профиль сохранён:");
        System.out.println(profile);


        // 3. Получаем профиль и связанного пользователя
        session = sessionFactory.openSession();

        System.out.println("\n3. Получаем профиль и связанного пользователя:");

        UserProfile savedProfile =
                session.find(UserProfile.class, profile.getId());

        System.out.println(savedProfile);
        System.out.println("Имя пользователя: "
                + savedProfile.getUser().getName());

        session.close();


        // 4. Получаем пользователя и связанный профиль
        session = sessionFactory.openSession();

        System.out.println("\n4. А теперь пользователь и связанный профиль:");

        User savedUser =
                session.find(User.class, user.getId());

        System.out.println(savedUser);
        System.out.println("Профиль: "
                + savedUser.getProfile());

        session.close();


        // 5. Пытаемся удалить пользователя раньше профиля
        session = sessionFactory.openSession();
        session.beginTransaction();

        System.out.println("\n5. Попытка удалить пользователя до профиля:");

        User userForDelete =
                session.find(User.class, user.getId());


        try {
            session.remove(userForDelete);
            session.getTransaction().commit();

            System.out.println("Пользователь удалён.");
        } catch (Exception e) {

            System.out.println("Удаление не удалось:");
            System.out.println(e.getMessage());

            if (session.getTransaction().isActive()) {
                session.getTransaction().rollback();
            }
        }

        session.close();


        // 6. Сначала удаляем профиль, затем пользователя
        session = sessionFactory.openSession();
        session.beginTransaction();

        System.out.println("\n6. Правильное удаляение (профиль, затем пользователь):");

        UserProfile profileForDelete =
                session.find(UserProfile.class, profile.getId());

        User userForFinalDelete =
                session.find(User.class, user.getId());

        session.remove(profileForDelete);
        session.remove(userForFinalDelete);

        session.getTransaction().commit();

        session.close();

        System.out.println("\n Профиль и пользователь удалены.");

        sessionFactory.close();
    }
}