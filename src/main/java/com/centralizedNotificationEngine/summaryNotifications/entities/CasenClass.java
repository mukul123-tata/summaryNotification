package com.centralizedNotificationEngine.summaryNotifications.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "TicketNumber",
        "ServiceID",
        "Accountname",
        "bandwidth",
        "impact",
        "state",
        "StatusReason",
        "to_email",
        "cc_email",
        "opened_at"
})
public class CasenClass {

    @JsonProperty("TicketNumber")
    private String TicketNumber;
    @JsonProperty("ServiceID")
    private String ServiceID;
    @JsonProperty("Accountname")
    private String Accountname;
    @JsonProperty("bandwidth")
    private String bandwidth;
    @JsonProperty("impact")
    private String impact;
    @JsonProperty("state")
    private String state;
    @JsonProperty("StatusReason")
    private String StatusReason;
    @JsonProperty("to_email")
    private String to_email;
    @JsonProperty("cc_email")
    private String cc_email;
    @JsonProperty("opened_at")
    private String opened_at;

    @JsonProperty("TicketNumber")
    public String getTicketNumber() {
        return TicketNumber;
    }

    @JsonProperty("TicketNumber")
    public void setTicketNumber(String ticketNumber) {
        this.TicketNumber = ticketNumber;
    }

    @JsonProperty("ServiceID")
    public String getServiceID() {
        return ServiceID;
    }

    @JsonProperty("ServiceID")
    public void setServiceID(String serviceID) {
        this.ServiceID = serviceID;
    }

    @JsonProperty("Accountname")
    public String getAccountname() {
        return Accountname;
    }

    @JsonProperty("Accountname")
    public void setAccountname(String accountname) {
        this.Accountname = accountname;
    }

    @JsonProperty("bandwidth")
    public String getBandwidth() {
        return bandwidth;
    }

    @JsonProperty("bandwidth")
    public void setBandwidth(String bandwidth) {
        this.bandwidth = bandwidth;
    }

    @JsonProperty("impact")
    public String getImpact() {
        return impact;
    }

    @JsonProperty("impact")
    public void setImpact(String impact) {
        this.impact = impact;
    }

    @JsonProperty("state")
    public String getState() {
        return state;
    }

    @JsonProperty("state")
    public void setState(String state) {
        this.state = state;
    }

    @JsonProperty("StatusReason")
    public String getStatusReason() {
        return StatusReason;
    }

    @JsonProperty("StatusReason")
    public void setStatusReason(String statusReason) {
        this.StatusReason = statusReason;
    }

    @JsonProperty("to_email")
    public String getToEmail() {
        return to_email;
    }

    @JsonProperty("to_email")
    public void setToEmail(String toEmail) {
        this.to_email = toEmail;
    }

    @JsonProperty("cc_email")
    public String getCcEmail() {
        return cc_email;
    }

    @JsonProperty("cc_email")
    public void setCcEmail(String ccEmail) {
        this.cc_email = ccEmail;
    }

    @JsonProperty("opened_at")
    public String getOpenedAt() {
        return opened_at;
    }

    @JsonProperty("opened_at")
    public void setOpenedAt(String openedAt) {
        this.opened_at = openedAt;
    }
}
