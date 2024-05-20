package presentation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import bll.validators.AgeValidator;
import bll.validators.ClientBLL;
import model.Client;

/**
 * Fereastra pentru adaugarea sau editarea unui client
 */
public class ClientAddEdit extends JFrame {
    private JTextField firstName;
    private JTextField lastName;
    private JTextField address;
    private JTextField age;
    private boolean isEditing; // verifica daca se editeaza sau se adauga
    private ClientView clientView;

    /**
     * Constructor pentru adaugare client
     * @param clientView referinta catre fereastra principala a clientului
     */
    public ClientAddEdit(ClientView clientView) {
        super("Add Client");
        initializeUI();
        this.clientView = clientView;
        isEditing = false;
        setVisible(true);
    }

    /**
     * Constructor pentru editare client
     * @param clientView referinta catre fereastra principala a clientului
     * @param clientToEdit clientul de editat
     */
    public ClientAddEdit(ClientView clientView, Client clientToEdit) {
        super("Edit Client");
        initializeUI();
        this.clientView = clientView;
        populateFields(clientToEdit);
        isEditing = true;
        setVisible(true);
    }

    /**
     * Initializeaza componentele GUI pentru adaugare/editare client
     */
    private void initializeUI() {
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        firstName = new JTextField();
        lastName = new JTextField();
        address = new JTextField();
        age = new JTextField();

        JPanel panel = new JPanel(new GridLayout(5, 2));
        panel.add(new JLabel("First Name:"));
        panel.add(firstName);
        panel.add(new JLabel("Last Name:"));
        panel.add(lastName);
        panel.add(new JLabel("Address:"));
        panel.add(address);
        panel.add(new JLabel("Age:"));
        panel.add(age);

        JButton saveBtn = new JButton("Save");
        saveBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveClient();
            }
        });

        panel.add(saveBtn);

        getContentPane().add(panel);
    }

    /**
     * Populeaza campurile text din GUI cu informatiile din clientul dat
     * @param client clientul ale carui info vor fi afisate
     */
    private void populateFields(Client client) {
        firstName.setText(client.getFirstName());
        lastName.setText(client.getLastName());
        address.setText(client.getAddress());
        age.setText(String.valueOf(client.getAge()));
    }

    /**
     * salveaza info despre client fie prin adaugare client, fie prin actualizare client existent
     */
    private void saveClient() {
        int clientAge = Integer.parseInt(age.getText());
        AgeValidator ageValidator = new AgeValidator();
        ageValidator.validate(new Client("", "", "", clientAge)); // client temporar pt validare

        if (isEditing) {
            // edit client
            int clientId = clientView.getClientIdToEdit();
            String firstNameS = firstName.getText();
            String lastNameS = lastName.getText();
            String addressS = address.getText();
            int ageI = Integer.parseInt(age.getText());
            Client updatedClient = new Client(clientId, firstNameS, lastNameS, addressS, ageI);
            ClientBLL clientBLL = new ClientBLL();
            clientBLL.update(updatedClient);
        } else {
            // add client
            String firstNameS = firstName.getText();
            String lastNameS = lastName.getText();
            String addressS = address.getText();
            int ageI = Integer.parseInt(age.getText());
            Client newClient = new Client(firstNameS, lastNameS, addressS, ageI);
            ClientBLL clientBLL = new ClientBLL();
            clientBLL.insert(newClient);
        }
        // inchide fereastra
        dispose();
        // actualizeaza tabela din clientView dupa salvare
        clientView.refreshTable();
    }
}
