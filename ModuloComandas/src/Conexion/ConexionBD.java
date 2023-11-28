/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Conexion;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;
import java.sql.ResultSetMetaData;

/**
 *
 * @author Jose Sebastian Llamas Valle
 */
public class ConexionBD {
    private static final String JDBC_URL = "jdbc:mysql://148.211.124.58:3306/neotokio";
    private static final String JDBC_USER = "neotokio";
    private static final String JDBC_PASSWORD = "jFrB)(A!_s1AYwj0";

    public static Connection getConnection() {
        Connection conn = null;
        try {
            // Cargar el driver JBDC MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establecer conexión
            conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

    public static void main(String[] args) {
        Connection connection = getConnection();
        if (connection != null) {
            System.out.println("Conexion exitosa a la base de datos neotokio!");

            boolean continueChecking = true;
            while (continueChecking) {
                try {
                    // Obtener una lista de todas las tablas
                    DatabaseMetaData metaData = (DatabaseMetaData) connection.getMetaData();
                    String catalog = null;
                    String schemaPattern = null;
                    String tableNamePattern = null;
                    String[] types = {"TABLE"};
                    ResultSet tables = metaData.getTables(catalog, schemaPattern, tableNamePattern, types);

                    System.out.println("Lista de tablas dentro de la base de datos:");
                    while (tables.next()) {
                        String tableName = tables.getString("TABLE_NAME");
                        System.out.println(tableName);
                    }

                    tables.close();

                    // Solicitar al usuario que seleccione una tabla
                    Scanner scanner = new Scanner(System.in);
                    System.out.print("Ingresa el nombre de la tabla para ver sus filas y atributos: ");
                    String selectedTable = scanner.nextLine();

                    // Construir y ejecutar la consulta SQL para obtener todas las filas
                    String query = "SELECT * FROM " + selectedTable;
                    Statement statement = connection.createStatement();
                    ResultSet resultSet = statement.executeQuery(query);

                    // Obtener metadatos para recuperar los nombres de los atributos
                    ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
                    int columnCount = resultSetMetaData.getColumnCount();

                    // Imprimir los nombres de los atributos
                    System.out.println("Atributos de la tabla " + selectedTable + ":");
                    for (int i = 1; i <= columnCount; i++) {
                        String attributeName = resultSetMetaData.getColumnName(i);
                        System.out.println(attributeName);
                    }

                    // Imprimir las filas
                    System.out.println("Filas de la tabla " + selectedTable + ":");
                    while (resultSet.next()) {
                        for (int i = 1; i <= columnCount; i++) {
                            System.out.print(resultSet.getString(i) + "\t");
                        }
                        System.out.println();
                    }

                    resultSet.close();
                    statement.close();

                    // Preguntar al usuario si desea seguir checando la Base de Datos.re
                    System.out.print("¿Deseas seguir revisando la base de datos? (Si/No): ");
                    String userInput = scanner.nextLine().toLowerCase();
                    continueChecking = userInput.equals("si") || userInput.equals("si");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Error al conectarse a la base de datos.");
        }
    }

    public PreparedStatement prepareStatement(String consulta) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
