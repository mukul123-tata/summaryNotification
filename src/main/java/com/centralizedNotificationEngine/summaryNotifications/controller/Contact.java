package com.centralizedNotificationEngine.summaryNotifications.controller;

import com.centralizedNotificationEngine.summaryNotifications.payload.ErrorResponse;
import com.centralizedNotificationEngine.summaryNotifications.payload.SuccessResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/contact")
@CrossOrigin("*")
public class Contact {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/")
    public ResponseEntity<?> createUser() throws Exception {
        try{
            String sql = "SELECT * FROM Contactn";
            Object contacts = jdbcTemplate.queryForList(sql);
            return SuccessResponse.successHandler(HttpStatus.OK,false,"Detailed fetched successfully",contacts);
        }catch (Exception ex){
            return ErrorResponse.errorHandler(HttpStatus.BAD_REQUEST,true,ex.getMessage());
        }
    }
}
