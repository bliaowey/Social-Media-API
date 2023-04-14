package Service;

import Model.Message;
import DAO.MessageDAO;
import java.util.List;

public class MessageService {
    public MessageDAO messageDAO;

    /*
     * No params constructor
     */
    public MessageService(){
        messageDAO = new MessageDAO();
    }

    /*
     * Param constructor
     * @params: messageDAO
     */
    public MessageService(MessageDAO messageDao){
        this.messageDAO = messageDao;
    }

    /*
     * getAllMessages returns all the messages in the message table
     * @params: none
     * @returns: a list of all the messages
     */
    public List<Message> getAllMessages(){
        return messageDAO.getAllMessages();
    }

    /*
     * getMessageByID returns the message with the given message ID
     * @params: the message ID
     * @returns: the message in the table with said ID
     */
    public Message getMessageByID(int id){
        return messageDAO.getMessageByID(id);
    }

    /*
     * insertMessage inserts a message into the database
     * @params: the message to be inserted
     * @returns: the message if it was successfully inserted, null if not
     */
    public Message insertMessage(Message message){
        if(this.getMessageByID(message.getMessage_id()) == null) {
            return messageDAO.insertMessage(message);
        }else {
            return null;
        }
    }

    /*
     * deleteMessage deletes a message
     * @params: the id of the message to be deleted
     * @returns: the message if it was deleted, null if the message wasn't in the table to begin with
     */
    public Message deletMessage(int id){
        return messageDAO.deleteMessage(id);
    }

     /*
     * updateMessage updates the message text based on the message id
     * @params: the message id and the updated message text
     * @returns: the updated message
     */
    public Message updateMessage(int id, String newText){
        return messageDAO.updateMessage(id, newText);
    }

    /*
     * getAllMessagesByUserId returns all the messages posted by a given user
     * @params: the user id
     * @returns: all the messages from that user
     */
    public List<Message> getAllMessagesByUserId(int id){
        return messageDAO.getAllMessagesByUserId(id);
    }
}
