package com.centralizedNotificationEngine.summaryNotifications.config;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConnectingToDB {

    public void Execute(String sqlStr) throws SQLException {
        Connection connection = DriverManager.getConnection("jdbc:mysql://10.133.208.200:3306/flt","flt","flt123");
        Statement statement = connection.createStatement();
        statement.execute(sqlStr);
    }

    public List<Map<String, Object>> QueryForList(String sqlStr) throws SQLException {
        Connection connection = DriverManager.getConnection("jdbc:mysql://10.133.208.200:3306/flt","flt","flt123");
        Statement statement = connection.createStatement();
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
