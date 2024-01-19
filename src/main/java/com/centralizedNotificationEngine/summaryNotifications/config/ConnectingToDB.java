package com.centralizedNotificationEngine.summaryNotifications.config;

import java.sql.*;

public class ConnectingToDB {

    public void Execute(String sqlStr) throws SQLException {
     //  Connection developmentConnection = DriverManager.getConnection("jdbc:mysql://10.133.208.200:3306/flt","flt","flt123");
     //  Connection uatConnection = DriverManager.getConnection("jdbc:mysql://10.133.232.146:3307/test","suvarnaj","suvarna123");
        Connection productionConnection = DriverManager.getConnection("jdbc:mysql://10.150.10.9:3306/CNS","suvarnaj_prod","Suvja@23");
        Statement statement = productionConnection.createStatement();
        statement.execute(sqlStr);
    }
}
