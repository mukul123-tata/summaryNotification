package com.centralizedNotificationEngine.summaryNotifications.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import lombok.*;
import lombok.experimental.FieldNameConstants;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Casen {

    public String TicketNumber;

    public String ServiceID;

    public String Accountname;

    public String bandwidth;

    public String category;

    public String state;

    public String StatusReason;

    public String to_email;

    public String cc_email;

    public String opened_at;

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
