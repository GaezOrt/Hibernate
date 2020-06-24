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
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class Banker {


    private JPanel panel;
    private JTextField identificationNumber;
    private JButton searchButton;
    private JLabel name;
    public JTextField nameTextField;
    public JTextField IDTextField;
    public JTextField ageTextField;
    public JTextField moneyTextField;
    public JButton registerButton;
    public JLabel Age;
    public JLabel Money;
    public JLabel idLabel;
    private JList list1;

    public void init() {
        JFrame frame = new JFrame();
        frame.setSize(400, 400);
        frame.setContentPane(panel);
        frame.setVisible(true);
            if (searchButton.isEnabled()) {
                EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("org.hibernate.tutorial.jpa");
                SessionFactory sessionFactory = new Configuration().addAnnotatedClass(Bank.class).addAnnotatedClass(Client.class).configure().buildSessionFactory();
                EntityManager entityManager = entityManagerFactory.createEntityManager();

                Session session = sessionFactory.openSession();
                session.beginTransaction();
                System.out.println("XD");
                Bank bank = new Bank();
                bank.setConnected(true);
                Query query= entityManager.createQuery("from Bank where connecteed = true");

                //Query query=session.createQuery("SELECT bank_table where connecteed = :checker ");

                List<Bank> list= query.getResultList();

                System.out.println(list.get(0).getName());
                session.getTransaction().commit();
                session.close();
              //  bank.setIdCertificate(Integer.parseInt(identificationNumber.getText()));

              //  name.setText(session.get(Bank.class, bank.getIdCertificate()).getName());
              //  Money.setText(session.get(Bank.class, bank.getIdCertificate()).getMoney().toString());
              //  Age.setText(session.get(Bank.class, bank.getIdCertificate()).getEdad().toString());
               }

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (registerButton.isEnabled()) {

                    Bank bank = new Bank();
                    bank.setIdCertificate(Integer.parseInt(IDTextField.getText()));
                    bank.setName(nameTextField.getText());
                    bank.setEdad(Integer.parseInt(ageTextField.getText()));
                    bank.setMoney(Integer.parseInt(moneyTextField.getText()));
                    bank.setConnected(true);
                    EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("org.hibernate.tutorial.jpa");
                    EntityManager entityManager = entityManagerFactory.createEntityManager();
                    entityManager.getTransaction().begin();
                    entityManager.merge(bank);
                    entityManager.getTransaction().commit();
                    entityManagerFactory.close();


                }
            }
        });
    }

    public static void main(String[] args) {
        new Banker().init();
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        panel = new JPanel();
        panel.setLayout(new GridLayoutManager(10, 3, new Insets(0, 0, 0, 0), -1, -1));
        identificationNumber = new JTextField();
        identificationNumber.setText("");
        panel.add(identificationNumber, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final Spacer spacer1 = new Spacer();
        panel.add(spacer1, new GridConstraints(5, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        searchButton = new JButton();
        searchButton.setText("Search");
        panel.add(searchButton, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        name = new JLabel();
        name.setText("");
        name.setToolTipText("Name");
        panel.add(name, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        Money = new JLabel();
        Money.setText("");
        panel.add(Money, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        Age = new JLabel();
        Age.setText("");
        panel.add(Age, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        nameTextField = new JTextField();
        nameTextField.setText("Name");
        panel.add(nameTextField, new GridConstraints(6, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        IDTextField = new JTextField();
        IDTextField.setText("ID");
        panel.add(IDTextField, new GridConstraints(7, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        ageTextField = new JTextField();
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
        idLabel = new JLabel();
        idLabel.setText("");
        panel.add(idLabel, new GridConstraints(4, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panel;
    }

}
