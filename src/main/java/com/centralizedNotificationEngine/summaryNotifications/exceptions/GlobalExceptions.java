package com.centralizedNotificationEngine.summaryNotifications.exceptions;

import com.centralizedNotificationEngine.summaryNotifications.payload.ErrorResponse;
import com.fasterxml.jackson.core.JsonParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import java.text.SimpleDateFormat;
import java.util.Date;

@RestControllerAdvice
public class GlobalExceptions {

    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptions.class);

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<?> HttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex, WebRequest request) {
        Date date = new Date();
        String strDate = formatter.format(date);
        logger.info("Request method not supported");
        //jdbcTemplate.execute("insert into CN_LOG_ERROR (AccountName, Status, Message, API_Name, Created_At) values ('null', 400, 'Request method not supported', '" + Constant.API_Name.GLOBAL_ERROR + "', '"+strDate+"')");
        return ErrorResponse.errorHandler(HttpStatus.BAD_REQUEST, true, "Invalid method");
    }

    @ExceptionHandler(JsonParseException.class)
    public ResponseEntity<?> JsonParseException(JsonParseException ex, WebRequest request) {
        Date date = new Date();
        String strDate = formatter.format(date);
        logger.info("Invalid payload");
        //jdbcTemplate.execute("insert into CN_LOG_ERROR (AccountName, Status, Message, API_Name, Created_At) values ('null', 400, 'Invalid payload', '" + Constant.API_Name.GLOBAL_ERROR + "', '"+strDate+"')");
        return ErrorResponse.errorHandler(HttpStatus.BAD_REQUEST, true, "Invalid payload");
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<?> MissingServletRequestParameterException(MissingServletRequestParameterException ex, WebRequest request) {
        Date date = new Date();
        String strDate = formatter.format(date);
        logger.info("Required request parameter is not present");
        //jdbcTemplate.execute("insert into CN_LOG_ERROR (AccountName, Status, Message, API_Name, Created_At) values ('null', 400, 'Required request parameter is not present', '" + Constant.API_Name.GLOBAL_ERROR + "', '"+strDate+"')");
        return ErrorResponse.errorHandler(HttpStatus.BAD_REQUEST, true, ex.getMessage());
    }

    @ExceptionHandler(MissingServletRequestPartException.class)
    public ResponseEntity<?> MissingServletRequestPartException(MissingServletRequestPartException ex, WebRequest request) {
        Date date = new Date();
        String strDate = formatter.format(date);
        logger.info("Required request part is not present");
       // jdbcTemplate.execute("insert into CN_LOG_ERROR (AccountName, Status, Message, API_Name, Created_At) values ('null', 400, 'Required request part is not present', '" + Constant.API_Name.GLOBAL_ERROR + "', '"+strDate+"')");
        return ErrorResponse.errorHandler(HttpStatus.BAD_REQUEST, true, ex.getMessage());
    }

}
