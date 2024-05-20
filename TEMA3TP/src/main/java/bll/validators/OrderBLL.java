package bll.validators;

import dao.OrderDAO;
import model.Orders;
import model.Product;

import java.util.NoSuchElementException;

/**
 * Gestioneaza operatiile pentru efectuarea comenzii: plasarea comenzii + actualizarea stocului produselor
 */
public class OrderBLL {
    private OrderDAO orderDAO;
    private ProductBLL productBLL;

    /**
     * Constructor care initializeaza OrderDAO si ProductBLL
     */
    public OrderBLL() {
        orderDAO = new OrderDAO();
        productBLL = new ProductBLL();
    }

    /**
     * Plaseaza o comanda
     * @param order comanda de plasat
     * @throws NoSuchElementException daca stocul nu este disponibil pentru cantitatea solicitata
     */
    public void placeOrder(Orders order) {
        boolean isAvailable = productBLL.checkQuantity(order.getProductID(), order.getQuantity());
        if (!isAvailable) {
            throw new NoSuchElementException("Stock not available for the requested quantity!");
        }

        // inseram comanda in bd
        orderDAO.insert(order);

        // actualizam stocul produsului
        Product product = productBLL.findProductById(order.getProductID());
        if (product != null) {
            int newQuantity = product.getQuantity() - order.getQuantity();
            /*if (newQuantity < 0) {
                throw new IllegalArgumentException("Quantity must be greater than or equal to 0!");
            }*/
            product.setQuantity(newQuantity);
            productBLL.update(product); // acualizam produsul in bd
        }
    }
}
