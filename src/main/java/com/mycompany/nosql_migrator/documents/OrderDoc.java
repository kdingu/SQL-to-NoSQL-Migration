package com.mycompany.nosql_migrator.documents;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OrderDoc {
    
    public OrderDoc(){
        orderdetail = new ArrayList<>();
    }
    
    private Integer ID;

    private Date OrderDate;
    private Date RequiredDate;
    private Date ShippedDate;
    private BigDecimal Freight;
    private String ShipName;
    private String ShipAddress;
    private String ShipCity;
    private String ShipRegion;
    private String ShipPostalCode;
    private String Shipcountry;

    private Integer CustomerID;
    private Integer EmployeeID;
    private Integer ShipperID;

    private final List<OrderdetailDoc> orderdetail;

    public Integer getID() {
        return ID;
    }

    public void setID(Integer ID) {
        this.ID = ID;
    }

    public Date getOrderDate() {
        return OrderDate;
    }

    public void setOrderDate(Date OrderDate) {
        this.OrderDate = OrderDate;
    }

    public Date getRequiredDate() {
        return RequiredDate;
    }

    public void setRequiredDate(Date RequiredDate) {
        this.RequiredDate = RequiredDate;
    }

    public Date getShippedDate() {
        return ShippedDate;
    }

    public void setShippedDate(Date ShippedDate) {
        this.ShippedDate = ShippedDate;
    }

    public BigDecimal getFreight() {
        return Freight;
    }

    public void setFreight(BigDecimal Freight) {
        this.Freight = Freight;
    }

    public String getShipName() {
        return ShipName;
    }

    public void setShipName(String ShipName) {
        this.ShipName = ShipName;
    }

    public String getShipAddress() {
        return ShipAddress;
    }

    public void setShipAddress(String ShipAddress) {
        this.ShipAddress = ShipAddress;
    }

    public String getShipCity() {
        return ShipCity;
    }

    public void setShipCity(String ShipCity) {
        this.ShipCity = ShipCity;
    }

    public String getShipRegion() {
        return ShipRegion;
    }

    public void setShipRegion(String ShipRegion) {
        this.ShipRegion = ShipRegion;
    }

    public String getShipPostalCode() {
        return ShipPostalCode;
    }

    public void setShipPostalCode(String ShipPostalCode) {
        this.ShipPostalCode = ShipPostalCode;
    }

    public String getShipcountry() {
        return Shipcountry;
    }

    public void setShipcountry(String Shipcountry) {
        this.Shipcountry = Shipcountry;
    }

    public Integer getCustomerID() {
        return CustomerID;
    }

    public void setCustomerID(Integer CustomerID) {
        this.CustomerID = CustomerID;
    }

    public Integer getEmployeeID() {
        return EmployeeID;
    }

    public void setEmployeeID(Integer EmployeeID) {
        this.EmployeeID = EmployeeID;
    }

    public Integer getShipperID() {
        return ShipperID;
    }

    public void setShipperID(Integer ShipperID) {
        this.ShipperID = ShipperID;
    }
    
    public void addToOrderdetailsList(OrderdetailDoc inDoc){
        this.orderdetail.add(inDoc);
    }
    
//    public OrderdetailDoc getodDoc(){
//        for (OrderdetailDoc a: orderdetail) {
//            return a;
//        }
//        return null;
//    }
    
    public void getodDoc(List<OrderdetailDoc> a){
        
        for(OrderdetailDoc temp : orderdetail){
            a.add(temp);
        }
        
    }
    
    public List<OrderdetailDoc> getodDoc(){
        
        return this.orderdetail;
        
    }
}
