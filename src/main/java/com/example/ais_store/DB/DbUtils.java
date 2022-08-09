package com.example.ais_store.DB;
import com.example.ais_store.Models.Category;
import com.example.ais_store.Models.Product;

import java.sql.*;
import java.util.ArrayList;

public class DbUtils {

    private static Connection con;

    public static void initialization(String name){

        try{
            con = DriverManager.getConnection("jdbc:sqlite:" + name);

            PreparedStatement st_create_category =
                    con.prepareStatement("create table " +
                            "if not exists 'Category' ('id_category' INTEGER PRIMARY KEY AUTOINCREMENT, " +
                            "'name' TEXT UNIQUE);");

            PreparedStatement st_create_product =
                    con.prepareStatement("create table " +
                            "if not exists 'Product' ('id_product' INTEGER PRIMARY KEY AUTOINCREMENT, " +
                            "'id_category' INTEGER, " +
                            "'name' TEXT UNIQUE, " +
                            "'price' DECIMAL, " +
                            "'product_number' INTEGER);");

            int result_pr = st_create_product.executeUpdate();
            int result_cat = st_create_category.executeUpdate();
        } catch (SQLException e){
            System.out.println("Не вірний SQL запит");
            e.printStackTrace();
        }
    }

    public static void insertCategory(String name) throws SQLException {

        PreparedStatement statement = con.prepareStatement("INSERT INTO Category (name) VALUES (?)");
        statement.setString(1, name);
        int result = statement.executeUpdate();

        final boolean oldAutoCommit = statement.getConnection().getAutoCommit();
        statement.getConnection().setAutoCommit(false);

        try {
            statement.executeUpdate();
        } catch(Exception e) {
            statement.getConnection().rollback();
        } finally {
            statement.getConnection().commit();
            statement.getConnection().setAutoCommit(oldAutoCommit);
        }
    }

    public static void setCategoryName(String newName, int id_category) throws SQLException {

        PreparedStatement statement = con.prepareStatement("update Category " +
                "set name=(?) " +
                "where id_category=(?)");
        statement.setString(1, newName);
        statement.setInt(2, id_category);

        final boolean oldAutoCommit = statement.getConnection().getAutoCommit();
        statement.getConnection().setAutoCommit(false);

        try {
            statement.executeUpdate();
        } catch(Exception e) {
            statement.getConnection().rollback();
        } finally {
            statement.getConnection().commit();
            statement.getConnection().setAutoCommit(oldAutoCommit);
        }
    }

    public static void insertProduct(int id_category, String name, double price, int product_number) throws SQLException {

        PreparedStatement statement = con.prepareStatement("INSERT INTO Product (id_category, name, price, product_number) " +
                "VALUES ((?), (?), (?), (?))");
        statement.setInt(1, id_category);
        statement.setString(2, name);
        statement.setDouble(3, price);
        statement.setInt(4, product_number);

        final boolean oldAutoCommit = statement.getConnection().getAutoCommit();
        statement.getConnection().setAutoCommit(false);

        try {
            statement.executeUpdate();
        } catch(Exception e) {
            statement.getConnection().rollback();
        } finally {
            statement.getConnection().commit();
            statement.getConnection().setAutoCommit(oldAutoCommit);
        }
    }

    public static void setProductCategory(int id_product, int id_category) throws SQLException {
        PreparedStatement statement = con.prepareStatement("update Product " +
                "set id_category=(?) " +
                "where id_product=(?)");
        statement.setInt(1, id_category);
        statement.setInt(2, id_product);

        final boolean oldAutoCommit = statement.getConnection().getAutoCommit();
        statement.getConnection().setAutoCommit(false);

        try {
            statement.executeUpdate();
        } catch(Exception e) {
            statement.getConnection().rollback();
        } finally {
            statement.getConnection().commit();
            statement.getConnection().setAutoCommit(oldAutoCommit);
        }
    }

    public static int getProductNumber(int id_product) {
        try {
            PreparedStatement st = con.prepareStatement("SELECT product_number " +
                    "FROM Product " +
                    "WHERE id_product=(?)");
            st.setInt(1, id_product);
            ResultSet res = st.executeQuery();

            while (res.next()) {
                int product_number = res.getInt("product_number");
                System.out.println(product_number);
                return product_number;
            }
            res.close();
            st.close();
        } catch(SQLException e) {
            System.out.println("Не вірний SQL запит на вибірку даних");
            e.printStackTrace();
        }
        return 0;
    }

    public static void decreaseProductNumber(int id_product, int n) throws SQLException {
        PreparedStatement statement = con.prepareStatement("update Product " +
                "set product_number=product_number - (?) " +
                "where id_product=(?)");
        statement.setInt(1, n);
        statement.setInt(2, id_product);

        final boolean oldAutoCommit = statement.getConnection().getAutoCommit();
        statement.getConnection().setAutoCommit(false);

        try {
            statement.executeUpdate();
        } catch(Exception e) {
            statement.getConnection().rollback();
        } finally {
            statement.getConnection().commit();
            statement.getConnection().setAutoCommit(oldAutoCommit);
        }
    }

    public static void increaseProductNumber(int id_product, int n) throws SQLException {
        PreparedStatement statement = con.prepareStatement("update Product " +
                "set product_number= product_number + (?) " +
                "where id_product=(?)");
        statement.setInt(1, n);
        statement.setInt(2, id_product);

        final boolean oldAutoCommit = statement.getConnection().getAutoCommit();
        statement.getConnection().setAutoCommit(false);

        try {
            statement.executeUpdate();
        } catch(Exception e) {
            statement.getConnection().rollback();
        } finally {
            statement.getConnection().commit();
            statement.getConnection().setAutoCommit(oldAutoCommit);
        }
    }

    public static void updateIdProduct(int new_id_product, int old_id_product) throws SQLException {
        PreparedStatement statement = con.prepareStatement("update Product set id_product=(?) where id_product=(?)");
        statement.setDouble(1, new_id_product);
        statement.setInt(2, old_id_product);

        final boolean oldAutoCommit = statement.getConnection().getAutoCommit();
        statement.getConnection().setAutoCommit(false);

        try {
            statement.executeUpdate();
        } catch(Exception e) {
            statement.getConnection().rollback();
        } finally {
            statement.getConnection().commit();
            statement.getConnection().setAutoCommit(oldAutoCommit);
        }
    }

    public static void updateIdCategoryForProduct(int new_id_category, int id_product) throws SQLException {
        PreparedStatement statement = con.prepareStatement("update Product set id_category=(?) where id_product=(?)");
        statement.setDouble(1, new_id_category);
        statement.setInt(2, id_product);

        final boolean oldAutoCommit = statement.getConnection().getAutoCommit();
        statement.getConnection().setAutoCommit(false);

        try {
            statement.executeUpdate();
        } catch(Exception e) {
            statement.getConnection().rollback();
        } finally {
            statement.getConnection().commit();
            statement.getConnection().setAutoCommit(oldAutoCommit);
        }
    }
    public static void updateProductName(String newName, int id_product) throws SQLException {
        PreparedStatement statement = con.prepareStatement("update Product set name=(?) where id_product=(?)");
        statement.setString(1, newName);
        statement.setInt(2, id_product);

        final boolean oldAutoCommit = statement.getConnection().getAutoCommit();
        statement.getConnection().setAutoCommit(false);

        try {
            statement.executeUpdate();
        } catch(Exception e) {
            statement.getConnection().rollback();
        } finally {
            statement.getConnection().commit();
            statement.getConnection().setAutoCommit(oldAutoCommit);
        }
    }

    public static void updatePrice(double newPrice, int id_product) throws SQLException {
        PreparedStatement statement = con.prepareStatement("update Product set price=(?) where id_product=(?)");
        statement.setDouble(1, newPrice);
        statement.setInt(2, id_product);

        final boolean oldAutoCommit = statement.getConnection().getAutoCommit();
        statement.getConnection().setAutoCommit(false);

        try {
            statement.executeUpdate();
        } catch(Exception e) {
            statement.getConnection().rollback();
        } finally {
            statement.getConnection().commit();
            statement.getConnection().setAutoCommit(oldAutoCommit);
        }
    }

    public static void updateProductNumber(int new_product_number, int id_product) throws SQLException {
        PreparedStatement statement = con.prepareStatement("update Product set product_number=(?) where id_product=(?)");
        statement.setDouble(1, new_product_number);
        statement.setInt(2, id_product);

        final boolean oldAutoCommit = statement.getConnection().getAutoCommit();
        statement.getConnection().setAutoCommit(false);

        try {
            statement.executeUpdate();
        } catch(Exception e) {
            statement.getConnection().rollback();
        } finally {
            statement.getConnection().commit();
            statement.getConnection().setAutoCommit(oldAutoCommit);
        }
    }

    public static void updateIdCategory(int new_category_id, int old_category_id) throws SQLException {
        setUpdatedCategoryId(new_category_id, old_category_id);
        PreparedStatement updateCategory = con.prepareStatement("update Category " +
                                                                     "set id_category=(?) where id_category=(?)");
        updateCategory.setInt(1, new_category_id);
        updateCategory.setInt(2, old_category_id);

        final boolean oldAutoCommit = updateCategory.getConnection().getAutoCommit();

        updateCategory.getConnection().setAutoCommit(false);

        try {
            updateCategory.executeUpdate();
        } catch(Exception e) {
            updateCategory.getConnection().rollback();
        } finally {
            updateCategory.getConnection().commit();
            updateCategory.getConnection().setAutoCommit(oldAutoCommit);
        }
    }

    public static void setUpdatedCategoryId(int new_category_id, int old_category_id) throws SQLException {
        PreparedStatement updateProducts = con.prepareStatement("update Product " +
                "set id_category=(?) where id_category=(?)");
        updateProducts.setInt(1, new_category_id);
        updateProducts.setInt(2, old_category_id);

        final boolean oldAutoCommit = updateProducts.getConnection().getAutoCommit();

        updateProducts.getConnection().setAutoCommit(false);

        try {
            updateProducts.executeUpdate();
        } catch(Exception e) {
            updateProducts.getConnection().rollback();
        } finally {
            updateProducts.getConnection().commit();
            updateProducts.getConnection().setAutoCommit(oldAutoCommit);
        }
    }


    public static void updateCategoryName(String newName, int id_category) throws SQLException {
        PreparedStatement statement = con.prepareStatement("update Category set name=(?) where id_category=(?)");
        statement.setString(1, newName);
        statement.setInt(2, id_category);

        final boolean oldAutoCommit = statement.getConnection().getAutoCommit();
        statement.getConnection().setAutoCommit(false);

        try {
            statement.executeUpdate();
        } catch(Exception e) {
            statement.getConnection().rollback();
        } finally {
            statement.getConnection().commit();
            statement.getConnection().setAutoCommit(oldAutoCommit);
        }
    }

    public static ArrayList<Product> showAllProducts() {
        var products = new ArrayList<Product>();

        try {
            Statement st = con.createStatement();
            ResultSet res = st.executeQuery("SELECT * FROM Product");

            while (res.next()) {
                int id_product = res.getInt("id_product");
                int id_category = res.getInt("id_category");
                String name = res.getString("name");
                double price = res.getDouble("price");
                int product_number = res.getInt("product_number");
                Product product = new Product(id_product, id_category, name, price, product_number, price*product_number);
                products.add(product);
            }
            res.close();
            st.close();
        } catch(SQLException e) {
            System.out.println("Не вірний SQL запит на вибірку даних");
            e.printStackTrace();
        }
        return products;
    }
    public static ArrayList<Product> showAllProductsFromCategory(int id) {
        var products = new ArrayList<Product>();

        try {
            PreparedStatement st = con.prepareStatement("SELECT * FROM Product WHERE id_category = (?)");
            st.setInt(1, id);
            ResultSet res = st.executeQuery();

            while (res.next()) {
                int id_product = res.getInt("id_product");
                int id_category = res.getInt("id_category");
                String name = res.getString("name");
                double price = res.getDouble("price");
                int product_number = res.getInt("product_number");
                Product product = new Product(id_product, id_category, name, price, product_number,
                        price*product_number);
                products.add(product);
            }
            res.close();
            st.close();

        } catch(SQLException e) {
            System.out.println("Не вірний SQL запит на вибірку даних");
            e.printStackTrace();
        }
        return products;
    }


    public static Product showProductByID(int id){
        try{
            PreparedStatement st = con.prepareStatement("SELECT * FROM Product WHERE id_product=(?)");
            st.setInt(1, id);
            ResultSet res = st.executeQuery();

            while (res.next()) {
                int id_product = res.getInt("id_product");
                int id_category = res.getInt("id_category");
                String name = res.getString("name");
                double price = res.getDouble("price");
                int product_number = res.getInt("product_number");

                Product product = new Product(id_product, id_category, name, price, product_number, price*product_number);
                return product;
            }
            res.close();
            st.close();
        } catch(SQLException e){
            System.out.println("Не вірний SQL запит на вибірку даних");
            e.printStackTrace();
        }
        return null;
    }

    public static Category showCategoryByID(int id){
        try{
            PreparedStatement st = con.prepareStatement("SELECT * FROM Category WHERE id_category=(?)");
            st.setInt(1, id);
            ResultSet res = st.executeQuery();
//            if (!res.next()) {
//                throw new Exception();
//            }

            while (res.next()) {
                int id_category = res.getInt("id_category");
                String name = res.getString("name");
                Category category =  new Category(id_category, name);
                return category;
            }

            res.close();
            st.close();
        } catch(SQLException e){
            System.out.println("Не вірний SQL запит на вибірку даних");
            e.printStackTrace();
        }
        return null;
    }

    public static ArrayList<String> getCategoryNames() {
        ArrayList<String> categoryNames = new ArrayList<>();
        categoryNames.add("Всі");
        try{
            PreparedStatement st = con.prepareStatement("SELECT name FROM Category");
            ResultSet res = st.executeQuery();

            while (res.next()) {
                String name = res.getString("name");
                categoryNames.add(name);
            }
            res.close();
            st.close();
        }catch(SQLException e){
            System.out.println("Не вірний SQL запит на вибірку даних");
            e.printStackTrace();
        }
        return categoryNames;
    }

    public static int getCategoryIdByName(String name) {
        try{
            PreparedStatement st = con.prepareStatement("SELECT id_category FROM Category WHERE name=(?)");
            st.setString(1, name);
            ResultSet res = st.executeQuery();

            while (res.next()) {
                int id_category = res.getInt("id_category");
                return id_category;
            }
            res.close();
            st.close();
        }catch(SQLException e){
            System.out.println("Не вірний SQL запит на вибірку даних");
            e.printStackTrace();
        }
        return 0;
    }

    public static ArrayList<Category> showAllCategories(){
        ArrayList<Category> categories = new ArrayList<>();

        try{
            Statement st = con.createStatement();
            ResultSet res = st.executeQuery("SELECT * FROM Category");

            while (res.next()) {
                int id_category = res.getInt("id_category");
                String name = res.getString("name");
                Category category =  new Category(id_category, name);
                categories.add(category);
            }
            res.close();
            st.close();
        }catch(SQLException e){
            System.out.println("Не вірний SQL запит на вибірку даних");
            e.printStackTrace();
        }
        return categories;
    }

    public static double getTotalSumOfProduct(int id) {
        try{
            PreparedStatement st = con.prepareStatement("SELECT (price*product_number) AS total_sum " +
                                                            "FROM Product " +
                                                            "WHERE id_product=(?)");
            st.setInt(1, id);
            ResultSet res = st.executeQuery();

            while (res.next()) {
                double total_sum = res.getInt("total_sum");
                return total_sum;
            }
            res.close();
            st.close();
        } catch(SQLException e){
            System.out.println("Не вірний SQL запит на вибірку даних");
            e.printStackTrace();
        }

        return 0;
    }

    public static double getTotalSumOfProductsInCategory(int id) {
        try{
            PreparedStatement st = con.prepareStatement("SELECT SUM(price*product_number) AS total_sum " +
                                                            "FROM Product " +
                                                            "WHERE id_category=(?)");
            st.setInt(1, id);
            ResultSet res = st.executeQuery();

            while (res.next()) {
                double total_sum = res.getInt("total_sum");
                return total_sum;
            }
            res.close();
            st.close();
        } catch(SQLException e){
            System.out.println("Не вірний SQL запит на вибірку даних");
            e.printStackTrace();
        }

        return 0;
    }

    public static void deleteCategory(int id) throws SQLException {
        PreparedStatement deleteAllProductsOfCategory = con.prepareStatement("delete from Product " +
                                                                                        "where id_category=(?)");
        PreparedStatement deleteCategory = con.prepareStatement("delete from Category" +
                                                                            " where id_category=(?)");
        deleteAllProductsOfCategory.setInt(1, id);
        deleteCategory.setInt(1, id);

        deleteAllProductsOfCategory.execute();
        deleteCategory.execute();

        deleteAllProductsOfCategory.close();
        deleteCategory.close();
    }

    public static void deleteProduct(int id) throws SQLException {
        PreparedStatement statement = con.prepareStatement("delete from Product where id_product=(?)");
        statement.setInt(1, id);
        statement.execute();
        statement.close();
    }
}