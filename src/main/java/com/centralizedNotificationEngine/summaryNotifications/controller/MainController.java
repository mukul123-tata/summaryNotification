package com.centralizedNotificationEngine.summaryNotifications.controller;

import com.centralizedNotificationEngine.summaryNotifications.entities.*;
import com.centralizedNotificationEngine.summaryNotifications.payload.ErrorResponse;
import com.centralizedNotificationEngine.summaryNotifications.payload.SuccessResponse;
import com.google.gson.Gson;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.annotation.Schedules;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/data")
@CrossOrigin("*")
public class MainController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${http.basicauth.username}")
    private String username;

    @Value("${http.basicauth.password}")
    private String password;

    @Value("${http.notifier.api.url}")
    private String baseUrl;

    @Value("${http.notifier.summary.api.url}")
    private String baseUrl1;

    HashMap<Object,Object> hashMap = new HashMap<>();
    HttpHeaders headers = new HttpHeaders();
    ArrayList<Notes> notesArrayList = new ArrayList<Notes>();
    Contact contact = new Contact();
    Notification notification = new Notification();
    EventName eventName  = new EventName();
    TicketInfo ticketInfo = new TicketInfo();
    AdditionalInformation additionalInformation = new AdditionalInformation();

    @Schedules({
            @Scheduled(cron = "${cronjob.expression}"),
    })
    public ResponseEntity<?> createUser() throws Exception {
        try{
            Gson gson = new Gson();
            String response = null;
            String authStr =username+":"+password;
            String base64Creds = Base64.getEncoder().encodeToString(authStr.getBytes());
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.add("Authorization", "Basic " + base64Creds);

            String sql = "select TOP 10 \"Ticket Number\" TicketNumber, \"Service ID\" ServiceID, \"Account name\" Accountname, bandwidth, category, state, \"Status Reason\" StatusReason, to_email, cc_email, opened_at from Casen";
            Object[] contacts = jdbcTemplate.queryForList(sql).stream().toArray();

            contact.setWatchList("str@gmail.com");
            contact.setTo("MUKUL.SHARMA1@contractor.tatacommunications.com");
            contact.setCc("MUKUL.SHARMA1@contractor.tatacommunications.com");

            notification.setOrder("str");
            notification.setType("Spring boot kafka integration with MS Teams Graph API");

            eventName.setEventName("email body to send msg  Spring boot kafka integration with MS Teams Graph API");

            additionalInformation.setTemplateName("create incident ticket");

            Notes notes1 = new Notes("string","testingforAPI");
            Notes notes2 = new Notes("string","testingforAPI");
            notesArrayList.add(notes1);
            notesArrayList.add(notes2);

            hashMap.put("contact",contact);
            hashMap.put("notification",notification);
            hashMap.put("eventName",eventName);
            hashMap.put("ticketInfo",ticketInfo);
            hashMap.put("additionalInfo",additionalInformation);
            hashMap.put("notes",notesArrayList);

            for(Object data:contacts){
                String s = gson.toJson(data);
                Casen casen = gson.fromJson(s, Casen.class);
                ticketInfo.setNumber(casen.getTicketNumber());
                ticketInfo.setCreatedBy(casen.getOpened_at());
                ticketInfo.setServiceId(casen.getServiceID());
                ticketInfo.setAccount(casen.getAccountname());
                ticketInfo.setCategory(casen.getCategory());
                ticketInfo.setState(casen.getState());
                ticketInfo.setStatusReason(casen.getStatusReason());
                String requestJson  = gson.toJson(hashMap);
                HttpEntity<String> entity = new HttpEntity<String>(requestJson,headers);
                response = restTemplate.postForObject(baseUrl, entity, String.class);
            }
            System.out.println("+++++++++++++++++"+response);
            return SuccessResponse.successHandler(HttpStatus.OK,false,response,null);
        }catch (Exception ex){
            System.out.println("Error---------"+ex.getMessage());
            return ErrorResponse.errorHandler(HttpStatus.BAD_REQUEST,true,ex.getMessage());
        }
    }

    @GetMapping("sendData/casen")
    public ResponseEntity<?> send() throws JSONException {
        try {
            String response = null;
            headers.setContentType(MediaType.APPLICATION_JSON);
            Gson gson = new Gson();
            String sql = "select \"Ticket Number\" TicketNumber, \"Service ID\" ServiceID, \"Account name\" Accountname, bandwidth, category, state, \"Status Reason\" StatusReason, to_email, cc_email, opened_at from Casen";
            Object[] contacts = jdbcTemplate.queryForList(sql).toArray();
            List<SerializationClass> casens = new ArrayList<>();
            for (int i = 0; i < contacts.length; i++) {
                String s = gson.toJson(contacts[i]);
                SerializationClass casen = gson.fromJson(s, SerializationClass.class);
                casens.add(casen);
                casens.get(i).setCc_email("MUKUL.SHARMA1@contractor.tatacommunications.com");
                casens.get(i).setTo_email("MUKUL.SHARMA1@contractor.tatacommunications.com");
            }
            Map<String, List<SerializationClass>> collect = casens.stream().collect(Collectors.groupingBy(SerializationClass::getAccountname));
            Map<String, String> map = new HashMap<>();
            List<Map<String, List<SerializationClass>>> list = new ArrayList<>();
            for (String key : collect.keySet()) {
                map.put(key, key);
            }
            for (Map.Entry<String, List<SerializationClass>> entry : collect.entrySet()) {
                Map<String, List<SerializationClass>> updatedMap = new HashMap<>();
                if (entry.getKey().equals(map.get(entry.getKey()))) {
                    updatedMap.put("AccDetails", entry.getValue());
                    list.add(updatedMap);
                } else {
                    updatedMap.put(entry.getKey(), entry.getValue());
                }
            }
            for (int i = 0; i < list.size(); i++) {
                String requestJson = gson.toJson(list.get(i));
                HttpEntity<String> entity = new HttpEntity<String>(requestJson, headers);
                response = restTemplate.postForObject(baseUrl1, entity, String.class);
            }
            System.out.println("+++++++++++++++++"+response);
            return SuccessResponse.successHandler(HttpStatus.OK, false, response, null);
        }catch (Exception ex){
            System.out.println("Error---------"+ex.getMessage());
            return ErrorResponse.errorHandler(HttpStatus.BAD_REQUEST,true,ex.getMessage());
        }
    }
}
