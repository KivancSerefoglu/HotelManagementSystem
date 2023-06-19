package edu.ozyegin.cs.entity;

import java.util.Objects;

public class user {
    private int user_id;
    private int role_id;
    private String user_name;
    private String user_phone;
    private String user_mail;

    public user(){}

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public user user_id(int user_id) {
        this.user_id = user_id;
        return this;
    }

    public int getRole_id() {
        return role_id;
    }

    public void setRole_id(int role_id) {
        this.role_id = role_id;
    }

    public user role_id(int role_id) {
        this.role_id = role_id;
        return this;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public user user_name(String user_name) {
        this.user_name = user_name;
        return this;
    }

    public String getUser_phone() {
        return user_phone;
    }

    public void setUser_phone(String user_phone) {
        this.user_phone = user_phone;
    }

    public user user_phone(String user_phone) {
        this.user_phone = user_phone;
        return this;
    }

    public String getUser_mail() {
        return user_mail;
    }

    public void setUser_mail(String user_mail) {
        this.user_mail = user_mail;
    }

    public user user_mail(String user_mail) {
        this.user_mail = user_mail;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        user user = (user) o;
        return role_id == user.role_id && user_name.equals(user.user_name) && user_phone.equals(user.user_phone) && user_mail.equals(user.user_mail);
    }

    @Override
    public int hashCode() {
        return Objects.hash( role_id, user_name, user_phone, user_mail);
    }
}
