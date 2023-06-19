package edu.ozyegin.cs.entity;

import java.util.Objects;

public class guest {
    private int guest_id;
    private String guest_name;
    private String guest_phone;


    public guest(){}

    public int getGuest_id() {
        return guest_id;
    }

    public void setGuest_id(int guest_id) {
        this.guest_id = guest_id;
    }

    public guest guest_id(int guest_id) {
        this.guest_id = guest_id;
        return this;
    }

    public String getGuest_name() {
        return guest_name;
    }

    public void setGuest_name(String guest_name) {
        this.guest_name = guest_name;
    }

    public guest guest_name(String guest_name) {
        this.guest_name = guest_name;
        return this;
    }

    public String getGuest_phone() {
        return guest_phone;
    }

    public void setGuest_phone(String guest_phone) {
        this.guest_phone = guest_phone;
    }

    public guest guest_phone(String guest_phone) {
        this.guest_phone = guest_phone;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        guest guest = (guest) o;
        return guest_name.equals(guest.guest_name) && guest_phone.equals(guest.guest_phone);
    }

    @Override
    public int hashCode() {
        return Objects.hash(guest_name, guest_phone);
    }
}
