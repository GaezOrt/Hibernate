import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.sql.rowset.serial.SerialBlob;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.List;

public class Banker {


    JPanel panel;
    JButton searchButton;
    JTextField nameTextField;
    JTextField IDTextField;
    JTextField moneyTextField;
    JButton registerButton;
    JList list1;
    JTextField textField1;
    List<Bank> list;
    static String name;
    public void init() {
        JFrame frame = new JFrame();
        frame.setSize(400, 400);
        frame.setContentPane(panel);
        frame.setVisible(true);

        //React when index clicked
        list1.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                JList lists = (JList) evt.getSource();
                if (evt.getClickCount() == 2) {
                    JFileChooser chooser = new JFileChooser();
                    chooser.showOpenDialog(chooser);
                    File fileToUpload = new File(chooser.getSelectedFile().getAbsolutePath());
                    name=list.get(lists.locationToIndex(evt.getPoint())).getName();
                    try {
                        byte[] fileContent = Files.readAllBytes(fileToUpload.toPath());
                        Blob blob = new SerialBlob(fileContent);
                        updateRowWithBlob(fileContent);
                    } catch (IOException | SQLException e) {
                        e.printStackTrace();
                    }
                } else if (evt.getClickCount() == 3) {

                    // Triple-click detected
                    int index = lists.locationToIndex(evt.getPoint());
                }
            }
        });

        //Register user to list
        registerButton.addActionListener(e -> {
            if (registerButton.isEnabled()) {

                Bank bank = new Bank();
                byte[]s=new byte[10];
                bank.setData(s);
                bank.setName(textField1.getText());
                bank.setConnected(true);
                EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("org.hibernate.tutorial.jpa");
                EntityManager entityManager = entityManagerFactory.createEntityManager();
                entityManager.getTransaction().begin();
                if (!nameExists(list, bank.getName())) {
                    System.out.println("Adding");
                    entityManager.merge(bank);
                    entityManager.getTransaction().commit();
                    entityManagerFactory.close();

                } else {
                    System.out.println("NO SE PUEDE");
                }


            }
        });

        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("org.hibernate.tutorial.jpa");
        SessionFactory sessionFactory = new Configuration().addAnnotatedClass(Bank.class).addAnnotatedClass(Client.class).configure().buildSessionFactory();
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        Session session = sessionFactory.openSession();
        session.beginTransaction();
        System.out.println("XD");
        Bank bank = new Bank();
        bank.setConnected(true);
        final DefaultListModel model = new DefaultListModel();
        list1.setModel(model);


        final Thread updater = new Thread(() -> {
            while (true) {
                Query query = entityManager.createQuery("from Bank where connecteed = true");

                //Query query=session.createQuery("SELECT bank_table where connecteed = :checker ");

                list = query.getResultList();


                for (int i = 0; i < list.size(); i++) {
                    if (!model.contains(list.get(i).getName())) {
                        model.addElement(list.get(i).getName());

                    }
                }

            }
        });
        session.getTransaction().commit();
        session.close();
        updater.start();
    }

    private void updateRowWithBlob(byte[] data) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("org.hibernate.tutorial.jpa");
        SessionFactory sessionFactory = new Configuration().addAnnotatedClass(Bank.class).addAnnotatedClass(Client.class).configure().buildSessionFactory();
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        Session session = sessionFactory.openSession();
        session.beginTransaction();


        Query query = entityManager.createQuery("from Bank where name = :namee");
        query.setParameter("namee",name);
        List<Bank> bank= query.getResultList();
        if(bank!=null) {
            bank.get(0).setData(data);
            System.out.println("Setting data");
            System.out.println(bank.get(0).getId());
            session.update(bank.get(0));
            session.getTransaction().commit();
            session.close();
        }
    }

    private boolean nameExists(List<Bank> list, String name) {
        if (list == null) {
            return false;
        }
        for (Bank listObject : list) {
            if (listObject.getName().equals(name)) {
                return true;
            }
        }

        return false;
    }

    public static void main(String[] args) {
        new Banker().init();
    }


    {

        $$$setupUI$$$();
    }

    private void $$$setupUI$$$() {
        panel = new JPanel();
        panel.setLayout(new GridLayoutManager(10, 3, new Insets(0, 0, 0, 0), -1, -1));
        final Spacer spacer1 = new Spacer();
        panel.add(spacer1, new GridConstraints(5, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        searchButton = new JButton();
        searchButton.setText("Search");
        panel.add(searchButton, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));

        nameTextField = new JTextField();
        nameTextField.setText("Name");
        panel.add(nameTextField, new GridConstraints(6, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        IDTextField = new JTextField();
        IDTextField.setText("ID");
        panel.add(IDTextField, new GridConstraints(7, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        JTextField ageTextField = new JTextField();
        ageTextField.setText("Age");
        panel.add(ageTextField, new GridConstraints(8, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        moneyTextField = new JTextField();
        moneyTextField.setText("Money");
        panel.add(moneyTextField, new GridConstraints(9, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        registerButton = new JButton();
        registerButton.setText("Register");
        panel.add(registerButton, new GridConstraints(9, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("Name");
        panel.add(label1, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("Value");
        panel.add(label2, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("Age");
        panel.add(label3, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label4 = new JLabel();
        label4.setText("ID");
        panel.add(label4, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }


    public JComponent $$$getRootComponent$$$() {
        return panel;
    }


}
