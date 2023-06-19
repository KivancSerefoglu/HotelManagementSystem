package edu.ozyegin.cs.entity;

import java.util.Objects;

public class roomType {
    private int rType_id;
    private int r_capacity;
    private String r_desc;

    public roomType(){}

    public int getrType_id() {
        return rType_id;
    }

    public void setrType_id(int rType_id) {
        this.rType_id = rType_id;
    }

    public roomType rType_id(int rType_id) {
        this.rType_id = rType_id;
        return this;
    }

    public int getR_capacity() {
        return r_capacity;
    }

    public void setR_capacity(int r_capacity) {
        this.r_capacity = r_capacity;
    }

    public roomType r_capacity(int r_capacity) {
        this.r_capacity = r_capacity;
        return this;
    }

    public String getR_desc() {
        return r_desc;
    }

    public void setR_desc(String r_desc) {
        this.r_desc = r_desc;
    }

    public roomType r_desc(String r_desc) {
        this.r_desc = r_desc;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        roomType roomType = (roomType) o;
        return r_capacity == roomType.r_capacity && r_desc.equals(roomType.r_desc);
    }

    @Override
    public int hashCode() {
        return Objects.hash(r_capacity, r_desc);
    }
}
