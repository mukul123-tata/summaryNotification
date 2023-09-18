package com.centralizedNotificationEngine.summaryNotifications.entities;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "ticketReferenceTCL",
        "nimsId",
        "maintenanceType",
        "activityStatus",
        "executionOwner",
        "activityWindowIST",
        "activityWindowGMT",
        "expectedImpact",
        "activityDescription",
        "expectedImpactDuration_dd_hh_mm",
        "extendedUpToTimeWindowIST",
        "extendedUpToTimeWindowGMT",
        "revisedActivityWindowIST",
        "revisedActivityWindowGMT",
        "eventDescription",
        "bsName",
        "circle",
        "city",
        "bsType",
        "ip",
        "siteID",
        "infraProvider",
        "iorID",
        "bsoCktID",
        "outageStartTime",
        "outageResolvedTime",
        "impactedCustomer",
        "sia",
        "AccDetails"
})
public class AdditionalInformation {

    public AdditionalInformation(){}

    @JsonProperty("ticketReferenceTCL")
    private String ticketReferenceTCL;

    @JsonProperty("nimsId")
    private String nimsId;

    @JsonProperty("maintenanceType")
    private String maintenanceType;

    @JsonProperty("activityStatus")
    private String activityStatus;

    @JsonProperty("executionOwner")
    private String executionOwner;

    @JsonProperty("activityWindowIST")
    private String activityWindowIST;

    @JsonProperty("activityWindowGMT")
    private String activityWindowGMT;

    @JsonProperty("expectedImpact")
    private String expectedImpact;

    @JsonProperty("activityDescription")
    private String activityDescription;

    @JsonProperty("expectedImpactDuration_dd_hh_mm")
    private String expectedImpactDuration_dd_hh_mm;

    @JsonProperty("extendedUpToTimeWindowIST")
    private String extendedUpToTimeWindowIST;

    @JsonProperty("extendedUpToTimeWindowGMT")
    private String extendedUpToTimeWindowGMT;

    @JsonProperty("revisedActivityWindowIST")
    private String revisedActivityWindowIST;

    @JsonProperty("revisedActivityWindowGMT")
    private String revisedActivityWindowGMT;

    private String eventDescription;

    private String bsName;

    private String circle;

    private String city;

    private String bsType;

    private String ip;

    private String siteID;

    private String infraProvider;

    private String iorID;

    private String bsoCktID;

    private String outageStartTime;

    private String outageResolvedTime;

    private String impactedCustomer;

    private String sia;

    public List<CasenClass> AccDetails;

    @JsonProperty("ticketReferenceTCL")
    public String getTicketReferenceTCL() {
        return ticketReferenceTCL;
    }

    @JsonProperty("ticketReferenceTCL")
    public void setTicketReferenceTCL(String ticketReferenceTCL) {
        this.ticketReferenceTCL = ticketReferenceTCL;
    }

    @JsonProperty("nimsId")
    public String getNimsId() {
        return nimsId;
    }

    @JsonProperty("nimsId")
    public void setNimsId(String nimsId) {
        this.nimsId = nimsId;
    }

    @JsonProperty("maintenanceType")
    public String getMaintenanceType() {
        return maintenanceType;
    }

    @JsonProperty("maintenanceType")
    public void setMaintenanceType(String maintenanceType) {
        this.maintenanceType = maintenanceType;
    }

    @JsonProperty("activityStatus")
    public String getActivityStatus() {
        return activityStatus;
    }

    @JsonProperty("activityStatus")
    public void setActivityStatus(String activityStatus) {
        this.activityStatus = activityStatus;
    }

    @JsonProperty("executionOwner")
    public String getExecutionOwner() {
        return executionOwner;
    }

    @JsonProperty("executionOwner")
    public void setExecutionOwner(String executionOwner) {
        this.executionOwner = executionOwner;
    }

    @JsonProperty("activityWindowIST")
    public String getActivityWindowIST() {
        return activityWindowIST;
    }

    @JsonProperty("activityWindowIST")
    public void setActivityWindowIST(String activityWindowIST) {
        this.activityWindowIST = activityWindowIST;
    }

    @JsonProperty("activityWindowGMT")
    public String getActivityWindowGMT() {
        return activityWindowGMT;
    }

    @JsonProperty("activityWindowGMT")
    public void setActivityWindowGMT(String activityWindowGMT) {
        this.activityWindowGMT = activityWindowGMT;
    }

    @JsonProperty("expectedImpact")
    public String getExpectedImpact() {
        return expectedImpact;
    }

    @JsonProperty("expectedImpact")
    public void setExpectedImpact(String expectedImpact) {
        this.expectedImpact = expectedImpact;
    }

    @JsonProperty("activityDescription")
    public String getActivityDescription() {
        return activityDescription;
    }

    @JsonProperty("activityDescription")
    public void setActivityDescription(String activityDescription) {
        this.activityDescription = activityDescription;
    }

    @JsonProperty("expectedImpactDuration_dd_hh_mm")
    public String getExpectedImpactDuration_dd_hh_mm() {
        return expectedImpactDuration_dd_hh_mm;
    }

    @JsonProperty("expectedImpactDuration_dd_hh_mm")
    public void setExpectedImpactDuration_dd_hh_mm(String expectedImpactDuration_dd_hh_mm) {
        this.expectedImpactDuration_dd_hh_mm = expectedImpactDuration_dd_hh_mm;
    }

    @JsonProperty("extendedUpToTimeWindowIST")
    public String getExtendedUpToTimeWindowIST() {
        return extendedUpToTimeWindowIST;
    }

    @JsonProperty("extendedUpToTimeWindowIST")
    public void setExtendedUpToTimeWindowIST(String extendedUpToTimeWindowIST) {
        this.extendedUpToTimeWindowIST = extendedUpToTimeWindowIST;
    }

    @JsonProperty("extendedUpToTimeWindowGMT")
    public String getExtendedUpToTimeWindowGMT() {
        return extendedUpToTimeWindowGMT;
    }

    @JsonProperty("extendedUpToTimeWindowGMT")
    public void setExtendedUpToTimeWindowGMT(String extendedUpToTimeWindowGMT) {
        this.extendedUpToTimeWindowGMT = extendedUpToTimeWindowGMT;
    }

    @JsonProperty("revisedActivityWindowIST")
    public String getRevisedActivityWindowIST() {
        return revisedActivityWindowIST;
    }

    @JsonProperty("revisedActivityWindowIST")
    public void setRevisedActivityWindowIST(String revisedActivityWindowIST) {
        this.revisedActivityWindowIST = revisedActivityWindowIST;
    }

    @JsonProperty("revisedActivityWindowGMT")
    public String getRevisedActivityWindowGMT() {
        return revisedActivityWindowGMT;
    }

    @JsonProperty("revisedActivityWindowGMT")
    public void setRevisedActivityWindowGMT(String revisedActivityWindowGMT) {
        this.revisedActivityWindowGMT = revisedActivityWindowGMT;
    }

    public void setEventDescription(String eventDescription){
        this.eventDescription = eventDescription;
    }
    public String getEventDescription(){
        return this.eventDescription;
    }
    public void setBsName(String bsName){
        this.bsName = bsName;
    }
    public String getBsName(){
        return this.bsName;
    }
    public void setCircle(String circle){
        this.circle = circle;
    }
    public String getCircle(){
        return this.circle;
    }
    public void setCity(String city){
        this.city = city;
    }
    public String getCity(){
        return this.city;
    }
    public void setBsType(String bsType){
        this.bsType = bsType;
    }
    public String getBsType(){
        return this.bsType;
    }
    public void setIp(String ip){
        this.ip = ip;
    }
    public String getIp(){
        return this.ip;
    }
    public void setSiteID(String siteID){
        this.siteID = siteID;
    }
    public String getSiteID(){
        return this.siteID;
    }
    public void setInfraProvider(String infraProvider){
        this.infraProvider = infraProvider;
    }
    public String getInfraProvider(){
        return this.infraProvider;
    }
    public void setIorID(String iorID){
        this.iorID = iorID;
    }
    public String getIorID(){
        return this.iorID;
    }
    public void setBsoCktID(String bsoCktID){
        this.bsoCktID = bsoCktID;
    }
    public String getBsoCktID(){
        return this.bsoCktID;
    }
    public void setOutageStartTime(String outageStartTime){
        this.outageStartTime = outageStartTime;
    }
    public String getOutageStartTime(){
        return this.outageStartTime;
    }
    public void setOutageResolvedTime(String outageResolvedTime){
        this.outageResolvedTime = outageResolvedTime;
    }
    public String getOutageResolvedTime(){
        return this.outageResolvedTime;
    }
    public void setImpactedCustomer(String impactedCustomer){
        this.impactedCustomer = impactedCustomer;
    }
    public String getImpactedCustomer(){
        return this.impactedCustomer;
    }
    public void setSia(String sia){
        this.sia = sia;
    }
    public String getSia(){
        return this.sia;
    }

    public List<CasenClass> getAccDetails() {
        return AccDetails;
    }

    public void setAccDetails(List<CasenClass> accDetails) {
        AccDetails = accDetails;
    }

}
