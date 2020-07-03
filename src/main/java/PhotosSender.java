import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.stat.Statistics;

import javax.imageio.ImageIO;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

public class PhotosSender {

    static String fileName;
    JPanel panel;
    JButton searchButton;
    JTextField nameTextField;
    JTextField IDTextField;
    JTextField moneyTextField;
    JButton registerButton;
    JList list1;
    JTextField textField1;
    List<User> list;
    static String ownName;
    static String nameToUploadFile;
    EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("org.hibernate.tutorial.jpa");
    SessionFactory sessionFactory = new Configuration().addAnnotatedClass(User.class).addAnnotatedClass(Client.class).configure().buildSessionFactory();
    EntityManager entityManager = entityManagerFactory.createEntityManager();
    Query query = entityManager.createQuery("from User where connecteed = true");
    Query nameQuery = entityManager.createQuery("from User where name = :namee");

    public void init() throws IOException {

        JFrame frame = new JFrame();
        frame.setSize(400, 400);
        frame.setContentPane(panel);
        frame.setVisible(true);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {

            }

            @Override
            public void windowClosing(WindowEvent e) {
                System.out.println(ownName);
                deletingUser(ownName);
            }
        });
        Thread downloader = new Thread(() -> {

            try {
                imageDownloader();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        downloader.setName("image downloader");
        downloader.start();

        //React when index clicked
        list1.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                if( evt.getClickCount()==1){
                    System.out.println(list1.locationToIndex(evt.getPoint()));
                    System.out.println(list.get(list1.locationToIndex(evt.getPoint())).getName());
                }
                if (evt.getClickCount() == 2) {
                    JFileChooser chooser = new JFileChooser();
                    chooser.showOpenDialog(chooser);
                     File fileToUpload = new File(chooser.getSelectedFile().getAbsolutePath());
                    nameToUploadFile = list.get(list1.locationToIndex(evt.getPoint())).getName();
                      try {
                        byte[] fileContent = Files.readAllBytes(fileToUpload.toPath());

                        updateRowWithBlob(fileContent, fileToUpload.getName());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        //Register user to list
        registerButton.addActionListener(e -> {
            if (registerButton.isEnabled()) {

                User bank = new User();
                byte[] s = new byte[10];
                bank.setData(s);
                bank.setName(textField1.getText());
                bank.setConnected(true);
                if (!entityManager.getTransaction().isActive()) {
                    entityManager.getTransaction().begin();
                }
                if (!nameExists(list, bank.getName())) {
                    ownName = textField1.getText();
                    System.out.println("Adding");
                    entityManager.merge(bank);

                } else {
                    System.out.println("NO SE PUEDE");
                }


            }
        });


        final Thread updater = new Thread(() -> {
            final DefaultListModel model = new DefaultListModel();

            while (true) {
                list1.setModel(model);
                list = query.getResultList();

                for (int i = 0; i < list.size(); i++) {
                    if (!model.contains(list.get(i).getName())) {
                        model.addElement(list.get(i).getName());
                        System.out.println("XD");
                    }
                }
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }

        });
        updater.start();
    }

    private void updateRowWithBlob(byte[] data, String filename) {


        query = entityManager.createQuery("from User where name = :namee");
        query.setParameter("namee", nameToUploadFile);
        List<User> bank = query.getResultList();
        if (bank != null) {
            System.out.println("Static name to upload setted by click "
                    +nameToUploadFile);
            bank.get(0).setFileName(filename);
            bank.get(0).setData(data);
            System.out.println("Setting data");
            System.out.println(bank.get(0).getName());
            Session session = sessionFactory.openSession();
            session.beginTransaction();

            session.update(bank.get(0));
            session.getTransaction().commit();
            session.close();
            System.out.println("Finished uploading file" );
        }
    }

    void deletingUser(String name) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("org.hibernate.tutorial.jpa");
        SessionFactory sessionFactory = new Configuration().addAnnotatedClass(User.class).addAnnotatedClass(Client.class).configure().buildSessionFactory();
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        Session session = sessionFactory.openSession();
        session.beginTransaction();


        Query query = entityManager.createQuery("from User where name = :namee");
        query.setParameter("namee", name);
        List<User> bank = query.getResultList();
        if (bank != null) {
            session.delete(bank.get(0));
            session.getTransaction().commit();
        }
    }

    void imageDownloader() throws InterruptedException {
        Statistics stats = sessionFactory.getStatistics();
        stats.setStatisticsEnabled(true);
        while (true) {
            System.out.println("Number of active connections " + stats.getSessionOpenCount());
            Thread.sleep(5000);
            if (ownName != null) {
                try {
                    System.out.println("Searching for name " + ownName);
                    Query sa = entityManager.createQuery("from User where name = :namee");
                    sa.setParameter("namee", ownName);

                    List<User> bank = sa.getResultList();
                    if (bank != null) {
                        if (bank.size() > 0 && bank.get(0).getFilename() != null) {
                            System.out.println("file name"+bank.get(0).getFilename());
                            ByteArrayInputStream bais = new ByteArrayInputStream(bank.get(0).getData());
                            BufferedImage image = ImageIO.read(bais);

                            File outputfile = new File(bank.get(0).getFilename());
                            outputfile.setWritable(true);
                            ImageIO.write(image, "jpg", outputfile);
                            System.out.println("getting data");
                            bank.get(0).setData(null);
                            bank.get(0).setFileName(null);

                            Session session = sessionFactory.openSession();
                            session.beginTransaction();

                            session.update(bank.get(0));
                            session.getTransaction().commit();
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private boolean nameExists(List<User> list, String name) {
        if (list == null) {
            return false;
        }
        for (User listObject : list) {
            if (listObject.getName().equals(name)) {
                return true;
            }
        }

        return false;
    }

    public static void main(String[] args) throws IOException {
        new PhotosSender().init();
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
