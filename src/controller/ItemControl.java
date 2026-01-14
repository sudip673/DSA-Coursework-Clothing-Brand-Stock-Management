package controller;

import model.ItemModel;
import model.SaleModel;
import java.util.LinkedList;

public class ItemControl {

    // 1. INVENTORY (Existing LinkedList)
    private LinkedList<ItemModel> clothList;

    // 2. DATA STRUCTURES (New Manual Implementation)
    private SalesQueue pendingQueue;
    private SalesStack salesHistory;

    // Constructor
    public ItemControl() {
        // Initialize Inventory
        clothList = new LinkedList<>();

        // Initialize Data Structures with fixed capacity (as per array implementation)
        pendingQueue = new SalesQueue(20);
        salesHistory = new SalesStack(100);

        loadInitial();
    }

    public void loadInitial() {
        ItemModel item1 = new ItemModel(1, "Blazing hot red Hoodie", "Hoodie", 25, 2500);
        ItemModel item2 = new ItemModel(2, "Blazing hot black pant", "Pant", 35, 1200);
        ItemModel item3 = new ItemModel(3, "Summer nav Shirt", "Shirt", 20, 9000);
        ItemModel item4 = new ItemModel(4, "Blazing hot red Tee", "Tee", 225, 500);
        ItemModel item5 = new ItemModel(5, "Blazing hot red Cap", "Cap", 215, 600);

        clothList.add(item1);
        clothList.add(item2);
        clothList.add(item3);
        clothList.add(item4);
        clothList.add(item5);
    }

    // ==========================================
    //       PART A: INVENTORY MANAGEMENT
    // ==========================================
    public boolean addItem(ItemModel cloth) {
        if (cloth == null) {
            return false;
        }
        if (findById(cloth.getItemId()) != null) {
            return false; // Duplicate ID check
        }
        clothList.add(cloth);
        return true;
    }

    public ItemModel findById(int id) {
        for (ItemModel item : clothList) {
            if (item.getItemId() == id) {
                return item;
            }
        }
        return null;
    }

    public void removeById(int id) {
        ItemModel item = findById(id);
        if (item != null) {
            clothList.remove(item);
        }
    }

    public LinkedList<ItemModel> getClothList() {
        return clothList;
    }

    // ==========================================
    //       PART B: SALES LOGIC (NEW)
    // ==========================================
    /**
     * 1. Add to Queue (FIFO) Adds a drafted sale to the waiting line.
     */
    public void addSaleToQueue(SaleModel sale) {
        if (!pendingQueue.isFull()) {
            pendingQueue.enQueue(sale);
        } else {
            System.out.println("Queue is full!");
        }
    }

    /**
     * 2. Confirm Sale (FIFO Dequeue -> Stock Update -> LIFO Stack Push)
     * Processes the sale at the front of the line.
     */
    public SaleModel confirmNextSale() {
        if (pendingQueue.isEmpty()) {
            return null;
        }

        // A. Remove from Queue
        SaleModel sale = pendingQueue.deQueue();

        // B. UPDATE STOCK (Decrease by Sold Quantity)
        for (ItemModel soldItem : sale.getCartItems()) {
            // Find the real item in inventory using ID
            ItemModel inventoryItem = findById(soldItem.getItemId());

            if (inventoryItem != null) {
                int currentStock = inventoryItem.getQuantity();
                int boughtQty = soldItem.getQuantity(); // This is the amount user bought

                // Math: New Stock = Old Stock - Bought Amount
                if (currentStock >= boughtQty) {
                    inventoryItem.setQuantity(currentStock - boughtQty);
                }
            }
        }

        // C. Push to History Stack
        salesHistory.push(sale);
        return sale;
    }

    /**
     * 3. Undo Sale (LIFO Pop -> Stock Restore) Reverses the last completed
     * sale.
     */
    public SaleModel undoLastSale() {
        if (salesHistory.isEmpty()) {
            return null;
        }

        // A. Pop from Stack
        SaleModel sale = salesHistory.pop(); // Slide 21

        // B. RESTORE STOCK (Increase by Sold Quantity)
        for (ItemModel soldItem : sale.getCartItems()) {
            ItemModel inventoryItem = findById(soldItem.getItemId());

            if (inventoryItem != null) {
                int currentStock = inventoryItem.getQuantity();
                int boughtQty = soldItem.getQuantity();

                // Math: New Stock = Old Stock + Bought Amount
                inventoryItem.setQuantity(currentStock + boughtQty);
            }
        }
        return sale;
    }

    public SaleModel cancelNextSale() {
        if (pendingQueue.isEmpty()) {
            return null;
        }
        return pendingQueue.deQueue();
    }

    // ==========================================
    //       PART C: GETTERS FOR GUI
    // ==========================================
    public SalesQueue getPendingQueue() {
        return pendingQueue;
    }

    public SalesStack getSalesHistory() {
        return salesHistory;
    }
}
