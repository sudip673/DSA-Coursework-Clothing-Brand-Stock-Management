package controller;

import model.ItemModel;
import java.util.LinkedList;

public class ItemControl {

    // LinkedList to store Cloths
    private LinkedList<ItemModel> clothList;

    // Constructor
    public ItemControl() {
        clothList = new LinkedList<>();
        loadInitial();
    }

    public void loadInitial(){
        ItemModel item1 = new ItemModel(1, "Blazing hot red Hoodie","Hoodie", 25, 2500);
        ItemModel item2 = new ItemModel(2, "Blazing hot black pant","Pant", 35, 1200);
        ItemModel item3 = new ItemModel(3, "Summer nav Shirt","Shirt", 20, 9000);
        ItemModel item4 = new ItemModel(4, "Blazing hot red Tee","Tee", 225, 500);
        ItemModel item5 = new ItemModel(5, "Blazing hot red Cap","Cap", 215, 600);
        
        //add to table
        clothList.add(item1);
        clothList.add(item2);
        clothList.add(item3);
        clothList.add(item4);
        clothList.add(item5);
    }
    
    // UPDATED: Add Cloth to LinkedList with Validation
    // Returns true if added successfully, false if ID exists
    public boolean addItem(ItemModel cloth) {
        if (cloth == null) {
            return false;
        }

        // 1. Check if the ID already exists using your helper method
        ItemModel existingItem = findById(cloth.getItemId());

        // 2. If existingItem is NOT null, that means the ID is taken
        if (existingItem != null) {
            // ID Collision detected! Do not add.
            return false; 
        }

        // 3. If we get here, the ID is unique. Add it.
        clothList.add(cloth);
        return true;
    }

    // Find item by ID
    public ItemModel findById(int id) {
        for (ItemModel item : clothList) {
            if (item.getItemId() == id) {
                return item;
            }
        }
        return null;
    }

    // Remove item by ID
    public void removeById(int id) {
        ItemModel item = findById(id);
        if (item != null) {
            clothList.remove(item);
        }
    }

    public LinkedList<ItemModel> getClothList() {
        return clothList;
    }
}