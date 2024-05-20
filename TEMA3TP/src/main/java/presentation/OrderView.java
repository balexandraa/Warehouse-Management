package presentation;

import javax.swing.*;

import bll.validators.OrderBLL;
import bll.validators.ProductBLL;
import bll.validators.ClientBLL;
import model.Client;
import model.Orders;
import model.Product;
import java.util.List;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * GUI pentru vizualizare si gestionare comenzi
 */
public class OrderView extends JFrame {
    private JComboBox<String> productComboBox;
    private JComboBox<String> clientComboBox;
    private JTextField quantityField;
    private JButton placeOrderButton;
    private ProductBLL productBLL;
    private ClientBLL clientBLL;
    private OrderBLL orderBLL;

    public OrderView() {
        setTitle("Order Management");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        productBLL = new ProductBLL();
        clientBLL = new ClientBLL();
        orderBLL = new OrderBLL();

        productComboBox = new JComboBox<>();
        clientComboBox = new JComboBox<>();
        quantityField = new JTextField(10);
        placeOrderButton = new JButton("Place Order");

        loadProducts();
        loadClients();

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 2));
        panel.add(new JLabel("Select Product:"));
        panel.add(productComboBox);
        panel.add(new JLabel("Select Client:"));
        panel.add(clientComboBox);
        panel.add(new JLabel("Enter Quantity:"));
        panel.add(quantityField);
        panel.add(new JLabel()); // pt a ocupa spatiul
        panel.add(placeOrderButton);

        placeOrderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // obtinem id produs selectat
                String selectedProductName = (String) productComboBox.getSelectedItem();
                Product selectedProduct = productBLL.findProductByName(selectedProductName);

                // obtinem id client selectat
                String selectedClientName = (String) clientComboBox.getSelectedItem();
                String[] nameParts = selectedClientName.split(" ");
                String firstName = nameParts[0];
                String lastName = nameParts[1];

                Client selectedClient = clientBLL.findClientByName(firstName, lastName);

                int quantity = Integer.parseInt(quantityField.getText());

                // adaugam noua comanda in bd
                Orders order = new Orders(selectedClient.getID(), selectedProduct.getID(), quantity);
                orderBLL.placeOrder(order);

                // resetam valoarea
                quantityField.setText("");
            }
        });

        getContentPane().add(panel);
        setLocationRelativeTo(null);
    }

    /**
     * Incarca produsele disponibile in ComboBox
     */
    private void loadProducts() {
        List<Product> products = productBLL.findAll();
        for (Product product : products) {
            productComboBox.addItem(product.getName());
        }
    }

    /**
     * Incarca clientii disponibili in ComboBox
     */
    private void loadClients() {
        List<Client> clients = clientBLL.findAll();
        for (Client client : clients) {
            clientComboBox.addItem(client.getFirstName() + " " + client.getLastName());
        }
    }

}
