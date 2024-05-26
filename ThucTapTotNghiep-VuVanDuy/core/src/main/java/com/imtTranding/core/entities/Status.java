package com.imtTranding.core.entities;


public enum Status {

    DELETED(0),
    ACTIVE(1),
    NOT_APPROVED(-2),
    LOCKED(-1),
    CANCEL(-3),
    OTHER(-10);
    private int value;
    Status(int value) {
        this.value = value;
    }




    public int getValue() {
        return value;
    }
    public static Status getStatus(Integer status) {
        switch (status) {
            case 0:
                return Status.DELETED;
            case 1:
                return Status.ACTIVE;
            case -2:
                return Status.NOT_APPROVED;
            case -1:
                return Status.LOCKED;
            default:
                return Status.OTHER;
        }
    }
    @Override
    public String toString() {
        return String.valueOf(value);
    }

}
