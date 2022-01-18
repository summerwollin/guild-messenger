package com.example.guildmessenger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class MessageController {
    @Autowired
    private MessageService messageService;

    @RequestMapping("/")
    public String home() {
        return "Hello Guild!";
    }

    // TODO: We should never expose information regarding the shape or specifics of the database
    //  instead of returning the full Message entity we should return a scrubbed version without the primary key ID
    @RequestMapping(method = RequestMethod.POST, value = "/messages")
    @ResponseStatus(HttpStatus.CREATED)
    public Message create(@Valid @RequestBody MessageRequest request) {
        return messageService.createMessage(request);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        Map<String, String> requestErrors = new HashMap<>();
        List<ObjectError> errors = ex.getBindingResult().getAllErrors();
        for (ObjectError error: errors) {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            requestErrors.put(fieldName, errorMessage);
        }
        return requestErrors;
    }

}
