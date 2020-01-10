package com.mycompany.nosql_migrator.migrator;

import com.google.gson.JsonObject;
import com.mycompany.nosql_migrator.connectors.MongoConnector;
import com.mycompany.nosql_migrator.connectors.SQLConnector;
import com.mycompany.nosql_migrator.documents.OrderDoc;
import com.mycompany.nosql_migrator.documents.OrderdetailDoc;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class Order {

    SQLConnector sqlconn = new SQLConnector();
    ResultSet rs = null;
    ResultSet innerRS = null;
    String query = "select o.ID,"
            + " o.OrderDate,"
            + " o.RequiredDate,"
            + " o.ShippedDate,"
            + " o.Freight,"
            + " o.ShipName,"
            + " o.ShipAddress,"
            + " o.ShipCity,"
            + " o.ShipRegion,"
            + " o.ShipPostalCode,"
            + " o.ShipCountry,"
            + " o.CustomerId,"
            + " o.EmployeeID,"
            + " o.ShipperId"
            + " from [Order] o";
    //used to embed the correct Orderdetail into Order
    int orderid;
    MongoConnector mc = new MongoConnector();

    public void migrate() throws SQLException {

        //connect to SQL Server and get all Orders into ResulSet
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://127.0.0.1:1434;"
                + "databaseName=NORTHWINDNETMDF;"
                + "integratedSecurity=true");
        Statement stmt = conn.createStatement();
        try {
            rs = stmt.executeQuery(query);
            System.out.println("Connected to SQL!");
        } catch (SQLException e) {
            System.out.println("Error connecting to SQL: \n" + e);
            System.exit(0);
        }

        //populate list
        List<OrderDoc> orderList = new ArrayList<>();
        int listSize = 0;
        System.out.println("Populating orderList array...");
        while (rs.next()) {

            //create OrderDoc with all values of RS record
            OrderDoc orderDoc = new OrderDoc();

            orderDoc.setID((Integer) rs.getObject("ID"));
            orderDoc.setOrderDate((Date) rs.getObject("OrderDate"));
            orderDoc.setRequiredDate((Date) rs.getObject("RequiredDate"));
            orderDoc.setShippedDate((Date) rs.getObject("ShippedDate"));
            orderDoc.setFreight((BigDecimal) rs.getObject("Freight"));
            orderDoc.setShipName((String) rs.getObject("ShipName"));
            orderDoc.setShipAddress((String) rs.getObject("ShipAddress"));
            orderDoc.setShipCity((String) rs.getObject("ShipCity"));
            orderDoc.setShipRegion((String) rs.getObject("ShipRegion"));
            orderDoc.setShipPostalCode((String) rs.getObject("ShipPostalCode"));
            orderDoc.setShipcountry((String) rs.getObject("ShipCountry"));
            orderDoc.setCustomerID((Integer) rs.getObject("CustomerID"));
            orderDoc.setEmployeeID((Integer) rs.getObject("EmployeeID"));
            orderDoc.setShipperID((Integer) rs.getObject("ShipperID"));

            //Starting INNER EXTRACTION for embedded OrderDetails
            //get Orderdetails values that correspond to the above RS order record
            orderid = orderDoc.getID();
            PreparedStatement stmt2 = conn.prepareStatement("select OrderID,"
                    + " ProductID,"
                    + " UnitPrice,"
                    + " Quantity,"
                    + " Discount"
                    + " from OrderDetail"
                    + " where OrderID = ?");
            try {
                stmt2.setInt(1, orderid);
                innerRS = stmt2.executeQuery();
//                System.out.println("innerRS");
                while (innerRS.next()) {
                    OrderdetailDoc odDoc = new OrderdetailDoc();
                    odDoc.setProductID((Integer) innerRS.getObject("ProductID"));
                    odDoc.setUnitPrice((BigDecimal) innerRS.getObject("UnitPrice"));
                    odDoc.setQuantity((short) innerRS.getObject("Quantity"));
                    odDoc.setDiscount((Float) innerRS.getObject("Discount"));
                    orderDoc.addToOrderdetailsList(odDoc);
                }
            } catch (SQLException e) {
                System.out.println(e);
                System.exit(0);
            } finally {
                innerRS.close();
                stmt2.close();
//                System.out.println("Closed innerRS");
            }
            orderList.add(orderDoc);
            listSize++;
        }

        //disconnect and close rs
        try {
            rs.close();
            stmt.close();
            conn.close();
            System.out.println("Disconnected from SQL Server!");
        } catch (SQLException e) {
            System.out.println(e);
        }

        //convert to JSON
        List<JsonObject> allJSONOrders = new ArrayList<>();
        int i = 0;
        System.out.println("Converting orderList object to JsonObjects and populating allJSONOrders array...");
        while (i < listSize) {
            JsonObject jsonOrder = new JsonObject();
            OrderDoc tempOrder = orderList.get(i);
            jsonOrder.addProperty("ID", tempOrder.getID());
            jsonOrder.addProperty("OrderDate", String.valueOf(tempOrder.getOrderDate()));
            jsonOrder.addProperty("RequiredDate", String.valueOf(tempOrder.getRequiredDate()));
            jsonOrder.addProperty("ShippedDate", String.valueOf(tempOrder.getShippedDate()));
            jsonOrder.addProperty("Freight", tempOrder.getFreight());
            jsonOrder.addProperty("ShipName", tempOrder.getShipName());
            jsonOrder.addProperty("ShipAddress", tempOrder.getShipAddress());
            jsonOrder.addProperty("ShipCity", tempOrder.getShipCity());
            jsonOrder.addProperty("ShipRegion", tempOrder.getShipRegion());
            jsonOrder.addProperty("ShipPostalCode", tempOrder.getShipPostalCode());
            jsonOrder.addProperty("Shipcountry", tempOrder.getShipcountry());
            jsonOrder.addProperty("CustomerID", tempOrder.getCustomerID());
            jsonOrder.addProperty("EmployeeID", tempOrder.getEmployeeID());
            jsonOrder.addProperty("ShipperID", tempOrder.getShipperID());

            //Embedded JSON
            List<OrderdetailDoc> returnedODDocs = new ArrayList<>();
            tempOrder.getodDoc(returnedODDocs);
            Iterator<OrderdetailDoc> itr = returnedODDocs.iterator();
            int ii = 1;
            JsonObject mainInnerJsonOrder = new JsonObject();
            while (itr.hasNext()) {
                OrderdetailDoc odDoc = itr.next();
                JsonObject innerJsonOrder = new JsonObject();
                innerJsonOrder.addProperty("ProductID", odDoc.getProductID());
                innerJsonOrder.addProperty("UnitPrice", odDoc.getUnitPrice());
                innerJsonOrder.addProperty("Quantity", odDoc.getQuantity());
                innerJsonOrder.addProperty("Discount", odDoc.getDiscount());
                mainInnerJsonOrder.add("OrderDetail" + ii, innerJsonOrder);
                ii++;
            }
            jsonOrder.add("Order Detail", mainInnerJsonOrder);
            allJSONOrders.add(jsonOrder);
            //increment list size counter
            i++;
        }

        //load JSON to MongoDB
        System.out.println("Connecting to MongoDB and loading JsonObjects...");

        //connect and load onto MongoDB
        mc.connect_and_load("Orders", allJSONOrders);
    }

}
