package com.secflag.demo.error;

public class InvalidEventTransaction extends Exception {
    public InvalidEventTransaction() {
        super("invalid-event-transaction");
    }
}
