package Controller;

import io.javalin.Javalin;
import io.javalin.http.Context;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;

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
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();

        /* Account User Registration creation and Login operations */
        app.post("/login", this::verifyAccountLoginHandler);
        app.post("/register", this::postAccountHandler);
        /* Posting a new Message */
        app.post("/messages", this::postMessageHandler);
        /* Retrive all Messages */
        app.get("/messages", this::getAllMessagesHandler);
        /* Retrive a Message by its id */
        app.get("/messages/{message_id}", this::getMessageByIdHandler);
        /* Delete a Message by its id */
        app.delete("/messages/{message_id}", this::deleteMessageHandler);
        /* Update a Message by its id */
        app.patch("/messages/{message_id}", this::updateMessageHandler);
        /* Retrive a Message by its user poster id */
        app.get("/accounts/{account_id}/messages", this::getMessageByPosterIdHandler);

        return app;
    }

    /**
     * Handler to post a new account.
     * @param ctx the context object handles information HTTP requests and generates responses within Javalin. It will
     *            be available to this method automatically thanks to the app.post method.
     * @throws JsonProcessingException will be thrown if there is an issue converting JSON into an object.
     */
    private void postAccountHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
        Account addedAccount = accountService.addAccount(account);
        if (addedAccount != null) {
            ctx.json(mapper.writeValueAsString(addedAccount));
            ctx.status(200);
        } else {
           ctx.status(400);
        }
    }

    /**
     * Handler to verify account login.
     * @param ctx the context object handles information HTTP requests and generates responses within Javalin. It will
     *            be available to this method automatically thanks to the app.post method.
     * @throws JsonProcessingException will be thrown if there is an issue converting JSON into an object.
     */
    private void verifyAccountLoginHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
        Account loggedInAccount = accountService.verifyLogin(account);
        if (loggedInAccount != null) {
            ctx.json(mapper.writeValueAsString(loggedInAccount));
            ctx.status(200);
        } else {
            ctx.status(401);
        }
    }

    /**
     * Handler to post a new Message.
     * @param ctx the context object handles information HTTP requests and generates responses within Javalin. It will
     *            be available to this method automatically thanks to the app.post method.
     * @throws JsonProcessingException will be thrown if there is an issue converting JSON into an object.
     */
    private void postMessageHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(ctx.body(), Message.class);
        Message addedMessage = messageService.addMessage(message);
        if (addedMessage!=null) {
            ctx.json(mapper.writeValueAsString(addedMessage));
            ctx.status(200);
        } else {
            ctx.status(400);
        }
    }

    /**
     * Handler to retrieve all messages. 
     * @param ctx the context object handles information HTTP requests and generates responses within Javalin. It will
     *            be available to this method automatically thanks to the app.put method.
     */
    private void getAllMessagesHandler(Context ctx) {
        List<Message> messages = messageService.getAllMessages();
        ctx.json(messages);
        ctx.status(200);
    }

    /**
     * Handler to retrieve a message by its id. 
     * @param ctx the context object handles information HTTP requests and generates responses within Javalin. It will
     *            be available to this method automatically thanks to the app.put method.
     */
    private void getMessageByIdHandler(Context ctx) throws JsonProcessingException {
        int messageId = Integer.valueOf(ctx.pathParam("message_id"));
        Message message = messageService.getMessageById(messageId);
        if (message != null)
            ctx.json(message);
        ctx.status(200);
    }

    /**
     * Handler to delete a message.
     * @param ctx the context object handles information HTTP requests and generates responses within Javalin. It will
     *            be available to this method automatically thanks to the app.post method.
     * @throws JsonProcessingException will be thrown if there is an issue converting JSON into an object.
     */
    private void deleteMessageHandler(Context ctx) throws JsonProcessingException {
        int messageId = Integer.valueOf(ctx.pathParam("message_id"));
        Message message = messageService.deleteMessage(messageId);
        if (message != null)
            ctx.json(message);
        ctx.status(200);
    }

    /**
     * Handler to update a message.
     * @param ctx the context object handles information HTTP requests and generates responses within Javalin. It will
     *            be available to this method automatically thanks to the app.post method.
     * @throws JsonProcessingException will be thrown if there is an issue converting JSON into an object.
     */
    private void updateMessageHandler(Context ctx) throws JsonProcessingException {
        int messageId = Integer.valueOf(ctx.pathParam("message_id"));
        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> bodyMap = mapper.readValue(ctx.body(), Map.class);
        String ms = bodyMap.get("message_text");
        Message updatedMessage = messageService.updateMessage(messageId, ms);
        if (updatedMessage != null) {
            ctx.json(updatedMessage);
            ctx.status(200);
        } else {
            ctx.status(400);
        }
    }

    /**
     * Handler to retrieve a message by its user or poster id. 
     * @param ctx the context object handles information HTTP requests and generates responses within Javalin. It will
     *            be available to this method automatically thanks to the app.put method.
     */
    private void getMessageByPosterIdHandler(Context ctx) throws JsonProcessingException {
        int usrId = Integer.valueOf(ctx.pathParam("account_id"));
        List<Message> message = messageService.getMessageByPoster(usrId);
        ctx.json(message);
        ctx.status(200);
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void exampleHandler(Context context) {
        context.json("sample text");
    }


}