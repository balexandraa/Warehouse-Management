package bll.validators;


import dao.ProductDAO;
import model.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Gestioneaza operatiile efectuate pe produs: valideaza datele si comunica cu DAO
 */
public class ProductBLL {
   // private List<Validator<Product>> validators;
    private ProductDAO productDAO;
    private QuantityValidator quantityValidator;

    /**
     * Constructor care initializeaza ProductDAO si QuantityValidator
     */
    public ProductBLL() {
     //   validators = new ArrayList<>();
        quantityValidator = new QuantityValidator();
      //  validators.add(quantityValidator);
        productDAO = new ProductDAO();
    }

    /**
     * Verifica daca exista suficiente produse pentru a efectua o comanda
     * @param productId ID-ul produsului
     * @param requestedQuantity cantitatea solicitata
     * @return true daca exista suficiente produse, false altfel
     * @throws NoSuchElementException daca produsul nu este gasit
     */
    //metoda pt verificare daca exista suficiente produse pt a fi efectuata comanda
    public boolean checkQuantity(int productId, int requestedQuantity) {
        Product product = productDAO.findById(productId);
        if (product == null) {
            throw new NoSuchElementException("The product with id =" + productId + " was not found!");
        }
        return product.getQuantity() >= requestedQuantity;
    }

    /**
     * Gaseste produslui dupa ID
     * @param id ID-ul produsului
     * @return produsul gasit
     * @throws NoSuchElementException daca produsul nu este gasit
     */
    public Product findProductById(int id) {
        Product product = productDAO.findById(id);
        if (product == null) {
            throw new NoSuchElementException("The product with id =" + id + " was not found!");
        }
        return product;
    }

    /**
     * Gaseste produsul dupa nume
     * @param name numele produsului
     * @return produsul gasit
     * @throws NoSuchElementException daca produsul nu este gasit
     */
    public Product findProductByName(String name) {
        Product product = productDAO.findByName(name);
        if (product == null) {
            throw new NoSuchElementException("The product with name =" + name + " was not found!");
        }
        return product;
    }

    /**
     * Gaseste toate produsele
     * @return lista de produse
     * @throws NoSuchElementException daca nu exista produse
     */
    public List<Product> findAll() {
        List<Product> products = productDAO.findAll();
        if (products == null) {
            throw new NoSuchElementException("There are NO products!!");
        }
        return products;
    }

    /**
     * Insereaza un produs nou
     * @param p produsul de inserat
     * @return produsul inserat
     * @throws NoSuchElementException daca inserarea a esuat
     */
    public Product insert(Product p) {
        // validare cantitate
        /*for (Validator<Product> validator : validators) {
            validator.validate(p);
        }*/

        quantityValidator.validateInsert(p);

        Product product = productDAO.insert(p);
        if (product == null) {
            throw new NoSuchElementException("Insertion Failed!");
        }
        return product;
    }

    /**
     * Sterge un produs
     * @param id ID-ul produsului de sters
     */
    public void delete(int id) {
        productDAO.delete(id);
    }

    /**
     * Actualizeaza un produs
     * @param p produsul de actualizat
     * @return produsul actualizat
     * @throws NoSuchElementException daca actualizarea a esuat
     */
    public Product update(Product p) {
        // Validare cantitate
       /* for (Validator<Product> validator : validators) {
            validator.validate(p);
        }*/

        quantityValidator.validateUpdate(p);
        Product product = productDAO.update(p);
        if (product == null) {
            throw new NoSuchElementException("Updating Failed!");
        }
        return product;
    }
}
