/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

/**
 *
 * @author Tejas Shahi
 */
import model.SaleModel;

public class SalesStack {
    private SaleModel[] arr;
    private int top;
    private int capacity;

    public SalesStack(int size) {
        capacity = size;
        arr = new SaleModel[capacity];
        top = -1;
    }

    public void push(SaleModel x) {
        if (top == capacity - 1) return; // Full
        arr[++top] = x;
    }

    public SaleModel pop() {
        if (top == -1) return null; // Empty
        return arr[top--];
    }

    public boolean isEmpty() { return top == -1; }
    
    // Helper for Table Display
    public SaleModel[] getItems() {
        SaleModel[] current = new SaleModel[top + 1];
        for (int i = 0; i <= top; i++) current[i] = arr[i];
        return current;
    }
}