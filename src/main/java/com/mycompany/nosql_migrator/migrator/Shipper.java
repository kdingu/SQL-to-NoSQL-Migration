package com.mycompany.nosql_migrator.migrator;

import com.google.gson.JsonObject;
import com.mycompany.nosql_migrator.connectors.MongoConnector;
import com.mycompany.nosql_migrator.documents.ShipperDoc;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Shipper {

    public void migrate() throws SQLException {

        Connection conn = null;
        Statement stmt = null;
        String query = "select * from Shipper";
        ResultSet rs = null;
        MongoConnector mc = new MongoConnector();

        //connect to SQL Server and get table data
        try {
            conn = DriverManager.getConnection("jdbc:sqlserver://127.0.0.1:1434;"
                    + "databaseName=NORTHWINDNETMDF;"
                    + "integratedSecurity=true");
            stmt = conn.createStatement();
            rs = stmt.executeQuery(query);
            System.out.println("Connected to SQL");
        } catch (SQLException e) {
            System.out.println("Error connecting to SQL: \n" + e);
            System.exit(0);
        }

        //HELPERS
        //List to store ShipperDoc populated objects
        List<ShipperDoc> shipperList = new ArrayList<>();
        int listSize = 0;

        //List to store JSON Shippers
        List<JsonObject> allJSONShippers = new ArrayList<>();

        //populate java ShipperDoc object and put into shipperList
        while (rs.next()) {
            ShipperDoc tempShipper = new ShipperDoc();
            tempShipper.setID((int) rs.getObject("ID"));
            tempShipper.setCompanyName((String) rs.getObject("Name"));
            tempShipper.setPhone((String) rs.getObject("Phone"));
            shipperList.add(tempShipper);
            listSize++;
        }

        System.out.println("SQL Data succesfully fetched!");

        //close connection on SQL Server
        try {
            rs.close();
            stmt.close();
            conn.close();
            System.out.println("Closed all SQL connections!");
        } catch (SQLException e) {
            System.out.println("SQL Exception on sql disconnect block: \n" + e);
        }

        //populate JSON object with data from java class
        int i = 0;
        while (i < listSize) {
            JsonObject jsonShipper = new JsonObject();
            ShipperDoc tempShipper = shipperList.get(i);
            jsonShipper.addProperty("ShipperID", tempShipper.getID());
            jsonShipper.addProperty("CompanyName", tempShipper.getCompanyName());
            jsonShipper.addProperty("Phone", tempShipper.getPhone());
            allJSONShippers.add(jsonShipper);
            i++;
        }

        //connect and load onto MongoDB
        mc.connect_and_load("Shippers", allJSONShippers);

    }

}
