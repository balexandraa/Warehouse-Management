package presentation;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import dao.ProductDAO;
import model.Product;
import bll.validators.ProductBLL;

/**
 * GUI pentru vizualizare si gestionare produs
 */
public class ProductView extends JFrame {
    private JTable productTable;

    public ProductView() {
        super("Product Management");
        setSize(700, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        ProductBLL productBLL = new ProductBLL();
        List<Product> products = productBLL.findAll();

        /*DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(new Object[]{"ID", "Name", "Price", "Producer", "Quantity", "Edit", "Delete"});

        for (Product product : products) {
            model.addRow(new Object[]{product.getID(), product.getName(), product.getPrice(), product.getProducer(), product.getQuantity(), "Edit", "Delete"});
        }*/

        // Create a table model using AbstractDAO's generareTabel method
        DefaultTableModel model = new ProductDAO().createJTable(products);

        // Add edit and delete columns manually
        model.addColumn("Edit");
        model.addColumn("Delete");

        // Populate edit and delete columns
        for (int i = 0; i < products.size(); i++) {
            model.setValueAt("Edit", i, 5);
            model.setValueAt("Delete", i, 6);
        }

        productTable = new JTable(model);

        productTable.getColumnModel().getColumn(5).setCellRenderer(new ButtonRenderer());
        productTable.getColumnModel().getColumn(5).setCellEditor(new ButtonEditor(new JTextField(), productTable, this));
        productTable.getColumnModel().getColumn(6).setCellRenderer(new ButtonRenderer());
        productTable.getColumnModel().getColumn(6).setCellEditor(new ButtonEditor(new JTextField(), productTable, this));

        JScrollPane scrollPane = new JScrollPane(productTable);
        getContentPane().add(scrollPane);

        JButton addButton = new JButton("Add");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ProductAddEdit(ProductView.this);
                setVisible(true);
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);

        getContentPane().add(buttonPanel, BorderLayout.SOUTH);
        setLocationRelativeTo(null);
    }

    /**
     * Actualizeaza tabelul de produse cu datele curente din baza de date
     */
    public void refreshTable() {
        ProductBLL productBLL = new ProductBLL();
        List<Product> products = productBLL.findAll();

        // Update table model using AbstractDAO's generareTabel method
        DefaultTableModel model = new ProductDAO().createJTable(products);

        // Add edit and delete columns manually
        model.addColumn("Edit");
        model.addColumn("Delete");

        // Populate edit and delete columns
        for (int i = 0; i < products.size(); i++) {
            model.setValueAt("Edit", i, 5);
            model.setValueAt("Delete", i, 6);
        }

        // Set new model to the table
        productTable.setModel(model);

        // Reapply custom cell renderer and editor
        productTable.getColumnModel().getColumn(5).setCellRenderer(new ButtonRenderer());
        productTable.getColumnModel().getColumn(5).setCellEditor(new ButtonEditor(new JTextField(), productTable, this));
        productTable.getColumnModel().getColumn(6).setCellRenderer(new ButtonRenderer());
        productTable.getColumnModel().getColumn(6).setCellEditor(new ButtonEditor(new JTextField(), productTable, this));
    }

    public int getProductIdToEdit() {
        int selectedRow = productTable.getSelectedRow();
        return (int) productTable.getValueAt(selectedRow, 0);
    }
}
