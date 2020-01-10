/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.nosql_migrator.migrator;

import com.google.gson.JsonObject;
import com.mycompany.nosql_migrator.connectors.MongoConnector;
import com.mycompany.nosql_migrator.connectors.SQLConnector;
import com.mycompany.nosql_migrator.documents.SupplierDoc;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author User
 */
public class Supplier {

    SQLConnector sc = new SQLConnector();
    String query = "select * from Supplier";
    MongoConnector mc = new MongoConnector();

    public void migrate() throws SQLException {

        //open connection and get table data
        Connection conn = sc.connect();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(query);

        //populate supplierList List with table data
        System.out.println("Populating supplierList with table data...");
        List<SupplierDoc> supplierList = new ArrayList<>();
        while (rs.next()) {

            SupplierDoc tempSupplier = new SupplierDoc();

            tempSupplier.setID((int) rs.getObject("ID"));
            tempSupplier.setName((String) rs.getObject("Name"));
            tempSupplier.setContactName((String) rs.getObject("ContactName"));
            tempSupplier.setContactTitle((String) rs.getObject("ContactTitle"));
            tempSupplier.setAddress((String) rs.getObject("Address"));
            tempSupplier.setCity((String) rs.getObject("City"));
            tempSupplier.setRegion((String) rs.getObject("Region"));
            tempSupplier.setPostalCode((String) rs.getObject("PostalCode"));
            tempSupplier.setCountry((String) rs.getObject("Country"));
            tempSupplier.setPhone((String) rs.getObject("Phone"));
            tempSupplier.setFax((String) rs.getObject("Fax"));
            tempSupplier.setHomePage((String) rs.getObject("HomePage"));

            supplierList.add(tempSupplier);

        }
        //close connection will SQL Server
        System.out.println("SQL Disconnected!");
        sc.disconnectAll(rs, stmt, conn);

        //convert SupplierDoc object in supplierList to JsonObject and store in a JsonObject List
        System.out.println("Converting Suppliers to Json and loading into json list...");
        List<JsonObject> allJsonSuppliers = new ArrayList<>();
        for (SupplierDoc temp : supplierList) {

            JsonObject tempJson = new JsonObject();

            tempJson.addProperty("ID", temp.getID());
            tempJson.addProperty("Name", temp.getName());
            tempJson.addProperty("ContactName", temp.getContactName());
            tempJson.addProperty("ContactTitle", temp.getContactTitle());
            tempJson.addProperty("Address", temp.getAddress());
            tempJson.addProperty("City", temp.getCity());
            tempJson.addProperty("Region", temp.getRegion());
            tempJson.addProperty("PostalCode", temp.getPostalCode());
            tempJson.addProperty("Country", temp.getCountry());
            tempJson.addProperty("Phone", temp.getPhone());
            tempJson.addProperty("Fax", temp.getFax());
            tempJson.addProperty("HomePage", temp.getHomePage());

            allJsonSuppliers.add(tempJson);

        }

        //connect and load onto MongoDB 
        mc.connect_and_load("Employees", allJsonSuppliers);

    }

}
