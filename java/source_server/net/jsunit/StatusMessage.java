package net.jsunit;

import java.util.Date;

public class StatusMessage {
    private String message;
    private Date date;

    public StatusMessage(String message) {
        this.message = message;
        this.date = new Date();
    }

    public String getMessage() {
        return message;
    }

    public Date getDate() {
        return date;
    }
}
