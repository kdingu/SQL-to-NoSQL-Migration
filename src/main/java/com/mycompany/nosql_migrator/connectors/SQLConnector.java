/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.nosql_migrator.connectors;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author User
 */
public class SQLConnector {

    public Connection connect(){
        
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://127.0.0.1:1434;"
                                            + "databaseName=NORTHWINDNETMDF;"
                                            + "integratedSecurity=true"); //change string as needed to connect to your database
            System.out.println("Connected to SQL");
            return conn;
        } catch (SQLException e) {
            System.out.println("Error connecting to SQL: \n"+e);
            System.exit(0);
        }
        return null;
        
    }
    
    public void disconnectAll(ResultSet rs, Statement stmt, Connection conn) throws SQLException{
         
        rs.close();
        stmt.close();
        conn.close();
        
    }
    
    public void diconnectConn(Connection conn) throws SQLException{
        
        conn.close();
        
    }
    
    public void disconnectSome(ResultSet rs, Statement stmt) throws SQLException{
        
        rs.close();
        stmt.close();
        
    }

}
