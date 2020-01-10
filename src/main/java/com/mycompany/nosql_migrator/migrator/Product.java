package com.mycompany.nosql_migrator.migrator;

import com.google.gson.JsonObject;
import com.mycompany.nosql_migrator.connectors.MongoConnector;
import com.mycompany.nosql_migrator.connectors.SQLConnector;
import com.mycompany.nosql_migrator.documents.ProductDoc;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Product {

    SQLConnector sc = new SQLConnector();
    String query = "select * from Product";
    String query2 = "select ID, [Name], [Description] from Category where ID = ? ";
    MongoConnector mc = new MongoConnector();
    ResultSet rs2 = null;
    ResultSet rs = null;

    public void migrate() throws SQLException {

        //open connection and get table data
        Connection conn = sc.connect();
        Statement stmt = conn.createStatement();
        rs = stmt.executeQuery(query);

        //populate prodList List with table data
        System.out.println("Populating prodList with table data...");
        List<ProductDoc> prodList = new ArrayList<>();
        while (rs.next()) {

            ProductDoc tempProd = new ProductDoc();

            tempProd.setID((int) rs.getObject("ID"));
            tempProd.setName((String) rs.getObject("Name"));
            tempProd.setSupplierID((int) rs.getObject("SupplierID"));
            tempProd.setQuantityPerUnit((String) rs.getObject("QuantityPerUnit"));
            tempProd.setUnitPrice((BigDecimal) rs.getObject("UnitPrice"));
            tempProd.setUnitsInStock((short) rs.getObject("UnitsInStock"));
            tempProd.setUnitsOnOrder((short) rs.getObject("UnitsOnOrder"));
            tempProd.setReorderLevel((short) rs.getObject("ReorderLevel"));
            tempProd.setDiscontinued((boolean) rs.getObject("Discontinued"));

            //set embedded category
            int embeddedID = (int) rs.getObject("CategoryID");
            PreparedStatement ps = conn.prepareStatement("select ID, [Name], [Description] from Category where ID = ?");
            try {
                ps.setInt(1, embeddedID);
                rs2 = ps.executeQuery();
                while (rs2.next()) {
                    tempProd.getCategory().setID((int) rs2.getObject("ID"));
                    tempProd.getCategory().setName((String) rs2.getObject("Name"));
                    tempProd.getCategory().setDescription((String) rs2.getObject("Description"));
                }

            } catch (SQLException e) {
                System.out.println(e);
            } finally {
                rs2.close();
                ps.close();
            }

            prodList.add(tempProd);

        }
        //close connection will SQL Server
        System.out.println("SQL Disconnected!");
        sc.disconnectAll(rs, stmt, conn);
        
        //convert SupplierDoc object in supplierList to JsonObject and store in a JsonObject List
        System.out.println("Converting Suppliers to Json and loading into json list...");
        List<JsonObject> allJsonPrds = new ArrayList<>();
        for (ProductDoc temp : prodList) {

            JsonObject tempJson = new JsonObject();

            tempJson.addProperty("ID", temp.getID());
            tempJson.addProperty("Name", temp.getName());
            tempJson.addProperty("SupplierID", temp.getSupplierID());
            tempJson.addProperty("QuantityPerUnit", temp.getQuantityPerUnit());
            tempJson.addProperty("UnitPrice", temp.getUnitPrice());
            tempJson.addProperty("UnitsInStock", temp.getUnitsInStock());
            tempJson.addProperty("UnitsOnOrder", temp.getUnitsOnOrder());
            tempJson.addProperty("ReorderLevel", temp.getReorderLevel());
            tempJson.addProperty("Discontinued", temp.isDiscontinued());
            
            JsonObject tempEmbJson = new JsonObject();
            tempEmbJson.addProperty("CategoryID", temp.getCategory().getID());
            tempEmbJson.addProperty("CategoryName", temp.getCategory().getName());
            tempEmbJson.addProperty("CategoryDescription", temp.getCategory().getDescription());
            
            tempJson.add("Category", tempEmbJson);
            

            allJsonPrds.add(tempJson);

        }
        
        //connect and load onto MongoDB
        mc.connect_and_load("Products", allJsonPrds);

    }

}
