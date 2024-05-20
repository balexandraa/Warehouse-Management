package model;

/**
 * Clasa care reprezinta produsul
 */
public class Product {
    private int ID;
    private String name;
    private int price;
    private String producer;
    private int quantity;

    public Product() {

    }

    /**
     * Constructor pentru GUI
     * @param name nume produs
     * @param price pret produs
     * @param producer producator produs
     * @param quantity cantitate produs
     */
    public Product(String name, int price, String producer, int quantity) {
        this.name = name;
        this.price = price;
        this.producer = producer;
        this.quantity = quantity;
    }

    /**
     * Constructor pentru BD
     * @param ID id produs
     * @param name nume produs
     * @param price pret produs
     * @param producer producator
     * @param quantity cantitate produs
     */
    public Product(int ID, String name, int price, String producer, int quantity) {
        this.ID = ID;
        this.name = name;
        this.price = price;
        this.producer = producer;
        this.quantity = quantity;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getProducer() {
        return producer;
    }

    public void setProducer(String producer) {
        this.producer = producer;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "Product{" +
                "ID=" + ID +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", producer='" + producer + '\'' +
                ", quantity=" + quantity +
                '}';
    }
}
