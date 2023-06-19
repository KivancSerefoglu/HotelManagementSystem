package edu.ozyegin.cs.entity;

import java.util.Date;
import java.util.Objects;

public class booking {
    private int book_id;
    private int room_id;
    private int user_id;
    private int guest_id;
    private int guest_count;
    private int is_paid;
    private double price;
    private int confirmation_status;
    private int is_booked;
    private String checkin_date;
    private String checkout_date;

    public booking(){}


    public int getBook_id() {
        return book_id;
    }

    public void setBook_id(int book_id) {
        this.book_id = book_id;
    }

    public booking book_id(int book_id) {
        this.book_id = book_id;
        return this;
    }

    public int getRoom_id() {
        return room_id;
    }

    public void setRoom_id(int room_name) {
        this.room_id = room_id;
    }

    public booking room_id(int room_id) {
        this.room_id = room_id;
        return this;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public booking user_id(int user_id) {
        this.user_id = user_id;
        return this;
    }

    public int getGuest_id() {
        return guest_id;
    }

    public void setGuest_id(int guest_id) {
        this.guest_id = guest_id;
    }

    public booking guest_id(int guest_id) {
        this.guest_id = guest_id;
        return this;
    }

    public int getGuest_count() {
        return guest_count;
    }

    public void setGuest_count(int guest_count) {
        this.guest_count = guest_count;
    }

    public booking guest_count(int guest_count) {
        this.guest_count = guest_count;
        return this;
    }

    public int getIs_paid() {
        return is_paid;
    }

    public void setIs_paid(int is_paid) {
        this.is_paid = is_paid;
    }

    public booking is_paid(int is_paid) {
        this.is_paid = is_paid;
        return this;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public booking price(double price) {
        this.price = price;
        return this;
    }

    public int getConfirmation_status() {
        return confirmation_status;
    }

    public void setConfirmation_status(int confirmation_status) {
        this.confirmation_status = confirmation_status;
    }

    public booking confirmation_status(int confirmation_status) {
        this.confirmation_status = confirmation_status;
        return this;
    }

    public int getIs_booked() {
        return is_booked;
    }

    public void setIs_booked(int is_booked) {
        this.is_booked = is_booked;
    }

    public booking is_booked(int is_booked) {
        this.is_booked = is_booked;
        return this;
    }

    public String getCheckin_date() {
        return checkin_date;
    }

    public void setCheckin_date(String checkin_date) {
        this.checkin_date = checkin_date;
    }

    public booking checkin_date(String checkin_date) {
        this.checkin_date = checkin_date;
        return this;
    }

    public String getCheckout_date() {
        return checkout_date;
    }

    public void setCheckout_date(String checkout_date) {
        this.checkout_date = checkout_date;
    }

    public booking checkout_date(String checkout_date) {
        this.checkout_date = checkout_date;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        booking booking = (booking) o;
        return room_id == booking.room_id && user_id == booking.user_id && guest_id == booking.guest_id && guest_count == booking.guest_count && is_paid == booking.is_paid && Double.compare(booking.price, price) == 0 && confirmation_status == booking.confirmation_status && is_booked == booking.is_booked && checkin_date.equals(booking.checkin_date) && checkout_date.equals(booking.checkout_date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(room_id, user_id, guest_id, guest_count, is_paid, price, confirmation_status, is_booked, checkin_date, checkout_date);
    }
}
