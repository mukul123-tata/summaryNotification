package com.centralizedNotificationEngine.summaryNotifications.entities;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Setter
@Getter
public class Contact {

    public String to;
    public String cc;
    public String watchList;
}
