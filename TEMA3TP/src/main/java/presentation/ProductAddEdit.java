package presentation;

import bll.validators.ClientBLL;
import bll.validators.ProductBLL;
import model.Client;
import model.Product;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Fereastra pentru adaugarea sau editarea unui produs
 */
public class ProductAddEdit extends JFrame {
    private JTextField name;
    private JTextField price;
    private JTextField producer;
    private JTextField quantity;
    private boolean isEditing;
    private ProductView productView;

    /**
     * Constructor pentru adaugare produs
     * @param productView referinta catre fereastra principala a produsului
     */
    public ProductAddEdit(ProductView productView) {
        super("Add Product");
        initializeUI();
        this.productView = productView;
        isEditing = false;
        setVisible(true);
    }

    /**
     * Constructor pentru editare produs
     * @param productView referinta catre fereastra principala a produsului
     * @param productToEdit produs de editat
     */
    public ProductAddEdit(ProductView productView, Product productToEdit) {
        super("Edit Product");
        initializeUI();
        this.productView = productView;
        populateFields(productToEdit);
        isEditing = true;
        setVisible(true);
    }

    /**
     * Initializeaza componentele GUI pentru adaugare/editare produs
     */
    private void initializeUI() {
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        name = new JTextField();
        price = new JTextField();
        producer = new JTextField();
        quantity = new JTextField();

        JPanel panel = new JPanel(new GridLayout(5, 2));
        panel.add(new JLabel("Name:"));
        panel.add(name);
        panel.add(new JLabel("Price:"));
        panel.add(price);
        panel.add(new JLabel("Producer:"));
        panel.add(producer);
        panel.add(new JLabel("Quantity:"));
        panel.add(quantity);

        JButton saveBtn = new JButton("Save");
        saveBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveProduct();
            }
        });

        panel.add(saveBtn);

        getContentPane().add(panel);
    }

    /**
     * Populeaza campurile text din GUI cu informatiile din produsul dat
     * @param product produsul ale carui info vor fi afisate
     */
    private void populateFields(Product product) {
        name.setText(product.getName());
        price.setText(String.valueOf(product.getPrice()));
        producer.setText(product.getProducer());
        quantity.setText(String.valueOf(product.getQuantity()));
    }

    /**
     * salveaza info despre produs fie prin adaugare produs, fie prin actualizare produs existent
     */
    private void saveProduct() {
        if (isEditing) {
            // edit produs
            int productId = productView.getProductIdToEdit();
            String nameS = name.getText();
            int priceS =Integer.parseInt(price.getText());
            String producerS = producer.getText();
            int quantityS = Integer.parseInt(quantity.getText());
            Product updatedProduct = new Product(productId, nameS, priceS, producerS, quantityS);
            ProductBLL productBLL = new ProductBLL();
            productBLL.update(updatedProduct);
        } else {
            // add produs
            String nameS = name.getText();
            int priceS =Integer.parseInt(price.getText());
            String producerS = producer.getText();
            int quantityS = Integer.parseInt(quantity.getText());
            Product newProduct = new Product(nameS, priceS, producerS, quantityS);
            ProductBLL productBLL = new ProductBLL();
            productBLL.insert(newProduct);
        }
        dispose();
        productView.refreshTable();
    }
}
