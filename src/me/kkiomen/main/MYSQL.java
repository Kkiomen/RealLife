package me.kkiomen.main;

import java.sql.*;
import java.util.UUID;

public class MYSQL {

    final String username="root"; // Enter in your db username
    final String password=""; // Enter your password for the db
    final String url = "jdbc:mysql://localhost:3306/websitemc"; // Enter URL with db name

    //Connection vars
    public static Connection connection; //This is the variable we will use to connect to database

    public void connect(){
        try {
            connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String sql = "CREATE TABLE IF NOT EXISTS `users` ( " +
                "`id` INT NOT NULL AUTO_INCREMENT , " +
                "`nick` TEXT NOT NULL , " +
                "`uuid` TEXT NOT NULL , " +
                "`firstname` VARCHAR(10) NOT NULL , " +
                "`secondname` VARCHAR(15) NOT NULL , " +
                "`age` INT NOT NULL , " +
                "PRIMARY KEY (`id`)) ENGINE = InnoDB;";

        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            // use executeUpdate() to update the databases table.
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void close(){
        try {
            if (connection!=null && !connection.isClosed()){

                connection.close();
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void updateUUID(String username, String UUID) throws SQLException {
        String sql = "INSERT INTO myTable(Something) VALUES ('?');";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, "Something"); // Set the first "?" to "Something"
        stmt.executeUpdate();
    }

    public String getID(UUID UUID) throws SQLException {

        String sqlSelect = "SELECT id FROM users WHERE uuid=?;";
        PreparedStatement  stmt2 = connection.prepareStatement(sqlSelect);
        stmt2.setString(1, String.valueOf(UUID));
        ResultSet results = stmt2.executeQuery();
        Integer tmpID = 0;

        while (results.next()){
            tmpID = results.getInt("id");
        }
        String tmpIDString = String.valueOf(tmpID);

        return tmpIDString;
    }


    public String getFirstname(UUID UUID) throws SQLException {

        String sqlSelect = "SELECT firstname FROM users WHERE uuid=?;";
        PreparedStatement  stmt2 = connection.prepareStatement(sqlSelect);
        stmt2.setString(1, String.valueOf(UUID));
        ResultSet results = stmt2.executeQuery();

        String tmpFirstname = null;

        while (results.next()){
            tmpFirstname = results.getString("firstname");
        }

        return tmpFirstname;
    }

    public String getSecondName(UUID UUID) throws SQLException {

        String sqlSelect = "SELECT secondname FROM users WHERE uuid=?;";
        PreparedStatement  stmt2 = connection.prepareStatement(sqlSelect);
        stmt2.setString(1, String.valueOf(UUID));
        ResultSet results = stmt2.executeQuery();

        String tmpSecondname = null;
        while (results.next()){
            tmpSecondname = results.getString("secondname");
        }

        return tmpSecondname;
    }


}
