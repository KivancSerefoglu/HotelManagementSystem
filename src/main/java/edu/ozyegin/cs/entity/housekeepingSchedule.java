package edu.ozyegin.cs.entity;

import java.util.Date;
import java.util.Objects;

public class housekeepingSchedule {
    private  int schedule_id;
    private int user_id;
    private int room_id;
    private int is_cleaned;
    private String cleaning_date;

    public housekeepingSchedule(){}

    public int getSchedule_id() {
        return schedule_id;
    }

    public void setSchedule_id(int schedule_id) {
        this.schedule_id = schedule_id;
    }

    public housekeepingSchedule schedule_id(int schedule_id){
        this.schedule_id=schedule_id;
        return this;
    }

    public void setRoom_id(int room_id) {
        this.room_id = room_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public housekeepingSchedule user_id(int user_id){
        this.user_id=user_id;
        return this;
    }

    public int getRoom_id() {
        return room_id;
    }

    public housekeepingSchedule room_id(int room_id){
        this.room_id=room_id;
        return this;
    }

    public int getIs_cleaned() {
        return is_cleaned;
    }

    public void setIs_cleaned(int is_cleaned) {
        this.is_cleaned = is_cleaned;
    }

    public housekeepingSchedule is_cleaned(int is_cleaned){
        this.is_cleaned=is_cleaned;
        return this;
    }

    public String getCleaning_date() {
        return cleaning_date;
    }

    public void setCleaning_date(String cleaning_date) {
        this.cleaning_date = cleaning_date;
    }

    public housekeepingSchedule cleaning_date(String cleaning_date){
        this.cleaning_date=cleaning_date;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        housekeepingSchedule that = (housekeepingSchedule) o;
        return user_id == that.user_id && room_id == that.room_id && is_cleaned == that.is_cleaned && cleaning_date.equals(that.cleaning_date);
    }

    @Override
    public int hashCode() {
        return Objects.hash( user_id, room_id, is_cleaned, cleaning_date);
    }
}
