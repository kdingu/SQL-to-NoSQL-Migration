package com.mycompany.nosql_migrator.migrator;

import com.google.gson.JsonObject;
import com.mycompany.nosql_migrator.connectors.MongoConnector;
import com.mycompany.nosql_migrator.connectors.SQLConnector;
import com.mycompany.nosql_migrator.documents.EmployeeDoc;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Employee {

    SQLConnector sc = new SQLConnector();
    String query = "select * from Employee";
    MongoConnector mc = new MongoConnector();

    public void migrate() throws SQLException {

        //open connection and get table data
        Connection conn = sc.connect();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(query);

        //populate empList List with table data
        System.out.println("Populating empList with table data...");
        List<EmployeeDoc> empList = new ArrayList<>();
        while (rs.next()) {

            EmployeeDoc tempEmp = new EmployeeDoc();

            tempEmp.setID((int) rs.getObject("ID"));
            tempEmp.setLastName((String) rs.getObject("LastName"));
            tempEmp.setFirstName((String) rs.getObject("FirstName"));
            tempEmp.setTitle((String) rs.getObject("Title"));
            tempEmp.setTitleOfCourtesy((String) rs.getObject("TitleOfCourtesy"));
            tempEmp.setBirthDate((Date) rs.getObject("BirthDate"));
            tempEmp.setHireDate((Date) rs.getObject("HireDate"));
            tempEmp.setAddress((String) rs.getObject("Address"));
            tempEmp.setCity((String) rs.getObject("City"));
            tempEmp.setRegion((String) rs.getObject("Region"));
            tempEmp.setPostalCode((String) rs.getObject("PostalCode"));
            tempEmp.setCountry((String) rs.getObject("Country"));
            tempEmp.setHomePhone((String) rs.getObject("HomePhone"));
            tempEmp.setExtension((String) rs.getObject("Extension"));
            tempEmp.setPhoto((String) rs.getObject("Photo"));
            tempEmp.setNotes((String) rs.getObject("Notes"));
            tempEmp.setReportsTo((Integer) rs.getObject("ReportsTo"));

            empList.add(tempEmp);

        }
        //close connection will SQL Server
        System.out.println("SQL Disconnected!");
        sc.disconnectAll(rs, stmt, conn);

        //convert SupplierDoc object in supplierList to JsonObject and store in a JsonObject List
        System.out.println("Converting Suppliers to Json and loading into json list...");
        List<JsonObject> allJsonEmps = new ArrayList<>();
        for (EmployeeDoc temp : empList) {

            JsonObject tempJson = new JsonObject();

            tempJson.addProperty("ID", temp.getID());
            tempJson.addProperty("LastName", temp.getLastName());
            tempJson.addProperty("FirstName", temp.getFirstName());
            tempJson.addProperty("Title", temp.getTitle());
            tempJson.addProperty("TitleOfCourtesy", temp.getTitleOfCourtesy());
            tempJson.addProperty("BirthDate", String.valueOf(temp.getBirthDate()));
            tempJson.addProperty("HireDate", String.valueOf(temp.getHireDate()));
            tempJson.addProperty("Address", temp.getAddress());
            tempJson.addProperty("City", temp.getCity());
            tempJson.addProperty("Region", temp.getRegion());
            tempJson.addProperty("PostalCode", temp.getPostalCode());
            tempJson.addProperty("Country", temp.getCountry());
            tempJson.addProperty("HomePhone", temp.getHomePhone());
            tempJson.addProperty("Extension", temp.getExtension());
            tempJson.addProperty("Photo", temp.getPhoto());
            tempJson.addProperty("Notes", temp.getNotes());
            tempJson.addProperty("ReportsTo", temp.getReportsTo());

            allJsonEmps.add(tempJson);

        }

        //connect and load onto MongoDB
        mc.connect_and_load("Employees", allJsonEmps);

    }

}
