package Controller;

import io.javalin.Javalin;
import io.javalin.http.Context;
import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    AccountService accountService;
    MessageService messageService;

    public SocialMediaController(){
        accountService = new AccountService();
        messageService = new MessageService();
    }
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.post("/register", this::postAccountHandler); //Create and post new account
        app.post("/login", this::loginHandler); //Login into an account
        app.post("/messages", this::postMessageHandler); //Post a message
        app.get("/messages", this::getAllMessagesHandler); //Get all messages
        app.get("/messages/{message_id}", this::getMessageByIDHandler); //Get message with ID
        app.delete("/messages/{message_id}", this::deleteMessageByIDHandler); //Delete message by ID
        app.patch("/messages/{message_id}", this::updateMessageHandler); //Update message by ID
        app.get("/accounts/{account_id}/messages", this::getAllMessagesFromUserHandler); //Get messages posted by a user
        return app;
    }

    /**
     * This is the handler to post an account
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void postAccountHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
        Account addedAccount = accountService.addAccount(account);
        if (addedAccount != null && addedAccount.getUsername() != "" && addedAccount.getPassword().length() >= 4){
            ctx.json(mapper.writeValueAsString(addedAccount));
        } else {
            ctx.status(400);
        }
    }

     /**
     * This is the handler to login on an account (it is a post method)
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void loginHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
        Account loginAccount = accountService.getUserAccount(account.getUsername(), account.getPassword());
        if (loginAccount != null){
            ctx.json(mapper.writeValueAsString(loginAccount));
        } else {
            ctx.status(401);
        }
    }

    /**
     * This is the handler for posting a new message
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void postMessageHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(ctx.body(), Message.class);
        Message postedMessage = messageService.insertMessage(message);
        if (postedMessage != null && postedMessage.getMessage_text() != "" && postedMessage.getMessage_text().length() < 255){
            ctx.json(mapper.writeValueAsString(postedMessage));
        } else {
            ctx.status(400);
        }
    }

    /**
     * This is the handler for getting all messages
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void getAllMessagesHandler(Context ctx) {
        ctx.json(messageService.getAllMessages());
    }

    /**
     * This is the handler for getting a message based on its ID
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void getMessageByIDHandler(Context ctx) {
        int message_id = Integer.parseInt(ctx.pathParam("message_id"));
        Message message = messageService.getMessageByID(message_id);
        if (message == null){
            ctx.json("");
        } else {
            ctx.json(message);
        }
    }

    /**
     * This is the handler for deleting a message based on its ID
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void deleteMessageByIDHandler(Context ctx){
        int message_id = Integer.parseInt(ctx.pathParam("message_id"));
        Message message = messageService.deletMessage(message_id);
        if (message == null){
            ctx.json("");
        }else {
            ctx.json(message);
        }
    }

    /**
     * This is the handler for updating a message based on its ID
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void updateMessageHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(ctx.body(), Message.class);
        int message_id = Integer.parseInt(ctx.pathParam("message_id"));
        Message updatedMessage = messageService.updateMessage(message_id, message.getMessage_text());
        if(updatedMessage != null && updatedMessage.getMessage_text() != "" && updatedMessage.getMessage_text().length() < 255){
            ctx.json(mapper.writeValueAsString(updatedMessage));
        } else {
            ctx.status(400);
        }
    }

    private void getAllMessagesFromUserHandler(Context ctx){
        int account_id = Integer.parseInt(ctx.pathParam("account_id"));
        List<Message> messages = messageService.getAllMessagesByUserId(account_id);
        if (messages == null){
            ctx.json("");
        } else {
            ctx.json(messages);
        }
    }
}