package com.mycompany.nosql_migrator.documents;

import java.math.BigDecimal;

public class ProductDoc {
    
    public ProductDoc () {
        Category = new EmbeddedCategory();
    }

    private int ID;

    private String Name;
    private String QuantityPerUnit;
    private BigDecimal UnitPrice;
    private short UnitsInStock;
    private short UnitsOnOrder;
    private short ReorderLevel;
    private boolean Discontinued;

    private int SupplierID;
    private EmbeddedCategory Category;

    public class EmbeddedCategory {

        public EmbeddedCategory() {
        }

        public EmbeddedCategory(CategoryDoc category) {
            ID = category.getID();
            Name = category.getName();
            Description = category.getDescription();
        }

        private int ID;
        private String Name;
        private String Description;

        public int getID() {
            return ID;
        }

        public void setID(int ID) {
            this.ID = ID;
        }

        public String getName() {
            return Name;
        }

        public void setName(String Name) {
            this.Name = Name;
        }

        public String getDescription() {
            return Description;
        }

        public void setDescription(String Description) {
            this.Description = Description;
        }

    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getQuantityPerUnit() {
        return QuantityPerUnit;
    }

    public void setQuantityPerUnit(String QuantityPerUnit) {
        this.QuantityPerUnit = QuantityPerUnit;
    }

    public BigDecimal getUnitPrice() {
        return UnitPrice;
    }

    public void setUnitPrice(BigDecimal UnitPrice) {
        this.UnitPrice = UnitPrice;
    }

    public short getUnitsInStock() {
        return UnitsInStock;
    }

    public void setUnitsInStock(short UnitsInStock) {
        this.UnitsInStock = UnitsInStock;
    }

    public short getUnitsOnOrder() {
        return UnitsOnOrder;
    }

    public void setUnitsOnOrder(short UnitsOnOrder) {
        this.UnitsOnOrder = UnitsOnOrder;
    }

    public short getReorderLevel() {
        return ReorderLevel;
    }

    public void setReorderLevel(short ReorderLevel) {
        this.ReorderLevel = ReorderLevel;
    }

    public boolean isDiscontinued() {
        return Discontinued;
    }

    public void setDiscontinued(boolean Discontinued) {
        this.Discontinued = Discontinued;
    }

    public int getSupplierID() {
        return SupplierID;
    }

    public void setSupplierID(int SupplierID) {
        this.SupplierID = SupplierID;
    }

    public EmbeddedCategory getCategory() {
        return Category;
    }

    public void setCategory(EmbeddedCategory Category) {
        this.Category = Category;
    }

}
