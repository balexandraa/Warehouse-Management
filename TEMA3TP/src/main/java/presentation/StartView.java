package presentation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Interfata de pornire a aplicatiei de management a depozitului
 */
public class StartView extends JFrame {
    private JButton clientButton, productButton, orderButton;

    public StartView() {
        // Set up the frame
        setTitle("Warehouse Management");
        setSize(320, 420);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create buttons
        clientButton = new JButton("Client");
        productButton = new JButton("Product");
        orderButton = new JButton("Order");

        clientButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ClientView clientView = new ClientView();
                clientView.setVisible(true);
            }
        });

        productButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               ProductView productView = new ProductView();
               productView.setVisible(true);
            }
        });

        orderButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                OrderView orderView = new OrderView();
                orderView.setVisible(true);
            }
        });

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3,0, 8, 8));
        panel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
        panel.add(clientButton);
        panel.add(productButton);
        panel.add(orderButton);

        add(panel);

        setVisible(true);
    }

    public static void main(String[] args) {
        new StartView();
    }
}
