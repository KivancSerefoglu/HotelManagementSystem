package edu.ozyegin.cs.entity;

import java.util.Objects;

public class inventory {

    private int room_id;
    private int amenities_id;
    private int quantity;


    public inventory(){}

    public int getRoom_id() {
        return room_id;
    }

    public void setRoom_id(int room_id) {
        this.room_id = room_id;
    }

    public inventory room_id(int room_id) {
        this.room_id = room_id;
        return this;
    }

    public int getAmenities_id() {
        return amenities_id;
    }

    public void setAmenities_id(int amenities_id) {
        this.amenities_id = amenities_id;
    }

    public inventory amenities_id(int amenities_id) {
        this.amenities_id = amenities_id;
        return this;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public inventory quantity(int quantity) {
        this.quantity = quantity;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        inventory inventory = (inventory) o;
        return room_id == inventory.room_id && amenities_id == inventory.amenities_id && quantity == inventory.quantity;
    }

    @Override
    public int hashCode() {
        return Objects.hash(room_id,amenities_id, quantity);
    }
}
