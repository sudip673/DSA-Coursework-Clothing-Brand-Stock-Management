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
    // ==========================================
    //       PART D: ALGORITHMS (Bubble Sort & Binary Search)
    // ==========================================


    public void bubbleSort(String criteria, boolean isAscending) {
        if (clothList.isEmpty()) {
            return;
        }

        int n = clothList.size();

        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {

                ItemModel item1 = clothList.get(j);
                ItemModel item2 = clothList.get(j + 1);
                boolean shouldSwap = false;

                // 1. Sort by PRICE (For Sort Button)
                if (criteria.equalsIgnoreCase("price")) {
                    if (isAscending) {
                        shouldSwap = item1.getPrice() > item2.getPrice();
                    } else {
                        shouldSwap = item1.getPrice() < item2.getPrice();
                    }
                } // 2. Sort by NAME (Required for Binary Search)
                else if (criteria.equalsIgnoreCase("name")) {
                    int compare = item1.getName().compareToIgnoreCase(item2.getName());
                    shouldSwap = isAscending ? (compare > 0) : (compare < 0);
                } // 3. Sort by CATEGORY (Required for Binary Search)
                else if (criteria.equalsIgnoreCase("category")) {
                    int compare = item1.getCategory().compareToIgnoreCase(item2.getCategory());
                    shouldSwap = isAscending ? (compare > 0) : (compare < 0);
                }

                // SWAP Logic
                if (shouldSwap) {
                    clothList.set(j, item2);
                    clothList.set(j + 1, item1);
                }
            }
        }
    }

    public java.util.ArrayList<ItemModel> binarySearch(String searchValue, String criteria) {
        java.util.ArrayList<ItemModel> foundItems = new java.util.ArrayList<>();

        int low = 0;
        int high = clothList.size() - 1;

        while (low <= high) {
            int mid = low + (high - low) / 2;
            ItemModel midItem = clothList.get(mid);

            // Determine what we are comparing
            String midValue = criteria.equalsIgnoreCase("name") ? midItem.getName() : midItem.getCategory();

            int compare = midValue.compareToIgnoreCase(searchValue);

            if (compare == 0) {
                // FOUND!
                foundItems.add(midItem);

                // Binary search finds one item. We must check neighbors for duplicates.
                // Check Left
                int left = mid - 1;
                while (left >= 0) {
                    ItemModel lItem = clothList.get(left);
                    String lVal = criteria.equalsIgnoreCase("name") ? lItem.getName() : lItem.getCategory();
                    if (lVal.equalsIgnoreCase(searchValue)) {
                        foundItems.add(lItem);
                        left--;
                    } else {
                        break;
                    }
                }

                // Check Right
                int right = mid + 1;
                while (right < clothList.size()) {
                    ItemModel rItem = clothList.get(right);
                    String rVal = criteria.equalsIgnoreCase("name") ? rItem.getName() : rItem.getCategory();
                    if (rVal.equalsIgnoreCase(searchValue)) {
                        foundItems.add(rItem);
                        right++;
                    } else {
                        break;
                    }
                }
                return foundItems; // Return all matches
            }

            if (compare < 0) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        return foundItems; // Not found
    }

    public java.util.ArrayList<ItemModel> performSearch(String query) {
        java.util.ArrayList<ItemModel> finalResults = new java.util.ArrayList<>();
        java.util.ArrayList<Integer> addedIds = new java.util.ArrayList<>();

        // 1. Search by NAME
        bubbleSort("name", true); // Sort first
        java.util.ArrayList<ItemModel> nameMatches = binarySearch(query, "name");

        for (ItemModel item : nameMatches) {
            finalResults.add(item);
            addedIds.add(item.getItemId());
        }

        // 2. Search by CATEGORY
        bubbleSort("category", true); // Sort first
        java.util.ArrayList<ItemModel> catMatches = binarySearch(query, "category");

        for (ItemModel item : catMatches) {
            // Avoid duplicates
            if (!addedIds.contains(item.getItemId())) {
                finalResults.add(item);
            }
        }

        return finalResults;
    }
}
