package com.mycompany.nosql_migrator.documents;

import java.sql.Blob;

public class CategoryDoc {
    
    private int ID;
    private String Name;
    private String Description;
    private Blob Picture;

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

    public Blob getPicture() {
        return Picture;
    }

    public void setPicture(Blob Picture) {
        this.Picture = Picture;
    }
    
}
