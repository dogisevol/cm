package com.example.cashman;

public enum Error {

    DEVICE_NOT_FOUND_ERROR("Device has not been found"),
    WITHDRAWAL_ERROR("Required amount cannot be withdrawn");

    private final String text;

    Error(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}
