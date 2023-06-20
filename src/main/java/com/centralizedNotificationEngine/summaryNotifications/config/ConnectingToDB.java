package com.centralizedNotificationEngine.summaryNotifications.config;

import java.sql.*;

public class ConnectingToDB {

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        //Loading the required JDBC Driver class
        //Creating a connection to the database
        Connection connection = DriverManager.getConnection("jdbc:mysql://10.133.208.200:3306/flt","flt","flt123");

        //Executing SQL query and fetching the result
        Statement statement = connection.createStatement();
        String sqlStr = "select * from TOP_324_REPORT";
        ResultSet resultSet = statement.executeQuery(sqlStr);
        while (resultSet.next()) {
            System.out.println(resultSet.getMetaData());
        }
    }
}
