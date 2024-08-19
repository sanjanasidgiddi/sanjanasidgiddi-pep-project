package Service;

import DAO.MessageDAO;
import Model.Message;

import java.util.List;

public class MessageService {
    public MessageDAO messageDAO;

    /**
     * No-args constructor for bookService which creates a MessageDAO.
     */
    public MessageService(){
        messageDAO = new MessageDAO();
    }
    /**
     * Constructor for a MessageService when a MessageDAO is provided.
     * @param messageDAO
     */
    public MessageService(MessageDAO messageDAO){
        this.messageDAO = messageDAO;
    }
    /**
     * TODO: Use the messageDAO to retrieve all messages.
     * @return all messages.
     */
    public List<Message> getAllMessages() {
        return messageDAO.getAllMessages();
    }
    /**
     * TODO: Use the messageDAO to persist a Message to the database.
     * @param message a message object.
     * @return message if it was successfully persisted, null if it was not successfully persisted 
     */
    public Message addMessage(Message message) {
        if (messageDAO.getMessageById(message.getMessage_id()) != null) {
            /* Message already exists. */
            return null;
        }
        return messageDAO.insertMessage(message);
        
    }
    /**
     * TODO: Use the messageDAO to retrieve a message with msg id.
     * @return message by id
     */
    public Message getMessageById(int mid) {
        return messageDAO.getMessageById(mid);
    }

    /**
     * TODO: Use the messageDAO to retrieve all messages by a particular user.
     * @return all message by poster
     */
    public List<Message> getMessageByPoster(int pid) {
        return messageDAO.getMessagesByParticularUser(pid);
    }

    /**
     * TODO: Use the messageDAO to update a message.
     * @return message 
     */
    public Message updateMessage(int messageid, String mText) {
        return messageDAO.updateMessage(messageid, mText);
    }

    /**
     * TODO: Use the messageDAO to delete a message.
     * @return message 
     */
    public Message deleteMessage(int messageid) {
        return messageDAO.deleteMessage(messageid);
    }
}
