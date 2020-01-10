package com.mycompany.nosql_migrator.documents;

import java.math.BigDecimal;

public class OrderdetailDoc {
    
    private Integer ProductID;
    private BigDecimal UnitPrice;
    private short Quantity;
    private Float Discount;

    public Integer getProductID() {
        return ProductID;
    }

    public void setProductID(Integer ProductID) {
        this.ProductID = ProductID;
    }

    public BigDecimal getUnitPrice() {
        return UnitPrice;
    }

    public void setUnitPrice(BigDecimal UnitPrice) {
        this.UnitPrice = UnitPrice;
    }

    public short getQuantity() {
        return Quantity;
    }

    public void setQuantity(short Quantity) {
        this.Quantity = Quantity;
    }

    public Float getDiscount() {
        return Discount;
    }

    public void setDiscount(Float Discount) {
        this.Discount = Discount;
    }
    
    
    
}
