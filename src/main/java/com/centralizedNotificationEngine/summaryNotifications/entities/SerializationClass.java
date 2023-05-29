package com.centralizedNotificationEngine.summaryNotifications.entities;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class SerializationClass {

    private String TicketNumber;

    private String ServiceID;

    private String Accountname;

    private String bandwidth;

    private String category;

    private String state;

    private String StatusReason;

    private String to_email;

    private String cc_email;

    private String opened_at;
}
