package les5;

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

        Session session = sessionFactory.openSession();
        session.beginTransaction();

        Student student1 = new Student("Анна");
        Student student2 = new Student("Пётр");
        Student student3 = new Student("Виктор");

        Course course1 = new Course("Java");
        Course course2 = new Course("Hibernate");
        Course course3 = new Course("PostgreSQL");
        Course course4 = new Course("Docker");

        session.persist(student1);
        session.persist(student2);
        session.persist(student3);

        session.persist(course1);
        session.persist(course2);
        session.persist(course3);
        session.persist(course4);

        student1.getCourses().add(course1);
        student1.getCourses().add(course2);
        student1.getCourses().add(course3);

        student2.getCourses().add(course1);
        student2.getCourses().add(course4);

        student3.getCourses().add(course2);
        student3.getCourses().add(course4);

        session.getTransaction().commit();
        session.close();

        System.out.println("Студенты, курсы и связи сохранены.");

        sessionFactory.close();

    }
}