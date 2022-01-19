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

    // TODO: Pagination would be a great way to improve this API. It would allow users more flexibility to set the
    //  number of messages per page and number of pages of messages they want to get. If our limit of only returning
    //  up to 100 messages was due to response size constraints and database access constraints then pagination would
    //  help alleviate those concerns and allow our users to get more messages.
    @RequestMapping(method = RequestMethod.GET, value = "/messages")
    @ResponseStatus(HttpStatus.OK)
    public List<Message> getMessages(@RequestParam String senderId, @RequestParam String recipientId) {
        return messageService.getMessages(senderId, recipientId);
    }

    // TODO: We should never expose information regarding the shape or specifics of the database
    //  instead of returning the full Message entity we should return a scrubbed version without the primary key ID
    @RequestMapping(method = RequestMethod.POST, value = "/messages")
    @ResponseStatus(HttpStatus.CREATED)
    public Message createMessage(@Valid @RequestBody MessageRequest request) {
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
