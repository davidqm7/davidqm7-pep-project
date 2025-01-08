package Controller;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;
import io.javalin.Javalin;
import io.javalin.http.Context;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    MessageService messageService;
    AccountService accountService;

    public SocialMediaController(){
        this.messageService = new MessageService();
        this.accountService = new AccountService();
    }
    
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.post("/register", this::createAccount);
        app.post("/login", this::loginToAccount);
        app.post("/messages", this::createMessages);
        app.get("/messages", this::getMessages);
        app.get("/messages/{message_id}", this::getMessageById);
        app.delete("/messages/{message_id}", this::deleteMessage);
        app.patch("/messages/{message_id}", this::updateMessage);
        app.get("/accounts/{account_id}/messages", this::getMessagesFromUser);


        return app;
    }

    private void createAccount(Context ctx)throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        try{
        Account account = mapper.readValue(ctx.body(), Account.class);
        Account registerAccount = accountService.addAccount(account);
        if(registerAccount != null){
            ctx.json(mapper.writeValueAsString(registerAccount));
        }
        else{
            ctx.status(400);
        }
    }catch(IllegalArgumentException e){
        ctx.status(400); 
    }
    }

    public void loginToAccount(Context ctx) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Account loginAccount = mapper.readValue(ctx.body(),Account.class);
        
        Account account = accountService.authenticateAccount(loginAccount.getUsername(),loginAccount.getPassword());

        if(account != null){
            ctx.json(account);
        }else{
            ctx.status(401);
        }   
    }

    public void createMessages(Context ctx) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        try {
            Message message = mapper.readValue(ctx.body(), Message.class);
            Message newMessage = messageService.addMessage(message);
            if(newMessage != null){
                ctx.json(mapper.writeValueAsString(newMessage));
            }
            else{
                ctx.status(400); 
            }
            
        } catch (IllegalArgumentException e) {
            ctx.status(400);
        }
    }

    public void getMessages (Context ctx){
        List<Message> messages = messageService.getAllMessages();
        ctx.json(messages);
    }

    public void getMessageById(Context ctx){
        int message_id = Integer.parseInt(ctx.pathParam("message_id")); 
        Message returnedMessage = messageService.getMessageById(message_id);
        if(returnedMessage != null){
            ctx.json(returnedMessage);
        }else{
            ctx.status(200).result("");
        }
    }

    public void deleteMessage(Context ctx){
        try {
            int message_id = Integer.parseInt(ctx.pathParam("message_id"));  
            Message deletedMessage = messageService.getMessageById(message_id);
            messageService.deleteMessage(message_id);
            ctx.json(deletedMessage); 
            ctx.status(200); 
        } catch (IllegalArgumentException e) {
            ctx.status(200); 
        }
    }

    public void updateMessage(Context ctx) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        try {
            Map<String, String> body = mapper.readValue(ctx.body(), Map.class);
            String newMessageText = body.get("message_text");
            int message_id = Integer.parseInt(ctx.pathParam("message_id"));

            Message updatedMessage = messageService.updateMessage(message_id, newMessageText);
            
            if(updatedMessage != null){
                ctx.json(updatedMessage);
            }
            else{
                ctx.status(400);
            }
        } catch (IllegalArgumentException e) {
            ctx.status(400);
        }catch(Exception e){
            ctx.status(400);
        }
    }

    public void getMessagesFromUser(Context ctx){
        try {
            int account_id = Integer.parseInt(ctx.pathParam("account_id"));
            List<Message> messages = messageService.getMessagesByAccountId(account_id);
            ctx.json(messages);
            
        } catch (Exception e) {
            ctx.status(200);
        }
    }


    }


