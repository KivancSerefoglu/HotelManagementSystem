package edu.ozyegin.cs.entity;

import com.google.common.base.Objects;


public class amenities {
    private int amenities_id;
    private String amenities_name;

    public amenities(){}

    public int getAmenities_id() {
        return amenities_id;
    }

    public void setAmenities_id(int amenities_id) {
        this.amenities_id = amenities_id;
    }

    public amenities amenities_id(int amenities_id) {
        this.amenities_id = amenities_id;
        return this;
    }

    public String getAmenities_name() {
        return amenities_name;
    }

    public void setAmenities_name(String amenities_name) {
        this.amenities_name = amenities_name;
    }

    public amenities amenities_name(String amenities_name) {
        this.amenities_name = amenities_name;
        return this;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        amenities amenities = (amenities) o;
        return amenities_name.equals(amenities.amenities_name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getAmenities_name());
    }
}
