package com.centralizedNotificationEngine.summaryNotifications.controller;

import com.centralizedNotificationEngine.summaryNotifications.config.*;
import com.centralizedNotificationEngine.summaryNotifications.entities.*;
import com.centralizedNotificationEngine.summaryNotifications.payload.ErrorResponse;
import com.centralizedNotificationEngine.summaryNotifications.payload.SuccessResponse;
import com.google.gson.Gson;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.annotation.Schedules;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/data")
@CrossOrigin("*")
public class MainController {

    private static final Logger logger = LoggerFactory.getLogger(MainController.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${http.basicauth.username}")
    private String username;

    @Value("${http.basicauth.password}")
    private String password;

    @Value("${http.notifier.api.url}")
    private String RfTemplateBaseUrl;

    @Value("${http.notifier.summary.api.url}")
    private String SummaryNotificationbaseUrl;

    @Value("${http.notifier.summary.with.attachment.api.url}")
    private String SummaryNotificationWithAttachmentBaseUrl;

    HashMap<Object,Object> hashMap = new HashMap<>();
    HttpHeaders headers = new HttpHeaders();
    ArrayList<Notes> notesArrayList = new ArrayList<Notes>();
    Contact contact = new Contact();
    Notification notification = new Notification();
    EventName eventName  = new EventName();
    TicketInfo ticketInfo = new TicketInfo();
    AdditionalInformation additionalInformation = new AdditionalInformation();
    RegexConfig regexConfig = new RegexConfig();
    ConnectingToDB connectingToDB = new ConnectingToDB();
    EncryptionConfig encryptionConfig = new EncryptionConfig();
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");


    @Schedules({
            @Scheduled(cron = "${cronjob.expression}"),
    })
    public ResponseEntity<?> sendRfTemplateNotificationV1() throws Exception {
        try{
            Gson gson = new Gson();
            String response = null;
            String authStr =username+":"+password;
            String base64Creds = Base64.getEncoder().encodeToString(authStr.getBytes());
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.add("Authorization", "Basic " + base64Creds);

            String sql = "select \"Ticket Number\" TicketNumber, \"Service ID\" ServiceID, \"Account name\" Accountname, bandwidth, impact, state, \"Status Reason\" StatusReason, to_email, cc_email, \"opened_at\" opened_at from Casen where \"Account name\"='Axis Bank Limited'";
            Object[] contacts = jdbcTemplate.queryForList(sql).stream().toArray();

            notification.setOrder("str");
            notification.setType("Spring boot kafka integration with MS Teams Graph API");

            eventName.setEventName("email body to send msg  Spring boot kafka integration with MS Teams Graph API");

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

            List<CasenClass> casens = new ArrayList<>();
            for(int i=0;i<contacts.length;i++){
                String s = gson.toJson(contacts[i]);
                CasenClass casen = gson.fromJson(s, CasenClass.class);
                casens.add(casen);
                casens.get(i).setToEmail("MUKUL.SHARMA1@contractor.tatacommunications.com;suvarna.jagadale@tatacommunications.com");
                String to_Email  = casens.get(i).getToEmail();
                String[] to_Email_Split = to_Email.split(";");
                if(to_Email_Split.length==1){
                    String encryptToEmail = encryptionConfig.encrypt(casens.get(i).getToEmail());
                    casens.get(i).setToEmail(encryptToEmail);
                }else{
                    String s1="";
                    for(int t = 0; t < to_Email_Split.length; t++){
                        String encryptToEmail = encryptionConfig.encrypt(to_Email_Split[t]);
                        s1+=encryptToEmail+";";
                    }
                    StringBuffer sb= new StringBuffer(s1);
                    sb.deleteCharAt(sb.length()-1);
                    casens.get(i).setToEmail(sb.toString());
                }
            }

            additionalInformation.setAccDetails(casens);

            contact.setWatchList("str@gmail.com");
            contact.setTo(casens.get(0).getToEmail());
            contact.setCc("");

            String requestJson  = gson.toJson(hashMap);
            HttpEntity<String> entity = new HttpEntity<String>(requestJson,headers);
            response = restTemplate.postForObject(RfTemplateBaseUrl, entity, String.class);
            return SuccessResponse.successHandler(HttpStatus.OK,false,response,hashMap);
        }catch (Exception ex){
            return ErrorResponse.errorHandler(HttpStatus.BAD_REQUEST,true,ex.getMessage());
        }
    }


            @Schedules({
            @Scheduled(cron = "${cronjob.expression}"),
    })
    public ResponseEntity<?> sendSummaryNotificationV1() throws JSONException, SQLException, ClassNotFoundException {
        try {
            int sendListSize=0;
            String response = null;
            Gson gson = new Gson();
            headers.setContentType(MediaType.APPLICATION_JSON);
            String sql = "select \"Ticket Number\" TicketNumber, \"Service ID\" ServiceID, \"Account name\" Accountname, bandwidth, impact, state, \"Status Reason\" StatusReason, to_email, cc_email, \"opened_at\" opened_at from Casen where \"Account name\"='Axis Bank Limited'";
            Object[] contacts = jdbcTemplate.queryForList(sql).toArray();
            List<CasenClass> casens = new ArrayList<>();
            for (int i = 0; i < contacts.length; i++) {
                String s = gson.toJson(contacts[i]);
                CasenClass casen = gson.fromJson(s, CasenClass.class);
                casens.add(casen);

                //Validation's of accountName
                if(casens.get(i).getAccountname().equals("")){
                    Date date = new Date();
                    String strDate = formatter.format(date);
                    logger.info("Account Name is mandatory");
                    connectingToDB.Execute("insert into CN_LOG_ERROR (AccountName, Status, Message, API_Name, Created_At) values ('null', 400, 'Account Name is mandatory', '"+Constant.API_Name.SUMMARY_NOTIFICATION+"', '"+strDate+"')");
                    return ErrorResponse.errorHandler(HttpStatus.BAD_REQUEST,true,"Account Name is mandatory");
                }

                //Validation's of email format
                if(casens.get(i).getCcEmail().contains(",") || casens.get(i).getToEmail().contains(",")){
                    Date date = new Date();
                    String strDate = formatter.format(date);
                    logger.info("Invalid email format");
                    connectingToDB.Execute("insert into CN_LOG_ERROR (AccountName, Status, Message, API_Name, Created_At) values ('"+casens.get(i).getAccountname()+"', 400, 'Invalid email format', '"+Constant.API_Name.SUMMARY_NOTIFICATION+"', '"+strDate+"')");
                    return ErrorResponse.errorHandler(HttpStatus.BAD_REQUEST,true,"Invalid email format");
                }else if(casens.get(i).getToEmail().equals("")){
                    Date date = new Date();
                    String strDate = formatter.format(date);
                    logger.info("Email is mandatory");
                    connectingToDB.Execute("insert into CN_LOG_ERROR (AccountName, Status, Message, API_Name, Created_At) values ('"+casens.get(i).getAccountname()+"', 400, 'Email is mandatory', '"+Constant.API_Name.SUMMARY_NOTIFICATION+"', '"+strDate+"')");
                    return ErrorResponse.errorHandler(HttpStatus.BAD_REQUEST,true,"Email is mandatory");
                }

                //Validation's of toEmail
                String toEmail = casens.get(i).getToEmail();
                String[] toEmailSplit = toEmail.split(";");
                for(int t = 0; t < toEmailSplit.length; t++){
                    if(!(regexConfig.validateEmail(toEmailSplit[t]))){
                        Date date = new Date();
                        String strDate = formatter.format(date);
                        logger.info("To List is invalid");
                        connectingToDB.Execute("insert into CN_LOG_ERROR (AccountName, Status, Message, API_Name, Created_At) values ('"+casens.get(i).getAccountname()+"', 400, 'To List is invalid', '"+Constant.API_Name.SUMMARY_NOTIFICATION+"', '"+strDate+"')");
                        return ErrorResponse.errorHandler(HttpStatus.BAD_REQUEST,true,"To List is invalid");
                    }
                }

                //Validation's of ccEmail
//                String ccEmail = casens.get(i).getCcEmail();
//                String[] ccEmailSplit = ccEmail.split(";");
//                for(int c = 0; c < ccEmailSplit.length; c++){
//                    if(!(regexConfig.validateEmail(ccEmailSplit[c]))){
//                        Date date = new Date();
//					      String strDate = formatter.format(date);
//                        logger.info("Cc list is invalid");
//                        connectingToDB.Execute("insert into CN_LOG_ERROR (AccountName, Status, Message, API_Name, Created_At) values ('"+casens.get(i).getAccountname()+"', 400, 'Cc List is invalid', '"+Constant.API_Name.SUMMARY_NOTIFICATION+"', '"+strDate+"')");
//                        return ErrorResponse.errorHandler(HttpStatus.BAD_REQUEST,true,"Cc List is invalid");
//                    }
//                }

                casens.get(i).setToEmail("MUKUL.SHARMA1@contractor.tatacommunications.com;suvarna.jagadale@tatacommunications.com");
                String to_Email  = casens.get(i).getToEmail();
                String[] to_Email_Split = to_Email.split(";");
                if(to_Email_Split.length==1){
                    String encryptToEmail = encryptionConfig.encrypt(casens.get(i).getToEmail());
                    casens.get(i).setToEmail(encryptToEmail);
                }else{
                    String s1="";
                    for(int t = 0; t < to_Email_Split.length; t++){
                        String encryptToEmail = encryptionConfig.encrypt(to_Email_Split[t]);
                        s1+=encryptToEmail+";";
                    }
                    StringBuffer sb= new StringBuffer(s1);
                    sb.deleteCharAt(sb.length()-1);
                    casens.get(i).setToEmail(sb.toString());
                }

            }
            Map<String, List<CasenClass>> collect = casens.stream().collect(Collectors.groupingBy(CasenClass::getAccountname,Collectors.mapping(Function.identity(),Collectors.collectingAndThen(Collectors.toList(),e->e.stream().sorted(Comparator.comparing(CasenClass::getImpact).reversed()).collect(Collectors.toList())))));
            Map<String, String> map = new HashMap<>();
            List<Map<String, List<CasenClass>>> list = new ArrayList<>();
            for (String key : collect.keySet()) {
                map.put(key, key);
            }
            for (Map.Entry<String, List<CasenClass>> entry : collect.entrySet()) {
                Map<String, List<CasenClass>> updatedMap = new HashMap<>();
                if (entry.getKey().equals(map.get(entry.getKey()))) {
                    updatedMap.put("AccDetails", entry.getValue());
                    list.add(updatedMap);
                } else {
                    updatedMap.put(entry.getKey(), entry.getValue());
                }
            }
            for (int i = 0; i < list.size(); i++) {
                sendListSize+=list.get(i).get("AccDetails").size();
                String requestJson = gson.toJson(list.get(i));
                HttpEntity<String> entity = new HttpEntity<String>(requestJson,headers);
                response = restTemplate.postForObject(SummaryNotificationbaseUrl, entity, String.class);
            }
            System.out.println("Original list size are : "+contacts.length);
            System.out.println("=================");
            System.out.println("Send list size are : "+sendListSize);
            return SuccessResponse.successHandler(HttpStatus.OK, false, response, list);
        }catch (Exception ex){
            System.out.println("Error---------"+ex.getMessage());
            Date date = new Date();
            String strDate = formatter.format(date);
            connectingToDB.Execute("insert into CN_LOG_ERROR (AccountName, Status, Message, API_Name, Created_At) values ('null', 400, '"+ex.getMessage()+"', '"+Constant.API_Name.SUMMARY_NOTIFICATION+"', '"+strDate+"')");
            return ErrorResponse.errorHandler(HttpStatus.BAD_REQUEST,true,ex.getMessage());
        }
    }



    //        @Schedules({
//            @Scheduled(cron = "${cronjob.expression}"),
//    })
    @PostMapping("/v1.1/sendData/summaryNotification")
    public ResponseEntity<?> sendSummaryNotificationV2(@RequestPart("file")MultipartFile file) throws JSONException, SQLException, ClassNotFoundException {
        try {
            int sendListSize=0;
            String response = null;
            Gson gson = new Gson();
            String sql = "select \"Ticket Number\" TicketNumber, \"Service ID\" ServiceID, \"Account name\" Accountname, bandwidth, impact, state, \"Status Reason\" StatusReason, to_email, cc_email, \"opened_at\" opened_at from Casen where \"Account name\"='Axis Bank Limited'";
            Object[] contacts = jdbcTemplate.queryForList(sql).toArray();
            List<CasenClass> casens = new ArrayList<>();
            for (int i = 0; i < contacts.length; i++) {
                String s = gson.toJson(contacts[i]);
                CasenClass casen = gson.fromJson(s, CasenClass.class);
                casens.add(casen);

                //Validation's of accountName
                if(casens.get(i).getAccountname().equals("")){
                    Date date = new Date();
                    String strDate = formatter.format(date);
                    logger.info("Account Name is mandatory");
                    connectingToDB.Execute("insert into CN_LOG_ERROR (AccountName, Status, Message, API_Name, Created_At) values ('null', 400, 'Account Name is mandatory', '"+Constant.API_Name.SUMMARY_NOTIFICATION+"', '"+strDate+"')");
                    return ErrorResponse.errorHandler(HttpStatus.BAD_REQUEST,true,"Account Name is mandatory");
                }

                //Validation's of email format
                if(casens.get(i).getCcEmail().contains(",") || casens.get(i).getToEmail().contains(",")){
                    Date date = new Date();
                    String strDate = formatter.format(date);
                    logger.info("Invalid email format");
                    connectingToDB.Execute("insert into CN_LOG_ERROR (AccountName, Status, Message, API_Name, Created_At) values ('"+casens.get(i).getAccountname()+"', 400, 'Invalid email format', '"+Constant.API_Name.SUMMARY_NOTIFICATION+"', '"+strDate+"')");
                    return ErrorResponse.errorHandler(HttpStatus.BAD_REQUEST,true,"Invalid email format");
                }else if(casens.get(i).getToEmail().equals("")){
                    Date date = new Date();
                    String strDate = formatter.format(date);
                    logger.info("Email is mandatory");
                    connectingToDB.Execute("insert into CN_LOG_ERROR (AccountName, Status, Message, API_Name, Created_At) values ('"+casens.get(i).getAccountname()+"', 400, 'Email is mandatory', '"+Constant.API_Name.SUMMARY_NOTIFICATION+"', '"+strDate+"')");
                    return ErrorResponse.errorHandler(HttpStatus.BAD_REQUEST,true,"Email is mandatory");
                }

                //Validation's of toEmail
                String toEmail = casens.get(i).getToEmail();
                String[] toEmailSplit = toEmail.split(";");
                for(int t = 0; t < toEmailSplit.length; t++){
                    if(!(regexConfig.validateEmail(toEmailSplit[t]))){
                        Date date = new Date();
                        String strDate = formatter.format(date);
                        logger.info("To List is invalid");
                        connectingToDB.Execute("insert into CN_LOG_ERROR (AccountName, Status, Message, API_Name, Created_At) values ('"+casens.get(i).getAccountname()+"', 400, 'To List is invalid', '"+Constant.API_Name.SUMMARY_NOTIFICATION+"', '"+strDate+"')");
                        return ErrorResponse.errorHandler(HttpStatus.BAD_REQUEST,true,"To List is invalid");
                    }
                }

                //Validation's of ccEmail
//                String ccEmail = casens.get(i).getCcEmail();
//                String[] ccEmailSplit = ccEmail.split(";");
//                for(int c = 0; c < ccEmailSplit.length; c++){
//                    if(!(regexConfig.validateEmail(ccEmailSplit[c]))){
//                        Date date = new Date();
//					      String strDate = formatter.format(date);
//                        logger.info("Cc list is invalid");
//                        connectingToDB.Execute("insert into CN_LOG_ERROR (AccountName, Status, Message, API_Name, Created_At) values ('"+casens.get(i).getAccountname()+"', 400, 'Cc List is invalid', '"+Constant.API_Name.SUMMARY_NOTIFICATION+"', '"+strDate+"')");
//                        return ErrorResponse.errorHandler(HttpStatus.BAD_REQUEST,true,"Cc List is invalid");
//                    }
//                }

                casens.get(i).setToEmail("MUKUL.SHARMA1@contractor.tatacommunications.com;suvarna.jagadale@tatacommunications.com");
                String to_Email  = casens.get(i).getToEmail();
                String[] to_Email_Split = to_Email.split(";");
                if(to_Email_Split.length==1){
                    String encryptToEmail = encryptionConfig.encrypt(casens.get(i).getToEmail());
                    casens.get(i).setToEmail(encryptToEmail);
                }else{
                    String s1="";
                    for(int t = 0; t < to_Email_Split.length; t++){
                        String encryptToEmail = encryptionConfig.encrypt(to_Email_Split[t]);
                        s1+=encryptToEmail+";";
                    }
                    StringBuffer sb= new StringBuffer(s1);
                    sb.deleteCharAt(sb.length()-1);
                    casens.get(i).setToEmail(sb.toString());
                }

            }
            Map<String, List<CasenClass>> collect = casens.stream().collect(Collectors.groupingBy(CasenClass::getAccountname,Collectors.mapping(Function.identity(),Collectors.collectingAndThen(Collectors.toList(),e->e.stream().sorted(Comparator.comparing(CasenClass::getImpact).reversed()).collect(Collectors.toList())))));
            Map<String, String> map = new HashMap<>();
            List<Map<String, List<CasenClass>>> list = new ArrayList<>();
            for (String key : collect.keySet()) {
                map.put(key, key);
            }
            for (Map.Entry<String, List<CasenClass>> entry : collect.entrySet()) {
                Map<String, List<CasenClass>> updatedMap = new HashMap<>();
                if (entry.getKey().equals(map.get(entry.getKey()))) {
                    updatedMap.put("AccDetails", entry.getValue());
                    list.add(updatedMap);
                } else {
                    updatedMap.put(entry.getKey(), entry.getValue());
                }
            }
            for (int i = 0; i < list.size(); i++) {
                sendListSize+=list.get(i).get("AccDetails").size();
                String requestJson = gson.toJson(list.get(i));

                MultiValueMap<String,Object> multipartRequest = new LinkedMultiValueMap<>();

                HttpHeaders requestHeaders = new HttpHeaders();
                requestHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);//Main request's headers

                HttpHeaders requestHeadersAttachment = new HttpHeaders();
                requestHeadersAttachment.setContentType(MediaType.TEXT_PLAIN);// extract mediatype from file extension
                HttpEntity<ByteArrayResource> attachmentPart;
                ByteArrayResource fileAsResource = new ByteArrayResource(file.getBytes()){
                    @Override
                    public String getFilename(){
                        return file.getOriginalFilename();
                    }
                };
                attachmentPart = new HttpEntity<>(fileAsResource,requestHeadersAttachment);

                multipartRequest.set("file",attachmentPart);


                HttpHeaders requestHeadersJSON = new HttpHeaders();
                requestHeadersJSON.setContentType(MediaType.APPLICATION_JSON);
                HttpEntity<String> requestEntityJSON = new HttpEntity<>(requestJson, requestHeadersJSON);

                multipartRequest.set("AccDetails",requestEntityJSON);

                HttpEntity<MultiValueMap<String,Object>> requestEntity = new HttpEntity<>(multipartRequest,requestHeaders);//final request

                response = restTemplate.postForObject(SummaryNotificationWithAttachmentBaseUrl, requestEntity, String.class);
            }
            System.out.println("Original list size are : "+contacts.length);
            System.out.println("=================");
            System.out.println("Send list size are : "+sendListSize);
            return SuccessResponse.successHandler(HttpStatus.OK, false, response, list);
        }catch (Exception ex){
            System.out.println("Error---------"+ex.getMessage());
            Date date = new Date();
            String strDate = formatter.format(date);
            connectingToDB.Execute("insert into CN_LOG_ERROR (AccountName, Status, Message, API_Name, Created_At) values ('null', 400, '"+ex.getMessage()+"', '"+Constant.API_Name.SUMMARY_NOTIFICATION+"', '"+strDate+"')");
            return ErrorResponse.errorHandler(HttpStatus.BAD_REQUEST,true,ex.getMessage());
        }
    }

    @GetMapping("fetchData/errorLogs")
    public ResponseEntity<?> findErrorLogs(){
        try{
           //connectingToDB.Execute("CREATE TABLE CN_LOG_ERROR(" + "AccountName VARCHAR(255), Status NUMERIC(3), Message VARCHAR(255), API_Name VARCHAR(255), Created_At VARCHAR(255))");
             //connectingToDB.Execute("DROP TABLE CN_LOG_ERROR");
            //connectingToDB.Execute("DELETE FROM CN_LOG_ERROR");
           List<Map<String,Object>> data = connectingToDB.QueryForList("select * from CN_LOG_ERROR");
            return SuccessResponse.successHandler(HttpStatus.OK, false, "Successfully operation performed", data);
        }catch (Exception ex){
            return ErrorResponse.errorHandler(HttpStatus.BAD_REQUEST,true,ex.getMessage());
        }
    }

}
