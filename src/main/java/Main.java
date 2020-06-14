import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.cfg.Configuration;
import sun.security.krb5.Config;

import javax.persistence.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Main {

    public static void main(String args[]) {

        registerUser();


    }

    static void registerUser() {
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
        register.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Client client = new Client();
                client.setName(name.getText());
                client.setId(Integer.parseInt(id.getText()));
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
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
                    }
                });
                thread.start();


            }
        });

    }
}
