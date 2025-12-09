package com.smalaca.taskamanager.email;

public class EmailAddressValidator {
    public boolean isValid(String emailAddress) {
        return emailAddress != null && emailAddress.contains("@");
    }
}
