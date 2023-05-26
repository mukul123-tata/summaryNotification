package com.centralizedNotificationEngine.summaryNotifications.entities;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class SerializationClass {

    private String ticketNumber;

    private String serviceID;

    private String accountName;

    private String bandwidth;

    private String category;

    private String state;

    private String statusReason;

    private String to_email;

    private String cc_email;

    private String opened_at;
}
