package com.centralizedNotificationEngine.summaryNotifications.controller;

import com.centralizedNotificationEngine.summaryNotifications.entities.Casen;
import com.centralizedNotificationEngine.summaryNotifications.payload.ErrorResponse;
import com.centralizedNotificationEngine.summaryNotifications.payload.SuccessResponse;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.annotation.Schedules;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.stream.Stream;

@RestController
@RequestMapping("/contact")
@CrossOrigin("*")
public class Contact {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    Gson gson = new Gson();
    HashMap<Object,Object> hashMap = new HashMap<>();
    com.centralizedNotificationEngine.summaryNotifications.entities.Contact contact = new com.centralizedNotificationEngine.summaryNotifications.entities.Contact();

    @GetMapping("/")
    public ResponseEntity<?> createUser() throws Exception {
        try{
            String sql = "SELECT * FROM Casen";
            Object[] contacts = jdbcTemplate.queryForList(sql).stream().toArray();
            contact.setTo("mukul.sharma@tatacommunication.com");
            contact.setCc("mukul.sharma@tatacommunication.com");
            contact.setWatchList("string#gmail.com");
            hashMap.put("contact",contact);
            for(int i=0;i<contacts.length;i++){
                String s = gson.toJson(contacts[i]);
                Casen casen = gson.fromJson(s, Casen.class);
                System.out.println(casen.getTicketNumber());
            }
            return SuccessResponse.successHandler(HttpStatus.OK,false,"Detailed fetched successfully",hashMap);
        }catch (Exception ex){
            return ErrorResponse.errorHandler(HttpStatus.BAD_REQUEST,true,ex.getMessage());
        }
    }

//    @Schedules({
//            @Scheduled(cron = "${cronjob.expression}"),
//    })
    public void schedular(){
        String sql = "SELECT * FROM Casen";
        Object contacts = jdbcTemplate.queryForList(sql);
        System.out.println(contacts);
        System.out.println(new Date());
        System.out.println("--------------------");
    }
}
