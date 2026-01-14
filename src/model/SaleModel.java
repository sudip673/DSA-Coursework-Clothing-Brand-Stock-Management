/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;
import java.util.ArrayList;
/**
 *
 * @author Tejas Shahi
 */
public class SaleModel {
    private String customerName;
    private String phone;
    private ArrayList<ItemModel> cartItems; // Items with the quantity user BOUGHT
    private double totalAmount;

    public SaleModel(String customerName, String phone, ArrayList<ItemModel> cartItems) {
        this.customerName = customerName;
        this.phone = phone;
        this.cartItems = new ArrayList<>(cartItems); // Create a safe copy
        this.totalAmount = calculateTotal();
    }

    private double calculateTotal() {
        double sum = 0;
        for (ItemModel item : cartItems) {
            // Price * Quantity Bought
            sum += (item.getPrice() * item.getQuantity());
        }
        return sum;
    }

    // Getters
    public String getCustomerName() { return customerName; }
    public String getPhone() { return phone; }
    public ArrayList<ItemModel> getCartItems() { return cartItems; }
    public double getTotalAmount() { return totalAmount; }
}
