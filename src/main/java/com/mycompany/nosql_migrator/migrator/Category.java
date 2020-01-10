package com.mycompany.nosql_migrator.migrator;

import com.google.gson.JsonObject;
import com.mycompany.nosql_migrator.connectors.MongoConnector;
import com.mycompany.nosql_migrator.documents.CategoryDoc;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Category {

    Connection conn;
    Statement stmt;
    ResultSet rs;
    String query = "select * from Category";
    MongoConnector mc = new MongoConnector();

    public void migrate() throws SQLException {

        conn = DriverManager.getConnection("jdbc:sqlserver://127.0.0.1:1434;"
                + "databaseName=NORTHWINDNETMDF;"
                + "integratedSecurity=true");
        stmt = conn.createStatement();
        rs = stmt.executeQuery(query);
        System.out.println("Connected to SQL!");

        //put all records from Category table into ctgList ArrayList
        List<CategoryDoc> ctgList = new ArrayList<>();
        System.out.println("Loading Category table into ctgList...");
        while (rs.next()) {

            CategoryDoc tempCtgDoc = new CategoryDoc();

            tempCtgDoc.setID((int) rs.getObject("ID"));
            tempCtgDoc.setName((String) rs.getObject("Name"));
            tempCtgDoc.setDescription((String) rs.getObject("Description"));
            tempCtgDoc.setPicture(rs.getBlob("Picture"));

            ctgList.add(tempCtgDoc);

        }

        System.out.println("Close SQL connection!");
        //close conn, stmt and rs
        rs.close();
        stmt.close();
        conn.close();

        System.out.println("Converting ctgList objects into JSON and storing into jsonCtg ArrayList...");
        //convert all objects of ctgList into JsonObject and store into jsonCtg ArrayList
        List<JsonObject> allJsonCtg = new ArrayList<>();
        Iterator itr = ctgList.iterator();
        for (CategoryDoc temp : ctgList) {

            JsonObject tempJson = new JsonObject();

            tempJson.addProperty("ID", temp.getID());
            tempJson.addProperty("Name", temp.getName());
            tempJson.addProperty("Description", temp.getDescription());
            byte[] b = temp.getPicture().getBytes(1, (int) temp.getPicture().length());//needs fix
            String blobtext = new String(b);
            tempJson.addProperty("Picture", blobtext);
            allJsonCtg.add(tempJson);

        }

        //connect and load onto MongoDB
        mc.connect_and_load("Category", allJsonCtg);

    }

}
