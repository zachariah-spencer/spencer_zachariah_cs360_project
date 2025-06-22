package com.example.spencer_zachariah_cs360_project.db;

/**
 * Simple model class representing an inventory item.
 */
public class Item {
    public int id;
    public String name;
    public int quantity;

    public Item(int id, String name, int quantity) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
    }
}
