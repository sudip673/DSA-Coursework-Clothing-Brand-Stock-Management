package controller;

import model.ItemModel;
import java.util.LinkedList;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author sudip
 */
public class ItemControl {

    // LinkedList to store medicines
    private LinkedList<ItemModel> clothList;

    // Constructor
    public ItemControl() {
        clothList = new LinkedList<>();
    }

    // Add medicine to LinkedList
    public void addItem(ItemModel cloth) {
        if (cloth != null) {
            clothList.add(cloth);
        }
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
