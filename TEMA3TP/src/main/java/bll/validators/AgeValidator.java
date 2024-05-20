package bll.validators;

import model.Client;

/**
 * Validator pentru verificarea varstei unui client
 * Asigura ca varsta clientului se incadreaza in intervalul specificat
 */

public class AgeValidator implements Validator<Client>{
    private static final int MIN_AGE = 18;
    private static final int MAX_AGE = 110;

    /**
     * Valideaza varsta clientului dat
     * @param client
     * @throws IllegalArgumentException daca varsta clientului mai mica decat  {@code MIN_AGE} sau > {@code MAX_AGE}
     */
    @Override
    public void validate(Client client) {
        if (client.getAge() < MIN_AGE || client.getAge() > MAX_AGE) {
            throw new IllegalArgumentException("The Client Age limit is not respected!");
        }
    }
}
