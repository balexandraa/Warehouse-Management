package dao;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import connection.ConnectionFactory;

import javax.swing.table.DefaultTableModel;

import static java.lang.Integer.parseInt;

/**
 * Clasa abstracta pentru accesul la date. Implementeaza operatiile de baza pentru obiecte generice
 * @param <T> tipul obiectului de manipulat in BD
 */
public class AbstractDAO<T> {
	protected static final Logger LOGGER = Logger.getLogger(AbstractDAO.class.getName());

	private final Class<T> type;

	/**
	 * Constructorul clasei AbstractDAO: obtine tipul de clasa pentru operatiile generice
	 */
	@SuppressWarnings("unchecked")
	public AbstractDAO() {
		// de aici luam tabelul pe care lucram (clasa specificata)
		this.type = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	}

	/**
	 * Creaza interograre SQL pentru selectarea unui obiect dintr-un anumit camp
	 * @param field numele campului dupa care se face selectarea
	 * @return interogarea SQL generata
	 */
	//numele  clasei trebuie sa fie acelasi cu numele tabelului
	private String createSelectQuery(String field) {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT ");
		sb.append(" * ");
		sb.append(" FROM ");
		sb.append(type.getSimpleName());
		sb.append(" WHERE " + field + " =?");
		return sb.toString();
	}

	/**
	 * Creaza interograre SQL pentru selectarea tuturor obiectelor din tabel
	 * @return interogarea SQL generata
	 */
	private String createSelectAllQuery() {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT ");
		sb.append(" * ");
		sb.append(" FROM ");
		sb.append(type.getSimpleName());
		return sb.toString();
	}

	/**
	 * Creaza interograre SQL pentru inserarea unui obiect in tabel
	 * @param row obiectul de inserat in tabel
	 * @return interogarea SQL generata
	 */
	private String createInsertQuery(T row) {
		StringBuilder sb = new StringBuilder();
		sb.append("INSERT INTO ");
		sb.append(type.getSimpleName());   // numele tabelului => numele clasei
		sb.append(" VALUES ( ");

		// luam toate field urile obiectului
		Field[] fields = type.getDeclaredFields();

		for (int i = 0; i < fields.length; i++) {
			fields[i].setAccessible(true);
			Object value = null;
			try {
				// luam valoarea fiecarui field
				value = fields[i].get(row);
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}

			//valoarea care trb completata
			sb.append("?");

			//punem virgula daca nu e ultimul element
			if (i < fields.length - 1) {
				sb.append(", ");
			}
		}
		sb.append(")");
		return sb.toString();
	}

	/**
	 * Creaza interograre SQL pentru stergerea unui obiect din tabel dupa ID
	 * @param id ID-ul obiectului de sters
	 * @return interogarea SQL generata
	 */
	public String createDeleteQuery(int id) {
		StringBuilder sb = new StringBuilder();
		sb.append("DELETE FROM ");
		sb.append(type.getSimpleName());
		sb.append(" WHERE ");
		sb.append("id =?");
		return sb.toString();
	}

	/**
	 * Creaza interograre SQL pentru actualizarea unui obiect din tabel
	 * @param object obiectul de actualizat
	 * @return interogarea SQL generata
	 */
	public String createUpdateQuery(T object) {
		StringBuilder sb = new StringBuilder();
		sb.append("UPDATE ");
		sb.append(type.getSimpleName());
		sb.append(" SET ");

		//luam toate filed urile mai putin id
		Field[] fields = type.getDeclaredFields();
		for (int i = 0; i < fields.length; i++) {
			fields[i].setAccessible(true);
			String fieldName = fields[i].getName();
			if (!fieldName.equals("ID")) {
				sb.append(fieldName).append(" = ?");
				if (i < fields.length - 1) {
					sb.append(", ");
				}
			}
		}
		sb.append(" WHERE ID = ?");
		return sb.toString();
	}

	/**
	 * Gaseste toate obiectele din tabel
	 * @return lista obiectelor
	 */
	public List<T> findAll() {
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		String query = createSelectAllQuery();
		try {
			connection = ConnectionFactory.getConnection();
			statement = connection.prepareStatement(query);
			resultSet = statement.executeQuery();
			return createObjects(resultSet);
		} catch (SQLException e) {
			LOGGER.log(Level.WARNING, type.getName() + "DAO:findAll " + e.getMessage());
		} finally {
			ConnectionFactory.close(resultSet);
			ConnectionFactory.close(statement);
			ConnectionFactory.close(connection);
		}
		return null;
	}

	/**
	 * Gaseste un obiect dupa ID
	 * @param id ID-ul obiectului cautat
	 * @return obiectul gasit sau null daca nu exista
	 */
	public T findById(int id) {
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		String query = createSelectQuery("id");
		try {
			connection = ConnectionFactory.getConnection();
			statement = connection.prepareStatement(query);
			statement.setInt(1, id);
			resultSet = statement.executeQuery();
			return createObjects(resultSet).get(0);
		} catch (SQLException e) {
			LOGGER.log(Level.WARNING, type.getName() + "DAO:findById " + e.getMessage());
		} finally {
			ConnectionFactory.close(resultSet);
			ConnectionFactory.close(statement);
			ConnectionFactory.close(connection);
		}
		return null;
	}

	/**
	 * Gaseste un obiect dupa nume
	 * @param name numele obiectului
	 * @return obiectul gasit sau null daca nu exista
	 */
	public T findByName(String name) {
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		String query = createSelectQuery("name");
		try {
			connection = ConnectionFactory.getConnection();
			statement = connection.prepareStatement(query);
			statement.setString(1, name);
			resultSet = statement.executeQuery();
			return createObjects(resultSet).get(0);
		} catch (SQLException e) {
			LOGGER.log(Level.WARNING, type.getName() + "DAO:findByName " + e.getMessage());
		} finally {
			ConnectionFactory.close(resultSet);
			ConnectionFactory.close(statement);
			ConnectionFactory.close(connection);
		}
		return null;
	}

	/**
	 * Creeaza o lista de obiecte de tipul specificat, pe baza rezultatului unei interogari din ResultSet
	 * @param resultSet rezultatului unei interogari in BD
	 * @return lista de obiecte de tipul specificat, populata cu datele din ResultSet
	 */
	private List<T> createObjects(ResultSet resultSet) {
		List<T> list = new ArrayList<T>();
		Constructor[] ctors = type.getDeclaredConstructors();
		Constructor ctor = null;
		for (int i = 0; i < ctors.length; i++) {
			ctor = ctors[i];
			if (ctor.getGenericParameterTypes().length == 0)
				break;
		}
		try {
			while (resultSet.next()) {
				ctor.setAccessible(true);
				T instance = (T)ctor.newInstance();
				for (Field field : type.getDeclaredFields()) {
					String fieldName = field.getName();
					Object value = resultSet.getObject(fieldName);
					PropertyDescriptor propertyDescriptor = new PropertyDescriptor(fieldName, type);
					Method method = propertyDescriptor.getWriteMethod();
					method.invoke(instance, value);
				}
				list.add(instance);
			}
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IntrospectionException e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * Insereaza un nou obiect in tabel
	 * @param t obiectul de inserat
	 * @return obiectul inserat sau null in caz de esec
	 */
	public T insert(T t) {
		Connection connection = null;
		PreparedStatement statement = null;
		String query = createInsertQuery(t);
		try {
			connection = ConnectionFactory.getConnection();
			statement = connection.prepareStatement(query);
			//setam parametrii pt query
			Field[] fields = type.getDeclaredFields();
			for (int i = 0; i < fields.length; i++) {
				fields[i].setAccessible(true);
				Object value = fields[i].get(t);
				if (value.getClass().getSimpleName().equals("String"))
					statement.setString(i+1, value.toString());
				else
					statement.setInt(i+1, parseInt(value.toString()));
			}
			statement.executeUpdate();
			return t;
		} catch (SQLException | IllegalAccessException e) {
			LOGGER.log(Level.WARNING, type.getName() + "DAO:insert " + e.getMessage());
		} finally {
			ConnectionFactory.close(statement);
			ConnectionFactory.close(connection);
		}
		return null;
	}

	/**
	 * Sterge un obiect din tabel dupa ID
	 * @param id ID-ul obiectului de sters
	 */
	public void delete(int id) {
		Connection connection = null;
		PreparedStatement statement = null;
		String query = createDeleteQuery(id);
		try {
			connection = ConnectionFactory.getConnection();
			statement = connection.prepareStatement(query);
			statement.setInt(1, id);
			statement.executeUpdate();
		} catch (SQLException e) {
			LOGGER.log(Level.WARNING, type.getName() + "DAO:delete " + e.getMessage());
		} finally {
			ConnectionFactory.close(statement);
			ConnectionFactory.close(connection);
		}
	}

	/**
	 * Actualizeaza un obiect existent in tabel
	 * @param t obiectul de acualizat
	 * @return obiectul actualizat sau null in caz de esec
	 */
	public T update(T t) {
		Connection connection = null;
		PreparedStatement statement = null;
		String query = createUpdateQuery(t);
		try {
			connection = ConnectionFactory.getConnection();
			statement = connection.prepareStatement(query);
			Field[] fields = type.getDeclaredFields();
			int paramIndex = 1;
			for (int i = 0; i < fields.length; i++) {
				fields[i].setAccessible(true);
				String fieldName = fields[i].getName();
				// luam valorile campurilor diferite de id
				if (!fieldName.equals("ID")) {
					Object val = fields[i].get(t);
					// obtinem tipul campului pt a decide tipul param in interogare
					Class<?> fieldType = fields[i].getType();
					if (fieldType == int.class || fieldType == Integer.class) {
						statement.setInt(paramIndex, (Integer) val);
					} else if (fieldType == String.class) {
						statement.setString(paramIndex, (String) val);
					}
					paramIndex++;
				}
			}
			// setam param pt id
			Field idField = type.getDeclaredField("ID");
			idField.setAccessible(true);
			Object idValue = idField.get(t);
			statement.setInt(paramIndex, (Integer) idValue);
			statement.executeUpdate();
		} catch (SQLException | IllegalAccessException | NoSuchFieldException e) {
			LOGGER.log(Level.WARNING, type.getName() + "DAO:update " + e.getMessage());
		} finally {
			ConnectionFactory.close(statement);
			ConnectionFactory.close(connection);
		}
		return t;
	}

	/**
	 * Genereaza un tabel dintr-o lista de obiecte
	 * @param obj lista de obiecte din care se genereaza tabelul
	 * @return DefaultTableModel care contine datele tabelului
	 */
	public DefaultTableModel createJTable(List<T> obj) {
		int coloaneTabel = 0;
		for (Field field : obj.get(0).getClass().getDeclaredFields()) {
			coloaneTabel++;
		}
		String[] denumireColoane = new String[coloaneTabel];
		coloaneTabel = 0;
		for (Field field : obj.get(0).getClass().getDeclaredFields()) {
			denumireColoane[coloaneTabel++] = field.getName();
		}
		Object[][] valori = new Object[obj.size()][coloaneTabel];
		coloaneTabel = 0;
		for (int i = 0; i < obj.size(); i++) {
			for (Field field : obj.get(i).getClass().getDeclaredFields()) {
				field.setAccessible(true);
				try {
					Object value;
					value = field.get(obj.get(i));
					valori[i][coloaneTabel++] = value;
				} catch (IllegalAccessException e) {
					throw new RuntimeException(e);
				}
			}
			coloaneTabel = 0;
		}
		return new DefaultTableModel(valori, denumireColoane);
	}

}
