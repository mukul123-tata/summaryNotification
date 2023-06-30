package com.centralizedNotificationEngine.summaryNotifications.config;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexConfig {

    String emailRegex = "^(.+)@(.+)$";
    String accountNameRegex = "^[a-zA-Z\\s]*$";
    String eventNameRegex = "^[a-zA-Z_\\s]*$";

    public Boolean validateEmail(String email){
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public Boolean validateEventName(String eventName){
        Pattern pattern = Pattern.compile(eventNameRegex);
        Matcher matcher = pattern.matcher(eventName);
        return matcher.matches();
    }

    public Boolean validateAccountName(String accountName){
        Pattern pattern = Pattern.compile(accountNameRegex);
        Matcher matcher = pattern.matcher(accountName);
        return matcher.matches();
    }

}
