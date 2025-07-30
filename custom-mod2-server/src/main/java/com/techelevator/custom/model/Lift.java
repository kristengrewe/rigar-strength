package com.techelevator.custom.model;

import java.util.Objects;

public class Lift {

    private int liftId;
    private String liftName;

    public Lift(int liftId, String liftName) {
        this.liftId = liftId;
        this.liftName = liftName;
    }

    public int getLiftId() {
        return liftId;
    }

    public String getLiftName() {
        return liftName;
    }

    public void setLiftId(int liftId) {
        this.liftId = liftId;
    }

    public void setLiftName(String liftName) {
        this.liftName = liftName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Lift lift = (Lift) o;
        return liftId == lift.liftId && Objects.equals(liftName, lift.liftName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(liftId, liftName);
    }

    @Override
    public String toString() {
        return "{" + liftName +
                "}";
    }
}
