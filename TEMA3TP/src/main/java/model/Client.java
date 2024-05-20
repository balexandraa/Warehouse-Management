package model;

/**
 * Clasa care reprezinta clientul
 */
public class Client {
    private int ID;
    private String firstName;
    private String lastName;
    private String address;
    private int age;

    /**
     * Constructor implicit
     */
    public Client() {

    }

    /**
     * Constructor pentru GUI
     * @param firstName prenume client
     * @param lastName nume client
     * @param address adresa client
     * @param age varsta client
     */
    public Client(String firstName, String lastName, String address, int age) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.age = age;
    }

    /**
     * Constructor pentru BD
     * @param ID id client
     * @param firstName prenume client
     * @param lastName nume client
     * @param address adresa client
     * @param age varsta client
     */
    public Client(int ID, String firstName, String lastName, String address, int age) {
        this.ID = ID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.age = age;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "Client{" +
                "ID=" + ID +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", address='" + address + '\'' +
                ", age=" + age +
                '}';
    }
}
