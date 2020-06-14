import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import javax.persistence.*;
import javax.swing.*;


public class Main {

    public static void main(String[] args) {

        registerUser();


    }

    private static void registerUser() {
        JFrame frame = new JFrame();
        JPanel panel = new JPanel();
        JTextField name = new JTextField("Name");
        JTextField id = new JTextField("ID");
        JButton register = new JButton("Register");
        panel.add(name);
        panel.add(id);
        panel.add(register);
        frame.add(panel);
        frame.setSize(200, 200);
        frame.setVisible(true);
        register.addActionListener(e -> {
            Client client = new Client();
            client.setName(name.getText());
            client.setId(Integer.parseInt(id.getText()));
            Thread thread = new Thread(() -> {
                EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("org.hibernate.tutorial.jpa");
                SessionFactory sessionFactory = new Configuration().addAnnotatedClass(Bank.class).addAnnotatedClass(Client.class).configure().buildSessionFactory();

                Session session = sessionFactory.openSession();
                session.beginTransaction();
                System.out.println(session.get(Bank.class, 2).getName());


                EntityManager entityManager = entityManagerFactory.createEntityManager();
                entityManager.getTransaction().begin();
                entityManager.persist(client);
                entityManager.getTransaction().commit();
                entityManagerFactory.close();
            });
            thread.start();


        });

    }
}
