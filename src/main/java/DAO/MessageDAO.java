package DAO;
import Util.ConnectionUtil;
import Model.Message;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * MessageDAO Functionalities Implemented.
 */
public class MessageDAO {
    /**
     * TODO: retrieve all messages from the Message table.
     * @return all Messages.
     */
    public List<Message> getAllMessages(){
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();
        try {
            String sql = "SELECT * FROM message";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                Message message = new Message(rs.getInt("message_id"),
                        rs.getInt("posted_by"),
                        rs.getString("message_text"),
                        rs.getLong("time_posted_epoch"));
                messages.add(message);
            }
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }
        return messages;
    }

    /**
     * TODO: retrieve a message from the message table, identified by its message_id.
     * @return a message identified by mid.
     */
    public Message getMessageById(int mid){
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "SELECT * FROM message WHERE message_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, mid);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()){
                return new Message(rs.getInt("message_id"),
                        rs.getInt("posted_by"),
                        rs.getString("message_text"),
                        rs.getLong("time_posted_epoch"));
            }
        } catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * TODO: insert a message into the message table.
     */
    public Message insertMessage(Message message) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            boolean mCheck = !"".equals(message.getMessage_text()) && message.getMessage_text().length() <= 255;
            /* Checking to see if posted_by is a real existing Account user. */
            String s = "SELECT * FROM account WHERE account_id = ?";
            PreparedStatement ps = connection.prepareStatement(s);
            ps.setInt(1, message.getPosted_by());
            ResultSet rs = ps.executeQuery();
            boolean uCheck = rs.next();
            /* Valid message by valid user, going ahead with persisting into the database. */
            if (mCheck && uCheck) {
                String sql = "INSERT INTO message (posted_by, message_text, time_posted_epoch) VALUES (?, ?, ?)" ;
                PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                preparedStatement.setInt(1, message.getPosted_by());
                preparedStatement.setString(2, message.getMessage_text());
                preparedStatement.setLong(3, message.getTime_posted_epoch());
                preparedStatement.executeUpdate();
                ResultSet pkeyResultSet = preparedStatement.getGeneratedKeys();
                if(pkeyResultSet.next()){
                    int generated_message_id = pkeyResultSet.getInt(1);
                    return new Message(generated_message_id, message.getPosted_by(), message.getMessage_text(), message.getTime_posted_epoch());
                }
            }
        } catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * TODO: retrieve all messages from the message table by a particular user.
     * @returnall messages by poster user id.
     */
    public List<Message> getMessagesByParticularUser(int poster){
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();
        try {
            String sql = "SELECT * FROM message WHERE posted_by = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, poster);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                Message message = new Message(rs.getInt("message_id"),
                        rs.getInt("posted_by"),
                        rs.getString("message_text"),
                        rs.getLong("time_posted_epoch"));
                messages.add(message);
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return messages;
    }

    /**
     * TODO: Update a message in the message table.
     */
    public Message updateMessage(int messageID, String mText){
        Connection connection = ConnectionUtil.getConnection();
        try {
            boolean mCheck = !("").equals(mText) && mText.length() <= 255;
            /* Checking to see if message exists. */
            String s = "SELECT * FROM message WHERE message_id = ?";
            PreparedStatement ps = connection.prepareStatement(s);
            ps.setInt(1, messageID);
            ResultSet rs = ps.executeQuery();
            boolean uCheck = rs.next();
            /* Valid message that already exists, going ahead with replacing message text. */
            if (mCheck && uCheck) {
                String sql = "UPDATE message SET message_text = ? WHERE message_id = ?" ;
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, mText);
                preparedStatement.setInt(2, messageID);
                preparedStatement.executeUpdate();
                /* Checking to see if update succeeded. */
                String upS = "SELECT * FROM message WHERE message_id = ?";
                PreparedStatement ups = connection.prepareStatement(upS);
                ups.setInt(1, messageID);
                ResultSet urs = ups.executeQuery();
                if (urs.next())
                    return new Message(urs.getInt("message_id"), urs.getInt("posted_by"), urs.getString("message_text"), urs.getLong("time_posted_epoch"));
            } 
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * TODO: Delete a message in the message table.
     */
    public Message deleteMessage(int messageId){
        Connection connection = ConnectionUtil.getConnection();
        try {
            /* Checking to see if message exists. */
            String s = "SELECT * FROM message WHERE message_id = ?";
            PreparedStatement ps = connection.prepareStatement(s);
            ps.setInt(1, messageId);
            ResultSet rs = ps.executeQuery();
            boolean mCheck = rs.next();
            /* Message already exists, going ahead with deleting the message. */
            if (mCheck) {
                String sql = "DELETE FROM message WHERE message_id = ?" ;
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setInt(1, messageId);
                Message mRes = new Message(messageId, rs.getInt("posted_by"), rs.getString("message_text"), rs.getLong("time_posted_epoch"));
                preparedStatement.executeUpdate();
                return mRes;
            } 
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
}
