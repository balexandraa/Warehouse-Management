package bll.validators;

import dao.ClientDAO;
import model.Client;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Gestioneaza operatiile efectuate pe client: valideaza datele si comunica cu DAO
 */

public class ClientBLL {
    private List<Validator<Client>> validators;
    private ClientDAO clientDAO;

    /**
     * Constructorul clasei care o initializeaza cu validatorul pentru varsta si obiectul ClientDAO
     */

    public ClientBLL() {
        validators = new ArrayList<Validator<Client>>();
        validators.add(new AgeValidator());
        clientDAO = new ClientDAO();
    }


    /**
     * Gaseste clientul dupa ID
     * @param id ID-ul clientului cautat
     * @return clientul gasit
     * @throws NoSuchElementException daca nu se gaseste un client dupa ID-ul primit ca parametru
     */
    public Client findClientById(int id) {
        Client client = clientDAO.findById(id);
        if (client == null) {
            throw new NoSuchElementException("The client with id =" + id + " was not found!");
        }
        return client;
    }

    /**
     * Gaseste clientul dupa nume (nume + prenume)
     * @param firstName prenumele clientului cautat
     * @param lastName numele clientului cautat
     * @return clientul gasit
     * @throws NoSuchElementException daca nu se gaseste clientul
     */
    //apeleaza metoda fidByNAme2 care are 2 parametrii (cea din abstract are doar un param)
    public Client findClientByName(String firstName, String lastName) {
        Client client = clientDAO.findByName2(firstName, lastName);
        if (client == null) {
            throw new NoSuchElementException("The client with name = " + firstName + " " + lastName + " " + " was not found!");
        }
        return client;
    }

    /**
     * Gaseste toti clientii
     * @return lista de clienti
     * @throws NoSuchElementException daca nu sunt clienti
     */
    public List<Client> findAll() {
        List<Client> clients = clientDAO.findAll();
        if (clients == null) {
            throw new NoSuchElementException("There are NO clients!!");
        }
        return clients;
    }

    /**
     * insereaza un client in BD
     * @param c clientul de inserat
     * @return NoSuchElementException daca inserarea a esuat
     */
    public Client insert(Client c) {
        Client client = clientDAO.insert(c);
        if (client == null) {
            throw new NoSuchElementException("Insertion Failed!");
        }
        return client;
    }

    /**
     * Sterge un client in BD
     * @param id ID-ul clientului de sters
     */
    public void delete(int id) {
        clientDAO.delete(id);
    }

    /**
     * Actualizeaza un client in BD
     * @param c clientul de actualizat
     * @return clientul actualizat
     * @throws NoSuchElementException daca acualizarea a esuat
     */
    public Client update(Client c) {
        Client client = clientDAO.update(c);
        if (client == null) {
            throw new NoSuchElementException("Updating Failed!");
        }
        return client;
    }
}
