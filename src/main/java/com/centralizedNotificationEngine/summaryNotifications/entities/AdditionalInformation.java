package com.centralizedNotificationEngine.summaryNotifications.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AdditionalInformation {

    @JsonProperty("AccDetails")
    public List<CasenClass> accDetails;
}
