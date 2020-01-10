/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.nosql_migrator;

import com.mycompany.nosql_migrator.migrator.Product;
import com.mycompany.nosql_migrator.migrator.Employee;
import com.mycompany.nosql_migrator.migrator.Customer;
import com.mycompany.nosql_migrator.migrator.Category;
import com.mycompany.nosql_migrator.migrator.Shipper;
import com.mycompany.nosql_migrator.migrator.Supplier;
import com.mycompany.nosql_migrator.migrator.Order;
import java.sql.SQLException;

/**
 *
 * @author User
 */
public class Main {
    
    static Order order = new Order();
    static Shipper shipper = new Shipper();
    static Category category = new Category();
    static Supplier supplier = new Supplier();
    static Employee employee = new Employee();
    static Customer customer = new Customer();
    static Product product = new Product();
    
    public static void main(String args[]) throws SQLException{
        
        System.out.println("MIGRATING ORDER TABLE...");
        order.migrate();
        System.out.println("MIGRATING SHIPPER TABLE...");
        shipper.migrate();
        System.out.println("MIGRATING CATEGORY TABLE...");
        category.migrate();
        System.out.println("MIGRATING SUPPLIER TABLE...");
        supplier.migrate();
        System.out.println("MIGRATING EMPLOYEE TABLE...");
        employee.migrate();
        System.out.println("MIGRATING CUSTOMER TABLE...");
        customer.migrate();
        System.out.println("MIGRATING PRODUCT TABLE...");
        product.migrate();
        
    }
    
}
