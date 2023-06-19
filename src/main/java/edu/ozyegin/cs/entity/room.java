package edu.ozyegin.cs.entity;

import java.util.Objects;

public class room {
    private int room_id;
    private String room_name;
    private int room_type;

    public room(){}

    public int getRoom_id() {
        return room_id;
    }

    public void setRoom_id(int room_id) {
        this.room_id = room_id;
    }

    public room room_id(int room_id) {
        this.room_id = room_id;
        return this;
    }

    public String getRoom_name() {
        return room_name;
    }

    public void setRoom_name(String room_name) {
        this.room_name = room_name;
    }

    public room room_name(String room_name) {
        this.room_name = room_name;
        return this;
    }

    public int getRoom_type() {
        return room_type;
    }

    public void setRoom_type(int room_type) {
        this.room_type = room_type;
    }

    public room room_type(int room_type) {
        this.room_type = room_type;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        room room = (room) o;
        return room_name == room.room_name && room_type == room.room_type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(room_name, room_type);
    }
}