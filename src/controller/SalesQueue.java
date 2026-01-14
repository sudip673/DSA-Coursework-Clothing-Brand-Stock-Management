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

public class SalesQueue {
    private int SIZE = 50;
    private SaleModel[] items;
    private int front, rear;

    public SalesQueue(int size) {
        this.SIZE = size;
        items = new SaleModel[SIZE];
        front = -1;
        rear = -1;
    }

    public boolean isFull() { return front == 0 && rear == SIZE - 1; }
    public boolean isEmpty() { return front == -1; }

    public void enQueue(SaleModel element) {
        if (isFull()) {
            System.out.println("Queue Full");
        } else {
            if (front == -1) front = 0;
            rear++;
            items[rear] = element;
            System.out.println("Queued: " + element.getCustomerName());
        }
    }

    public SaleModel deQueue() {
        if (isEmpty()) return null;
        SaleModel element = items[front];
        if (front >= rear) {
            front = -1; rear = -1; // Reset
        } else {
            front++;
        }
        return element;
    }
    
    // Helper for Table Display
    public SaleModel[] getItems() {
        if (isEmpty()) return new SaleModel[0];
        int size = rear - front + 1;
        SaleModel[] current = new SaleModel[size];
        for (int i = 0; i < size; i++) current[i] = items[front + i];
        return current;
    }
    
    public SaleModel peek() { return isEmpty() ? null : items[front]; }
}