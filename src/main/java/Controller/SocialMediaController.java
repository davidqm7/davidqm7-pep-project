package Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Model.Account;
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

        return app;
    }

    private void createAccount(Context ctx)throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
        Account registerAccount = accountService.addAccount(account);
        if(registerAccount != null){
            ctx.json(mapper.writeValueAsString(registerAccount));
        }
        else{
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

    


}