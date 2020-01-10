package com.mycompany.nosql_migrator.migrator;

import com.google.gson.JsonObject;
import com.mycompany.nosql_migrator.connectors.MongoConnector;
import com.mycompany.nosql_migrator.connectors.SQLConnector;
import com.mycompany.nosql_migrator.documents.CustomerDoc;
import com.mycompany.nosql_migrator.documents.EmployeeDoc;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Customer {

    SQLConnector sc = new SQLConnector();
    String query = "select * from Customer";
    MongoConnector mc = new MongoConnector();

    public void migrate() throws SQLException {

        //open connection and get table data
        Connection conn = sc.connect();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(query);

        //populate custList List with table data
        System.out.println("Populating custList with table data...");
        List<CustomerDoc> custList = new ArrayList<>();
        while (rs.next()) {

            CustomerDoc tempEmp = new CustomerDoc();

            tempEmp.setID((int) rs.getObject("ID"));
            tempEmp.setCode((String) rs.getObject("Code"));
            tempEmp.setName((String) rs.getObject("Name"));
            tempEmp.setContactName((String) rs.getObject("ContactName"));
            tempEmp.setContactTitle((String) rs.getObject("ContactTitle"));
            tempEmp.setAddress((String) rs.getObject("Address"));
            tempEmp.setCity((String) rs.getObject("City"));
            tempEmp.setRegion((String) rs.getObject("Region"));
            tempEmp.setPostalCode((String) rs.getObject("PostalCode"));
            tempEmp.setCountry((String) rs.getObject("Country"));
            tempEmp.setPhone((String) rs.getObject("Phone"));
            tempEmp.setFax((String) rs.getObject("Fax"));

            custList.add(tempEmp);

        }
        //close connection will SQL Server
        System.out.println("SQL Disconnected!");
        sc.disconnectAll(rs, stmt, conn);
        
        //convert CustomerDoc object in supplierList to JsonObject and store in a JsonObject List
        System.out.println("Converting Customers to Json and loading into json list...");
        List<JsonObject> allJsonEmps = new ArrayList<>();
        for (CustomerDoc temp : custList) {

            JsonObject tempJson = new JsonObject();

            tempJson.addProperty("ID", temp.getID());
            tempJson.addProperty("Code", temp.getCode());
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

            allJsonEmps.add(tempJson);

        }

        //connect and load onto MongoDB
        mc.connect_and_load("Customers", allJsonEmps);

    }

}
