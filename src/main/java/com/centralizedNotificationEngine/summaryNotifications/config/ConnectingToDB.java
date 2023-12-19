package com.centralizedNotificationEngine.summaryNotifications.config;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConnectingToDB {

    public void Execute(String sqlStr) throws SQLException {
   //    Connection developmentConnection = DriverManager.getConnection("jdbc:mysql://10.133.208.200:3306/flt","flt","flt123");
        Connection uatConnection = DriverManager.getConnection("jdbc:mysql://10.133.232.146:3307/test","suvarnaj","suvarna123");
      //  Connection productionConnection = DriverManager.getConnection("jdbc:mysql://10.150.10.9:3306/CNS","suvarnaj_prod","Suvja@23");
        Statement statement = uatConnection.createStatement();
        statement.execute(sqlStr);
    }

    public void ExecuteMDM(String sqlStr) throws SQLException {
      //  Connection developmentConnection = DriverManager.getConnection("jdbc:mysql://inp44pddb003.vsnl.co.in:1527/flt","etl_staging","etl_staging02");
       Connection uatConnection = DriverManager.getConnection("jdbc:mysql://inp44pddb003.vsnl.co.in:1527/flt","etl_staging","etl_staging02");
     //   Connection productionConnection = DriverManager.getConnection("jdbc:mysql://10.150.10.9:3306/CNS","suvarnaj_prod","Suvja@23");
        Statement statement = uatConnection.createStatement();
        statement.execute(sqlStr);
    }

    public List<Map<String, Object>> QueryForList(String sqlStr) throws SQLException {
    //   Connection developmentConnection = DriverManager.getConnection("jdbc:mysql://10.133.208.200:3306/flt","flt","flt123");
       Connection uatConnection = DriverManager.getConnection("jdbc:mysql://10.133.232.146:3307/test","suvarnaj","suvarna123");
     //   Connection productionConnection = DriverManager.getConnection("jdbc:mysql://10.150.10.9:3306/CNS","suvarnaj_prod","Suvja@23");
        Statement statement = uatConnection.createStatement();
        ResultSet resultSet = statement.executeQuery(sqlStr);
        List<Map<String,Object>> rows = new ArrayList<Map<String,Object>>();
        while (resultSet.next()) {
            Map<String,Object> map = new HashMap<String,Object>();
            for (int i = 0; i < resultSet.getMetaData().getColumnCount(); i++) {
                map.put(resultSet.getMetaData().getColumnName(i+1),resultSet.getObject(i+1));
            }
            rows.add(map);
        }
        return rows;
    }
}
