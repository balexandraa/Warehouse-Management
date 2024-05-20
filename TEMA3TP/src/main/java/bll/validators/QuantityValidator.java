package bll.validators;

import model.Product;

/**
 * Validator pentru validare cantitate produs
 */
public class QuantityValidator implements Validator<Product> {

    /**
     * Valideaza cantitatea atunci cand se efectueaza operatia de inserare a unui produs
     * @param product produsul pt care se valideaza cantitatea
     * @throws IllegalArgumentException daca cantitatea mai mica sau egal decat 0
     */
    public void validateInsert(Product product) {
        if (product.getQuantity() <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0!");
        }
    }

    /**
     * Valideaza cantitatea atunci cand se efectueaza operatia de actualizare a unui produs
     * @param product produsul pt care se valideaza cantitatea
     * @throws IllegalArgumentException daca cantitatea mai mica decat 0
     */
    public void validateUpdate(Product product) {
        if (product.getQuantity() < 0) {
            throw new IllegalArgumentException("Quantity must be greater than or equal to 0!");
        }
    }

    @Override
    public void validate(Product product) {
       /* if (product.getQuantity() <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0!");
        }*/
    }
}
