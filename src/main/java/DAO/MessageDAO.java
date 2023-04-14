package DAO;

import Model.Message;
import Util.ConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MessageDAO {
    
    /*
     * getAllMessages() returns all the messages in the database
     * @params: none
     * @returns: a list of all the messages in the database
     */
    public List<Message> getAllMessages(){
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();

        try {
            String sql = "SELECT * FROM message";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                Message message = new Message(rs.getInt("message_id"), rs.getInt("posted_by"), rs.getString("message_text"), rs.getLong("time_posted_epoch"));
                messages.add(message);
            }
        } catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return messages;
    }

    /*
     * insertMessage inserts the given message into the database
     * @params: the message to be inserted
     * @returns: the message if it was succesfully inserted, null if not
     */
    public Message insertMessage(Message message){
        Connection connection = ConnectionUtil.getConnection();
        
        try{
            String sql = "INSERT INTO message (posted_by, message_text, time_posted_epoch) VALUES(?, ?, ?)" ;
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, message.getPosted_by());
            preparedStatement.setString(2, message.getMessage_text());
            preparedStatement.setLong(3, message.getTime_posted_epoch());

            preparedStatement.executeUpdate();
            ResultSet pkeyResultSet = preparedStatement.getGeneratedKeys();
            if (pkeyResultSet.next()){
                int generated_message_id = (int) pkeyResultSet.getLong(1);
                return new Message(generated_message_id, message.getPosted_by(), message.getMessage_text(), message.getTime_posted_epoch());
            }
        } catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    /*
     * getMessageByID gets a message by it's message id
     * @params: the message id
     * @returns: the message with the id, if no message has the id, returns null
     */
    public Message getMessageByID(int id){
        Connection connection = ConnectionUtil.getConnection();

        try {
            String sql = "SELECT * FROM message WHERE message_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();

            while(rs.next()){
                Message message = new Message(
                    rs.getInt("message_id"),
                    rs.getInt("posted_by"),
                    rs.getString("message_text"),
                    rs.getLong("time_posted_epoch")
                );
                return message;
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    /*
     * deleteMessage deletes a message based on the id
     * @params: the id of the message to be deleted
     * @returns: the deleted message
     */
    public Message deleteMessage(int id){
        Connection connection = ConnectionUtil.getConnection();
        Message m = this.getMessageByID(id);
        if (m != null){
            m.setMessage_id(id);
        }
        try {
            String sql = "DELETE FROM message WHERE message_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
            return m;
        } catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }
    
    /*
     * updateMessage updates the message text based on the message id
     * @params: the message id and the updated message text
     * @returns: the updated message
     */
    public Message updateMessage(int id, String newText){
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "UPDATE message SET message_text = ? WHERE message_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, newText);
            preparedStatement.setInt(2, id);

            preparedStatement.executeUpdate();
            Message m = this.getMessageByID(id);
            if (m != null){
                m.setMessage_id(id);
            }
            return m;
        } catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }
    
    /*
     * getAllMessagesByUserId returns all the messages posted by a given user
     * @params: the user id
     * @returns: all the messages from that user
     */

     public List<Message> getAllMessagesByUserId(int id){
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();

        try{
            String sql = "SELECT * FROM message WHERE posted_by = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();
            
            while(rs.next()){
                Message message = new Message(rs.getInt("message_id"), rs.getInt("posted_by"), rs.getString("message_text"), rs.getLong("time_posted_epoch"));
                messages.add(message);
            }
            return messages;
        } catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
     }
}
