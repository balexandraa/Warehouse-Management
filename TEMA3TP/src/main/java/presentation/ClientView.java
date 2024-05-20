package presentation;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import bll.validators.ProductBLL;
import dao.ClientDAO;
import model.Client;
import bll.validators.ClientBLL;
import model.Product;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * GUI pentru vizualizare si gestionare clienti
 */
public class ClientView extends JFrame {
    private JTable clientTable;

    /**
     * Constructor pentru ClientView
     */
    public ClientView() {
        super("Client Management");
        setSize(700, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Dispose when closed, so it doesn't close the entire application

        // Fetch all clients
        ClientBLL clientBLL = new ClientBLL();
        List<Client> clients = clientBLL.findAll();

        // Create a table model
       // DefaultTableModel model = new DefaultTableModel();

/*        model.setColumnIdentifiers(new Object[]{"ID", "First Name", "Last Name", "Address", "Age", "Edit", "Delete"});

        // Add clients to the table model
        for (Client client : clients) {
            model.addRow(new Object[]{client.getID(), client.getFirstName(), client.getLastName(), client.getAddress(), client.getAge(),"Edit", "Delete"});
        }*/

        DefaultTableModel model = new ClientDAO().createJTable(clients);

        // Add edit and delete columns manually
        model.addColumn("Edit");
        model.addColumn("Delete");

        // Populate edit and delete columns
        for (int i = 0; i < clients.size(); i++) {
            model.setValueAt("Edit", i, 5);
            model.setValueAt("Delete", i, 6);
        }

        // Create the table
        clientTable = new JTable(model);

        // Set custom cell renderer for the action column
        clientTable.getColumnModel().getColumn(5).setCellRenderer(new ButtonRenderer());
        clientTable.getColumnModel().getColumn(5).setCellEditor(new ButtonEditor(new JTextField(), clientTable, this));
        clientTable.getColumnModel().getColumn(6).setCellRenderer(new ButtonRenderer());
        clientTable.getColumnModel().getColumn(6).setCellEditor(new ButtonEditor(new JTextField(), clientTable, this));

        // Add the table to a scroll pane
        JScrollPane scrollPane = new JScrollPane(clientTable);
        getContentPane().add(scrollPane);

        // Adăugați butonul "Add" în partea de jos a paginii
        JButton addButton = new JButton("Add");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Implementați acțiunea pentru butonul "Add" aici
             //   JOptionPane.showMessageDialog(ClientView.this, "Button 'Add' clicked");
                new ClientAddEdit(ClientView.this);
                setVisible(true);
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);

        getContentPane().add(buttonPanel, BorderLayout.SOUTH);
        setLocationRelativeTo(null);

      //  setVisible(true);
    }

    /**
     * Actualizeaza tabelul de clienti cu datele curente din baza de date
     */
    public void refreshTable() {
        // Fetch all clients
        ClientBLL clientBLL = new ClientBLL();
        List<Client> clients = clientBLL.findAll();

       /* // Clear existing table data
        DefaultTableModel model = (DefaultTableModel) clientTable.getModel();
        model.setRowCount(0);

        // Add clients to the table model
        for (Client client : clients) {
            model.addRow(new Object[]{client.getID(), client.getFirstName(), client.getLastName(), client.getAddress(), client.getAge(),"Edit", "Delete"});
        }*/

        // Update table model using AbstractDAO's generareTabel method
        DefaultTableModel model = new ClientDAO().createJTable(clients);


        // Add edit and delete columns manually
        model.addColumn("Edit");
        model.addColumn("Delete");

        // Populate edit and delete columns
        for (int i = 0; i < clients.size(); i++) {
            model.setValueAt("Edit", i, 5);
            model.setValueAt("Delete", i, 6);
        }

        // Set new model to the table
        clientTable.setModel(model);

        // Reapply custom cell renderer and editor
        clientTable.getColumnModel().getColumn(5).setCellRenderer(new ButtonRenderer());
        clientTable.getColumnModel().getColumn(5).setCellEditor(new ButtonEditor(new JTextField(), clientTable, this));
        clientTable.getColumnModel().getColumn(6).setCellRenderer(new ButtonRenderer());
        clientTable.getColumnModel().getColumn(6).setCellEditor(new ButtonEditor(new JTextField(), clientTable, this));
    }

    /**
     * @return ID-ul clientului selectat pentru editare
     */
    public int getClientIdToEdit() {
        // Returnează ID-ul clientului selectat pentru editare
        int selectedRow = clientTable.getSelectedRow();
        return (int) clientTable.getValueAt(selectedRow, 0);
    }
}

/**
 * Clasa pentru afisarea unui buton intr-o celula a tabelului
 */
//BUTTON RENDERER CLASS
class ButtonRenderer extends JButton implements  TableCellRenderer
{

    /**
     * Constructor pentru ButtonRenderer
     */
    //CONSTRUCTOR
    public ButtonRenderer() {
        //SET BUTTON PROPERTIES
        setOpaque(true);
    }
    @Override
    public Component getTableCellRendererComponent(JTable table, Object obj,
                                                   boolean selected, boolean focused, int row, int col) {

        //SET PASSED OBJECT AS BUTTON TEXT
        setText((obj==null) ? "":obj.toString());

        return this;
    }

}

/**
 * Clasa pentru editarea unei celule de tip buton
 */
//BUTTON EDITOR CLASS
class ButtonEditor extends DefaultCellEditor
{
    protected JButton btn;
    private String lbl;
    private Boolean clicked;
    private JTable table;
    private Object view; // CLientView or ProductView
    int row;

    /**
     * Constructor pentru ButtonEditor
     * @param txt textul de introdus
     * @param table tabelul pe care este apelat
     * @param view instanta pentru ClientView
     */
    public ButtonEditor(JTextField txt, JTable table, Object view) {
        super(txt);
        this.table = table;
        this.view = view;

        btn=new JButton();
        btn.setOpaque(true);

        //WHEN BUTTON IS CLICKED
        btn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                fireEditingStopped();
                if (lbl.equals("Delete")) {
                    if (view instanceof ClientView) {
                        ClientView clientView = (ClientView) view;
                        ClientBLL clientBLL = new ClientBLL();
                        int id = (int) table.getValueAt(row, 0); // id-ul clientului din prima coloana
                        clientBLL.delete(id);
                        refreshTable();
                    } else if (view instanceof ProductView) {
                        ProductView productView = (ProductView) view;
                        ProductBLL productBLL = new ProductBLL();
                        int id = (int) table.getValueAt(row, 0); // id produs din prima coloana
                        productBLL.delete(id);
                        productView.refreshTable();
                    }
                }
                else if (lbl.equals("Edit")) {
                    if (view instanceof ClientView) {
                        ClientView clientView = (ClientView) view;
                        int clientId = (int) table.getValueAt(row, 0);
                        ClientBLL clientBLL = new ClientBLL();
                        Client clientToEdit = clientBLL.findClientById(clientId);
                        new ClientAddEdit(clientView, clientToEdit);
                    } else if (view instanceof ProductView) {
                        ProductView productView = (ProductView) view;
                        int productId = (int) table.getValueAt(row, 0);
                        ProductBLL productBLL = new ProductBLL();
                        Product productToEdit = productBLL.findProductById(productId);
                        new ProductAddEdit(productView, productToEdit);
                    }
                }
            }
        });
    }

    //OVERRIDE A COUPLE OF METHODS
    @Override
    public Component getTableCellEditorComponent(JTable table, Object obj,
                                                 boolean selected, int row, int col) {

        //SET TEXT TO BUTTON,SET CLICKED TO TRUE,THEN RETURN THE BTN OBJECT
        lbl=(obj==null) ? "":obj.toString();
        btn.setText(lbl);
        clicked=true;
        this.row = row; // retinem randul
        return btn;
    }

    //IF BUTTON CELL VALUE CHNAGES,IF CLICKED THAT IS
    @Override
    public Object getCellEditorValue() {

        if(clicked)
        {
            //SHOW US SOME MESSAGE
         //   JOptionPane.showMessageDialog(btn, lbl+" succed!");
        }
        //SET IT TO FALSE NOW THAT ITS CLICKED
        clicked=false;
        return new String(lbl);
    }

    @Override
    public boolean stopCellEditing() {

        //SET CLICKED TO FALSE FIRST
        clicked=false;
        return super.stopCellEditing();
    }

    @Override
    protected void fireEditingStopped() {
        // TODO Auto-generated method stub
        super.fireEditingStopped();
    }

    private void refreshTable() {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.removeRow(row); // sterge randul din tabela
    }
}

