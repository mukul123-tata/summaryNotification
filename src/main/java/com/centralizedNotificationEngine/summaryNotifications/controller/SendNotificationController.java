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
import org.springframework.http.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.annotation.Schedules;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/data")
@CrossOrigin("*")
public class SendNotificationController {

    private static final Logger logger = LoggerFactory.getLogger(SendNotificationController.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${http.notifier.summary.api.url}")
    private String SummaryNotificationbaseUrl;

    HashMap<Object,Object> hashMap = new HashMap<>();
    HttpHeaders headers = new HttpHeaders();
    Contact contact = new Contact();
    RegexConfig regexConfig = new RegexConfig();
    ConnectingToDB connectingToDB = new ConnectingToDB();
    EncryptionConfig encryptionConfig = new EncryptionConfig();
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    Date date = new Date();

    @Schedules({
            @Scheduled(cron = "${cronjob.expression}"),
    })
    public ResponseEntity<?> sendSummaryNotification() throws JSONException, SQLException, ClassNotFoundException {
        try {
            logger.info("Starting Scheduler..");
            String response = null;
            Gson gson  = new Gson();

//            List<String> accountLists = new ArrayList<String>();
//            accountLists.add("EXL SERVICE.COM (I) PVT LTD");
//            accountLists.add("EXL Service.com (India) Private Limited");
//            accountLists.add("EXL SERVICE.COM INDIA PVT LTD");
//            accountLists.add("EXL SERVICE.COM LLC");
//            accountLists.add("EXL SERVICE.COM(INDIA) PVT LTD");
//            accountLists.add("HCL Technologies Limited");
//            accountLists.add("HCL TECHNOLOGIES LIMITED, SWISS BRANCH");
//            accountLists.add("Axis Bank Limited");
//            accountLists.add("Axis Bank Ltd");
//            accountLists.add("Axis Bank UK Limited");
//            accountLists.add("Bajaj Finance Limited");
//            accountLists.add("Bajaj Finance Ltd");
//            accountLists.add("HDFC Bank Limited");
//            accountLists.add("HDFC BANK LTD");
//            accountLists.add("ICICI Bank Limited");
//            accountLists.add("ICICI Bank Limited Singapore Branch");
//            accountLists.add("ICICI Bank Ltd");
//
//            accountLists.add("Bank Of Baroda");
//            accountLists.add("DBS BANK LTD");
//            accountLists.add("HDFC LIFE INSURANCE COMPANY LIMITED");
//            accountLists.add("HDFC SECURITIES LTD");
//            accountLists.add("Housing Development Finance Corporation Limited");
//            accountLists.add("PRATHAMA U.P. GRAMIN BANK");
//            accountLists.add("Punjab Gramin Bank");
//            accountLists.add("PUNJAB NATIONAL BANK");
//            accountLists.add("Punjab National Bank-erstwhile OBC");
//            accountLists.add("Punjab National Bank-erstwhile UBI");
//            accountLists.add("Starbucks");
//            accountLists.add("The Federal Bank Limited");
//            accountLists.add("The South Indian Bank Limited");
//            accountLists.add("Trent Ltd");
//            accountLists.add("UNION BANK OF INDIA");
//            accountLists.add("UNION BANK OF INDIA- erstwhile AB");
//            accountLists.add("UNION BANK OF INDIA- erstwhile CB");
//            accountLists.add("UNION BANK OF INDIA_erstwhile AB");
//            accountLists.add("Jubilant Foodworks Limited");

//            for(int m = 0;m<accountLists.size();m++) {

                String sql = "select \"Ticket Number\" TicketNumber, \"Service ID\" ServiceID, \"Account name\" Accountname, bandwidth, impact, state, \"Status Reason\" StatusReason, to_email, cc_email, \"opened_at\" opened_at, product, a_end_site_address, latest_update from Casen";
                Object[] contacts = jdbcTemplate.queryForList(sql).toArray();

                if(contacts.length==0) {
                    logger.info("There is no data in the db.");
                } else {

                    List<CasenClass> casens = new ArrayList<>();
                    for (int i = 0; i < contacts.length; i++) {
                        String toJsonString = gson.toJson(contacts[i]);
                        CasenClass fromJsonString = gson.fromJson(toJsonString, CasenClass.class);
                        casens.add(fromJsonString);

                        //Validation's of accountName
                        if (casens.get(i).getAccountname().equals("")) {
                            String strDate = formatter.format(date);
                            logger.info("Account Name is mandatory.");
                            connectingToDB.Execute("insert into CN_LOG_ERROR (AccountName, Status, Message, API_Name, Created_At) values ('null', 400, 'Account Name is mandatory', '" + Constant.API_Name.SUMMARY_NOTIFICATION + "', '" + strDate + "')");
                            return ErrorResponse.errorHandler(HttpStatus.BAD_REQUEST, true, "Account Name is mandatory");
                        }

                        //Validation's of email format
                        if (casens.get(i).getCcEmail().contains(",") || casens.get(i).getToEmail().contains(",")) {
                            String strDate = formatter.format(date);
                            logger.info("Invalid email format.");
                            connectingToDB.Execute("insert into CN_LOG_ERROR (AccountName, Status, Message, API_Name, Created_At) values ('" + casens.get(i).getAccountname() + "', 400, 'Invalid email format', '" + Constant.API_Name.SUMMARY_NOTIFICATION + "', '" + strDate + "')");
                            return ErrorResponse.errorHandler(HttpStatus.BAD_REQUEST, true, "Invalid email format");
                        } else if (casens.get(i).getToEmail().equals("")) {
                            String strDate = formatter.format(date);
                            logger.info("Tolist is empty.");
                            connectingToDB.Execute("insert into CN_LOG_ERROR (AccountName, Status, Message, API_Name, Created_At) values ('" + casens.get(i).getAccountname() + "', 400, 'Email is mandatory', '" + Constant.API_Name.SUMMARY_NOTIFICATION + "', '" + strDate + "')");
                            return ErrorResponse.errorHandler(HttpStatus.BAD_REQUEST, true, "Email is mandatory");
                        }

                        //Validation's of toEmail
                        String toEmail = casens.get(i).getToEmail().replaceAll(";;",",");
                        String[] toEmailSplit = toEmail.split(";");
                        for (int t = 0; t < toEmailSplit.length; t++) {
                            logger.info("toEmailSplit++{}",toEmailSplit[t]);
                            if (!(regexConfig.validateEmail(toEmailSplit[t]))) {
                                String strDate = formatter.format(date);
                                logger.info("To List is invalid.");
                                connectingToDB.Execute("insert into CN_LOG_ERROR (AccountName, Status, Message, API_Name, Created_At) values ('" + casens.get(i).getAccountname() + "', 400, 'To List is invalid', '" + Constant.API_Name.SUMMARY_NOTIFICATION + "', '" + strDate + "')");
                                return ErrorResponse.errorHandler(HttpStatus.BAD_REQUEST, true, "To List is invalid");
                            }
                        }

                        //Validation's of ccEmail
//                String ccEmail = casens.get(i).getCcEmail();
//                String[] ccEmailSplit = ccEmail.split(";");
//                for(int c = 0; c < ccEmailSplit.length; c++){
//                    if(!(regexConfig.validateEmail(ccEmailSplit[c]))){
//					      String strDate = formatter.format(date);
//                        logger.info("Cc list is invalid.");
//                        connectingToDB.Execute("insert into CN_LOG_ERROR (AccountName, Status, Message, API_Name, Created_At) values ('"+casens.get(i).getAccountname()+"', 400, 'Cc List is invalid', '"+Constant.API_Name.SUMMARY_NOTIFICATION+"', '"+strDate+"')");
//                        return ErrorResponse.errorHandler(HttpStatus.BAD_REQUEST,true,"Cc List is invalid");
//                    }
//                }

                        // Comment these snippet if production is readiness
                        casens.get(i).setToEmail("MUKUL.SHARMA1@contractor.tatacommunications.com");

                        String to_Email = casens.get(i).getToEmail().replaceAll(";;",",");
                        String[] to_Email_Split = to_Email.split(";");
                        if (to_Email_Split.length == 1) {
                            String encryptToEmail = encryptionConfig.encrypt(casens.get(i).getToEmail());
                            casens.get(i).setToEmail(encryptToEmail);
                        } else {
                            String updatedEncryptToEmail = "";
                            for (int t = 0; t < to_Email_Split.length; t++) {
                                String encryptToEmail = encryptionConfig.encrypt(to_Email_Split[t]);
                                updatedEncryptToEmail += encryptToEmail + ";";
                            }
                            StringBuffer stringBuffer = new StringBuffer(updatedEncryptToEmail);
                            stringBuffer.deleteCharAt(stringBuffer.length() - 1);
                            casens.get(i).setToEmail(stringBuffer.toString());
                        }

                    }
                    Map<String, List<CasenClass>> collect = casens.stream().collect(Collectors.groupingBy(CasenClass::getAccountname, Collectors.mapping(Function.identity(), Collectors.collectingAndThen(Collectors.toList(), e -> e.stream().sorted(Comparator.comparing(CasenClass::getImpact).reversed()).collect(Collectors.toList())))));
                    Map<String, String> retrieveAllKey = new HashMap<>();
                    List<Map<String, List<CasenClass>>> updatedAccountlist = new ArrayList<>();
                    for (String key : collect.keySet()) {
                        retrieveAllKey.put(key, key);
                    }
                    for (Map.Entry<String, List<CasenClass>> entry : collect.entrySet()) {
                        Map<String, List<CasenClass>> updatedAccountMap = new HashMap<>();
                        if (entry.getKey().equals(retrieveAllKey.get(entry.getKey()))) {
                            updatedAccountMap.put("AccDetails", entry.getValue());
                            updatedAccountlist.add(updatedAccountMap);
                        } else {
                            updatedAccountMap.put(entry.getKey(), entry.getValue());
                        }
                    }
                    headers.setContentType(MediaType.APPLICATION_JSON);
                    for (int i = 0; i < updatedAccountlist.size(); i++) {
                        String requestJson = gson.toJson(updatedAccountlist.get(i));
                        HttpEntity<String> entity = new HttpEntity<String>(requestJson, headers);
                        response = restTemplate.postForObject(SummaryNotificationbaseUrl, entity, String.class);
                    }
                    logger.info("Data send to notification.");
                }
      //      }
            logger.info("Scheduler Completed Successfully..");
            return SuccessResponse.successHandler(HttpStatus.OK, false, response, null);
        }catch (Exception ex){
            logger.error("Exception occurred while fetching data {} {}", ex.getMessage(), ex);
            String strDate = formatter.format(date);
            connectingToDB.Execute("insert into CN_LOG_ERROR (AccountName, Status, Message, API_Name, Created_At) values ('null', 400, '"+ex.getMessage()+"', '"+Constant.API_Name.SUMMARY_NOTIFICATION+"', '"+strDate+"')");
            return ErrorResponse.errorHandler(HttpStatus.BAD_REQUEST,true,ex.getMessage());
        }
    }

}
