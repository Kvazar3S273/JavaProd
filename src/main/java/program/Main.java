package program;

import com.github.javafaker.Faker;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

import static program.Helper.readInputDouble;
import static program.Helper.readInputInt;

public class Main {
    static Scanner in = new Scanner(System.in);

    public static void main(String[] args) {
        String strConn = "jdbc:mariadb://localhost:3306/javadb";
        //InsertIntoDB(strConn);
        //GenerateProducts(strConn);
        List<Product> list = SelectFromDB(strConn);
        PrintProductList(list);
        //UpdateForDB(strConn);
        //DeleteFromDB(strConn);
        list = SelectFromDB(strConn);
        PrintProductList(list);
    }

    private static void PrintProductList(List<Product> prods) {
        for (Product p : prods) {
            System.out.println(p.toString());
        }
    }

    public static boolean isTableExist(String strConn) {
        boolean exist = false;
        try (Connection con = DriverManager.getConnection(strConn, "root", "");
             PreparedStatement preparedStatement = con.prepareStatement("SELECT * FROM products");) {
            try (ResultSet resultSet = preparedStatement.executeQuery();) {
                if (resultSet.next()) {
                    exist = true;
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }

        } catch (SQLException exception) {
            System.out.println(exception.getMessage());
        }
        return exist;
    }

    private static final String insertFakerSQL = " INSERT INTO products " +
            " (id,name,price,description) VALUES " + "(?,?,?,?);";

    private static void GenerateProducts(String strConn) {
        Faker faker = Faker.instance(new Locale("uk"));
        try (Connection con = DriverManager.getConnection(strConn, "root", "");

             PreparedStatement preparedStatement = con.prepareStatement(insertFakerSQL);) {

            if (!isTableExist(strConn)) {
                List<Product> generatedProdList = new ArrayList<>();
                for (int i = 0; i < 10; i++) {
                    preparedStatement.setInt(1, faker.random().nextInt(0, 10));
                    preparedStatement.setString(2, faker.commerce().productName());
                    preparedStatement.setDouble(3, faker.random().nextDouble());
                    preparedStatement.setString(4, faker.commerce().color());
                    preparedStatement.addBatch();
                }
                System.out.println("База згенерована");
            } else {
                System.out.println("Ви не можете згенерувати базу, так як вона вже згенерована!");
            }
        } catch (SQLException ex) {
            System.out.println("Error connection: " + ex.getMessage());
        }


    }

    private static void InsertIntoDB(String strConn) {
        try (Connection con = DriverManager.getConnection(strConn, "root", "")) {
            System.out.println("Successful connection");
            String query = "INSERT INTO `products` (`name`,`price`,`description`)" +
                    "VALUES (?,?,?);";
            try (PreparedStatement stmt = con.prepareStatement(query)) {
                String name, description;
                double price;
                System.out.println("Enter name: ");
                name = in.nextLine();
                System.out.println("Enter price: ");
                price = readInputDouble();
                //price = Double.parseDouble(in.nextLine());
                System.out.println("Enter description: ");
                description = in.nextLine();
                stmt.setString(1, name);
                stmt.setDouble(2, price);
                stmt.setString(3, description);
                int rows = stmt.executeUpdate();
                System.out.println("Update rows: " + rows);

            } catch (Exception ex) {
                System.out.println("Error statements: " + ex.getMessage());
            }
        } catch (Exception ex) {
            System.out.println("Error connection: " + ex.getMessage());
        }
    }

    private static List<Product> SelectFromDB(String strConn) {
        try (Connection con = DriverManager.getConnection(strConn, "root", "")) {
            String selectSql = "SELECT * FROM products";
            try {
                PreparedStatement ps = con.prepareStatement(selectSql);
                ResultSet resultSet = ps.executeQuery();
                List<Product> products = new ArrayList<>();
                while (resultSet.next()) {
                    Product p = new Product(resultSet.getInt("id"),
                            resultSet.getString("name"),
                            resultSet.getBigDecimal("price"),
                            resultSet.getString("description"));
                    products.add(p);
                }
                return products;
            } catch (Exception ex) {
                System.out.println("Error executeQuery: " + ex.getMessage());
            }
        } catch (Exception ex) {
            System.out.println("Error connection: " + ex.getMessage());
        }
        return null;
    }

    private static void UpdateForDB(String strConn) {
        try (Connection con = DriverManager.getConnection(strConn, "root", "")) {
            String query = "UPDATE products SET name = ? WHERE id = ?";
            try (PreparedStatement stmt = con.prepareStatement(query)) {
                System.out.print("\nEnter id: ");
                int id = readInputInt();
                System.out.print("Enter new name: ");
                //in.nextLine();
                String name = in.nextLine();
                stmt.setString(1, name);
                stmt.setInt(2, id);

                int rows = stmt.executeUpdate();

                System.out.println("Successful update " + rows);
            } catch (Exception ex) {
                System.out.println("Error update:" + ex.getMessage());
            }
        } catch (Exception ex) {
            System.out.println("Error connection: " + ex.getMessage());
        }
    }

    private static void DeleteFromDB(String strConn) {
        SelectFromDB(strConn);
        try (Connection con = DriverManager.getConnection(strConn, "root", "")) {
            String query = "DELETE FROM products WHERE id = ?";
            try (PreparedStatement stmt = con.prepareStatement(query)) {
                System.out.print("Enter Id: ");
                int id = readInputInt();
                stmt.setInt(1, id);
                int rows = stmt.executeUpdate();
                System.out.println("Successful delete " + rows);

            } catch (Exception ex) {
                System.out.println("Error delete: " + ex.getMessage());
            }

        } catch (Exception ex) {
            System.out.println("Error connection: " + ex.getMessage());
        }

    }
}
