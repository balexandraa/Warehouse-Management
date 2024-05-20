package model;

/**
 * Clasa care reprezinta comanda
 */
public class Orders {
    private int ID;
    private int clientID;
    private int productID;
    private int quantity;
//    private int price; //daca fac si Bill

    /**
     * Constructor implicit
     */
    public Orders() {

    }

    /**
     * Constructor pentru BD
     * @param clientID id client
     * @param productID id produs
     * @param quantity cantitatea comandata
     */
    public Orders(int clientID, int productID, int quantity) {
        this.clientID = clientID;
        this.productID = productID;
        this.quantity = quantity;
    }

    public Orders(int ID, int clientID, int productID, int quantity) {
        this.ID = ID;
        this.clientID = clientID;
        this.productID = productID;
        this.quantity = quantity;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getClientID() {
        return clientID;
    }

    public void setClientID(int clientID) {
        this.clientID = clientID;
    }

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "Order{" +
                "ID=" + ID +
                ", clientID=" + clientID +
                ", productID=" + productID +
                ", quantity=" + quantity +
                '}';
    }
}
