package edu.ozyegin.cs.entity;

import com.google.common.base.Objects;

import javax.management.relation.Role;

public class roles {
    private int role_id;
    private String role_name;
    private String role_desc;

    public roles(){}

    public int getRole_id() {
        return role_id;
    }

    public void setRole_id(int role_id) {
        this.role_id = role_id;
    }

    public roles role_id(int role_id) {
        this.role_id = role_id;
        return this;
    }

    public String getRole_name() {
        return role_name;
    }

    public void setRole_name(String role_name) {
        this.role_name = role_name;
    }

    public roles role_name(String role_name) {
        this.role_name = role_name;
        return this;
    }

    public String getRole_desc() {
        return role_desc;
    }

    public void setRole_desc(String role_desc) {
        this.role_desc = role_desc;
    }

    public roles role_desc(String role_desc) {
        this.role_desc = role_desc;
        return this;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        roles roles = (roles) o;
        return  Objects.equal(getRole_desc(), roles.getRole_desc()) &&
                Objects.equal(getRole_name(), roles.getRole_name());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getRole_name(), getRole_desc());
    }
}

