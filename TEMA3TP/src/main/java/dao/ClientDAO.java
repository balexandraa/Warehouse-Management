package dao;

import model.Client;

import connection.ConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

/**
 * Clasa pentru accesul la date pentru obiectele de tip Client
 */
public class ClientDAO extends AbstractDAO<Client> {

    /**
     * Cauta un client dupa nume + prenume
     * @param firstName prenumele clientului
     * @param lastName numele clientuluo
     * @return clientul gasit in BD sau null daca nu exista
     */
    //metoda in plus fata de cele din abstractDAO pt ca are 2 param
    public Client findByName2(String firstName, String lastName) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String query = "SELECT * FROM Client WHERE firstName = ? AND lastName = ?";
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            statement.setString(1, firstName);
            statement.setString(2, lastName);
            resultSet = statement.executeQuery();
            // daca se poate muta cursorul la urm rand
            if (resultSet.next()) {
                Client client = new Client();
                client.setID(resultSet.getInt("id"));
                client.setFirstName(resultSet.getString("firstName"));
                client.setLastName(resultSet.getString("lastName"));
                client.setAge(resultSet.getInt("age"));
                return client;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "ClientDAO:findClientByName " + e.getMessage());
        } finally {
            ConnectionFactory.close(resultSet);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return null;
    }


}
